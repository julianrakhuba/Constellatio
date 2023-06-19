package aaa;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
@SuppressWarnings("exports")
public class Node {
    private Point2D position;
    private List<Node> connectedNodes;
    private double forceX;
    private double forceY;

    public Node(double x, double y) {
        position = new Point2D(x, y);
        connectedNodes = new ArrayList<>();
    }

	public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public double getForceX() {
        return forceX;
    }

    public void setForceX(double forceX) {
        this.forceX = forceX;
    }

    public double getForceY() {
        return forceY;
    }

    public void setForceY(double forceY) {
        this.forceY = forceY;
    }
}