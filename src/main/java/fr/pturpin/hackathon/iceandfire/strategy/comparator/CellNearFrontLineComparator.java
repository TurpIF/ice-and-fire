package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromFrontLine;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromOpponentCastle;

import java.util.Comparator;

public class CellNearFrontLineComparator implements Comparator<GameCell> {

    private final Comparator<GameCell> comparator;
    private final GameRepository gameRepository;
    private DistanceFromOpponentCastle distanceFromOpponentCastle;

    public CellNearFrontLineComparator(
            DistanceFromOpponentCastle distanceFromOpponentCastle,
            DistanceFromFrontLine distanceFromFrontLine,
            GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.distanceFromOpponentCastle = distanceFromOpponentCastle;

        comparator = Comparator.<GameCell>comparingInt(cell -> isOpponentCastle(cell) ? 1 : 0)
                .thenComparingInt(cell -> cell.isInMyTerritory() ? -1 : 0)
                .thenComparingInt(this::getOpponentNeighborsCount)
                .thenComparingInt(cell -> -distanceFromOpponentCastle.getDistanceOf(cell.getPosition()))
                .thenComparingInt(cell -> -distanceFromFrontLine.getDistanceOf(cell.getPosition()))
                .thenComparing(GameCell::getPosition, new PositionComparator());
    }

    private boolean isOpponentCastle(GameCell cell) {
        return distanceFromOpponentCastle.getDistanceOf(cell.getPosition()) == 0;
    }

    private int getOpponentNeighborsCount(GameCell cell) {
        if (cell.isInOpponentTerritory()) {
            return (int) cell.getPosition().getNeighbors().stream()
                    .map(gameRepository::getCell)
                    .filter(GameCell::isInOpponentTerritory)
                    .count();
        }
        return 0;
    }

    @Override
    public int compare(GameCell o1, GameCell o2) {
        return comparator.compare(o1, o2);
    }
}
