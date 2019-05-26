package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.BuildCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NotNextToTowerGuard implements BuildGuard {

    private final GameRepository gameRepository;

    public NotNextToTowerGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isUseless(BuildCommand command) {
        Set<Position> neighborhood = new HashSet<>();
        Position position = command.getCell().getPosition();
        Collection<Position> neighbors = position.getNeighbors();

        for (Position neighbor : neighbors) {
            neighborhood.add(neighbor);
            neighborhood.addAll(neighbor.getNeighbors());
        }

        return neighborhood.stream()
                .map(gameRepository::getPlayerBuildingAt)
                .anyMatch(optBuilding -> optBuilding
                        .filter(building -> building.getType() == BuildingType.TOWER)
                        .isPresent());
    }

}
