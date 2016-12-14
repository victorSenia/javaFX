package com.leo.test.javaFX.maze;

enum Direction6 {
    UP_RIGHT(.5, -.75), RIGHT(1, 0), DOWN_RIGHT(.5, .75), DOWN_LEFT(-.5, .75), LEFT(-1, 0), UP_LEFT(-.5, -.75),;

    private double x;

    private double y;

    Direction6(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction6 opposite() {
        switch (this) {
            case DOWN_LEFT:
                return UP_RIGHT;
            case DOWN_RIGHT:
                return UP_LEFT;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP_RIGHT:
                return DOWN_LEFT;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }
}

public class Hexagon implements Shape<Direction6> {
    private Wall up_left;

    private Wall up_right;

    private Wall right;

    private Wall down_right;

    private Wall down_left;

    private Wall left;

    public Hexagon(Wall up_left, Wall up_right, Wall right, Wall down_right, Wall down_left, Wall left) {
        this.up_left = up_left;
        this.up_right = up_right;
        this.right = right;
        this.down_right = down_right;
        this.down_left = down_left;
        this.left = left;
    }

    public Hexagon[][] field(int width, int height) {
        Hexagon[][] field = new Hexagon[height][width];
        Wall up_left, up_right, right, down_right, down_left, left;
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                if (x == 0)
                    left = new Wall();
                else
                    left = field[y][x - 1].getWall(Direction6.RIGHT);
                if (y == 0) {
                    up_left = new Wall();
                    up_right = new Wall();
                } else if (y % 2 == 1) {
                    up_left = field[y - 1][x].getWall(Direction6.DOWN_RIGHT);
                    if (x < width - 1)
                        up_right = field[y - 1][x + 1].getWall(Direction6.DOWN_LEFT);
                    else
                        up_right = new Wall();
                } else {
                    if (x > 0)
                        up_left = field[y - 1][x - 1].getWall(Direction6.DOWN_RIGHT);
                    else
                        up_left = new Wall();
                    up_right = field[y - 1][x].getWall(Direction6.DOWN_LEFT);
                }
                right = new Wall();
                down_left = new Wall();
                down_right = new Wall();
                field[y][x] = new Hexagon(up_left, up_right, right, down_right, down_left, left);
            }
        return field;
    }

    public Wall getWall(Direction6 direction) {
        switch (direction) {
            case DOWN_LEFT:
                return down_left;
            case UP_LEFT:
                return up_left;
            case DOWN_RIGHT:
                return down_right;
            case UP_RIGHT:
                return up_right;
            case LEFT:
                return left;
            case RIGHT:
                return right;
            default:
                return null;
        }
    }
}