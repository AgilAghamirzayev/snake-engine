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
    private  ArrayList<Point> obstacles = new ArrayList<>();

    public MyAlgorithm(Board board) {
        List<Point> walls = board.getWalls();
        List<Point> snake = board.getSnake();
        Point stone = board.getStones().get(0);
        apple = board.getApples().get(0);
        head = board.getHead();

        obstacles.addAll(walls);
        obstacles.add(stone);
        obstacles.addAll(snake);

        lee = new Lee(board.size(), board.size());
    }

    public Direction solve(){
        Optional<List<LPoint>> points = lee.trace(
                new LPoint(head.getX(), head.getY()),
                new LPoint(apple.getX(), apple.getY()),
                obstacles.stream().map(a -> new LPoint(a.getX(),a.getY())).collect(Collectors.toList()));
        if (points.isPresent()){
            List<LPoint> trace = points.get();
            System.out.printf("Green apple trace: %s\n",trace);
            LPoint next = trace.get(1);
            return coordinate(next,head);
        }
        return Direction.random();
    }

    private Direction coordinate(LPoint next, Point head) {
        if (next.getX() < head.getX()) return Direction.LEFT;
        if (next.getX() > head.getX()) return Direction.RIGHT;
        if (next.getY() > head.getY()) return Direction.UP;
        if (next.getY() < head.getY()) return Direction.DOWN;
        return Direction.random();
    }
}