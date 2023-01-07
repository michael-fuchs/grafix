package mifu.grafix.graph;

import java.util.ArrayList;
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

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GraphNode{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
