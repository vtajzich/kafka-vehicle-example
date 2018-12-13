package com.kafka.vehicle.domain;

import java.util.StringJoiner;

class DefaultPosition implements Position {

    private final int x;
    private final int y;

    public DefaultPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DefaultPosition.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }
}
