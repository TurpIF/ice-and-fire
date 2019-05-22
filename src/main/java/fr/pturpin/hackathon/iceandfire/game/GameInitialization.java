package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.Position;

public interface GameInitialization {

    void setMineSpotCount(int mineSpotCount);

    void addMineSpot(Position position);

}
