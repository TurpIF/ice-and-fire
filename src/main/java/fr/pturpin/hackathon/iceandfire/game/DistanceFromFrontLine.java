package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;

import java.util.stream.Stream;

public class DistanceFromFrontLine extends AbstractDistanceFrom {

    public DistanceFromFrontLine(Game game) {
        super(game);
    }

    @Override
    protected Stream<Position> getStartingCellsToVisit() {
        return getAllPositions().filter(this::isNotInMyTerritory);
    }

    private boolean isNotInMyTerritory(Position position) {
        GameCell cell = game.getCell(position);
        return !cell.isInMyTerritory();
    }
}
