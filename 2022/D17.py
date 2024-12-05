class Rock:
    def __init__(self, coordinates):
        self.coordinates = coordinates

    def move(self, axis, step):
        for coord in self.coordinates:
            coord[axis] = coord[axis] + step

    def moveSideways(self, command, rockGrid):
        if command == '<':
            self.moveLeft(rockGrid)
        if command == '>':
            self.moveRight(rockGrid)


    def moveLeft(self, rockGrid):
        if not any(coor[0] <= 0 for coor in self.coordinates) and not any(rockGrid[coor[1]][coor[0]-1] for coor in self.coordinates):
            self.move(0, -1)

    def moveRight(self, rockGrid):
        if not any(coor[0] >= 6 for coor in self.coordinates) and not any(rockGrid[coor[1]][coor[0]+1] for coor in self.coordinates):
            self.move(0, 1)

    def moveDown(self, rockGrid):
        if any(coor[1] <= 0 for coor in self.coordinates) or any(rockGrid[coor[1]-1][coor[0]] for coor in self.coordinates):
            #save rock in rockGrid
            for coor in self.coordinates:
                rockGrid[coor[1]][coor[0]] = True
            return False
        else:
            self.move(1, -1)
            return True

def part1():
    with open('input/input17.txt', 'r') as f:
        commands = f.readlines()[0].strip('\n')
        rock_patterns = [[[2,3], [3,3], [4,3], [5,3]], [[2,4], [3,4], [4,4], [3,3], [3,5]], [[2,3], [3,3], [4,3], [4,4], [4,5]],
                         [[2,3], [2,4], [2,5], [2,6]], [[2,3], [3,3], [2,4], [3,4]]]
        rockGrid = [[False for i in range(7)] for j in range(1000000)]
        tallest = 0
        j = 0
        command = commands[j % len(commands)]
        saved_tallest = 0
        for i in range(1000000000000):
            rock_pattern = rock_patterns[i % 5]
            rock = Rock([[coor[0], coor[1] + tallest] for coor in rock_pattern])
            rock.moveSideways(command, rockGrid)
            while rock.moveDown(rockGrid):
                j += 1
                command = commands[j % len(commands)]
                rock.moveSideways(command, rockGrid)
            j += 1
            command = commands[j % len(commands)]
            rock.moveSideways(command, rockGrid)
            tallest = max(tallest, max([coor[1] + 1 for coor in rock.coordinates]))
            if any(all(rockGrid[i]) for i in range(len(rockGrid))):
                max_ix = max(i for i in range(len(rockGrid)) if all(rockGrid[i]))
                rockGrid = rockGrid[max_ix+1:]
                saved_tallest += max_ix + 1
                tallest = tallest - max_ix - 1
        f.close()
    return tallest + saved_tallest

print(part1())


