package fr.pturpin.hackathon.iceandfire.strategy.comparator.cell;

import fr.pturpin.hackathon.iceandfire.cell.Position;

import java.util.Comparator;

public final class PositionComparator implements Comparator<Position> {

    private final Comparator<Position> comparator;

    public PositionComparator() {
        comparator = Comparator
                .<Position>comparingInt(position -> position.getX() % 2)
                .thenComparingInt(position -> position.getY() % 2)
                .thenComparingInt(Position::getX)
                .thenComparingInt(Position::getY);
    }

    @Override
    public int compare(Position o1, Position o2) {
        return comparator.compare(o1, o2);
    }

}
