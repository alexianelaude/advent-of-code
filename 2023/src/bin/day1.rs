
use std::fs::read_to_string;

fn main() {
    let sum = part2();
    println!("Part 2: {}", sum);
}

fn part1() -> i32 {
    let mut calibrSum = 0;
    for line in read_to_string("../../inputs/input1.txt").unwrap().lines() {
        let (firstNum, lastNum) = getFirstAndLastNum(line);
        let mut calibrStr = String::new();
        calibrStr.push(firstNum.unwrap());
        if lastNum.is_some() {
            calibrStr.push(lastNum.unwrap());
        }
        else {
            calibrStr.push(firstNum.unwrap());
        }
        calibrSum += calibrStr.parse::<i32>().unwrap();
    }
    return calibrSum;
}

fn getFirstAndLastNum(line: &str) -> (Option<char>, Option<char>) {
    let mut firstNum: Option<char> = None;
    let mut lastNum: Option<char> = None;
    for char in line.chars() {
        if char.is_numeric() {
            if firstNum == None {
                firstNum = Some(char);
            }
            lastNum = Some(char);
        }
    }
    return (firstNum, lastNum);
}

const NUMBER_STRINGS: [&str; 10] = ["zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"];

fn part2() -> i32 {
    let mut calibrSum = 0;
    for line in read_to_string("../../inputs/input1.txt").unwrap().lines() {
        let mut firstNum: Option<char> = None;
        let mut lastNum: Option<char> = None;
        (firstNum, lastNum) = getFirstAndLastNum(line);
        let splitString = line.split(char::is_numeric).collect::<Vec<&str>>();
        if !splitString.is_empty() {
            if !line.chars().nth(0).is_some_and(|x| x.is_numeric()) {
                matchDigitInString(splitString.get(0).expect("failed"), false).map(|x| firstNum = Some(x));
            }
            if !line.chars().nth(line.len()-1).is_some_and(|x| x.is_numeric()) {
                matchDigitInString(splitString.get(splitString.len() - 1).expect("failed"), true).map(|x| lastNum = Some(x));
            }
        }
        let mut calibrStr = String::new();
        calibrStr.push(firstNum.unwrap());
        if lastNum.is_some() {
            calibrStr.push(lastNum.unwrap());
        }
        else {
            calibrStr.push(firstNum.unwrap());
        }
        calibrSum += calibrStr.parse::<i32>().unwrap();
    }
    return calibrSum;
}

fn matchDigitInString(string: &str, matchLast: bool) -> Option<char> {
    let mut matchIndex: Option<usize> = None;
    let mut minMatchNumber: Option<char> = None;
    for i in 1..10 {
        let matches = if matchLast {
            string.rfind(NUMBER_STRINGS.get(i).expect("failed"))
        } else { string.find(NUMBER_STRINGS.get(i).expect("failed")) };
        if matches.is_some() {
            if matchIndex.is_none() {
                matchIndex = Some(matches.unwrap());
                minMatchNumber = Some(char::from_digit(i as u32, 10).unwrap());
            }
            else if (!matchLast && matches.unwrap() < matchIndex.unwrap()) || (matchLast && matches.unwrap() + NUMBER_STRINGS.get(i).iter().len() > matchIndex.unwrap() + minMatchNumber.iter().len()) {
                matchIndex = Some(matches.unwrap());
                minMatchNumber = Some(char::from_digit(i as u32, 10).unwrap());
            }
        }
    }
    return minMatchNumber;
}
