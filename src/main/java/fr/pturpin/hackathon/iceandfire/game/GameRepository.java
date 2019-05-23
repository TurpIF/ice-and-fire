package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.OpponentBuilding;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerBuilding;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

import java.util.Optional;

public interface GameRepository {

    int getPlayerGold();

    Optional<PlayerUnit> getPlayerUnitAt(Position position);

    Optional<PlayerBuilding> getPlayerBuildingAt(Position position);

    Optional<OpponentUnit> getOpponentUnitAt(Position position);

    Optional<OpponentBuilding> getOpponentBuildingAt(Position position);

    CellType getCellType(Position position);

    GameCell getCell(Position position);

    boolean isMineSpot(Position position);

    int getPlayerMineCount();

    void moveUnit(PlayerUnit playerUnit, GameCell newCell);
}
