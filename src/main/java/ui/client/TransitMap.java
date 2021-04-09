package main.java.ui.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import main.java.entities.transit.*;
import main.java.system.TransitSystem;

import java.util.ArrayList;
import java.util.Random;

/**
 * An object representing a gui for a map of the transit network
 */
public class TransitMap {
    private final int size;
    private final int divs;
    private TransitSystem system;
    private Pane map;
    private ArrayList<TransitLine> lines;

    /**
     * Create a new map of the given transit system with the given dimensions
     * 
     * @param system the transit system for which a map will be made
     * @param size   the size (in pixels) of the map
     * @param divs   the number of divisions on the grid for this map
     */
    public TransitMap(TransitSystem system, int size, int divs) {
        this.system = system;

        this.size = size;
        this.divs = divs;

        this.map = new Pane();
        map.setMinHeight(size);
        map.setMaxHeight(size);
        map.setMinWidth(size);
        map.setMaxWidth(size);

        lines = new ArrayList<>();
    }

    /**
     * Draw all the stops on a given on this map
     * 
     * @param transitLine the line whose stops will be drawn
     */
    private void plotStops(TransitLine transitLine) {
        int scale = this.size / this.divs;
        Paint color = transitLine.getColor();
        TransitStop[] stops = transitLine.getStops();

        // Draw circle for initial stop
        Circle circle = new Circle(stops[0].getX() * scale, stops[0].getY() * scale, scale / 4);
        circle.setFill(color);
        map.getChildren().add(circle);

        // iteratively draw dots for each stop connected by lines
        for (int i = 1; i < stops.length; i++) {
            Line line = new Line(stops[i - 1].getX() * scale, stops[i - 1].getY() * scale, stops[i].getX() * scale,
                    stops[i].getY() * scale);
            line.setStrokeWidth(4);
            line.setStroke(color);

            map.getChildren().add(line);

            circle = new Circle(stops[i].getX() * scale, stops[i].getY() * scale, scale / 4);
            circle.setFill(color);
            map.getChildren().add(circle);
        }
    }

    /**
     * Return a javafx Node representing the legend mapping colors to names of lines
     * on this map
     *
     * @return a javafx element which displays the legend
     */
    private Node createLegend() {
        GridPane legend = new GridPane();
        for (int i = 0; i < lines.size(); i++) {
            TransitLine line = lines.get(i);
            Rectangle box = new Rectangle(20, 10);
            box.setFill(line.getColor());
            Label lineName = new Label(line.getName());
            lineName.setStyle("-fx-font: 24 arial;");

            legend.add(box, 0, i);
            legend.add(lineName, 1, i);
        }
        return legend;
    }

    /**
     * Draw a grid on this map pane with the given number of divisions
     */
    private void drawGrid() {
        int interval = (this.size / this.divs);
        for (int i = 0; i < divs; i++) {
            Line gridLine = new Line(0, i * interval, size, i * interval);
            gridLine.setStroke(Color.DARKGRAY);
            map.getChildren().add(gridLine);

            gridLine = new Line(i * interval, 0, i * interval, this.size);
            gridLine.setStroke(Color.DARKGRAY);
            map.getChildren().add(gridLine);
        }
    }

    /**
     * Return a transit stop from the transit lines already on the map which is a
     * transfer stop for the given stop
     *
     * @param stop the stop for which a transfer will be returned
     * @return a stop which is a valid transfer of the given stop. null if no such
     *         stop exists
     */
    private TransitStop getTransfer(TransitStop stop) {
        for (TransitLine line : lines) {
            for (TransitStop currStop : line.getStops()) {
                if (currStop.canTransferTo(stop)) {
                    return currStop;
                }
            }
        }
        return null;
    }

    /**
     * Return the euclidean distance between two points (x1, y1) and (x2, y2)
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the distance between the given points
     */
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    /**
     * Returns the transit stop which is closest on the map to the given point
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return the stop closest to (x, y)
     */
    private TransitStop getNearestStop(double x, double y) {
        int scale = this.size / this.divs;
        TransitStop closest = lines.get(0).getStops()[0];
        double minDist = Float.MAX_VALUE;

        for (TransitLine line : lines) {
            for (TransitStop stop : line.getStops()) {
                double currDist = dist(x, y, stop.getX() * scale, stop.getY() * scale);
                if (currDist < minDist) {
                    closest = stop;
                    minDist = currDist;
                }
            }
        }
        return closest;
    }

    /**
     * Checks for and corrects points on the map which fall outside the grid. If the
     * point has a coordinate which is not on the map (i.e. it is less than 0 or
     * greater than this map's size) that point is snapped to the nearest valid
     * point on the map
     * 
     * @param stop the transit stop which may or may not be on the grid TODO:
     *             DOCSTRING
     */
    private boolean fixOverflow(TransitStop stop, boolean down) {
        if (stop.getX() < 0) {
            stop.setX(0);
            down = false;
        } else if (stop.getX() > this.divs) {
            stop.setX(this.divs);
            down = true;
        }
        if (stop.getY() < 0) {
            stop.setY(0);
            down = false;
        } else if (stop.getY() > this.divs) {
            stop.setY(this.divs);
            down = true;
        }
        return down;
    }

    /**
     * Assigns a point on the map to each station along the given subway line.
     *
     * @param subway the subway line whose stations will be given locations
     */
    private void calculateSubwayStopPos(SubwayLine subway) {
        SubwayStation[] stations = subway.getStops();
        boolean down = false;
        int xInit = (int) (Math.random() * 20);
        int yInit = 15 + (int) (Math.random() * 11);
        stations[0].setX(xInit);
        stations[0].setY(yInit);
        for (int i = 1; i < stations.length; i++) {
            int maxLen = estimateRange(subway, i - 1, false, down);
            int min = Math.max(1, maxLen - 2);
            int xOffset = min + (int) (Math.random() * (maxLen + 2 - min) + 1);
            int yOffset = -2 + (int) (Math.random() * 4);
            if (down) {
                yOffset = -5 + (int) (Math.random() * 7);
            }

            stations[i].setX(stations[i - 1].getX() + xOffset);
            stations[i].setY(stations[i - 1].getY() + yOffset);

            down = fixOverflow(stations[i], down);
        }
        subway.setColor(Color.BLACK);
    }

    /**
     * Assigns a point on the map to each stop along each given bus line, taking
     * transfers into account. Then draws each bus line on this map.
     * 
     * @param busLines an array of the bus lines whose stops will get points
     *                 assigned
     * @param colors   an array of colors which may be used to color code each line
     */
    private void calculateBusStops(BusLine[] busLines, ArrayList<Paint> colors) {
        Random random = new Random();
        for (BusLine currLine : busLines) {
            BusStop[] line = currLine.getStops();
            boolean vertical = Math.random() < .2;
            boolean down = false;
            for (int i = 0; i < line.length; i++) {
                TransitStop transfer = getTransfer(line[i]);

                if (i > 1) {
                    float xDist = line[i - 1].getX() - line[i - 2].getX();
                    float yDist = line[i - 1].getY() - line[i - 2].getY();
                    if (Math.abs(xDist) <= Math.abs(yDist)) {
                        vertical = true;
                        if (yDist < 0) {
                            down = true;
                        }
                    } else {
                        vertical = false;
                        if (xDist < 0) {
                            down = true;
                        }
                    }
                }

                if (transfer != null) {
                    line[i].setX(transfer.getX());
                    line[i].setY(transfer.getY());
                } else if (i == 0) {
                    line[i].setX(2 + (int) (Math.random() * ((38 - 2) + 1)));
                    line[i].setY(2 + (int) (Math.random() * ((38 - 2) + 1)));
                } else {
                    int maxLen = estimateRange(currLine, i - 1, vertical, down);
                    int xOffset = (maxLen - 3) + (int) (Math.random() * 4);
                    int yOffset = -2 + (int) (Math.random() * 5);
                    if (vertical) {
                        int temp = xOffset;
                        xOffset = yOffset;
                        yOffset = temp;
                    }

                    line[i].setX(line[i - 1].getX() + xOffset);
                    line[i].setY(line[i - 1].getY() + yOffset);

                    down = fixOverflow(line[i], down);
                }
            }

            if (currLine.getColor() == null) {
                if (colors.size() == 0) {
                    colors = acceptableLineColors();
                }
                int colorIdx = random.nextInt(colors.size());
                currLine.setColor(colors.get(colorIdx));
                colors.remove(colorIdx);
            }
            plotStops(currLine);
            lines.add(currLine);
        }
    }

    /**
     * Return a list of the colors which are allowed to be used to color bus lines
     * on this map
     *
     * @return an array of colors to be used for coloring bus lines
     */
    private ArrayList<Paint> acceptableLineColors() {
        ArrayList<Paint> colors = new ArrayList<>();
        colors.add(Color.AQUA);
        colors.add(Color.BLUEVIOLET);
        colors.add(Color.CORNFLOWERBLUE);
        colors.add(Color.CRIMSON);
        colors.add(Color.DARKGREEN);
        colors.add(Color.DARKORANGE);
        colors.add(Color.DIMGRAY);
        colors.add(Color.FIREBRICK);
        colors.add(Color.ROYALBLUE);
        colors.add(Color.DARKSEAGREEN);
        colors.add(Color.DARKCYAN);
        colors.add(Color.CORAL);

        return colors;
    }

    /**
     * Calculates roughly how far each stop should be and in which direction to
     * maximize the space taken up by this line on this map
     * 
     * @param line     the transit line which we want to find how far each stop
     *                 should be
     * @param prevIdx  the index of the stop on the given line which has been
     *                 plotted most recently
     * @param vertical whether or not this line is being plotted vertically on the
     *                 map
     * @param down     whether the line is going in the backwards direction
     * @return a neumeric recommendation as to where in relation to the last point
     *         the next point on this line should be
     */
    private int estimateRange(TransitLine line, int prevIdx, boolean vertical, boolean down) {
        int dist;
        int bound = 0;
        TransitStop prev = line.getStops()[prevIdx];
        if (!down) {
            bound = divs;
        }

        if (vertical) {
            dist = bound - prev.getY();
        } else {
            dist = bound - prev.getX();
        }

        int numStops = line.getStops().length;
        return dist / (numStops - prevIdx);
    }

    /**
     * Generates a random map representing the transit lines in this map's transit
     * system
     */
    private void generateMap() {
        drawGrid();
        SubwayLine subway = system.getSubwayLine();

        calculateSubwayStopPos(subway);

        lines.add(subway);

        calculateBusStops(system.getBusLines(), acceptableLineColors());

        plotStops(subway);
    }

    /**
     * Returns a JavaFX scene with a map of transit stops in the system, a legend
     * mapping colors to line names, and an indicator which displays the name of the
     * stop being hovered over
     * 
     * @return a rendering of this map
     */
    public Scene getTransitMap() {
        generateMap();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(this.map);

        borderPane.setRight(createLegend());

        GridPane bottom = new GridPane();
        String stopLabelDefault = "Stop: hover over a stop to see its name";
        Label stopLabel = new Label(stopLabelDefault);
        stopLabel.setStyle("-fx-font: 24 arial;");
        bottom.add(stopLabel, 0, 0);

        Button refreshBtn = new Button("Re-Generate Map");
        // refresh the rendering of the map on click
        refreshBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                map.getChildren().clear();
                stopLabel.setText(stopLabelDefault);
                generateMap();
            }
        });

        bottom.add(refreshBtn, 0, 2);

        borderPane.setBottom(bottom);

        // update the label for the nearest stop when the mouse is moved
        map.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TransitStop close = getNearestStop(event.getX(), event.getY());
                stopLabel.setText("Stop: " + close.getName());
            }
        });

        return new Scene(borderPane, size + 150, size + 150);
    }
}