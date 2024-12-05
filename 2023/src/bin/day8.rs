use std::collections::HashMap;
use std::fs::read_to_string;
use num::integer::lcm;

fn main() {
    let sum = part2();
    println!("Part 2: {}", sum);
}

fn part1() -> usize {
    let mut steps = 0;
    let (instructions, graph) = parse_input();
    let mut current_node = graph.get(&"AAA".to_string()).unwrap();
    while current_node.name != "ZZZ".to_string() {
        if instructions.get(steps % instructions.len()).unwrap() == &'L' {
            let current_left = current_node.left.as_ref().unwrap();
            current_node = &graph[current_left];
        }
        else {
            let current_right = current_node.right.as_ref().unwrap();
            current_node = &graph[current_right];
        }
        steps += 1;
    }
    return steps;
}

#[derive(Clone, Debug)]
struct Node {
    left: Option<String>,
    right: Option<String>,
    name: String,
    num_iter_to_end: Option<usize>
}

fn parse_input() -> (Vec<char>, HashMap<String, Box<Node>>) {
    let binding = read_to_string("inputs/input8.txt").unwrap();
    let mut lines = binding.lines();
    let first_line = lines.nth(0).unwrap();
    let instructions = first_line.chars().collect::<Vec<char>>();
    let mut graph = HashMap::new();
    lines.next();
    for line in lines.into_iter() {
        let split_line = line.split("=").collect::<Vec<&str>>();
        let node_name = split_line.get(0).unwrap().trim().to_string();
        let right_left = split_line.get(1).unwrap().replace(&['(', ')'], "");
        let split_right_left = right_left.trim().split(",").collect::<Vec<&str>>();
        let left_name = split_right_left.get(0).unwrap().trim().to_string();
        let right_name = split_right_left.get(1).unwrap().trim().to_string();
        graph.insert(node_name.to_string(), Box::new(Node { left: Some(left_name), right: Some(right_name), name: node_name, num_iter_to_end: None }));
    }
    return (instructions, graph);
}

fn part2() -> i128 {
    let mut steps = 0;
    let (instructions, graph) = parse_input();
    let mut current_nodes = graph.clone().into_iter().filter(|(k,v)| k.ends_with("A")).map(|(k,v)| v).collect::<Vec<Box<Node>>>();
    let mut start_nodes = current_nodes.clone();
    while start_nodes.iter().any(|n| n.num_iter_to_end.is_none()) {
        if instructions.get(steps % instructions.len()).unwrap() == &'L' {
            current_nodes = current_nodes.iter().map(|node|
                graph[&node.left.clone().unwrap()].clone()).collect::<Vec<Box<Node>>>();
        } else {
            current_nodes = current_nodes.iter().map(|node|
                graph[&node.right.clone().unwrap()].clone()).collect::<Vec<Box<Node>>>();
        };
        steps += 1;
        for i in 0..current_nodes.len() {
            let node = current_nodes.get(i).unwrap();
            if node.name.ends_with("Z") {
                let mut start_node = start_nodes.get_mut(i).unwrap();
                *start_node = Box::new(Node { left: start_node.left.clone(), right: start_node.right.clone(), name: start_node.name.clone(), num_iter_to_end: Some(steps) });
            }
        }
    }
    return start_nodes.iter().map(|n| n.num_iter_to_end.unwrap() as i128).reduce(|cur, nxt| lcm(cur, nxt)).unwrap();
}