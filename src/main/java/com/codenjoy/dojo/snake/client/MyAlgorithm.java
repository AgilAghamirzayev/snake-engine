package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.client.lee.LPoint;
import com.codenjoy.dojo.snake.client.lee.Lee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MyAlgorithm {
    private  Point head;
    private  Point apple;
    private  Lee lee;
    private  ArrayList<LPoint> obstacles = new ArrayList<>();

    public MyAlgorithm(Board board) {
        List<Point> walls = board.getWalls();
        List<Point> snake = board.getSnake();
        Point stone = board.getStones().get(0);
        apple = board.getApples().get(0);
        head = board.getHead();

        obstacles.addAll(walls.stream().map(a -> LPoint.of(a.getX(), a.getY())).collect(Collectors.toList()));
        obstacles.add(LPoint.of(stone.getX(),stone.getY()));
        obstacles.addAll(snake.stream().map(a -> LPoint.of(a.getX(), a.getY())).collect(Collectors.toList()));

        lee = new Lee(board.size(), board.size());
    }

    public Direction solve(){

        LPoint l_head = LPoint.of(head.getX(),head.getY());
        LPoint l_apple = LPoint.of(apple.getX(),apple.getY());

        Optional<List<LPoint>> points = lee.trace(l_head, l_apple, obstacles);
        if (points.isPresent()){
            List<LPoint> trace = points.get();
            System.out.printf("Green apple trace: %s\n",trace);
            LPoint next = trace.get(1);
            return coordinate(next, head);
        }
        return solve();
    }

    private Direction coordinate(LPoint next, Point head) {
        if (next.getX() < head.getX()) return Direction.LEFT;
        if (next.getX() > head.getX()) return Direction.RIGHT;
        if (next.getY() > head.getY()) return Direction.UP;
        if (next.getY() < head.getY()) return Direction.DOWN;
        return Direction.random();
    }
}