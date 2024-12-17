package src;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day13 {

  public static void main(String[] args) throws IOException {
    var equations = buildEquations();
    long score = 0;
    for (var eq : equations) {
      score += eq.tokensToWin();
    }
    System.out.println("Day 13: " + score);
  }

  public record Equation(int xa, int ya, int xb, int yb, long xRes, long yRes) {

    public long tokensToWin() {
      BigDecimal q1 = new BigDecimal(xa).divide(new BigDecimal(ya), 1000, RoundingMode.HALF_DOWN);
      BigDecimal bNum = (new BigDecimal(xRes).subtract(q1.multiply(new BigDecimal(yRes)))).divide(new BigDecimal(xb).subtract(q1.multiply(new BigDecimal(yb))), 1000, RoundingMode.HALF_DOWN);
      BigDecimal aNum = (new BigDecimal(xRes).subtract(bNum.multiply(new BigDecimal(xb)))).divide(new BigDecimal(xa), 1000, RoundingMode.HALF_DOWN);
      double tolerance = 0.001f;
      // Check if bid decimal is close to whole integer

      if (aNum.longValue() > 0 && bNum.longValue() > 0 && isCloseToInteger(aNum, tolerance) && isCloseToInteger(bNum, tolerance)) {
        return getClosestLong(aNum) * 3L + getClosestLong(bNum);
      }
      return 0;
    }
  }

  public static long getClosestLong(BigDecimal value) {
    return value.setScale(0, RoundingMode.HALF_UP).longValue();
  }

  public static boolean isCloseToInteger(BigDecimal value, double tolerance) {
    BigDecimal remainder = value.remainder(BigDecimal.ONE);
    BigDecimal difference = remainder.min(BigDecimal.ONE.subtract(remainder));
    return difference.compareTo(BigDecimal.valueOf(tolerance)) < 0;
  }

  public static List<Equation> buildEquations() throws IOException {
    Path path = Paths.get("inputs/input13.txt");
    List<String> lines = Files.readAllLines(path);
    List<Equation> equations = new ArrayList<>();
    for (int i = 0; i < lines.size(); i +=4) {
      var eqA = lines.get(i).split(":")[1].trim().split(",\\s+");
      var xa = Integer.parseInt(eqA[0].substring(2));
      var ya = Integer.parseInt(eqA[1].substring(2));
      var eqB = lines.get(i + 1).split(":")[1].trim().split(",\\s+");
      var xb = Integer.parseInt(eqB[0].substring(2));
      var yb = Integer.parseInt(eqB[1].substring(2));
      var eqRes = lines.get(i + 2).split(":")[1].trim().split(",\\s+");
      long xRes = Long.parseLong(eqRes[0].substring(2)) + 10000000000000L;
      long yRes = Long.parseLong(eqRes[1].substring(2)) + 10000000000000L;
      equations.add(new Equation(xa, ya, xb, yb, xRes, yRes));
    }
    return equations;
  }
}
