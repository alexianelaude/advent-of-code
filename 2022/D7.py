class Directory:
    def __init__(self, name, files, parent):
        self.name = name
        self.children = files
        self.parent = parent

    def getSize(self):
        return sum([child.getSize() for child in self.children])

class File:
    def __init__(self, name, size, parent):
        self.name = name
        self.size = size
        self.parent = parent

    def getSize(self):
        return self.size


def buildDirTree():
    with open('input/input7.txt', 'r') as f:
        fullDirectory = Directory("/", [], None)
        currentDir = fullDirectory
        i = 0
        lines = [line.strip('\n') for line in f.readlines()]
        while i < len(lines):
            if lines[i].startswith("$"):
                if lines[i].split(" ")[1] == 'ls':
                    files = []
                    while i+1 < len(lines) and not lines[i+1].startswith("$"):
                        if lines[i+1].startswith("dir"):
                            files.append(Directory(lines[i+1].split(" ")[1], [], currentDir))
                        else:
                            files.append(File(lines[i+1].split(" ")[1], int(lines[i+1].split(" ")[0]), currentDir))
                        i += 1
                    currentDir.children = files
                if lines[i].split(" ")[1] == 'cd':
                    if lines[i].split(" ")[2] == '/':
                        currentDir = fullDirectory
                    elif lines[i].split(" ")[2] == '..':
                        currentDir = currentDir.parent
                    else:
                        currentDir = next(filter(lambda dir: dir.name == lines[i].split(" ")[2], currentDir.children), None)
            i = i+1
        f.close()
    return fullDirectory

MAX_SIZE = 100000

def getDirsWithMaxSize(dir):
    if isinstance(dir, File):
        return []
    childSizes = []
    for child in dir.children:
        childSizes = childSizes + getDirsWithMaxSize(child)
    if dir.getSize() < MAX_SIZE:
        childSizes.append(dir.getSize())
    return childSizes

def part1():
    fullDir = buildDirTree()
    dirSizes = getDirsWithMaxSize(fullDir)
    return sum(dirSizes)

def part2():
    fullDir = buildDirTree()
    available = 70000000 - fullDir.getSize()
    needed = 30000000 - available

    def findSmallestNeeded(dir, currentSmallest):
        if dir.getSize() < needed:
            return currentSmallest
        childSmallest = [findSmallestNeeded(child, currentSmallest) for child in dir.children]
        return min(min(childSmallest), dir.getSize())

    return findSmallestNeeded(fullDir, 70000000)


print(part2())
