package fr.pturpin.hackathon.iceandfire.strategy.simulator;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractCommandSimulator<T extends GameCommand> implements CommandSimulator<T> {

    private final Comparator<T> comparator;

    protected AbstractCommandSimulator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void simulate(List<T> candidates) {
        while (true) {
            T bestCandidate = findBestCandidate(candidates);
            if (bestCandidate == null) {
                break;
            }

            runCommand(bestCandidate);
        }
    }

    private T findBestCandidate(List<T> candidates) {
        T bestCandidate = null;
        Iterator<T> iterator = candidates.iterator();
        while (iterator.hasNext()) {
            T command = iterator.next();

            if (willNeverBeUsefulThisRound(command)) {
                iterator.remove();
            } else {
                if (command.isValid() && isUseful(command)) {
                    if (bestCandidate == null) {
                        bestCandidate = command;
                    } else if (comparator.compare(command, bestCandidate) > 0) {
                        bestCandidate = command;
                    }
                }
            }
        }
        return bestCandidate;
    }

    protected abstract void runCommand(T command);

    protected abstract boolean isUseful(T command);

    protected abstract boolean willNeverBeUsefulThisRound(T command);

}
