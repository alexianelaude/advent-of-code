use std::fs::read_to_string;
use itertools::Itertools;
use num::iter::Range;

fn main() {
    let sum = part1();
    println!("Part 1: {}", sum);
}

fn part1() -> i32 {
    let mut sum = 0;
    for line in read_to_string("inputs/input12.txt").unwrap().lines() {
        let split_line = line.split(" ").collect::<Vec<&str>>();
        let arrangements = split_line.get(1).unwrap().split(",").map(|x| x.parse::<i32>().unwrap()).collect::<Vec<i32>>();
        let springs = split_line.get(0).unwrap();
        let all_arrangements = find_all_arrangements(springs);
        let possible_arrangements = find_possible_arrangements(arrangements, all_arrangements);
        sum += possible_arrangements;
    }
    return sum;
}

fn find_all_arrangements(springs: &str) -> Vec<String> {
    let mut all_arrangements = Vec::new();
    let num_unknowns = springs.chars().filter(|x| *x == '?').count();
    let mut combinations : Range<i32>;
    for _ in 0..num_unknowns {
        combinations = combinations.cartesian_product(0..2);
    }
    for combin in combinations {
        let mut current_arrangement = Vec::new();
        let mut unknown_it = 0;
        for j in 0..springs.len() {
            if springs.get(j..j+1).unwrap() == "?" {
                current_arrangement.push(if combin.get(unknown_it).unwrap() == &0 { "." } else { "#" } );
                unknown_it += 1;
            } else {
                current_arrangement.push(springs.get(j..j+1).unwrap());
            }
        }
        let current_arrangement = current_arrangement.join("");
        all_arrangements.push(current_arrangement);
    }
    return all_arrangements;
}

fn find_possible_arrangements(arrangements: Vec<i32>, all_arrangements: Vec<String>) -> i32 {
    let mut possible_arrangements_num = 0;
    for arrangement in all_arrangements {
        let mut current_arrangement: Vec<i32> = Vec::new();
        let mut is_broken_streak = false;
        for i in 0..arrangement.len() {
            if arrangement.get(i..i+1).unwrap() == "#" {
                if is_broken_streak {
                    let mut last_value = current_arrangement.last().unwrap().clone();
                    last_value += 1;
                    current_arrangement.push(last_value);
                }
                else {
                    let new_value = 1;
                    current_arrangement.push(new_value);
                    is_broken_streak = false;
                }
            } else {
                is_broken_streak = false;
            }
        }
        if current_arrangement == arrangements {
            possible_arrangements_num += 1;
        }
    }
    return possible_arrangements_num;
}