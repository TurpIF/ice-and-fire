package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.PlayerBuilding;

import java.util.Collection;
import java.util.Optional;

public class NotNextToTowerTrainingGuard implements TrainingGuard {

    private final GameRepository gameRepository;

    public NotNextToTowerTrainingGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        GameCell cell = command.getCell();
        if (!cell.isInMyTerritory()) {
            return false;
        }

        Collection<Position> neighbors = cell.getPosition().getNeighbors();

        for (Position neighbor : neighbors) {
            Optional<PlayerBuilding> optBuilding = gameRepository.getPlayerBuildingAt(neighbor);
            if (optBuilding.isPresent()) {
                PlayerBuilding building = optBuilding.get();
                if (building.getType() == BuildingType.TOWER) {
                    return true;
                }
            }
        }

        return false;
    }

}
