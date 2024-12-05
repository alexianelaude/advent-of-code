def getRanges():
    with open('input/input4.txt', 'r') as f:
        ranges = []
        for line in f:
            range1, range2 = line.strip('\n').split(',')
            ranges.append([list(map(int,range1.split('-'))), list(map(int,range2.split('-')))])
        f.close()
    return ranges

def includedIn(range1, range2): #check whether range1 is included inside range2
    return (range1[0] >= range2[0] and range1[1] <= range2[1])

def part1():
    ranges = getRanges()
    contains = sum(map(lambda rangePair: includedIn(rangePair[0], rangePair[1]) or includedIn(rangePair[1], rangePair[0]), ranges))
    return contains

def overlaps(range1, range2): #check whether range1 overlaps range2 (with a bigger start index)
    return (range1[0] >= range2[0] and range1[0] <= range2[1])

def part2():
    ranges = getRanges()
    overlap_num = sum(map(lambda rangePair: overlaps(rangePair[0], rangePair[1]) or overlaps(rangePair[1], rangePair[0]), ranges))
    return overlap_num

print(part2())
