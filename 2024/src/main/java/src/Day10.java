package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day10 {

  public static void main(String[] args) throws IOException {
    var forest = new Forest();
    System.out.println("Part 1 is:" + forest.getScores());
  }

  public record Point(int line, int column) {}

  public record Head(Point point, int score) {}

  public static class Forest {

    private List<Head> heads;
    private int[][] forest;

    public Forest() throws IOException {
      Path path = Paths.get("inputs/input10.txt");
      var lines = Files.readAllLines(path);
      heads = new ArrayList<>();
      forest = new int[lines.size()][lines.get(0).length()];
      for (int i = 0; i < lines.size(); i++) {
        for (int j = 0; j < lines.get(i).length(); j++) {
          var val = Integer.parseInt(lines.get(i).substring(j, j+1));
          forest[i][j] = val;
          if (val == 0) {
            heads.add(new Head(new Point(i, j), 0));
          }
        }
      }
    }

    public int getScores() {
      for (Head head : heads) {
        var newScore = getScoreForHead(head);
        heads.set(heads.indexOf(head), new Head(head.point, newScore));
      }
      return heads.stream().mapToInt(Head::score).sum();
    }

    public int getScoreForHead(Head head) {
      var finalPaths = new HashSet<List<Point>>();
      var paths = new ArrayList<List<Point>>();
      paths.add(List.of(head.point));
      while (!paths.isEmpty()) {
        var path = paths.remove(0);
        var last = path.get(path.size() - 1);
        var lastIdx = path.size() - 1;
        if (last.line() > 0 && forest[last.line() - 1][last.column()] == lastIdx + 1) {
          var newPoint = new Point(last.line() - 1, last.column());
          checkPath(path, newPoint, finalPaths, paths);
        }
        if (last.line() < forest.length -1 && forest[last.line() + 1][last.column()] == lastIdx + 1) {
          var newPoint = new Point(last.line() + 1, last.column());
          checkPath(path, newPoint, finalPaths, paths);
        }
        if (last.column() > 0 && forest[last.line()][last.column() - 1] == lastIdx + 1) {
          var newPoint = new Point(last.line(), last.column() - 1);
          checkPath(path, newPoint, finalPaths, paths);
        }
        if (last.column() < forest[0].length - 1 && forest[last.line()][last.column() + 1] == lastIdx + 1) {
          var newPoint = new Point(last.line(), last.column() + 1);
          checkPath(path, newPoint, finalPaths, paths);
        }
      }
      return finalPaths.size();
    }

    public void checkPath(List<Point> path, Point newPoint, HashSet<List<Point>> finalPaths, List<List<Point>> paths) {
      var newPath = new ArrayList<>(path);
      newPath.add(newPoint);
      if (newPath.size() == 10) {
        finalPaths.add(newPath);
        return;
      }
      paths.add(newPath);
    }
  }
}
