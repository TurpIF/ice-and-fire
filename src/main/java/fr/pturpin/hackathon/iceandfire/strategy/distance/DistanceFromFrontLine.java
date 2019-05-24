package fr.pturpin.hackathon.iceandfire.strategy.distance;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

import java.util.stream.Stream;

public class DistanceFromFrontLine extends AbstractDistanceFrom {

    public DistanceFromFrontLine(GameRepository game) {
        super(game);
    }

    @Override
    protected Stream<Position> getStartingCellsToVisit() {
        return game.getAllCells()
                .filter(cell -> !cell.isInMyTerritory())
                .map(GameCell::getPosition);
    }

}
