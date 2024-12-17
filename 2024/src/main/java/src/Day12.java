package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day12 {

  public record Point(int line, int column) {
    public boolean isNeighbour(Point other) {
      return Math.abs(this.line - other.line) + Math.abs(this.column - other.column) == 1;
    }
  }

  record Side(int line, int column, boolean isHorizontal, boolean aboveOrLeft) implements Comparable<Side> {
    @Override
    public int compareTo(Side other) {
      if (this.isHorizontal != other.isHorizontal) {
        return Boolean.compare(this.isHorizontal, other.isHorizontal) * 10;
      }
      if (this.aboveOrLeft != other.aboveOrLeft) {
        return Boolean.compare(this.aboveOrLeft, other.aboveOrLeft) * 10;
      }
      if (isHorizontal) {
        if (this.line != other.line) {
          return (this.line - other.line) * 10;
        }
        return + (this.column - other.column);
      }
      if (this.column != other.column) {
        return (this.column - other.column) * 10;
      }
      return (this.line - other.line);
    }
  }

  public static class Region {

    public List<Point> points;
    public char value;

    public Region(char value) {
      this.points = new ArrayList<>();
      this.value = value;
    }

    public int perimeter() {
      var perimeter = 0;
      for (var point : points) {
        var neighbours = points.stream().filter(p -> p.isNeighbour(point)).count();
        perimeter += 4 - (int) neighbours;
      }
      return perimeter;
    }

    public long newPerimeter() {
      var sides = new ArrayList<Side>();
      for (var point : points) {
        var neighbours = points.stream().filter(p -> p.isNeighbour(point)).toList();
        var tempSides = new ArrayList<Side>(List.of(new Side(point.line, point.column, true, false), new Side(point.line, point.column, false, false), new Side(point.line + 1,  point.column,true, true), new Side(point.line, point.column + 1, false, true)));
        for (var neighbour : neighbours) {
          if (neighbour.line == point.line && neighbour.column == point.column + 1) {
            tempSides.remove(new Side(point.line, point.column + 1, false, true));
          }
          if (neighbour.line == point.line && neighbour.column == point.column - 1) {
            tempSides.remove(new Side(point.line, point.column, false, false));
          }
          if (neighbour.line == point.line + 1 && neighbour.column == point.column) {
            tempSides.remove(new Side(point.line + 1, point.column, true, true));
          }
          if (neighbour.line == point.line - 1 && neighbour.column == point.column) {
            tempSides.remove(new Side(point.line, point.column, true, false));
          }
        }
        sides.addAll(tempSides);
      }
      sides.sort(Side::compareTo);
      long sum = 1;
      for (int i = 0; i < sides.size() - 1; i++) {
        if (Math.abs(sides.get(i).compareTo(sides.get(i+1))) > 1) {
          sum++;
        }
      }
      return sum;
    }

    public int price() {
      return points.size() * perimeter();
    }

    public long newPrice() {
      return points.size() * newPerimeter();
    }
  }

  public static class Map {
    public List<Region> regions;
    public int length;
    public int width;

    public Map() throws IOException {
      Path path = Paths.get("inputs/input12.txt");
      var lines = Files.readAllLines(path);
      length = lines.size();
      width = lines.get(0).length();
      List<Point> visited = new ArrayList<>();
      List<Point> toVisit = new ArrayList<>();
      regions = new ArrayList<>();
      for (int i = 0; i < length; i++) {
        for (int j = 0; j < width; j++) {
          toVisit.add(new Point(i, j));
        }
      }
      while (visited.size() < length * width) {
        var p = toVisit.get(0);
        var newRegion = new Region(lines.get(p.line).charAt(p.column));
        visitPoint(p, newRegion, visited, toVisit, lines);
        regions.add(newRegion);
      }
    }

    public static void visitPoint(Point p, Region currentRegion, List<Point> visited, List<Point> toVisit, List<String> lines) {
      currentRegion.points.add(p);
      visited.add(p);
      toVisit.remove(p);
      if (p.column < lines.get(0).length() - 1 && lines.get(p.line).charAt(p.column + 1) == currentRegion.value) {
        var newPoint = new Point(p.line, p.column + 1);
        if (!visited.contains(newPoint)) {
          visitPoint(new Point(p.line, p.column + 1), currentRegion, visited, toVisit, lines);
        }
      }
      if (p.column > 0 && lines.get(p.line).charAt(p.column - 1) == currentRegion.value) {
        var newPoint = new Point(p.line, p.column - 1);
        if (!visited.contains(newPoint)) {
          visitPoint(new Point(p.line, p.column - 1), currentRegion, visited, toVisit, lines);
        }
      }
      if (p.line < lines.size() - 1 && lines.get(p.line + 1).charAt(p.column) == currentRegion.value) {
        var newPoint = new Point(p.line + 1, p.column);
        if (!visited.contains(newPoint)) {
          visitPoint(new Point(p.line + 1, p.column), currentRegion, visited, toVisit, lines);
        }
      }
      if (p.line > 0 && lines.get(p.line - 1).charAt(p.column) == currentRegion.value) {
        var newPoint = new Point(p.line - 1, p.column);
        if (!visited.contains(newPoint)) {
          visitPoint(new Point(p.line - 1, p.column), currentRegion, visited, toVisit, lines);
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    var map = new Map();
    long prices = map.regions.stream().mapToLong(Region::price).sum();
    System.out.println("Part 1: " + prices);
    long newPrices = map.regions.stream().mapToLong(Region::newPrice).sum();
    System.out.println("Part 2: " + newPrices);
  }

}
