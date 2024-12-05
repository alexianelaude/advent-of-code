def buildTreeGrid():
    with open('input/input8.txt', 'r') as f:
        lines = f.readlines()
        return [[int(char) for char in line.strip('\n')] for line in lines]

def isVisible(i, j, treeGrid):
    if i == 0 or j == 0 or i == len(treeGrid) - 1 or j == len(treeGrid[0])-1:
        return True
    height = treeGrid[i][j]
    return all([treeGrid[k][j] < height for k in range(i)]) or \
           all([treeGrid[k][j] < height for k in range(i+1, len(treeGrid))]) or\
           all([treeGrid[i][k] < height for k in range(j)]) or\
           all([treeGrid[i][k] < height for k in range(j+1,len(treeGrid[0]))])

def part1():
    grid = buildTreeGrid()
    inter = [sum([isVisible(i,j, grid) for i in range(len(grid))]) for j in range(len(grid[0]))]
    return sum(inter)

def viewScore(trees, height):
    blockIx = 0
    while blockIx < len(trees) and trees[blockIx] < height:
        blockIx += 1
    return len(trees[:blockIx+1])

def scenicScore(i, j, treeGrid):
    if i == 0 or j == 0 or i == len(treeGrid) - 1 or j == len(treeGrid[0])-1:
        return 0
    height = treeGrid[i][j]
    return viewScore(treeGrid[i][j+1:], height) * viewScore(treeGrid[i][:j][::-1], height) * viewScore([treeGrid[k][j] for k in range(i+1,len(treeGrid))], height) *\
           viewScore([treeGrid[k][j] for k in reversed(range(i))], height)

def part2():
    grid = buildTreeGrid()
    inter = [max([scenicScore(i, j, grid) for i in range(len(grid))]) for j in range(len(grid[0]))]

    return max(inter)
print(part2())