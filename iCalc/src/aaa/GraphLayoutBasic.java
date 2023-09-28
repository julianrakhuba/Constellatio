/*******************************************************************************
 *   Copyright (c) 2023 Constellatio
 *  
 *   This software is released under the [Educational/Non-Commercial License or Commercial License, choose one]
 *  
 *   Educational/Non-Commercial License (GPL):
 *  
 *   Permission is hereby granted, free of charge, to any person or organization
 *   obtaining a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including without
 *   limitation the rights to use, copy, modify, merge, publish, distribute,
 *   sublicense, and/or sell copies of the Software, and to permit persons to
 *   whom the Software is furnished to do so, subject to the following conditions:
 *  
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *  
 *   THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 *  
 *   Commercial License:
 *  
 *   You must obtain a separate commercial license if you
 *   wish to use this software for commercial purposes. Please contact
 *   rakhuba@gmail.com for licensing information.
 *  
 *  
 *******************************************************************************/
package aaa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GraphLayoutBasic extends Application {
    private static final int NODE_RADIUS = 10;
    private static final int PANE_WIDTH = 800;
    private static final int PANE_HEIGHT = 600;

    private List<Node> nodes;
    private Pane pane;

//    @SuppressWarnings("exports")  
	@Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        nodes = createGraphLayout();
        pane = new Pane();

        drawGraph();

        Button rearrangeButton = new Button("Rearrange");
        rearrangeButton.setOnAction(event -> rearrangeGraph());
        pane.getChildren().add(rearrangeButton);

        Scene scene = new Scene(pane, PANE_WIDTH, PANE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<Node> createGraphLayout() {
        List<Node> nodes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 13; i++) {
            double x = 50;//random.nextDouble() * PANE_WIDTH;
            double y = 50;//random.nextDouble() * PANE_HEIGHT;
            Node node = new Node(x, y);
            nodes.add(node);
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            for (int j = i + 1; j < nodes.size(); j++) {
                Node connectedNode = nodes.get(j);

                if (random.nextDouble() < 0.8) {
                    node.getConnectedNodes().add(connectedNode);
                    connectedNode.getConnectedNodes().add(node);
                }
            }
        }

        return nodes;
    }

    private void drawGraph() {
        pane.getChildren().removeIf(jj -> !(jj instanceof Button));

        for (Node node : nodes) {
            Circle circle = new Circle(node.getPosition().getX(), node.getPosition().getY(), NODE_RADIUS);
            circle.setFill(Color.BLUE);
            pane.getChildren().add(circle);
        }

        for (Node node : nodes) {
            for (Node connectedNode : node.getConnectedNodes()) {
                Line line = new Line(node.getPosition().getX(), node.getPosition().getY(),
                        connectedNode.getPosition().getX(), connectedNode.getPosition().getY());
                line.setStroke(Color.BLACK);
                pane.getChildren().add(line);
            }
        }
    }

    private void rearrangeGraph() {
        int numNodes = nodes.size();

        // Set up initial node positions randomly
        Random random = new Random();
        for (Node node : nodes) {
            double x = random.nextDouble() * PANE_WIDTH;
            double y = random.nextDouble() * PANE_HEIGHT;
            node.setPosition(new Point2D(x, y));
        }

        // Perform simple grid-based layout
        int numColumns = (int) Math.ceil(Math.sqrt(numNodes));
        int numRows = (int) Math.ceil((double) numNodes / numColumns);
        double columnWidth = PANE_WIDTH / numColumns;
        double rowHeight = PANE_HEIGHT / numRows;

        for (int i = 0; i < numNodes; i++) {
            Node node = nodes.get(i);
            int row = i / numColumns;
            int column = i % numColumns;
            double x = column * columnWidth + columnWidth / 2;
            double y = row * rowHeight + rowHeight / 2;
            node.setPosition(new Point2D(x, y));
        }

        // Redraw the graph
        drawGraph();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//class Node {
//    private Point2D position;
//    private List<Node> connectedNodes;
//
//    public Node(double x, double y) {
//        position = new Point2D(x, y);
//        connectedNodes = new ArrayList<>();
//    }
//
//    public Point2D getPosition() {
//        return position;
//    }
//
//    public void setPosition(Point2D position) {
//        this.position = position;
//    }
//
//    public List<Node> getConnectedNodes() {
//        return connectedNodes;
//    }
//}

