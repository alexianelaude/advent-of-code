package main.java;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class Day10 {

  public static void main(String[] args) throws URISyntaxException, IOException {
    Graph graph = Graph.buildGraph();
    graph.findLoopInGraph();
    var tilesNum = graph.findTilesNum();
    System.out.println("Part 2: " + tilesNum);
  }
}

class Graph {
  private final LinkedList<Node> nodeQueue = new LinkedList<>();
  private final List<List<List<Node>>> adjacenceMatrix;
  private final Node start;

  public Graph(List<List<List<Node>>> adjacenceMatrix, Node start) {
    this.adjacenceMatrix = adjacenceMatrix;
    this.start = start;
  }

  record Node(int row, int column) {
  }

  public void printGraph() throws IOException {
    Path path = Paths.get("inputs/input10.txt");
    List<String> lines = Files.readAllLines(path);
    for (int i = 0; i < adjacenceMatrix.size(); i++) {
      for (int j = 0; j < adjacenceMatrix.get(0).size(); j++) {
        if (!nodeQueue.contains(new Node(i, j))) {
          System.out.print(".");
        } else {
          System.out.print(lines.get(i).charAt(j));
        }
      }
      System.out.print("\n");
    }
  }

  public static Graph buildGraph() throws IOException {
    Path path = Paths.get("inputs/input10.txt");
    List<String> lines = Files.readAllLines(path);
    var row_num = lines.size();
    var column_num = lines.get(0).length();
    List<List<List<Node>>> graph = new ArrayList<>();
    for (int i = 0; i < row_num; i++) {
        graph.add(new ArrayList<>());
    }
    for (int i = 0; i < row_num; i++) {
      for (int j = 0; j < column_num; j++) {
        graph.get(i).add(new ArrayList<>());
      }
    }
    Node start = null;
    for (int i = 0; i < row_num; i++) {
      for (int j = 0; j < column_num; j++) {
        var value = lines.get(i).charAt(j);
        if (value == '7') {
          if (i < column_num - 1 && j > 0) {
            graph.get(i).get(j).add(new Node(i, j-1));
            graph.get(i).get(j).add(new Node(i + 1, j));
          }
        }
        if (value == '-') {
          if (j > 0 && j < column_num - 1) {
            graph.get(i).get(j).add(new Node(i, j-1));
            graph.get(i).get(j).add(new Node(i, j+1));
          }
        }
        if (value == 'L') {
          if (i > 0 && j < column_num - 1) {
            graph.get(i).get(j).add(new Node(i-1, j));
            graph.get(i).get(j).add(new Node(i, j+1));
          }
        }
        if (value == 'J') {
          if (i > 0 && j > 0) {
            graph.get(i).get(j).add(new Node(i-1, j));
            graph.get(i).get(j).add(new Node(i, j-1));
          }
        }
        if (value == 'F') {
          if (i < row_num - 1 && j < column_num - 1) {
            graph.get(i).get(j).add(new Node(i, j+1));
            graph.get(i).get(j).add(new Node(i+1, j));
          }
        }
        if (value == '|') {
          if (i < row_num - 1 && i > 0) {
            graph.get(i).get(j).add(new Node(i-1, j));
            graph.get(i).get(j).add(new Node(i+1, j));
          }
        }
        if (value == 'S') {
          start = new Node(i, j);
        }
      }
    }
    // The neighbors of the start are all nodes who have the start has neighbor
    for (int i = 0; i < row_num; i++) {
      for (int j = 0; j < column_num; j++) {
        if (graph.get(i).get(j).contains(start)) {
          graph.get(start.row()).get(start.column()).add(new Node(i, j));
        }
      }
    }
    return new Graph(graph, start);
  }

    public void findLoopInGraph() {
      nodeQueue.add(start);
      var currentNode = start;
      while (true) {
        var nextNode = adjacenceMatrix.get(currentNode.row()).get(currentNode.column()).stream().filter(node -> !nodeQueue.contains(node)).findFirst();
        if (nextNode.isEmpty()) {
          break;
        }
        else {
          nodeQueue.add(nextNode.get());
          currentNode = nextNode.get();
        }
      }
    }

    public int findTilesNum() {
      var area = 0;
      for (int i = 0; i < nodeQueue.size(); i++) {
        var pi = nodeQueue.get(i);
        var pi_next = nodeQueue.get((i + 1) % nodeQueue.size());
        area += (pi.row() - pi_next.row()) * (pi.column() + pi_next.column());
      }
      area = area / 2;
      return area - (nodeQueue.size() / 2) + 1;
    }
}


