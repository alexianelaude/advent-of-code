use std::fs::read_to_string;

fn main() {
    let sum = part1();
    println!("Part 1: {}", sum);
}

fn part1() -> i32{
    let mut sum = 0;
    for line in read_to_string("inputs/input9.txt").unwrap().lines() {
        let sequence = line.split(" ").map(|x| x.parse::<i32>().unwrap()).collect::<Vec<i32>>();
        let sequence_tree = run_sequence(sequence);
        let prediction = do_past_prediction(sequence_tree);
        sum += prediction;
    }
    return sum;
}

fn run_sequence(sequence: Vec<i32>) -> Vec<Vec<i32>> {
    let mut sequence_tree = Vec::new();
    sequence_tree.push(sequence.clone());
    while sequence_tree.last().unwrap().iter().any(|x| *x != 0) {
        let current_seq = sequence_tree.last().unwrap();
        let mut new_seq = Vec::new();
        for i in 0..current_seq.len() - 1 {
            new_seq.push(current_seq[i + 1] - current_seq[i]);
        }
        sequence_tree.push(new_seq);
    }
    return sequence_tree;
}

fn do_prediction(sequence_tree: Vec<Vec<i32>>) -> i32 {
    let mut last_predicted = 0;
    for i in (0..sequence_tree.len() - 1).rev() {
        let current_seq = sequence_tree.get(i).unwrap();
        let last_value = current_seq.last().unwrap();
        last_predicted = last_value + last_predicted;
    }
    return last_predicted;
}

fn do_past_prediction(sequence_tree: Vec<Vec<i32>>) -> i32 {
    let mut last_predicted = 0;
    for i in (0..sequence_tree.len() - 1).rev() {
        let current_seq = sequence_tree.get(i).unwrap();
        let first_value = current_seq.first().unwrap();
        last_predicted = first_value - last_predicted;
    }
    return last_predicted;
}