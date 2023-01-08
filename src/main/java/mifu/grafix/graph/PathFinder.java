package mifu.grafix.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiConsumer;


/**
 * A path finder that uses a depth-first search algorithm to find a path between two nodes.
 * It is a bit complicated to allow for the UI stepping, i.e. it is called from the UI thread
 * step by step and progresses. Thus it is not a recursive implementation, but instead uses a stack
 * to keep track of the current state (i.e. the current node and the next connection to try).
 */
public class PathFinder {

  private final GraphNode end;

  private final Set<GraphNode> visitedNodes = new HashSet<>();


  private Stack<PathFinderContext> finderContextStack = new Stack<>();

  boolean finished = false;


  ///////////////////////////////////////////////////////////////////////////
  //
  // Internal class represent the search context for a single node
  //
  ///////////////////////////////////////////////////////////////////////////

  private class PathFinderContext {
    final GraphNode currentNode;
    final List<GraphNode> visitedConnections = new ArrayList<>();
    final List<GraphNode> remainingConnections;

    public PathFinderContext(GraphNode currentNode) {
      this.currentNode = currentNode;
      this.remainingConnections = currentNode.getConnections();
    }

    public GraphNode getCurrentNode() {
      return currentNode;
    }
  }

  /** Data returned from each step to allow the UI to update. */
  public record StepResult(GraphNode currentNode, GraphNode nextNode, boolean probed) {}

  /**
   * Constructor. Does not require the graph itself, since the finder starts at 'start' and
   * just uses the connections of the nodes to search through the graph.
   * @param start the start node
   * @param end the end node
   */
  public PathFinder(GraphNode start, GraphNode end) {
    this.end = end;
    this.finderContextStack.push(new PathFinderContext(start));
    this.visitedNodes.add(start);
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Implementation
  //
  ///////////////////////////////////////////////////////////////////////////


  public StepResult step() {
    final boolean probed; // if false 'backtracked'

    if (finished)
      return null;

    final PathFinderContext lastContext = finderContextStack.peek();
    final GraphNode current = lastContext.currentNode;
    if (current == end) {
      finished = true;
      return null;
    } else {
        // We are backtracking
        if (lastContext.remainingConnections.isEmpty()) {
          // We have no more connections to try, so we need to backtrack further
          finderContextStack.pop();
          if (finderContextStack.isEmpty()) {
            // We have no more nodes to backtrack to, so we are done
            finished = true;
            return null;
          }
          return new StepResult(finderContextStack.peek().currentNode, current, false);
        } else {
          // We have more connections to try, so we need to try the next one
          final GraphNode next = lastContext.remainingConnections.remove(0);
          if (!visitedNodes.contains(next)) {
            // We have not visited this node yet, so we need to probe it
            lastContext.visitedConnections.add(next);
            visitedNodes.add(next);
            finderContextStack.push(new PathFinderContext(next));
            return new StepResult(next, current, true);
          } else {
            // We have already visited this node, so we need to try the next one
            return null;
          }
        }
      }
    }

  /**
   * Returns true, the path finder is finished, i.e. if a path is found or if there is no path.
   */
  public boolean isFinished() {
      return finished;
    }

  /**
   * Returns the path extracted from the current state of the path finder. After the path finder
   * is finished, this method can be called to get the full path.
   *
   * If the path finder is not finished, this method returns the current path where the path finder
   * is searching.
   */
  public List<GraphNode> getPath() {
      return finderContextStack.stream().map(PathFinderContext::getCurrentNode).toList();
    }
 }
