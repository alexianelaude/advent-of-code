
def move(stacks, moveLine):
    splitMove = moveLine.strip('\n').split(' ')
    numMove = int(splitMove[1])
    initStack, endStack = int(splitMove[3]) - 1, int(splitMove[5]) - 1
    for i in range(numMove):
        letter = stacks[initStack].pop()
        stacks[endStack].append(letter)

def part1():
    stacks = [[] for i in range(9)]
    with open('input/input5.txt', 'r') as f:
        lines = f.readlines()
        li = 0
        while lines[li] != '\n':
            line = lines[li]
            stack_indexes = [i // 4 for i in range(len(line)) if line[i] == '[']
            for ix in stack_indexes:
                stacks[ix].append(line[ix*4+1])
            li += 1
        stacks = [stack[::-1] for stack in stacks]
        for moveLine in lines[li+1:len(lines)]:
            moveMultiple(stacks, moveLine)
        f.close()
    return [stack.pop() for stack in stacks]

def moveMultiple(stacks, moveLine):
    splitMove = moveLine.strip('\n').split(' ')
    numMove = int(splitMove[1])
    initStack, endStack = int(splitMove[3]) - 1, int(splitMove[5]) - 1
    (stacks[initStack], stacks[endStack]) = (stacks[initStack][:len(stacks[initStack])-numMove], stacks[endStack] + stacks[initStack][len(stacks[initStack])-numMove:])

print(part1())