use std::collections::{HashMap, HashSet};
use std::fs::read_to_string;
use std::ops::Deref;

fn main() {
    let sum = part1();
    println!("Part 1: {}", sum);
}

fn part1() -> i64 {
    let locations = locations();
    return *locations.iter().min().unwrap();
}

struct Remap {
    initial_value: i64,
    final_value: Option<i64>
}
fn locations() -> Vec<i64> {
    let binding = read_to_string("inputs/input5.txt").unwrap();
    let lines = binding.lines().collect::<Vec<&str>>();
    let seed_line = lines.get(0).unwrap();
    let seed_list = seed_line.split(":").collect::<Vec<&str>>().get(1).unwrap().trim().split(" ").collect::<Vec<&str>>();
    let mut seed_pairs = seed_list.iter().map(|x| x.parse::<i64>().unwrap()).collect::<Vec<i64>>();
    let mut seeds = Vec::new();
    for j in 0..seed_pairs.len()  / 2 {
        let pair_range = (*seed_pairs.get(j * 2).unwrap()..*seed_pairs.get(j * 2 + 1).unwrap() + *seed_pairs.get(j * 2).unwrap()).collect::<Vec<i64>>();
        seeds.extend(pair_range);
    }
    let mut i = 1;
    while i < lines.len() {
        if lines.get(i).unwrap().is_empty() {
            i +=1;
            continue;
        }
        if lines.get(i).unwrap().starts_with(char::is_alphabetic) {
            i +=1;
            let mut tempSeeds = seeds.iter().map(|x| Remap { initial_value: *x, final_value: None }).collect::<Vec<Remap>>();
            while i < lines.len() && !lines.get(i).unwrap().is_empty() {
                let split_line = lines.get(i).unwrap().split(" ").collect::<Vec<&str>>();
                let dest = split_line.get(0).unwrap().parse::<i64>().unwrap();
                let source = split_line.get(1).unwrap().parse::<i64>().unwrap();
                let offset = split_line.get(2).unwrap().parse::<i64>().unwrap();
                for j in 0..tempSeeds.len() {
                    let s = tempSeeds.get(j).unwrap();
                    if s.final_value.is_some() {
                        continue;
                    }
                    if s.initial_value >= source && s.initial_value < source + offset {
                        tempSeeds[j] = Remap { initial_value: dest + s.initial_value - source, final_value: None };
                    }
                }
                i += 1;
            }
            seeds = tempSeeds.iter().map(|x| x.final_value.unwrap_or(x.initial_value)).collect::<Vec<i64>>();
        }
    }
    return seeds;
}

