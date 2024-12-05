import sys

START = (20, 0)
END = (20, 149)
DIM = (41, 172)
class Node:
    def __init__(self, height, id):
        self.height = height
        self.neighbours = []
        self.id = id

class Graph:
    def __init__(self, nodes):
        self.nodes = nodes

def createGraph():
    with open('input/input12.txt', 'r') as f:
        lines = [line.strip('\n') for line in f.readlines()]
        nodeArray = [[Node(ord('a'), i*DIM[1] + j) if lines[i][j] == 'S' else Node(ord('z'), i*DIM[1] + j) if lines[i][j] == 'E' else Node(ord(lines[i][j]), i*DIM[1] + j) for j in range(len(lines[i]))] for i in range(len(lines))]
        for i in range(len(nodeArray)):
            for j in range(len(nodeArray[0])):
                node = nodeArray[i][j]
                if i > 0 and nodeArray[i-1][j].height <= node.height + 1:
                    node.neighbours.append(nodeArray[i-1][j])
                if j > 0 and nodeArray[i][j-1].height <= node.height + 1:
                    node.neighbours.append(nodeArray[i][j-1])
                if i < len(nodeArray) - 1 and nodeArray[i+1][j].height <= node.height + 1:
                    node.neighbours.append(nodeArray[i+1][j])
                if j < len(nodeArray[0]) - 1 and nodeArray[i][j+1].height <= node.height + 1:
                    node.neighbours.append(nodeArray[i][j+1])
            f.close()
    return Graph([node for line in nodeArray for node in line]) #the list of nodes in line

def find_min_non_visited(visited, weights):
    mini = sys.maxsize
    for i in range(len(visited)):
        if not visited[i] and weights[i] <= mini:
            mini = weights[i]
            min_ix = i
    return min_ix
def findSmallestPath(graph, start_ix):
    weights = [sys.maxsize for i in range(len(graph.nodes))]
    visited = [False for i in range(len(graph.nodes))]
    weights[start_ix] = 0
    while not all(visited):
        node_ix = find_min_non_visited(visited, weights)
        visited[node_ix] = True
        node = graph.nodes[node_ix]
        for neighbour in node.neighbours:
            weights[neighbour.id] = min(weights[neighbour.id], weights[node_ix] + 1)
    return weights

def part1():
    return findSmallestPath(createGraph(), START[0]*DIM[1] + START[1])[END[0] * DIM[1] + END[1]]

def createReverseGraph(): #node a has node b has a neighbour if one can go from b to a
    with open('input/input12.txt', 'r') as f:
        lines = [line.strip('\n') for line in f.readlines()]
        nodeArray = [[Node(ord('a'), i*DIM[1] + j) if lines[i][j] == 'S' else Node(ord('z'), i*DIM[1] + j) if lines[i][j] == 'E' else Node(ord(lines[i][j]), i*DIM[1] + j) for j in range(len(lines[i]))] for i in range(len(lines))]
        for i in range(len(nodeArray)):
            for j in range(len(nodeArray[0])):
                node = nodeArray[i][j]
                if i > 0 and node.height <= nodeArray[i-1][j].height + 1:
                    node.neighbours.append(nodeArray[i-1][j])
                if j > 0 and node.height <= nodeArray[i][j-1].height + 1:
                    node.neighbours.append(nodeArray[i][j-1])
                if i < len(nodeArray) - 1 and node.height <= nodeArray[i+1][j].height + 1:
                    node.neighbours.append(nodeArray[i+1][j])
                if j < len(nodeArray[0]) - 1 and node.height <= nodeArray[i][j+1].height + 1:
                    node.neighbours.append(nodeArray[i][j+1])
            f.close()
    return Graph([node for line in nodeArray for node in line]) #the list of nodes in line

def part2():
    graph = createReverseGraph()
    weights = findSmallestPath(graph, END[0] * DIM[1] + END[1])
    a_weights = [weights[i] for i in range(len(weights)) if graph.nodes[i].height == ord('a')]
    return min(a_weights)

print(part2())


