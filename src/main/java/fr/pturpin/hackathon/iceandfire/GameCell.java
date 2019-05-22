package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

public class GameCell {

    private final Game game;
    private final Position position;

    public GameCell(Game game, Position position) {
        this.game = game;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public boolean containsAlly() {
        return game.getPlayerUnitAt(position).isPresent() || game.getPlayerBuildingAt(position).isPresent();
    }

    public boolean containsBeatableOpponentFor(PlayerUnit playerUnit) {
        return containsBeatableOpponentBuildingFor(playerUnit) && containsBeatableOpponentUnitFor(playerUnit);
    }

    private Boolean containsBeatableOpponentUnitFor(PlayerUnit playerUnit) {
        return game.getOpponentUnitAt(position).map(playerUnit::canBeat).orElse(true);
    }

    private Boolean containsBeatableOpponentBuildingFor(PlayerUnit playerUnit) {
        return game.getOpponentBuildingAt(position).map(playerUnit::canBeat).orElse(true);
    }

    public boolean containsBeatableOpponentFor(TrainedPlayerUnit trainedPlayerUnit) {
        return containsBeatableOpponentBuildingFor(trainedPlayerUnit) && containsBeatableOpponentUnitFor(trainedPlayerUnit);
    }

    private Boolean containsBeatableOpponentUnitFor(TrainedPlayerUnit trainedPlayerUnit) {
        return game.getOpponentUnitAt(position).map(trainedPlayerUnit::canBeat).orElse(true);
    }

    private Boolean containsBeatableOpponentBuildingFor(TrainedPlayerUnit trainedPlayerUnit) {
        return game.getOpponentBuildingAt(position).map(trainedPlayerUnit::canBeat).orElse(true);
    }

    public boolean isInMyTerritoryOrInItsNeighborhood() {
        return isInMyTerritory() || position.getNeighbors().stream()
                .map(game::getCell)
                .anyMatch(GameCell::isInMyTerritory);
    }

    public boolean isInMyTerritory() {
        return game.getCellType(position) == CaseType.ACTIVE_MINE;
    }
}
