package src;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day1 {
  public static void main(String[] args) throws URISyntaxException, IOException {
    var lists = buildLists();
    var dist = 0;
    for (var i = 0; i < lists.left().size(); i++) {
      dist += Math.abs(lists.left().get(i) - lists.right().get(i));
    }
    System.out.println("Part 1: " + dist);
    AtomicLong sim = new AtomicLong();
    lists.left().forEach(num -> {
      var occs = lists.right().stream().filter(n -> n.equals(num)).count();
      sim.getAndAdd(num * occs);
    });
    System.out.println("Part 2: " + sim.get());
  }

  public record Pair<T, U>(T left, U right) {
  }

  public static Pair<List<Integer>, List<Integer>> buildLists () throws IOException {
    Path path = Paths.get("inputs/input1.txt");
    List<String> lines = Files.readAllLines(path);
    List<Integer> list1 = new ArrayList<>();
    List<Integer> list2 = new ArrayList<>();
    lines.forEach(l -> {
      var ints = l.split("\\s+");
      if (ints.length > 1) {
      list1.add(Integer.parseInt(ints[0]));
      list2.add(Integer.parseInt(ints[1]));
      }
    });
    list1.sort(Integer::compareTo);
    list2.sort(Integer::compareTo);
    return new Pair<>(list1, list2);
  }
}
