package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.unit.OpponentBuilding;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerBuilding;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

import java.util.Optional;

public interface Game {

    int getPlayerGold();

    Optional<PlayerUnit> getPlayerUnitAt(Position position);

    Optional<PlayerBuilding> getPlayerBuildingAt(Position position);

    Optional<OpponentUnit> getOpponentUnitAt(Position position);

    Optional<OpponentBuilding> getOpponentBuildingAt(Position position);

    CaseType getCellType(Position position);

    GameCell getCell(Position position);
}
