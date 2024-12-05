def getXValues():
    with open('input/input10.txt', 'r') as f:
        Xvalues = [1]
        currentX = 1
        for line in f:
            if line.strip('\n') == 'noop':
                Xvalues.append(currentX)
            else:
                newX = int(line.strip('\n').split(" ")[1]) + currentX
                Xvalues = Xvalues + [currentX, newX]
                currentX = newX
        f.close()
    return Xvalues


def part1():
    Xvalues = getXValues()
    table = [ Xvalues[i-1]*i for i in range(20,221,40)]
    return sum(table)

def part2():
    drawing = ''
    Xvalues = getXValues()
    for i in range(240):
        if (i % 40) >= Xvalues[i] - 1 and (i % 40) <= Xvalues[i] +1:
            drawing +='#'
        else:
            drawing += "."
        if (i + 1) % 40 == 0:
            drawing += '\n'
    return drawing

print(part2())