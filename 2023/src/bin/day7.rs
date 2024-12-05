use std::cmp::Ordering;
use std::collections::HashMap;
use std::fs::read_to_string;
use lazy_static::lazy_static;

fn main() {
    let sum = part1();
    println!("Part 1: {}", sum);
}

fn part1() -> i64 {
    let mut winnings = 0;
    let mut hands = build_hands();
    hands.sort();
    for hand in hands.iter() {
        println!("{:?}", hand.cards);
    }
    for i in 0..hands.len() {
        let hand = hands.get(i).unwrap();
        winnings += hand.score as i64 * (i + 1) as i64;
    }
    return winnings;
}

lazy_static! {
static ref CARD_RANKS : HashMap<char, usize> = HashMap::from([
    ('T', 10),
    ('J', 1),
    ('Q', 12),
    ('K', 13),
    ('A', 14)
]);
    }

#[derive(Eq)]
struct Hand {
    cards: Vec<char>,
    score: usize
}

fn char_score(c: char) -> usize {
    if CARD_RANKS.contains_key(&c) {
        return *CARD_RANKS.get(&c).unwrap();
    }
    return c.to_digit(10).unwrap() as usize;
}


impl Hand {
    fn type_rank(&self) -> f64 {
        let mut card_counts = HashMap::new();
        for card in self.cards.iter() {
            let count = card_counts.entry(card).or_insert(0);
            *count += 1;
        }
        if card_counts.values().max().unwrap_or(&0) == &5 {
            return 5f64;
        }
        let mut new_card_counts = card_counts.clone();
        if card_counts.get(&'J').is_some() {
            let max_card = card_counts.iter().filter(|(k, v)| **k != &'J').max_by(|(k1,v1), (k2,v2)| v1.cmp(&v2)).map(|(k, v)| k).unwrap();
            let count = new_card_counts.entry(*max_card).or_insert(0);
            let j_values = card_counts.get(&'J').unwrap().clone();
            *count += j_values;
            new_card_counts.remove(&'J');
        }
        //full house
        if new_card_counts.values().max().unwrap_or(&0) == &3 && new_card_counts.values().min().unwrap_or(&0) == &2 {
            return 3.5;
        }
        // 2 pairs
        if new_card_counts.values().filter(|x| **x == 2).count() == 2 {
            return 2.5;
        }
        return new_card_counts.values().max().unwrap_or(&0).clone() as f64;
    }
}

impl Ord for Hand {
    fn cmp(&self, other: &Self) -> Ordering {
        let my_score = self.type_rank();
        let other_score = other.type_rank();
        if my_score != other_score {
            return my_score.total_cmp(&other_score);
        }
        for mut i in 0..self.cards.len() {
            let my_card = self.cards.get(i).unwrap();
            let other_card = other.cards.get(i).unwrap();
            if char_score(*my_card) != char_score(*other_card) {
                return char_score(*my_card).cmp(&char_score(*other_card));
            }
            i += 1;
        }
        return Ordering::Equal;
    }
}


impl PartialOrd for Hand {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

impl PartialEq for Hand {
    fn eq(&self, other: &Self) -> bool {
        self.cards.eq(&other.cards)
    }
}

fn build_hands() -> Vec<Hand> {
    let mut hands = Vec::new();
    for line in read_to_string("inputs/input7.txt").unwrap().lines() {
        let split_line = line.split(" ").collect::<Vec<&str>>();
        let cards = split_line.get(0).unwrap().chars().collect::<Vec<char>>();
        let score = split_line.get(1).unwrap().parse::<usize>().unwrap();
        let hand = Hand { cards: cards, score: score };
        hands.push(hand);
    }
    return hands;
}