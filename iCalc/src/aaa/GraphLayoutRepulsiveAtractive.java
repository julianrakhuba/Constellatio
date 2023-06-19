package aaa;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphLayoutRepulsiveAtractive extends Application {
    private static final int NODE_RADIUS = 10;
    private static final int PANE_WIDTH = 800;
    private static final int PANE_HEIGHT = 600;
    private static final double REPULSIVE_FORCE_FACTOR = 4000.0;
    private static final double ATTRACTIVE_FORCE_FACTOR = 0.2;

    private List<Node> nodes;
    private Pane pane;

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

        for (int i = 0; i < 10; i++) {
            double x = random.nextDouble() * PANE_WIDTH;
            double y = random.nextDouble() * PANE_HEIGHT;
            Node node = new Node(x, y);
            nodes.add(node);
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            for (int j = i + 1; j < nodes.size(); j++) {
                Node connectedNode = nodes.get(j);

                if (random.nextDouble() < 0.3) {
                    node.getConnectedNodes().add(connectedNode);
                    connectedNode.getConnectedNodes().add(node);
                }
            }
        }

        return nodes;
    }

    private void drawGraph() {
        pane.getChildren().removeIf( jj -> !(jj instanceof Button));

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
        // Calculate forces between nodes
        for (Node node : nodes) {
            for (Node otherNode : nodes) {
                if (node != otherNode) {
                    applyRepulsiveForce(node, otherNode);
                    applyAttractiveForce(node, otherNode);
                }
            }
        }

        // Update node positions based on forces
        for (Node node : nodes) {
            updateNodePosition(node);
        }

        // Redraw the graph
        drawGraph();
    }

    private void applyRepulsiveForce(Node node, Node otherNode) {
        double dx = otherNode.getPosition().getX() - node.getPosition().getX();
        double dy = otherNode.getPosition().getY() - node.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            double force = REPULSIVE_FORCE_FACTOR / (distance * distance);
            node.setForceX(node.getForceX() - force * dx / distance);
            node.setForceY(node.getForceY() - force * dy / distance);
        }
    }

    private void applyAttractiveForce(Node node, Node otherNode) {
        double dx = otherNode.getPosition().getX() - node.getPosition().getX();
        double dy = otherNode.getPosition().getY() - node.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            double force = ATTRACTIVE_FORCE_FACTOR * distance;
            node.setForceX(node.getForceX() + force * dx / distance);
            node.setForceY(node.getForceY() + force * dy / distance);
        }
    }

    private void updateNodePosition(Node node) {
        double velocityX = node.getForceX();
        double velocityY = node.getForceY();
        double damping = 0.9; // Damping factor to prevent overshooting

        double newX = node.getPosition().getX() + velocityX;
        double newY = node.getPosition().getY() + velocityY;

        // Apply damping
        velocityX *= damping;
        velocityY *= damping;

        node.setPosition(new Point2D(newX, newY));
        node.setForceX(0.0);
        node.setForceY(0.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// class Node {
//    private Point2D position;
//    private List<Node> connectedNodes;
//    private double forceX;
//    private double forceY;
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
//
//    public double getForceX() {
//        return forceX;
//    }
//
//    public void setForceX(double forceX) {
//        this.forceX = forceX;
//    }
//
//    public double getForceY() {
//        return forceY;
//    }
//
//    public void setForceY(double forceY) {
//        this.forceY = forceY;
//    }
//}
