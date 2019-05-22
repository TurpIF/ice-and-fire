package fr.pturpin.hackathon.iceandfire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Position {

    private static final int MIN_POSITION = 0;
    private static final int MAX_POSITION = 11;

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;

        checkValidity();
    }

    private void checkValidity() {
        if (x < MIN_POSITION || x > MAX_POSITION || y < MIN_POSITION || y > MAX_POSITION) {
            throw new IllegalArgumentException();
        }
    }

    public int distanceTo(Position position) {
        return Math.abs(x - position.getX()) + Math.abs(y - position.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Collection<Position> getNeighbors() {
        List<Position> neighbors = new ArrayList<>();

        if (x > MIN_POSITION) {
            neighbors.add(new Position(x - 1, y));
        }

        if (x < MAX_POSITION) {
            neighbors.add(new Position(x + 1, y));
        }

        if (y > MIN_POSITION) {
            neighbors.add(new Position(x, y - 1));
        }

        if (y < MAX_POSITION) {
            neighbors.add(new Position(x, y + 1));
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
