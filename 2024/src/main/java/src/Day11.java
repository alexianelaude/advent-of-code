package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11 {

  public record StoneWithBlinks(Stone startStone, int blinks) {}

  public static Map<StoneWithBlinks, Long> numberAfterBlinks = new HashMap<>();

  public record Stone(long value) {

    public List<Stone> blink() {
      if (value == 0) {
        return List.of(new Stone(1));
      } else {
        var valStr = String.valueOf(value);
        if (valStr.length() % 2 == 0) {
          return List.of(new Stone(Long.parseLong(valStr.substring(0, valStr.length() / 2))), new Stone(Long.parseLong(valStr.substring(valStr.length() / 2, valStr.length()))));
        } else {
          return List.of(new Stone(value * 2024));
        }
      }
    }
  }

  public static long blink(Stone start, int blinkNb) {
    if (numberAfterBlinks.containsKey(new StoneWithBlinks(start, blinkNb))) {
      return numberAfterBlinks.get(new StoneWithBlinks(start, blinkNb));
    }
    var afterBlink = start.blink();
    if (blinkNb == 1) {
      return afterBlink.size();
    }
    long sum = 0;
    for (var stone : afterBlink) {
      sum += blink(stone, blinkNb - 1);
    }
    numberAfterBlinks.put(new StoneWithBlinks(start, blinkNb), sum);
    return sum;
  }

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("inputs/input11.txt");
    var line = Files.readAllLines(path).get(0);
    var ints = line.split(" ");
    var stones = Arrays.stream(ints).map(Integer::parseInt).map(Stone::new).collect(Collectors.toList());
    long sum = 0;
    for (var stone : stones) {
      sum += blink(stone, 75);
    }
    System.out.println("Part 1: " + sum);
  }
}
