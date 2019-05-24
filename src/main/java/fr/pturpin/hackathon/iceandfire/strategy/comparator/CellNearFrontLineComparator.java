package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromFrontLine;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromOpponentCastle;

import java.util.Comparator;

public class CellNearFrontLineComparator implements Comparator<GameCell> {

    private final Comparator<GameCell> comparator;

    public CellNearFrontLineComparator(
            DistanceFromOpponentCastle distanceFromOpponentCastle,
            DistanceFromFrontLine distanceFromFrontLine) {
        comparator = Comparator.<GameCell>comparingInt(cell -> cell.isInMyTerritory() ? -1 : 0)
                .thenComparingInt(cell -> -distanceFromOpponentCastle.getDistanceOf(cell.getPosition()))
                .thenComparingInt(cell -> -distanceFromFrontLine.getDistanceOf(cell.getPosition()))
                .thenComparing(GameCell::getPosition, new PositionComparator());
    }

    @Override
    public int compare(GameCell o1, GameCell o2) {
        return comparator.compare(o1, o2);
    }
}
