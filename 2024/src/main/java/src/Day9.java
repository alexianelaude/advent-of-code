package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day9 {

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("inputs/input9.txt");
    LinkedList<Integer> spaces = new LinkedList<>();
    var line = Files.readAllLines(path).get(0);
    int fileId = 0;
    for (int i = 0; i < line.length(); i+= 2) {
      var fileNum = Integer.parseInt(line.substring(i, i+1));
      for (int j = 0; j < fileNum; j ++) {
        spaces.add(fileId);
      }
      if (i == line.length()-1) {
        continue;
      }
      var spaceNum = Integer.parseInt(line.substring(i+1, i+2));
      for (int j = 0; j < spaceNum; j ++) {
        spaces.add(-1);
      }
      fileId++;
    }
    List<Integer> compactedSpaces = new ArrayList<>();
    while (!spaces.stream().allMatch(i -> i == -1)) {
      var first = spaces.poll();
      if (first == -1) {
        var last = spaces.pollLast();
        while (last == -1) {
          last = spaces.pollLast();
        }
        compactedSpaces.add(last);
      }
      else {
        compactedSpaces.add(first);
      }
    }
    long checkSum = 0;
    for (int i = 0; i < compactedSpaces.size(); i++) {
      checkSum += (long) i * compactedSpaces.get(i);
    }
    System.out.println("Part 1: " + checkSum);
  }
}
