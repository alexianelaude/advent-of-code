NUMBER_DISTINCT = 14

def part1():
    with open('input/input6.txt', 'r') as f:
        line = f.readlines()[0]
        i = 0
        while i < len(line):
            if len(set(line[i:i+NUMBER_DISTINCT])) == NUMBER_DISTINCT:
                return i + NUMBER_DISTINCT
            i += 1
        return "Marker not found"

print(part1())