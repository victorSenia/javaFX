package com.leo.test.javaFX.maze;

/**
 * Created by Senchenko Victor on 12.12.2016.
 */
public interface Shape<T> {
    Shape[][] field(int width, int height);

    Wall getWall(T direction);
}
