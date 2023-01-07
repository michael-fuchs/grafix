package mifu.grafix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import mifu.grafix.graph.Graph;
import mifu.grafix.graph.GraphNode;

public class HelloApplication extends Application {

  public static final int WINDOW_WIDTH = 800;
  public static final int WINDOW_HEIGHT = 600;

  @Override
  public void start(Stage stage) throws IOException {
//    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

    Graph graph = new Graph(100, 1000);

    final List<GraphNode> path = graph.findPath(new HashSet<>(100), graph.getNodes().get(0), graph.getNodes().get(99));
    Collections.reverse(path);


    stage.setTitle("Graphix");
    Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    drawGraph(gc, graph, path);

    StackPane root = new StackPane();
    root.getChildren().add(canvas);
    stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
    stage.show();
  }

  private void drawGraph(GraphicsContext gc, Graph graph, List<GraphNode> path) {
    for(GraphNode node : graph.getNodes()) {
      for(GraphNode connection : node.getConnections()) {
        gc.strokeLine(node.getLocationX(), node.getLocationY(), connection.getLocationX(), connection.getLocationY());
      }
    }
    for (GraphNode n : graph.getNodes()) {
      gc.setFill(Color.WHITE);
      gc.fillOval(n.getLocationX() - 20, n.getLocationY() - 20, 2 * 20, 2 * 10);
      gc.strokeText("" + n.getId(), n.getLocationX(), n.getLocationY());
    }

    if (path != null) {
      for (int i = 0; i < path.size() - 1; i++) {
        gc.setStroke(Color.RED);
        gc.strokeLine(path.get(i).getLocationX(), path.get(i).getLocationY(), path.get(i + 1).getLocationX(), path.get(i + 1).getLocationY());
      }
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
