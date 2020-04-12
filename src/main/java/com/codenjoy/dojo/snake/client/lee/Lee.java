package com.codenjoy.dojo.snake.client.lee;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Lee {

    private final static List<LPoint> deltas = Arrays.asList(
            LPoint.of(0,-1), LPoint.of(-1,0),
            LPoint.of(+1,0), LPoint.of(0,+1));
    private final int width;
    private final int height;
    private final int[][] board;
    private final int EMPTY = 0;
    private final int START = -1; // any negative number
    private final int OBSTACLE = -1; // any negative number

    public Lee(int width, int height){
        this.width = width;
        this.height = height;
        this.board = new int[height][width];
    }

    public int get(LPoint p){
        return board[p.y][p.x];
    }

    void set(LPoint p, int val){
        board[p.y][p.x] = val;
    }

    boolean isOnBoard(LPoint p){
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }

    boolean isUnVisited(LPoint p){
        return get(p) == EMPTY;
    }

    private Stream<LPoint> neighbours(LPoint point){
        return deltas.stream()
                .map(d -> d.move(point))
                .filter(this::isOnBoard);
    }

    private Set<LPoint> neighboursUnvisited(LPoint point){
        return neighbours(point)
                .filter(this::isUnVisited) //.filter(p -> isUnVisited(p))
                .collect(Collectors.toSet());
    }

    private List<LPoint> neighboursByValue(LPoint point, int val){
        return neighbours(point)
                .filter(p -> get(p) == val)
                .collect(Collectors.toList());
    }

    private void initializeBoard(List<LPoint> obstacles){
        IntStream.range(0, width).boxed().flatMap(x ->
                IntStream.range(0, height).mapToObj(y ->
                        LPoint.of(x,y)
                )).forEach(p -> set(p, EMPTY));
        obstacles.forEach(p -> set(p, OBSTACLE));
    }

    public Optional<List<LPoint>> trace(LPoint start, LPoint finish, List<LPoint> obstacles){
        initializeBoard(obstacles);
        boolean found = false;
        set(start, START);
        Set<LPoint> curr = new HashSet<>();
        curr.add(start);
        int[] counter = {0};
        while (!(curr.isEmpty() || found)){
            counter[0]++;
            Set<LPoint> next = curr.stream()
                    .map(this::neighboursUnvisited)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            next.forEach(p -> set(p,counter[0]));
            found = next.contains(finish);
            //printMe(Collections.emptyList());
            curr.clear();
            curr.addAll(next);
        }
        if (!found) return Optional.empty();

        LinkedList<LPoint> path = new LinkedList<>();
        path.add(finish);
        set(start,0);
        LPoint curr_p = finish;
        while (counter[0] > 0){
            counter[0]--;
            LPoint prev_p = neighboursByValue(curr_p, counter[0]).get(0);
            path.addFirst(prev_p);
            curr_p = prev_p;
        }
       // printMe(path);
        return Optional.of(path);
    }

//    public String formatted(LPoint point, List<LPoint> path) {
//        int val = get(point);
//        if (val == OBSTACLE) return " XX";
//        if (path.isEmpty()) return String.format("%3d", val);       // intermediate steps
//        if (path.contains(point)) return String.format("%3d", val); // final step
//        return " --";
//    }
//
//    void printMe(List<LPoint> path) {
//        String s = IntStream.range(0, height).mapToObj(y ->
//                IntStream.range(0, width)
//                        .mapToObj(x -> LPoint.of(x, y))
//                        .map(p -> formatted(p, path))
//                        .collect(Collectors.joining())
//        ).collect(Collectors.joining("\n"));
//        System.out.printf("%s\n\n", s);
//    }
}