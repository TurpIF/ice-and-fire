package fr.pturpin.hackathon.iceandfire.cell;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;

public class GameCell {

    private final GameRepository gameRepository;
    private final Position position;

    public GameCell(GameRepository gameRepository, Position position) {
        this.gameRepository = gameRepository;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public boolean containsAlly() {
        return gameRepository.getPlayerUnitAt(position).isPresent() || gameRepository.getPlayerBuildingAt(position).isPresent();
    }

    public boolean containsBeatableOpponentFor(PlayerUnit playerUnit) {
        return containsBeatableOpponentBuildingFor(playerUnit) && containsBeatableOpponentUnitFor(playerUnit);
    }

    private Boolean containsBeatableOpponentUnitFor(PlayerUnit playerUnit) {
        return gameRepository.getOpponentUnitAt(position).map(playerUnit::canBeat).orElse(true);
    }

    private Boolean containsBeatableOpponentBuildingFor(PlayerUnit playerUnit) {
        return gameRepository.getOpponentBuildingAt(position).map(playerUnit::canBeat).orElse(true);
    }

    public boolean containsBeatableOpponentFor(TrainedPlayerUnit trainedPlayerUnit) {
        return containsBeatableOpponentBuildingFor(trainedPlayerUnit) && containsBeatableOpponentUnitFor(trainedPlayerUnit);
    }

    private Boolean containsBeatableOpponentUnitFor(TrainedPlayerUnit trainedPlayerUnit) {
        return gameRepository.getOpponentUnitAt(position).map(trainedPlayerUnit::canBeat).orElse(true);
    }

    private Boolean containsBeatableOpponentBuildingFor(TrainedPlayerUnit trainedPlayerUnit) {
        return gameRepository.getOpponentBuildingAt(position).map(trainedPlayerUnit::canBeat).orElse(true);
    }

    public boolean isInMyTerritoryOrInItsNeighborhood() {
        return isInMyTerritory() || position.getNeighbors().stream()
                .map(gameRepository::getCell)
                .anyMatch(GameCell::isInMyTerritory);
    }

    public boolean isInMyTerritory() {
        return gameRepository.getCellType(position) == CellType.ACTIVE_MINE;
    }
}
