def findErrors():
    errors = []
    with open('input/input3.txt', 'r') as f:
        for line in f:
            line = line.strip('\n')
            first_half = line[0:len(line) // 2]
            second_half = line[len(line) // 2: len(line)]
            error = list(set([char for char in first_half if char in second_half]))
            assert len(error) == 1, error
            errors.append(error[0])
        f.close()
    return errors

def charToPoint(char):
    if char.islower():
        return ord(char) - 96
    else:
        return ord(char) - 38

def part1():
    errors = findErrors()
    res = sum([charToPoint(char) for char in errors])
    return res

def findBadges():
    badges = []
    with open('input/input3.txt', 'r') as f:
        lines = [line.strip('\n') for line in f.readlines()]
        for i in range(0, len(lines), 3):
            badge = list(set([char for char in lines[i] if char in lines[i+1] and char in lines[i+2]]))
            assert len(badge) == 1, badge
            badges.append(badge[0])
        f.close()
    return badges


def part2():
    badges = findBadges()
    return sum([charToPoint(char) for char in badges])
