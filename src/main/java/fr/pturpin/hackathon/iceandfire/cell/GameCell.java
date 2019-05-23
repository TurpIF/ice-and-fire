package fr.pturpin.hackathon.iceandfire.cell;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.Objects;

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

    public boolean containsBeatableOpponentFor(TrainedUnit trainedUnit) {
        return containsBeatableOpponentBuildingFor(trainedUnit) && containsBeatableOpponentUnitFor(trainedUnit);
    }

    private Boolean containsBeatableOpponentUnitFor(TrainedUnit trainedUnit) {
        return gameRepository.getOpponentUnitAt(position).map(trainedUnit::canBeat).orElse(true);
    }

    private Boolean containsBeatableOpponentBuildingFor(TrainedUnit trainedUnit) {
        return gameRepository.getOpponentBuildingAt(position).map(trainedUnit::canBeat).orElse(true);
    }

    public boolean isInMyTerritoryOrInItsNeighborhood() {
        return isInMyTerritory() || position.getNeighbors().stream()
                .map(gameRepository::getCell)
                .anyMatch(GameCell::isInMyTerritory);
    }

    public boolean isInMyTerritory() {
        return gameRepository.getCellType(position) == CellType.ACTIVE_MINE;
    }

    public boolean isWall() {
        return gameRepository.getCellType(position) == CellType.NIL;
    }

    public boolean isMineSpot() {
        return gameRepository.isMineSpot(position);
    }

    public boolean isOccupied() {
        return gameRepository.getPlayerUnitAt(position).isPresent()
                || gameRepository.getPlayerBuildingAt(position).isPresent()
                || gameRepository.getOpponentUnitAt(position).isPresent()
                || gameRepository.getOpponentBuildingAt(position).isPresent();
    }

    public boolean isProtectedByOpponentTower() {
        return getPosition().getNeighbors().stream()
                .map(gameRepository::getCell)
                .anyMatch(GameCell::containsTower);
    }

    public boolean containsTower() {
        return gameRepository.getOpponentBuildingAt(position)
                .filter(building -> building.getType() == BuildingType.TOWER)
                .isPresent();
    }

    public void setEnteringUnit(PlayerUnit playerUnit) {
        gameRepository.moveUnit(playerUnit, this);
    }

    public void removeLeavingUnit(PlayerUnit playerUnit) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCell gameCell = (GameCell) o;
        return Objects.equals(gameRepository, gameCell.gameRepository) &&
                Objects.equals(position, gameCell.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameRepository, position);
    }
}
