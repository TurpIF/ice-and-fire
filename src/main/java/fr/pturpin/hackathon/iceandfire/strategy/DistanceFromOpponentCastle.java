package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;

import java.util.stream.Stream;

public class DistanceFromOpponentCastle extends AbstractDistanceFrom {

    public DistanceFromOpponentCastle(GameRepository game) {
        super(game);
    }

    @Override
    protected Stream<Position> getStartingCellsToVisit() {
        return Stream.of(findOpponentQg());
    }

    private Position findOpponentQg() {
        // FIXME repository should provide such method
        Position topLeftPosition = new Position(0, 0);
        boolean isAtTopLeft = game.getOpponentBuildingAt(topLeftPosition)
                .filter(building -> building.getType() == BuildingType.QG)
                .isPresent();

        if (isAtTopLeft) {
            return topLeftPosition;
        } else {
            return new Position(11, 11);
        }
    }
}
