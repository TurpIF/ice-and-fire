package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.BuildCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.PlayerBuilding;

import java.util.Collection;
import java.util.Optional;

public class NotNextToTowerGuard implements BuildGuard {

    private final GameRepository gameRepository;

    public NotNextToTowerGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isUseless(BuildCommand command) {
        Collection<Position> neighbors = command.getCell().getPosition().getNeighbors();

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
