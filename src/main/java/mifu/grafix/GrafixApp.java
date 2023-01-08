package mifu.grafix;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import mifu.grafix.graph.GraFX;
import mifu.grafix.graph.Graph;
import mifu.grafix.graph.GraphNode;
import mifu.grafix.graph.PathFinder;
import mifu.grafix.graph.PathFinder.StepResult;

public class GrafixApp extends Application {

  public static final int WINDOW_WIDTH = 1600;
  public static final int WINDOW_HEIGHT = 1000;


  public static void main(String[] args) {
    launch();
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Application instance
  //
  ///////////////////////////////////////////////////////////////////////////

  private Timer timer = new Timer();

  @Override
  public void start(Stage stage) throws IOException {

    // Create a random graph
    Graph graph = new Graph(40, 4);

    final GraphNode start = graph.getNodes().get(0);
    final GraphNode end = graph.getNodes().get(graph.getNodes().size() - 1);


    stage.setTitle("Graphix");
    Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    GraFX grafx = new GraFX(gc, graph, start, end);
    grafx.draw();

    StackPane root = new StackPane();
    root.getChildren().add(canvas);
    stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
    stage.show();

    PathFinder pathFinder = new PathFinder(start, end);
    Stepper stepper = new Stepper(grafx, pathFinder);
    stepper.start();
  }



//    final List<GraphNode> path = graph.findPath(new HashSet<>(), start, end,
//        (n1, n2) -> probeEdge(gc, n1, n2),
//        (n1, n2) -> resetEdge(gc, n1, n2));
//
//    Collections.reverse(path);
//    drawPath(gc, path);


  /**
   * Controls the animation with delay between the single steps.
   */
  public static class Stepper {

    GraFX grafx;
    PathFinder pathFinder;
    // The executor service is used to schedule the next step with a delay. It ensures
    // that waiting for the delay does not block the UI thread (which would freeze the UI)
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public Stepper(GraFX grafx, PathFinder pathFinder) {
      this.grafx = grafx;
      this.pathFinder = pathFinder;
    }

    public void start() {
      executorService.schedule(this::nextStep, 100, TimeUnit.MILLISECONDS);
    }

    private void nextStep() {
      // Next step in the graph (without drawing).
      final StepResult stepResult = pathFinder.step();
      if (stepResult != null) {
        // Draw the edge that has been probed or backtracked in the current step.
        // The 'runLater()' is required by JavaFX to run the drawing on the UI thread.
        Platform.runLater(() -> {
          if (stepResult.probed()) {
            grafx.probeEdge(stepResult.currentNode(), stepResult.nextNode());
          } else {
            grafx.resetEdge(stepResult.currentNode(), stepResult.nextNode());
          }
        });
      }

      if (!pathFinder.isFinished()) {
        // No path found yet, continue with the next step, but with a delay.
        executorService.schedule(this::nextStep, 200, TimeUnit.MILLISECONDS);
      } else {
        // Path found, draw it. The stepper finishes here.
        Platform.runLater(() -> {
          grafx.drawPath(pathFinder.getPath());
        });
      }
    }

  }
}
