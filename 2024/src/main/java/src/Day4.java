package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day4 {
    public static void main(String[] args) throws IOException {
        AtomicInteger xmasCount = new AtomicInteger();
        var allStrings = getAllStrings();
        var points = allStrings.stream().filter(w ->checkMas(w.word())).map(w -> w.point()).toList();
        var pointSet = new HashSet<>(points);
        System.out.println("Part 1: " + (points.size() - pointSet.size()));
    }

    public static boolean checkXmas(String word) {
      return word.equals("XMAS") || word.equals("SAMX");
    }

    public static boolean checkMas(String word) {
      return word.equals("MAS") || word.equals("SAM");
    }

    public static record Point(int middleI, int middleJ) { }

    public static record Word(String word, Point point) { }

    public static List<Word> getAllStrings() throws IOException {
      Path path = Paths.get("inputs/input4.txt");
      var lines = Files.readAllLines(path);
      var allPoints = new ArrayList<Word>();
      for (int i = 0; i < lines.size(); i++) {
        var line = lines.get(i);
        for (int j = 0; j < line.length(); j++) {
          if (j < line.length() - 2 && i < lines.size() - 2) {
            allPoints.add(new Word(String.format("%s%s%s", lines.get(i).substring(j, j+1), lines.get(i + 1).substring(j + 1, j+2), lines.get(i + 2).substring(j + 2, j+3)),  new Point(i + 1, j + 1)));
          }
          if (j >= 2 && i < lines.size() - 2) {
            allPoints.add(new Word(String.format("%s%s%s", lines.get(i).substring(j, j + 1), lines.get(i + 1).substring(j - 1, j), lines.get(i + 2).substring(j - 2, j - 1)), new Point(i + 1, j - 1)));
          }
        }
      }
      return allPoints;
    }
}
