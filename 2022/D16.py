import sys

def find_min_non_visited(not_visited, weights):
    mini = sys.maxsize
    min_id = not_visited[0]
    for node_id in not_visited:
        if weights.get(node_id) is not None and weights[node_id] <= mini:
            mini = weights[node_id]
            min_id = node_id
    return min_id

class Node:
    def __init__(self, id, pressure, neighbour_ids):
        self.neighbour_ids = neighbour_ids
        self.id = id
        self.pressure = pressure
        self.neighbours = []
        self.distance_to_nodes = {}

    def buildNeighbours(self, node_list):
        for neigh in self.neighbour_ids:
            node = [node for node in node_list if node.id == neigh][0]
            self.neighbours.append(node)

    def getAdditionalPressure(self, time_left):
        return self.pressure * time_left

    def buildDistancesToNode(self, graph):
        weights = {}
        visited = []
        not_visited = [node.id for node in graph]
        weights[self.id] = 0
        while len(not_visited) > 0:
            node_id = find_min_non_visited(not_visited, weights)
            not_visited.remove(node_id)
            visited.append(node_id)
            node = [node for node in graph if node.id == node_id][0]
            for neighbour in node.neighbours:
                weights[neighbour.id] = min(weights.get(neighbour.id, sys.maxsize), weights[node_id] + 1)
        self.distance_to_nodes = weights

    def possiblePressure(self, graph, open_nodes, minutes_left):
        sum = 0
        for node in graph:
            if node.id not in open_nodes:
                min_left = minutes_left - self.distance_to_nodes[node.id] - 1
                sum += node.pressure * min_left
        return sum


def buildGraph():
    with open('input/input16.txt', 'r') as f:
        node_list = []
        for line in f.readlines():
            split_line = line.strip('\n').split(";")
            id = split_line[0].split(" ")[1]
            rate = int(split_line[0].split("=")[-1])
            neighbour_ids = [name.strip(",") for name in split_line[1].split(" ")[5:]]
            node_list.append(Node(id, rate, neighbour_ids))
        for node in node_list:
            node.buildNeighbours(node_list)
        for node in node_list:
            node.buildDistancesToNode(node_list)
        return node_list

def findNextValve(graph, minutes_left, current_node, open_nodes):
    max_pressure = 0
    next_node = current_node
    for node in current_node.neighbours:
        possible_pressure = node.possiblePressure(graph, open_nodes, minutes_left - 1)
        if possible_pressure > max_pressure:
            max_pressure = possible_pressure
            next_node = node
    if next_node.id == current_node.id: #Nothing left to do
        return next_node
    return next_node



def findMaxPressure(graph, minutes_left, current_node, current_pressure, open_nodes):
    if minutes_left == 0:
        return current_pressure
    if current_node.id not in open_nodes:  # Open the valve or not?
        open_valve = not any([node.pressure - current_node.pressure > current_node.pressure for node in current_node.neighbours])
        if open_valve:
            open_nodes.append(current_node.id)
            minutes_left -= 1
            current_pressure += minutes_left * current_node.pressure
    current_node = findNextValve(graph, minutes_left, current_node, open_nodes)
    return findMaxPressure(graph, minutes_left - 1, current_node, current_pressure, open_nodes)

def part1():
    graph = buildGraph()
    init_node = [node for node in graph if node.id == 'AA'][0]
    return findMaxPressure(graph, 30, init_node, 0, [])

print(part1())
