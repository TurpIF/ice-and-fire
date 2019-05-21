package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

public interface GameCell {

    Position getPosition();

    boolean containsAlly();

    boolean containsBeatableOpponentFor(PlayerUnit playerUnit);

    boolean containsBeatableOpponentForLevel(int level);

    boolean isInMyTerritoryOrInItsNeighborhood();
}
