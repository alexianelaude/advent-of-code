package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 {

  public static int LINE_NUMBER = 103;
  public static int COLUMN_NUMBER = 101;

  public static class Robot {
    int line;
    int column;
    int lineMove;
    int columnMove;

    public Robot(int column, int line, int columnMove, int lineMove) {
      this.line = line;
      this.column = column;
      this.lineMove = lineMove;
      this.columnMove = columnMove;
    }

    public void move() {
      this.line = Math.floorMod(this.line + this.lineMove, LINE_NUMBER);
      this.column = Math.floorMod(this.column + this.columnMove, COLUMN_NUMBER);
    }
  }

  public static record Point(int line, int column) {
    public boolean counts() {
      return this.line != (LINE_NUMBER / 2) && this.column != (COLUMN_NUMBER / 2);
    }
    public int quarter() {
      if (this.line < LINE_NUMBER / 2) {
        if (this.column < COLUMN_NUMBER / 2) {
          return 1;
        }
        return 2;
      }
      if (this.column < COLUMN_NUMBER / 2) {
        return 3;
      }
      return 4;
    }
  }

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("inputs/input14.txt");
    List<String> lines = Files.readAllLines(path);
    List<Robot> robots = new ArrayList<>();
    for (var l : lines) {
      var parts = l.split(" ");
      var pos = parts[0].split(",");
      var move = parts[1].split(",");
      robots.add(new Robot(Integer.parseInt(pos[0].substring(2)), Integer.parseInt(pos[1]), Integer.parseInt(move[0].substring(2)), Integer.parseInt(move[1])));
    }
    for (int i = 0; i < 10000; i++) {
      robots.forEach(Robot::move);
      HashMap<Point, Boolean> robotsMap = new HashMap<>();
      for (var r : robots) {
        var p = new Point(r.line, r.column);
        robotsMap.putIfAbsent(p, true);
      }
      printRobots(robotsMap, i + 1);
    }
  }

  public static void printRobots(Map<Point, Boolean> robotsMap, int seconds) {
    System.out.println("After " + seconds + " seconds:");
    for (int i = 0; i < LINE_NUMBER; i++) {
      for (int j = 0; j < COLUMN_NUMBER; j++) {
        if (robotsMap.getOrDefault(new Point(i, j), false)) {
          System.out.print("X");
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
  }
}
