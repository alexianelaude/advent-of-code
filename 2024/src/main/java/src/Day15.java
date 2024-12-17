package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day15 {

  public static void main(String[] args) throws IOException {
    var warehouse = new Warehouse();
    warehouse.moveRobot();
    var sum = 0;
    for (var box : warehouse.boxes) {
      sum += box.position.line * 100 + box.position.column;
    }
    System.out.println("Part 1: " + sum);
  }

  public record Position(int line, int column) {
    public Position move(char command) {
      return switch (command) {
        case '>' -> new Position(this.line, this.column + 1);
        case '<' -> new Position(this.line, this.column - 1);
        case '^' -> new Position(this.line - 1, this.column);
        case 'v' -> new Position(this.line + 1, this.column);
        default -> throw new IllegalStateException("Unexpected value: " + command);
      };
    }
  }

  public static class Robot {
    public Position position;

    public Robot(int line, int column) {
      this.position = new Position(line, column);
    }
  }

  public static class Box {
    public Position position;

    public Box(int line, int column) {
      this.position = new Position(line, column);
    }
  }

  public static class Warehouse {

    public List<Box> boxes;
    public Robot robot;
    public List<Position> walls;
    public List<Character> commands;

    public Warehouse() throws IOException {
      Path path = Paths.get("inputs/input15.txt");
      List<String> lines = Files.readAllLines(path);
      boxes = new ArrayList<>();
      walls = new ArrayList<>();
      commands = new ArrayList<>();
      for (int i = 0; i < lines.size(); i++) {
        if (lines.get(i).startsWith("#")) {
          for (int j = 0; j < lines.get(i).length(); j++) {
            if (lines.get(i).charAt(j) == '#') {
              walls.add(new Position(i, j));
            }
            if (lines.get(i).charAt(j) == 'O') {
              boxes.add(new Box(i, j));
            }
            if (lines.get(i).charAt(j) == '@') {
              robot = new Robot(i, j);
            }
          }
        }
        else if (!lines.get(i).isBlank()) {
          for (int j = 0; j < lines.get(i).length(); j++) {
            commands.add(lines.get(i).charAt(j));
          }
        }
      }
    }

    public boolean tryMove(Position position, char command) {
      var newPosition = position.move(command);
      if (walls.contains(newPosition)) {
        return false;
      }
      if (boxes.stream().anyMatch(box -> box.position.equals(newPosition))) {
        Optional<Box> box = boxes.stream().filter(b -> b.position.equals(newPosition)).findFirst();
        if (box.isPresent()) {
          return tryMove(box.get().position, command);
        }
      }
      return true;
    }

    public void move(Position newPosition, char command) {
      if (boxes.stream().anyMatch(box -> box.position.equals(newPosition))) {
        Optional<Box> box = boxes.stream().filter(b -> b.position.equals(newPosition)).findFirst();
        if (box.isPresent()) {
          var newBoxPosition = box.get().position.move(command);
          move(newBoxPosition, command);
          box.get().position = newBoxPosition;
        }
      }
    }

    public void moveRobot() {
      for (var command : commands) {
        if (command != '\n') {
          if (tryMove(robot.position, command)) {
            var newPosition = robot.position.move(command);
            move(newPosition, command);
            robot.position = newPosition;
          }
        }
      }
    }
  }
}
