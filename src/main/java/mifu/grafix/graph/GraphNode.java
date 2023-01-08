package mifu.grafix.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphNode {

  final long id;

  final int locationX;
  final int locationY;

  public GraphNode(long id, int locationX, int locationY) {
    this.id = id;
    this.locationX = locationX;
    this.locationY = locationY;
  }

  List<GraphNode> connections = new ArrayList<GraphNode>();

  public void addConnection(GraphNode node) {
    connections.add(node);
  }

  public List<GraphNode> getConnections() {
    return connections;
  }

  public long getId() {
    return id;
  }

  public int getLocationX() {
    return locationX;
  }

  public int getLocationY() {
    return locationY;
  }

  /**
   * Returns a comparator that compares the distance of two nodes to this node.
   * It allows to sort all nodes of the graph nodes by distance int the view of this node.
   */
  public Comparator<GraphNode> distanceComparator() {
    return (n1, n2) -> distance(n1) - distance(n2);
  }

  /**
   * Returns the distance of the given node to this node.
   */
  public int distance(GraphNode node) {
    return (int) Math.sqrt(Math.pow(locationX - node.getLocationX(), 2) + Math.pow(locationY - node.getLocationY(), 2));
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GraphNode{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
