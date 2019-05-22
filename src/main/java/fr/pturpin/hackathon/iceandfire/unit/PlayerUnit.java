package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.Position;

public interface PlayerUnit {

    boolean canMove();

    int getId();

    Position getPosition();

    boolean canBeat(OpponentUnit opponentUnit);

    boolean canBeat(OpponentBuilding opponentBuilding);

}
