package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.Owner;

public interface GameNewTurn {

    void setPlayerGold(int playerGold);

    void setPlayerRevenue(int playerRevenue);

    void setOpponentGold(int opponentGold);

    void setOpponentRevenue(int opponentRevenue);

    void setGrid(CellType[] grid);

    void setBuildingCount(int buildingCount);

    void setUnitCount(int unitCount);

    void addBuilding(Owner owner, BuildingType buildingType, Position position);

    void addUnit(Owner owner, int unitId, int level, Position position);

}
