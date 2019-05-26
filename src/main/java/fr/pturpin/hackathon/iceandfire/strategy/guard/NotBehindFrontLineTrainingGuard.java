package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

import java.util.Collection;
import java.util.Optional;

public class NotBehindFrontLineTrainingGuard implements TrainingGuard {

    private final GameRepository gameRepository;

    public NotBehindFrontLineTrainingGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        // If there is a unit nearby and it completely in my territory, then it's useless.

        GameCell cell = command.getCell();
        return isCompletelyInMyTerritory(cell) && isUnitNearby(cell);
    }

    private boolean isCompletelyInMyTerritory(GameCell cell) {
        Collection<Position> neighbors = cell.getPosition().getNeighbors();

        return neighbors.stream()
                .map(gameRepository::getCellType)
                .allMatch(this::isMyTerritory);
    }

    private boolean isMyTerritory(CellType cellType) {
        return cellType == CellType.ACTIVE_MINE || cellType == CellType.NIL;
    }

    private boolean isUnitNearby(GameCell cell) {
        Collection<Position> neighbors = cell.getPosition().getNeighbors();

        return neighbors.stream()
                .map(gameRepository::getPlayerUnitAt)
                .anyMatch(Optional::isPresent);
    }
}
