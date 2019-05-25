package fr.pturpin.hackathon.iceandfire.strategy.distance;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

import java.util.stream.Stream;

public class DistanceFromOpponentCastle extends AbstractDistanceFrom {

    public DistanceFromOpponentCastle(GameRepository game) {
        super(game);
    }

    @Override
    protected Stream<Position> getStartingCellsToVisit() {
        Position opponentQgPosition = game.getOpponentQg().getPosition();
        return Stream.of(opponentQgPosition);
    }

}
