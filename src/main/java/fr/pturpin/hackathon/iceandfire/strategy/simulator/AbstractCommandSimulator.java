package fr.pturpin.hackathon.iceandfire.strategy.simulator;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractCommandSimulator<T extends GameCommand> implements CommandSimulator<T> {

    private final Comparator<T> comparator;

    protected AbstractCommandSimulator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void simulate(List<T> candidates) {
        removeNeverUseful(candidates);

        candidates.sort(comparator);

        List<T> ignored = new ArrayList<>();

        while (!candidates.isEmpty()) {
            T command = candidates.remove(candidates.size() - 1);

            if (command.isValid() && isUseful(command)) {
                runCommand(command);

                candidates.addAll(ignored);
                candidates.sort(comparator);

                ignored.clear();
            } else if (!willNeverBeUsefulThisRound(command)) {
                ignored.add(command);
            }
        }
    }

    private void removeNeverUseful(List<T> candidates) {
        candidates.removeIf(this::willNeverBeUsefulThisRound);
    }

    protected abstract void runCommand(T command);

    protected abstract boolean isUseful(T command);

    protected abstract boolean willNeverBeUsefulThisRound(T command);

}
