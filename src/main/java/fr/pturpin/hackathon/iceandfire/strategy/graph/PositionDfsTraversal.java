package fr.pturpin.hackathon.iceandfire.strategy.graph;

import fr.pturpin.hackathon.iceandfire.cell.Position;

import java.util.Collection;

public class PositionDfsTraversal extends DfsTraversal<Position> {
    @Override
    protected Collection<Position> getNeighbors(Position element) {
        return element.getNeighbors();
    }
}
