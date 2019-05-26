package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromFrontLine;

public class NextToFrontLineTrainingGard implements TrainingGuard {

    private final DistanceFromFrontLine distanceFromFrontLine;

    public NextToFrontLineTrainingGard(DistanceFromFrontLine distanceFromFrontLine) {
        this.distanceFromFrontLine = distanceFromFrontLine;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        // If the new unit will be next to the front line, then it's not useless, else yes

        Position position = command.getCell().getPosition();
        int distance = distanceFromFrontLine.getDistanceOf(position);

        return distance > 1;

        // GameCell cell = command.getCell();
        // return cell.isInMyTerritory();
    }

}
