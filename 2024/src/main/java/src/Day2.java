package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day2 {

  public static void main(String[] args) throws IOException {
    var reports = buildLevels();
    var valid = 0;
    for (var report: reports) {
      var allPossibleReports = IntStream.range(0, report.size()).mapToObj(i -> {
        var newReport = new ArrayList<>(report);
        newReport.remove(i);
        return newReport;
      }).toList();
      if (allPossibleReports.stream().anyMatch(Day2::checkValidReport)) {
        valid++;
      }
    }
    System.out.println("Part 2: " + valid);
  }

  public static boolean checkValidReport(List<Integer> report) {
    if (report.get(0) == report.get(1)) {
      return false;
    }
    var increasing = report.get(0) < report.get(1);
    for (int i = 0; i < report.size() - 1; i ++) {
      if (increasing && report.get(i) >= report.get(i + 1)) {
        return false;
      }
      if (!increasing && report.get(i) <= report.get(i + 1)) {
        return false;
      }
      if (Math.abs(report.get(i) - report.get(i + 1)) < 1 || Math.abs(report.get(i) - report.get(i + 1)) > 3) {
        return false;
      }
    }
    return true;
  }

  public static List<List<Integer>> buildLevels() throws IOException {
    Path path = Paths.get("inputs/input2.txt");
    List<String> lines = Files.readAllLines(path);
    List<List<Integer>> reports = new ArrayList<>();
    lines.forEach(l -> {
      var ints = l.split("\\s+");
      var report = new ArrayList<Integer>();
      for (var i = 0; i < ints.length; i++) {
        report.add(Integer.parseInt(ints[i]));
      }
      reports.add(report);
    });
    return reports;
  }
}
