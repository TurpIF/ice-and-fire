package fr.pturpin.hackathon.iceandfire;

public interface GameNewTurn {

    void setPlayerGold(int playerGold);

    void setPlayerRevenue(int playerRevenue);

    void setOpponentGold(int opponentGold);

    void setOpponentRevenue(int opponentRevenue);

    void setGrid(CaseType[] grid);

    void setBuildingCount(int buildingCount);

    void setUnitCount(int unitCount);

    void addBuilding(Owner owner, BuildingType buildingType, Position position);

    void addUnit(Owner owner, int unitId, int level, Position position);

}
