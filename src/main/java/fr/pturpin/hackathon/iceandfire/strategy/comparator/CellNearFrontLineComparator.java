package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromFrontLine;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromOpponentCastle;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;

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
                .thenComparingInt(this::evaluateCellType)
                .thenComparingInt(this::getOpponentNeighborsCount)
                .thenComparingInt(cell -> -distanceFromOpponentCastle.getDistanceOf(cell.getPosition()))
                .thenComparingInt(cell -> -distanceFromFrontLine.getDistanceOf(cell.getPosition()))
                .thenComparingDouble(this::l2NormFromOpponentCastle)
                .thenComparing(GameCell::getPosition, new PositionComparator());
    }

    private int evaluateCellType(GameCell cell) {
        CellType cellType = gameRepository.getCellType(cell.getPosition());
        if (cellType == CellType.NIL) {
            return 0;
        } else if (cellType == CellType.ACTIVE_MINE || cellType == CellType.INACTIVE_MINE) {
            return 1;
        } else if (cellType == CellType.NEUTRAL) {
            return 2;
        } else if (cellType == CellType.INACTIVE_THEIR) {
            return 3;
        } else if (cellType == CellType.ACTIVE_THEIR) {
            return 4;
        }
        throw new IllegalStateException("Unexpected value: " + cellType);
    }

    private double l2NormFromOpponentCastle(GameCell cell) {
        return -cell.getPosition().l2Norm(findOpponentQg());
    }

    private Position findOpponentQg() {
        // FIXME repository should provide such method
        Position topLeftPosition = new Position(0, 0);
        boolean isAtTopLeft = gameRepository.getOpponentBuildingAt(topLeftPosition)
                .filter(building -> building.getType() == BuildingType.QG)
                .isPresent();

        if (isAtTopLeft) {
            return topLeftPosition;
        } else {
            return new Position(11, 11);
        }
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
