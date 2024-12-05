use std::fs::read_to_string;

fn main() {
    let sum = part1();
    println!("Part 1: {}", sum);
}

enum Point {
    Galaxy,
    Void
}

impl Point {
    fn is_galaxy(&self) -> bool {
        match self {
            Point::Galaxy => true,
            Point::Void => false
        }
    }
}

fn build_universe() -> (Vec<Vec<Point>>, Vec<(usize, usize)>) {
    let mut universe : Vec<Vec<Point>> = Vec::new();
    let mut galaxies : Vec<(usize, usize)> = Vec::new();
    let binding = read_to_string("inputs/input11.txt").unwrap();
    let lines = binding.lines().collect::<Vec<&str>>();
    for i in 0..lines.len() {
        let mut current_line = Vec::new();
        let chars = lines.get(i).unwrap().chars().collect::<Vec<char>>();
        for j in 0..chars.len() {
            if chars.get(j).unwrap() == &'#' {
                current_line.push(Point::Galaxy);
                galaxies.push((i, j))
            } else {
                current_line.push(Point::Void);
            }
        }
        universe.push(current_line);
    }
    return (universe, galaxies);
}

fn find_expansions(universe: &Vec<Vec<Point>>) -> (Vec<usize>, Vec<usize>) {
    let mut row_expansions = Vec::new();
    let mut col_expansions = Vec::new();
    for i in 0..universe.len() {
        if universe[i].iter().all(|x| !x.is_galaxy()) {
            row_expansions.push(i);
        }
    }
    for j in 0..universe.get(0).unwrap().len() {
        let mut should_expand = true;
        for i in 0..universe.len() {
            if universe[i][j].is_galaxy() {
                should_expand = false;
                break;
            }
        }
        if should_expand {
            col_expansions.push(j);
        }
    }
    return (row_expansions, col_expansions);
}

fn part1() -> usize {
    let (universe, mut galaxies) = build_universe();
    let expansions = find_expansions(&universe);
    let mut path_sum = 0;
    for i in 0..galaxies.len() {
        for j in i+1..galaxies.len() {
            let g1 = galaxies.get(i).unwrap();
            let g2 = galaxies.get(j).unwrap();
            let dist = compute_dist(g1, g2, &universe, &expansions);
            path_sum += dist;
        }
    }
    return path_sum;
}

fn compute_dist(g1: &(usize, usize), g2: &(usize, usize), universe: &Vec<Vec<Point>>, expansions: &(Vec<usize>, Vec<usize>)) -> usize {
    let row_expansions_between = expansions.0.iter().filter(|x| **x > g1.0 && **x < g2.0 || **x < g1.0 && **x > g2.0).count() * 999999;
    let row_dist = if g1.0 > g2.0 { g1.0 - g2.0 + row_expansions_between } else { g2.0 - g1.0 + row_expansions_between };
    let col_expansions_between = expansions.1.iter().filter(|x| **x > g1.1 && **x < g2.1 || **x < g1.1 && **x > g2.1).count() * 999999;
    let col_dist = if g1.1 > g2.1 { g1.1 - g2.1 + col_expansions_between } else { g2.1 - g1.1 + col_expansions_between };
    return row_dist + col_dist;
}