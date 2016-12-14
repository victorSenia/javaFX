package com.leo.test.javaFX.maze;

/**
 * Created by Senchenko Victor on 09.12.2016.
 */
class Wall {
    private boolean closed = true;

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
