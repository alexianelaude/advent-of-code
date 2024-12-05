def dist(Hpos, Tpos):
    return abs(Hpos[0] - Tpos[0]) + abs(Hpos[1] - Tpos[1])

def findNewTpos(Hpos, Tpos):
    if dist(Hpos, Tpos) > 2:
        Tpos[0] += 1 if Hpos[0] - Tpos[0] > 0 else -1
        Tpos[1] += 1 if Hpos[1] - Tpos[1] > 0 else -1
    elif abs(Hpos[0] - Tpos[0]) > 1:
        Tpos[0] += 1 if Hpos[0] - Tpos[0] > 0 else -1
    elif abs(Hpos[1] - Tpos[1]) > 1:
        Tpos[1] += 1 if Hpos[1] - Tpos[1] > 0 else -1
    return Tpos

def part1():
    with open('input/input9.txt', 'r') as f:
        Hpos = [0,0]
        Tpos = [0,0]
        posList = [[0,0]]
        for line in f:
            (dir, num) = line.strip('\n').split(" ")
            for i in range(int(num)):
                if dir == 'U':
                    Hpos[0] = Hpos[0]+1
                if dir == 'D':
                    Hpos[0] = Hpos[0]-1
                if dir == 'L':
                    Hpos[1] = Hpos[1]-1
                if dir == 'R':
                    Hpos[1] = Hpos[1]+1
                Tpos = findNewTpos(Hpos, Tpos)
                if not any([pos == Tpos for pos in posList]):
                    posList.append(Tpos.copy())
        return len(posList)

def part2():
    with open('input/input9.txt', 'r') as f:
        knotsPos = [[0,0] for i in range(10)]
        posList = [[0,0]]
        for line in f:
            (dir, num) = line.strip('\n').split(" ")
            for i in range(int(num)):
                if dir == 'U':
                    knotsPos[0][0] = knotsPos[0][0]+1
                if dir == 'D':
                    knotsPos[0][0] = knotsPos[0][0]-1
                if dir == 'L':
                    knotsPos[0][1] = knotsPos[0][1]-1
                if dir == 'R':
                    knotsPos[0][1] = knotsPos[0][1]+1
                for i in range(1,len(knotsPos)):
                    knotsPos[i] = findNewTpos(knotsPos[i-1], knotsPos[i])
                if not any([pos == knotsPos[9] for pos in posList]):
                    posList.append(knotsPos[9].copy())
        return len(posList)

print(part2())