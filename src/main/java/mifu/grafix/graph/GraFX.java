package mifu.grafix.graph;

import java.util.List;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.TextAlignment;


/**
 * Helper class to draw a graph with JavaFX.
 */
public class GraFX {

  final GraphicsContext gc;
  final Graph graph;

  final GraphNode start;

  final GraphNode end;

  public GraFX(GraphicsContext gc, Graph graph, GraphNode start, GraphNode end) {
    this.gc = gc;
    this.graph = graph;
    this.start = start;
    this.end = end;
  }

  /**
   * Draw the found path. All nodes and edges on the path are highlighted in red.
   * @param path
   */
  public void drawPath(List<GraphNode> path) {
    if (path != null) {
      for (int i = 0; i < path.size() - 1; i++) {
        drawEdge(path.get(i), path.get(i + 1), Color.RED, 3);
        drawNode(path.get(i), NodeType.PATH);
      }
    }
  }

  /**
   * Draw an edge between two nodes, that was on the search path but lead into a dead end.
   * It's highlighted in 'salmon' to indicate that it was part of the search path.
   * The node at the end of the edge is highlighted in 'grey' to indicate that it was a dead end,
   * but has been visited.
   */
  public void resetEdge(GraphNode from, GraphNode to) {
    drawEdge(from, to, Color.LIGHTSALMON, 3);
    drawNode(to, NodeType.VISITED);
  }

  /**
   * Draw an edge between two nodes, that is currently being searched. It's highlighted in 'blue'.
   * The node at the end of the edge is also drawn, highlighted in 'yellow'.
   */
  public void probeEdge(GraphNode from, GraphNode to) {
    drawEdge(from, to, Color.BLUE, 3);
    drawNode(to, NodeType.CURRENT);
  }

  /**
   * Internel helper to draw an edge between two nodes in specified color and line width.
   */
  public void drawEdge(GraphNode from, GraphNode to, Color color,
      int width) {
    gc.setStroke(color);
    gc.setLineWidth(width);
    gc.setLineCap(StrokeLineCap.BUTT);
    gc.strokeLine(from.getLocationX(), from.getLocationY(),
        to.getLocationX(), to.getLocationY());
  }

  /**
   * Draws the graph, without path.
   */
  public void draw() {

    // First connections, then nodes, so that nodes are drawn over the connections
    for (GraphNode node : graph.getNodes()) {
      for (GraphNode connection : node.getConnections()) {
        gc.strokeLine(node.getLocationX(), node.getLocationY(), connection.getLocationX(),
            connection.getLocationY());
      }
    }

    for (GraphNode n : graph.getNodes()) {
      NodeType type =
          n.equals(start) ? NodeType.START
              : n.equals(end) ? NodeType.END: NodeType.NORMAL;
      drawNode(n, type);
    }
  }

  private void drawNode(GraphNode n, NodeType type) {

    switch (type) {
      case START:
        gc.setFill(Color.LIGHTGREEN);
        gc.setLineWidth(3);
        break;
      case END:
        gc.setFill(Color.LIGHTBLUE);
        gc.setLineWidth(3);
        break;
      case PATH:
        gc.setFill(Color.LIGHTCYAN);
        gc.setLineWidth(1);
        break;
      case VISITED:
        gc.setFill(Color.LIGHTGRAY);
        gc.setLineWidth(1);
        break;
      case CURRENT:
        gc.setFill(Color.YELLOW);
        gc.setLineWidth(1);
        break;
      case NORMAL:
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1);
        break;
    }

    // Ellipse background
    gc.fillOval(n.getLocationX() - 20, n.getLocationY() - 10, 2 * 20, 2 * 10);

    // Ellipse border
    gc.setStroke(Color.BLACK);
    gc.strokeOval(n.getLocationX() - 20, n.getLocationY() - 10, 2 * 20, 2 * 10);

    // Text
    gc.setLineWidth(1);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setTextBaseline(VPos.CENTER);
    gc.strokeText("" + n.getId(), n.getLocationX(), n.getLocationY());
  }

  private enum NodeType {
    START, END, PATH, VISITED, CURRENT, NORMAL
  }

}
