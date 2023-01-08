package mifu.grafix.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import mifu.grafix.GrafixApp;

public class Graph {


  List<GraphNode> nodes = new ArrayList<>();

  public Graph(int numNodes, int numConnections) {
    // create nodes
    for (int i = 0; i < numNodes; i++) {
      nodes.add(new GraphNode(i,
          (int) (Math.random() * (GrafixApp.WINDOW_WIDTH -10) + 5), // 5px distance from window border
          (int) (Math.random() * (GrafixApp.WINDOW_HEIGHT - 10) + 5)
      ));
    }

    // create connections
    for (GraphNode node : nodes) {
      nodes.stream()
          .filter(Predicate.not(node::equals)) // exclude self
          .sorted(node.distanceComparator()) // sort by distance to self (to get closest first)
          .limit(numConnections) // limit to numConnections
          .forEach(n -> { // add connection
            node.addConnection(n);
            n.addConnection(node); // undirected graph, so add the connection both ways
          });
    }
  }

  /**
   * Previously used recursive implementation, but this is not suitable for the UI stepping.
   * @param visitedNodes
   * @param start
   * @param end
   * @return
   */
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
