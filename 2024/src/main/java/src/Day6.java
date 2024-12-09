package src;

import static src.Day6.Direction.DOWN;
import static src.Day6.Direction.LEFT;
import static src.Day6.Direction.RIGHT;
import static src.Day6.Direction.UP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Day6 {

  public static void main(String[] args) throws IOException {
    var puzzleMap = new PuzzleMap();
    var lineNumber = puzzleMap.lineNumber;
    var columnNumber = puzzleMap.columnNumber;
    System.out.println("Part 1 is:" + puzzleMap.numberOfGuardPositions());
    System.out.println("Part 2 is:" + numberOfCycles(lineNumber, columnNumber));
  }

  public static int numberOfCycles(int lineNumber, int columnNumber) throws IOException {
    var cycles = 0;
    for (int i = 0; i < lineNumber; i++) {
      for (int j = 0; j < columnNumber; j++) {
        var puzzleMap = new PuzzleMap();
        if (!puzzleMap.isObstacle(new Point(i, j)) && !Objects.equals(puzzleMap.guard,new Point(i, j))) {
          if (puzzleMap.createsCycle(new Point(i, j))) {
            cycles++;
          }
        }
      }
    }
    return cycles;
  }

  public record Point(int line, int column) {}

  public enum Direction {
    UP, DOWN, LEFT, RIGHT
  }

  public static class PuzzleMap {
    private List<Point> obstacles;
    private Point guard;
    private final int lineNumber;
    private final int columnNumber;
    private Direction guardDirection;

    public PuzzleMap() throws IOException {
      Path path = Paths.get("inputs/input6.txt");
      var lines = Files.readAllLines(path);
      this.lineNumber = lines.size();
      this.columnNumber = lines.get(0).length();
      this.guardDirection = UP;
      var obstacles = new ArrayList<Point>();
      for (int i = 0; i < lines.size(); i++) {
        for (int j = 0; j < lines.get(i).length(); j++) {
          var val = lines.get(i).charAt(j);
          if (val == '#') {
            obstacles.add(new Point(i, j));
          }
          if (val == '^') {
            guard = new Point(i, j);
          }
        }
      }
      this.obstacles = obstacles;
    }

    public boolean isObstacle(Point point) {
      return obstacles.contains(point);
    }

    // Return true if guard is outside of Map
    public boolean moveGuard() {
      Point newPos;
      switch (guardDirection) {
        case UP:
          newPos = new Point(guard.line - 1, guard.column);
          if (newPos.line < 0) {
            return true;
          }
          if (isObstacle(newPos)) {
            guardDirection = RIGHT;
          } else {
            guard = newPos;
          }
          break;
        case DOWN:
          newPos = new Point(guard.line + 1, guard.column);
          if (newPos.line >= lineNumber) {
            return true;
          }
          if (isObstacle(newPos)) {
            guardDirection = LEFT;
          } else {
            guard = newPos;
          }
          break;
        case LEFT:
          newPos = new Point(guard.line, guard.column - 1);
          if (newPos.column < 0) {
            return true;
          }
          if (isObstacle(newPos)) {
            guardDirection = UP;
          } else {
            guard = newPos;
          }
          break;
        case RIGHT:
          newPos = new Point(guard.line, guard.column + 1);
          if (newPos.column >= columnNumber) {
            return true;
          }
          if (isObstacle(newPos)) {
            guardDirection = DOWN;
          } else {
            guard = newPos;
          }
          break;
      }
      return false;
    }

    public int numberOfGuardPositions() {
      var positions = new HashSet<Point>();
      positions.add(guard);
      while (!moveGuard()) {
        positions.add(guard);
      }
      return positions.size();
    }

    public record PositionAndDirection(Point position, Direction direction) {}

    public boolean createsCycle(Point newObstacle) {
      var positionsAndDirection = new HashSet<PositionAndDirection>();
      obstacles.add(newObstacle);
      while (!moveGuard()) {
        var currentPositionAndDirection = new PositionAndDirection(guard, guardDirection);
        if (positionsAndDirection.contains(currentPositionAndDirection)) {
          return true;
        } else {
          positionsAndDirection.add(new PositionAndDirection(guard, guardDirection));
        }
      }
      return false;
    }


  }
}
