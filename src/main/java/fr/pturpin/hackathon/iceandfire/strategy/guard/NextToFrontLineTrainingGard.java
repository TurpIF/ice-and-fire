package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;

public class NextToFrontLineTrainingGard implements TrainingGuard {

    @Override
    public boolean isUseless(TrainCommand command) {
        // If the new unit will be next to the front line, then it's not useless, else yes
        GameCell cell = command.getCell();
        return cell.isInMyTerritory();
    }

}
