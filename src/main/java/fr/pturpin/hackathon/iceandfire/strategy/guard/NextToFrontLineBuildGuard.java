package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.BuildCommand;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromOpponentLine;

public class NextToFrontLineBuildGuard implements BuildGuard {

    private final DistanceFromOpponentLine distanceFromOpponentLine;

    public NextToFrontLineBuildGuard(DistanceFromOpponentLine distanceFromOpponentLine) {
        this.distanceFromOpponentLine = distanceFromOpponentLine;
    }

    @Override
    public boolean isUseless(BuildCommand command) {
        // If the new unit will be next to the front line, then it's not useless, else yes

        Position position = command.getCell().getPosition();
        int distance = distanceFromOpponentLine.getDistanceOf(position);

        return distance > 2;
    }

}
