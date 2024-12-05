package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("inputs/input3.txt");
    var instr = Files.readString(path);
    Pattern pattern = Pattern.compile("mul\\([0-9]{1,3},[0-9]{1,3}\\)");
    Matcher matcher = pattern.matcher(instr);
    var doBounds = getBounds(instr, "do\\(\\)");
    var dontBounds = getBounds(instr,"don't\\(\\)");
    var sum = 0;
    while (matcher.find()) {
      var match = matcher.group();
      var closestDo = doBounds.stream().filter(b -> b < matcher.start()).max(Integer::compareTo).orElse(0);
      var closestDont = dontBounds.stream().filter(b -> b < matcher.start()).max(Integer::compareTo).orElse(-1);
      if (closestDo > closestDont) {
        var nums = match.substring(4, match.length() - 1).split(",");
        sum += Integer.parseInt(nums[0]) * Integer.parseInt(nums[1]);
      }
    }
    System.out.println("Part 2: " + sum);
  }

  public static List<Integer> getBounds(String instr, String pattern) {
    List<Integer> indexes = new ArrayList<>();
    Matcher matcher = Pattern.compile(pattern).matcher(instr);
    while (matcher.find()) {
      indexes.add(matcher.start());
    }
    return indexes;
    }
}
