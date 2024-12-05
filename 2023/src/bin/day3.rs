use std::cmp::min;
use std::fs::read_to_string;
use std::str::Lines;

fn main() {
    let sum = part2();
    println!("Part 2: {}", sum);
}

fn part1() -> i32 {
    let binding = read_to_string("inputs/input3.txt").unwrap();
    let file_lines = binding.lines();
    let mut part_sum = 0;
    for (line_num, line) in file_lines.enumerate() {
        let mut line_ix = 0;
        let mut temp_chars : Vec<char> = Vec::new();
        while line_ix < line.len() {
            let char = line.chars().nth(line_ix).unwrap();
            if char.is_numeric() {
                temp_chars.push(char);
                line_ix += 1;
                if line_ix == line.len() {
                    let num_str : String = temp_chars.clone().into_iter().map(|s| s.to_string())
                        .reduce(|cur: String, nxt: String| cur + &nxt)
                        .unwrap();
                    let num =  num_str.parse::<i32>().unwrap();
                    if is_valid_num(line_ix, temp_chars.len(), line_num, binding.lines().collect::<Vec<&str>>()) {
                        part_sum += num;
                    }
                }
            }
            else {
                if !temp_chars.is_empty() {
                    let num_str : String = temp_chars.clone().into_iter().map(|s| s.to_string())
                        .reduce(|cur: String, nxt: String| cur + &nxt)
                        .unwrap();
                    let num =  num_str.parse::<i32>().unwrap();
                    if is_valid_num(line_ix, temp_chars.len(), line_num, binding.lines().collect::<Vec<&str>>()) {
                        part_sum += num;
                    }
                    else {
                        println!("Invalid number: {}", num);
                    }
                    temp_chars.clear();
                }
                line_ix += 1;
            }
        }
    }
    return part_sum;
}

fn is_valid_num(line_ix: usize, num_len: usize, line_num: usize, file_lines: Vec<&str>) -> bool {
    if line_ix >= 1 + num_len && file_lines.get(line_num).unwrap().chars().nth(line_ix - 1 - num_len).is_some_and(|x| x != '.') {
        return true;
    }
    if file_lines.get(line_num).unwrap().chars().nth(line_ix).is_some_and(|x| x != '.') {
        return true;
    }
    if line_num >= 1 && file_lines.get(line_num - 1).is_some() {
        let start = if line_ix < num_len + 1 { 0 } else { line_ix - num_len - 1 };
        for i in start..line_ix + 1 {
            if file_lines.get(line_num - 1).unwrap().chars().nth(i).is_some_and(|x| x != '.') {
                return true;
            }
        }
    }
    if file_lines.get(line_num + 1).is_some() {
        let start = if line_ix < num_len + 1 { 0 } else { line_ix - num_len - 1 };
        for i  in start..line_ix + 1 {
            if file_lines.get(line_num + 1).unwrap().chars().nth(i).is_some_and(|x| x != '.') {
                return true;
            }
        }
    }
    return false;
}

struct Gear {
    num: i32,
    gear_pos : (usize, usize),
}

fn part2() -> i32 {
    let binding = read_to_string("inputs/input3.txt").unwrap();
    let file_lines = binding.lines();
    let mut gears : Vec<Gear> = Vec::new();
    for (line_num, line) in file_lines.enumerate() {
        let mut line_ix = 0;
        let mut temp_chars : Vec<char> = Vec::new();
        while line_ix < line.len() {
            let char = line.chars().nth(line_ix).unwrap();
            if char.is_numeric() {
                temp_chars.push(char);
                line_ix += 1;
                if line_ix == line.len() {
                    let num_str : String = temp_chars.clone().into_iter().map(|s| s.to_string())
                        .reduce(|cur: String, nxt: String| cur + &nxt)
                        .unwrap();
                    let num =  num_str.parse::<i32>().unwrap();
                    let gear_pos = gear_pos_for_num(line_ix, temp_chars.len(), line_num, binding.lines().collect::<Vec<&str>>());
                    if gear_pos.is_some() {
                        gears.push(Gear { num: num, gear_pos: gear_pos.unwrap() });
                    }
                }
            }
            else {
                if !temp_chars.is_empty() {
                    let num_str : String = temp_chars.clone().into_iter().map(|s| s.to_string())
                        .reduce(|cur: String, nxt: String| cur + &nxt)
                        .unwrap();
                    let num =  num_str.parse::<i32>().unwrap();
                    let gear_pos = gear_pos_for_num(line_ix, temp_chars.len(), line_num, binding.lines().collect::<Vec<&str>>());
                    if gear_pos.is_some() {
                        gears.push(Gear { num: num, gear_pos: gear_pos.unwrap() });
                    }
                    temp_chars.clear();
                }
                line_ix += 1;
            }
        }
    }
    // Now that we have all the gear positions, we combine them and sum their ratio
    let mut gear_sum = 0;
    for (i, gear) in gears.iter().enumerate() {
        for (j, other_gear) in gears.iter().enumerate() {
            if gear.gear_pos == other_gear.gear_pos && i != j  {
                gear_sum += gear.num * other_gear.num;
            }
        }
    }
    // Every pair is counted twice, so we divide by 2
    return gear_sum / 2;
}

fn gear_pos_for_num(line_ix: usize, num_len: usize, line_num: usize, file_lines: Vec<&str>) -> Option<(usize, usize)> {
    if line_ix >= 1 + num_len && file_lines.get(line_num).unwrap().chars().nth(line_ix - 1 - num_len).is_some_and(|x| x == '*') {
        return Some((line_num, line_ix - 1 - num_len));
    }
    if file_lines.get(line_num).unwrap().chars().nth(line_ix).is_some_and(|x| x == '*') {
        return Some((line_num, line_ix));
    }
    if line_num >= 1 && file_lines.get(line_num - 1).is_some() {
        let start = if line_ix < num_len + 1 { 0 } else { line_ix - num_len - 1 };
        for i in start..line_ix + 1 {
            if file_lines.get(line_num - 1).unwrap().chars().nth(i).is_some_and(|x| x == '*') {
                return Some((line_num - 1, i));
            }
        }
    }
    if file_lines.get(line_num + 1).is_some() {
        let start = if line_ix < num_len + 1 { 0 } else { line_ix - num_len - 1 };
        for i  in start..line_ix + 1 {
            if file_lines.get(line_num + 1).unwrap().chars().nth(i).is_some_and(|x| x == '*') {
                return Some((line_num + 1, i));
            }
        }
    }
    return None;
}

