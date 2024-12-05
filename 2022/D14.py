def getInitialGrid():
    with open('input/input14.txt', 'r') as f:
        caveGrid = [[False for j in range(800)] for i in range(200)]
        for line in f:
            blocks = line.strip('\n').split(' ')[::2]
            blocks = [(int(block.split(',')[0]), int(block.split(',')[1])) for block in blocks]
            for k in range(len(blocks)-1):
                if blocks[k][0] == blocks[k+1][0]:
                    (mini, maxi) = (min(blocks[k][1], blocks[k + 1][1]), max(blocks[k][1], blocks[k + 1][1]))
                    for i in range(mini,maxi+1):
                        caveGrid[i][blocks[k][0]] = True
                else:
                    (mini, maxi) = (min(blocks[k][0], blocks[k + 1][0]), max(blocks[k][0], blocks[k + 1][0]))
                    for j in range(mini, maxi + 1):
                        caveGrid[blocks[k][1]][j] = True
        return caveGrid

def addSand(caveGrid):
    (x,y) = (500, 0)
    isBlocked = False
    while y < len(caveGrid) - 1 and not isBlocked:
        if not caveGrid[y+1][x]:
            y += 1
        elif not caveGrid[y+1][x-1]:
            x -= 1
            y += 1
        elif not caveGrid[y+1][x+1]:
            x += 1
            y += 1
        else:
            isBlocked = True
    if isBlocked:
        caveGrid[y][x] = True
    return (x,y) == (500, 0)

def part1():
    caveGrid = getInitialGrid()
    fallsForever = False
    sumSand = 0
    while not fallsForever:
        fallsForever = not addSand(caveGrid)
        if not fallsForever:
            sumSand += 1
    return sumSand

def addFloorToGrid(caveGrid):
    maxY = max([j for j in range(len(caveGrid)) if any(caveGrid[j])])
    caveGrid[maxY+2] = [True for j in range(800)]
    return caveGrid[:maxY+3]

def part2():
    caveGrid = addFloorToGrid(getInitialGrid())
    blockedInInitialPos = False
    sumSand = 0
    while not blockedInInitialPos:
        blockedInInitialPos = addSand(caveGrid)
        sumSand += 1
    return sumSand

print(part2())





