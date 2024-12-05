
class Rock:
    def __init__(self):
        self.point = 1
        self.beats = Scissor
        self.beaten_by = Paper

class Paper:
    def __init__(self):
        self.point = 2
        self.beats = Rock
        self.beaten_by = Scissor

class Scissor:
    def __init__(self):
        self.point = 3
        self.beats = Paper
        self.beaten_by = Rock

def generateRPS(letter):
    if letter == 'A' or letter == 'X':
        return Rock()
    if letter == 'B' or letter == 'Y':
        return Paper()
    if letter == 'C' or letter == 'Z':
        return Scissor()
    else:
        print("Exception occurred: invalid input")

def winPoints(elf, me):
    if isinstance(elf, me.beats):
        return 6
    if isinstance(me, elf.beats):
        return 0
    else:
        return 3

def part1():
    with open('input/input2.txt', 'r') as f:
        points = 0
        for line in f:
            letters = line.strip("\n").split(" ")
            elf = generateRPS(letters[0])
            me = generateRPS(letters[1])
            points += me.point + winPoints(elf, me)
        return points

def part2():
    with open('input/input2.txt', 'r') as f:
        points = 0
        for line in f:
            letters = line.strip("\n").split(" ")
            elf = generateRPS(letters[0])
            if letters[1] == 'X':
                me = elf.beats()
            if letters[1] == 'Y':
                me = elf
            if letters[1] == 'Z':
                me = elf.beaten_by()
            points += me.point + winPoints(elf, me)
        return points

res = part2()
print(res)
