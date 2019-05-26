package fr.pturpin.hackathon.iceandfire.strategy.generator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.BuildCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerBuilding;

import java.util.List;
import java.util.stream.Collectors;

public class TowerBuildCommandGenerator implements CommandGenerator<BuildCommand> {

    private final GameRepository gameRepository;
    private final TrainedPlayerBuilding building;

    public TowerBuildCommandGenerator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.building = new TrainedPlayerBuilding(BuildingType.TOWER, gameRepository);
    }

    @Override
    public List<BuildCommand> generate() {
        return gameRepository.getAllCells()
                .filter(this::isCandidate)
                .map(this::createCommand)
                .collect(Collectors.toList());
    }

    private BuildCommand createCommand(GameCell cell) {
        return new BuildCommand(building, cell, gameRepository);
    }

    private boolean isCandidate(GameCell gameCell) {
        return gameCell.isInMyTerritory()
                && !gameRepository.getPlayerBuildingAt(gameCell.getPosition()).isPresent();
    }

}
