package com.leo.test.javaFX.maze;

enum Direction4 {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    private int x;

    private int y;

    Direction4(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Direction4 opposite() {
        switch (this) {
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

public class Square implements Shape<Direction4> {
    private Wall up;

    private Wall right;

    private Wall down;

    private Wall left;

    public Square(Wall up, Wall right, Wall down, Wall left) {
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
    }

    public static String fieldToString(Square[][] field) {
        StringBuilder builder = new StringBuilder();
        StringBuilder builderNext = new StringBuilder();
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (x == 0) {
                    builder.append(0);
                    builderNext.append(0);
                }
                builder.append(' ').append(field[y][x].getWall(Direction4.RIGHT).isClosed() ? '|' : ' ');
                builderNext.append(field[y][x].getWall(Direction4.DOWN).isClosed() ? '_' : ' ').append('.');
            }
            builder.append('\n').append(builderNext).append('\n');
            builderNext.setLength(0);
        }
        return builder.toString();
    }

    public Square[][] field(int width, int height) {
        Square[][] field = new Square[height][width];
        Wall up, right, down, left;
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                if (x == 0)
                    left = new Wall();
                else
                    left = field[y][x - 1].getWall(Direction4.RIGHT);
                if (y == 0)
                    up = new Wall();
                else
                    up = field[y - 1][x].getWall(Direction4.DOWN);
                right = new Wall();
                down = new Wall();
                field[y][x] = new Square(up, right, down, left);
            }
        return field;
    }

    public Wall getWall(Direction4 direction4) {
        switch (direction4) {
            case DOWN:
                return down;
            case UP:
                return up;
            case LEFT:
                return left;
            case RIGHT:
                return right;
            default:
                return null;
        }
    }
}

