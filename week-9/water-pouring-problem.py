# -*- coding:utf-8 -*-


def pour_problem(X, Y, goal, start=(0, 0)):
    """X 和 Y 是杯子的容量; (x,y) 当当前水位表示一个状态
    goal 是我们的目标水位，可能是任意一个杯子的当前水位
    start 是我们的开始状态.
    explored 的是我们探索过的状态
    frontier """
    if goal in start:
        return [start]
    explored = set()  # set the states we have visited
    frontier = [[start]]  # ordered list of paths we have blazed
    while frontier:
        path = frontier.pop(0)
        (x, y) = path[-1]  # Last state in the first path of the frontier
        for (state, action) in successors(x, y, X, Y).items():
            if state not in explored:
                explored.add(state)
                path2 = path + [action, state]
                if goal in state:
                    return path2
                else:
                    frontier.append(path2)
    return Fail


Fail = []


def successors(x, y, X, Y):
    """Return a dict of {state:action} pairs describing what can be reached from
    the (x, y) state and how."""
    assert x <= X and y <= Y  ## (x, y) is glass levels; X and Y are glass sizes
    return {((0, y + x) if y + x <= Y else (x - (Y - y), y + (Y - y))): 'X->Y',
            ((x + y, 0) if x + y <= X else (x + (X - x), y - (X - x))): 'X<-Y',
            (X, y): 'fill X',
            (x, Y): 'fill Y',
            (0, y): 'empty X',
            (x, 0): 'empty Y'
            }


if __name__ == "__main__":
    for line in pour_problem(4, 9, 6):
        print(line)
