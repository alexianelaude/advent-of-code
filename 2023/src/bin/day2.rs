use std::fs::read_to_string;

fn main() {
    let sum = part2();
    println!("Part 2: {}", sum);
}

fn part1() -> i32 {
    let mut game_sum = 0;
    for line in read_to_string("inputs/input2.txt").unwrap().lines() {
        let game_num_split = line.split(":").collect::<Vec<&str>>();
        let game_num = game_num_split.get(0).expect("failed")[5..].parse::<i32>().unwrap();
        let is_valid = is_valid_game(game_num_split.get(1).expect("failed"));
        if is_valid {
            game_sum += game_num;
        }
    }
    return game_sum;
}

const RED_LIMIT : i32= 12;
const GREEN_LIMIT : i32 = 13;
const BLUE_LIMIT : i32 = 14;

fn is_valid_game(game_str: &str) -> bool {
    let game_rounds_str = game_str.split(";");
    for game_round_str in game_rounds_str.into_iter() {
        for game_pick in game_round_str.split(",") {
            let game_pick_split = game_pick.trim().split(" ").collect::<Vec<&str>>();
            let game_pick_num = game_pick_split.get(0).unwrap().parse::<i32>().unwrap();
            let game_pick_color = game_pick_split.get(1).unwrap();
            if game_pick_color.starts_with("r") && game_pick_num > RED_LIMIT {
                return false;
            }
            else if game_pick_color.starts_with("b") && game_pick_num > BLUE_LIMIT {
                return false;
            }
            else if game_pick_color.starts_with("g") && game_pick_num > GREEN_LIMIT {
                return false;
            }
        }
    }
    return true;
}

struct GameRound {
    red_max: Option<i32>,
    green_max: Option<i32>,
    blue_max: Option<i32>,
}

impl GameRound {
    fn update(&self, color : &str, value : i32) -> GameRound {
        if color == "red" && value > self.red_max.unwrap_or(0) {
            return GameRound { red_max: Some(value), green_max: self.green_max, blue_max: self.blue_max };
        }
        else if color == "green" && value > self.green_max.unwrap_or(0) {
            return GameRound { red_max: self.red_max, green_max: Some(value), blue_max: self.blue_max };
        }
        else if color == "blue" && value > self.blue_max.unwrap_or(0) {
            return GameRound { red_max: self.red_max, green_max: self.green_max, blue_max: Some(value) };
        }
        else {
            return GameRound { red_max: self.red_max, green_max: self.green_max, blue_max: self.blue_max };
        }
    }

    fn power(&self) -> i32 {
        return self.red_max.unwrap() * self.green_max.unwrap() * self.blue_max.unwrap();
    }
}

fn part2() -> i32 {
    let mut game_power_sum = 0;
    for line in read_to_string("inputs/input2.txt").unwrap().lines() {
        let game_rounds = line.split(":").collect::<Vec<&str>>().get(1).unwrap().split(";").collect::<Vec<&str>>();
        let mut game_round = GameRound { red_max: None, green_max: None, blue_max: None };
        for game_round_str in game_rounds {
            for game_pick in game_round_str.split(",").collect::<Vec<&str>>().into_iter() {
                let game_pick_split = game_pick.trim().split(" ").collect::<Vec<&str>>();
                let game_pick_num = game_pick_split.get(0).unwrap().parse::<i32>().unwrap();
                let game_pick_color = game_pick_split.get(1).unwrap();
                game_round = game_round.update(game_pick_color, game_pick_num);
            }
        }
        game_power_sum += game_round.power();
    }
    return game_power_sum;
}