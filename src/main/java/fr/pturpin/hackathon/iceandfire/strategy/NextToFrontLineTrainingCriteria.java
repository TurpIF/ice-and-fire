package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;

public class NextToFrontLineTrainingCriteria implements TrainingUsefulnessCriteria {

    private final DistanceFromFrontLine distanceFromFrontLine;

    public NextToFrontLineTrainingCriteria(DistanceFromFrontLine distanceFromFrontLine) {
        this.distanceFromFrontLine = distanceFromFrontLine;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        // If the new unit will be next to the front line, then no, else yes
        GameCell cell = command.getCell();
        int distance = distanceFromFrontLine.getDistanceOf(cell.getPosition());
        return distance > 0;
    }

}
