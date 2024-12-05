package src;

import com.google.common.collect.Comparators;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

  public static void main(String[] args) throws IOException {
    var comparator = new PageComparator();
    Path path = Paths.get("inputs/input5.txt");
    var lines = Files.readAllLines(path);
    var sum = 0;
    var incorrectSum = 0;
    for (int i = 1177; i < lines.size(); i++) {
      var update = Arrays.stream(lines.get(i).split(",")).map(Integer::parseInt).collect(Collectors.toList());
      if (Comparators.isInOrder(update, comparator)) {
        sum += update.get(update.size() / 2);
      }
      else {
        update.sort(comparator);
        incorrectSum += update.get(update.size() / 2);
      }
    }
    System.out.println("Part 1 is:" + sum);
    System.out.println("Part 2 is:" + incorrectSum);
  }

  static class PageComparator implements Comparator<Integer> {

    public record ComparingPairs(Integer left, Integer right) {}

    List<ComparingPairs> pairs;

    public PageComparator() throws IOException {
      Path path = Paths.get("inputs/input5.txt");
      var lines = Files.readAllLines(path);
      var pairs = new ArrayList<ComparingPairs>();
      for (int i = 0; i < 1176; i++) {
        var l = lines.get(i);
        var nums = l.split("\\|");
        pairs.add(new ComparingPairs(Integer.parseInt(nums[0]), Integer.parseInt(nums[1])));
      }
      this.pairs = pairs;
    }

    @Override
    public int compare(Integer i1, Integer i2) {
      if (pairs.stream().anyMatch(p -> p.left.equals(i1) && p.right.equals(i2))) {
        return -1;
      }
      if (pairs.stream().anyMatch(p -> p.left.equals(i2) && p.right.equals(i1))) {
        return 1;
      }
      return 0; // Ints are equal
    }
  }
}
