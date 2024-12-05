use std::fs::read_to_string;

fn main() {
    let sum = part2();
    println!("Part 2: {}", sum);
}

fn part1() -> i32 {
    let mut winning_num_sum = 0;
    for line in read_to_string("inputs/input4.txt").unwrap().lines() {
        let split_line = line.split("|").collect::<Vec<&str>>();
        let winning_num_line = split_line.get(0).unwrap().split(":").collect::<Vec<&str>>().get(1).unwrap().trim();
        let winning_num_list = get_numbers(winning_num_line);
        let my_num_list = get_numbers(split_line.get(1).unwrap().trim());
        let mut score = 0;
        for num in my_num_list {
            if winning_num_list.contains(&num) {
                if score == 0 {
                    score = 1;
                }
                else {
                    score *= 2;
                }
            }
        }
        winning_num_sum += score;
    }
    return winning_num_sum;
}

fn get_numbers(num_list_str: &str) -> Vec<i32> {
    let mut num_list = Vec::new();
    for num_str in num_list_str.split(" ").into_iter() {
        if !num_str.is_empty() {
            num_list.push(num_str.parse::<i32>().unwrap());
        }
    }
    return num_list;
}

const LINE_NUMBER : usize = 199;

fn part2() -> i64 {
    let mut number_scratch_card: [i64; LINE_NUMBER] = [1;LINE_NUMBER];
    for (line_num, line) in read_to_string("inputs/input4.txt").unwrap().lines().into_iter().enumerate() {
        let split_line = line.split("|").collect::<Vec<&str>>();
        let winning_num_line = split_line.get(0).unwrap().split(":").collect::<Vec<&str>>().get(1).unwrap().trim();
        let winning_num_list = get_numbers(winning_num_line);
        let my_num_list = get_numbers(split_line.get(1).unwrap().trim());
        let mut score = 0;
        for num in my_num_list {
            if winning_num_list.contains(&num) {
                score += 1;
            }
        }
        for i in 0..score {
            if (i + line_num + 1) < LINE_NUMBER {
                number_scratch_card[i + line_num + 1] += number_scratch_card[line_num];
            }
        }
    }
    return number_scratch_card.iter().map(|x| *x).sum();
}
