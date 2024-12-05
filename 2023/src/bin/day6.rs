use std::collections::HashMap;
use std::fs::read_to_string;

fn main() {
    let sum = part1();
    println!("Part 1: {}", sum);
}

fn part1() -> i64 {
    let binding = read_to_string("inputs/input6.txt").unwrap();
    let mut lines = binding.lines().into_iter();
    let time_line = lines.nth(0).unwrap().split(":").collect::<Vec<&str>>().get(1).unwrap().trim();
    let times_str = time_line.split(" ").collect::<Vec<&str>>();
    let times = times_str.iter().filter(|x| !x.is_empty()).map(|x| x.trim().parse::<i64>().unwrap()).collect::<Vec<i64>>();
    let dist_line = lines.nth(0).unwrap().split(":").collect::<Vec<&str>>().get(1).unwrap().trim();
    let dists_str = dist_line.split(" ").collect::<Vec<&str>>();
    let dists = dists_str.iter().filter(|x| !x.is_empty()).map(|x| x.trim().parse::<i64>().unwrap()).collect::<Vec<i64>>();
    let mut possibilities = 0;
    for i in 0..times.len() {
        let time = times.get(i).unwrap();
        let dist = dists.get(i).unwrap();
        possibilities += race_possibilities(*time, *dist);
    }
    return possibilities;
}

fn race_possibilities(time: i64, dist: i64) -> i64 {
    let mut possibilities = 0;
    for i in 0..time {
        let mut time_left = time - i;
        let speed = i;
        let mut travel_dist = time_left * speed;
        if travel_dist >= dist {
            possibilities += 1;
        }
    }
    return possibilities;
}