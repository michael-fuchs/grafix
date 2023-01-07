package mifu.grafix.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mifu.grafix.HelloApplication;

public class Graph {


  List<GraphNode> nodes = new ArrayList<>();

  public Graph(int numNodes, int numConnections) {
    for (int i = 0; i < numNodes; i++) {
      nodes.add(new GraphNode(i, (int) (Math.random() * HelloApplication.WINDOW_WIDTH), (int) (Math.random() * HelloApplication.WINDOW_HEIGHT)));
    }

    for (int i = 0; i < numConnections; i++) {
      int node1 = (int) (Math.random() * numNodes);
      int node2 = (int) (Math.random() * numNodes);
      nodes.get(node1).addConnection(nodes.get(node2));
      nodes.get(node2).addConnection(nodes.get(node1));
    }
  }

  public List<GraphNode> findPath(Set<GraphNode> visitedNodes, GraphNode start, GraphNode end) {
    List<GraphNode> path = null;

    if (start == end) {
      path = new ArrayList<>();
      path.add(start);
      return path;
    }

    if (!visitedNodes.contains(start)) {
      visitedNodes.add(start);
      int i = 0;
      while (path == null && start.getConnections().size() > i) {
        GraphNode next = start.getConnections().get(i);
        path = findPath(visitedNodes, next, end);
        i++;
      }
      if (path != null) {
        path.add(start);
      }
    }
    return path;
  }

  public List<GraphNode> getNodes() {
    return nodes;
  }
}
