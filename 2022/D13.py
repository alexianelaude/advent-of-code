class Pair:
    def __init__(self, list):
        self.list = list

    def __lt__(self, other):
        return comparePairs(self.list, other.list) > 0

    def __eq__(self, other):
        return comparePairs(self.list, other.list) == 0

def comparePairs(left, right):
    if isinstance(left, int) and isinstance(right, int):
        if left < right:
            return 1
        elif left > right:
            return -1
        else:
            return 0
    elif isinstance(left, list) and isinstance(right, list):
        rightOrder = 0
        i = 0
        while rightOrder == 0:
            if i >= min(len(left), len(right)):
                break
            rightOrder = comparePairs(left[i], right[i])
            i += 1
        if rightOrder != 0:
            return rightOrder
        if len(left) == i and len(right) > i:
            return 1
        if len(right) == i and len(left) > i:
            return -1
        return rightOrder
    elif isinstance(left, int):
        return comparePairs([left], right)
    else:
        return comparePairs(left, [right])

def buildPair(line):
    all_list = []
    i = 0
    for i in range(len(line)):
        char = line[i]
        if char == '[':
            all_list.append([])
        elif char == ']':
            finished_list = all_list.pop()
            if len(all_list) > 0:
                all_list[-1].append(finished_list)
            else:
                all_list = [finished_list]
        elif char != ',':
            init_i = i
            while line[i].isnumeric():
                 i += 1
            all_list[-1].append(int(line[init_i:i]))
    return Pair(all_list[0])


def buildPairs():
    with open('input/input13.txt', 'r') as f:
        lines = [line.strip('\n') for line in f.readlines()]
        sum = 0
        for i in range(0,len(lines),3):
            left = buildPair(lines[i])
            right = buildPair(lines[i+1])
            pairIx = (i // 3) + 1
            if left.__cmp__(right) > 0:
                print(pairIx)
                sum += pairIx
        f.close()
    return sum

def part2():
    with open('input/input13.txt', 'r') as f:
        lines = [line.strip('\n') for line in f.readlines()]
        pair_list = []
        for i in range(0,len(lines),3):
            left = buildPair(lines[i])
            right = buildPair(lines[i+1])
            pair_list.append(left)
            pair_list.append(right)
        pair_list.append(Pair([[2]]))
        pair_list.append(Pair([[6]]))
        pair_list = sorted(pair_list)
        indexes = []
        for (i, pair) in enumerate(pair_list):
            if pair.__eq__(Pair([[2]])) or pair.__eq__(Pair([[6]])):
                indexes.append(i+1)
        f.close()
    return indexes[0] * indexes[1]

print(part2())
