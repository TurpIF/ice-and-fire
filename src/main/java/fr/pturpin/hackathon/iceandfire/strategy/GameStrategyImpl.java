package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.CellNearFrontLineComparator;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.MoveCommandComparator;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.TrainCommandComparator;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromFrontLine;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromOpponentCastle;
import fr.pturpin.hackathon.iceandfire.strategy.generator.CommandGenerator;
import fr.pturpin.hackathon.iceandfire.strategy.generator.MoveCommandGenerator;
import fr.pturpin.hackathon.iceandfire.strategy.generator.TrainCommandGenerator;
import fr.pturpin.hackathon.iceandfire.strategy.guard.*;
import fr.pturpin.hackathon.iceandfire.strategy.simulator.AbstractCommandSimulator;
import fr.pturpin.hackathon.iceandfire.strategy.simulator.CommandSimulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class GameStrategyImpl implements GameStrategy {

    private final GameRepository game;
    private final DistanceFromFrontLine distanceFromFrontLine;
    private final DistanceFromOpponentCastle distanceFromOpponentCastle;

    private final CommandGenerator<MoveCommand> moveCommandGenerator;

    private final CommandSimulator<MoveCommand> moveCommandSimulator;
    private final CommandSimulator<TrainCommand> trainCommandSimulator;
    private final TrainCommandGenerator trainCommandGenerator;

    private List<GameCommand> commands = new ArrayList<>();

    public GameStrategyImpl(GameRepository game) {
        this.game = game;

        distanceFromOpponentCastle = new DistanceFromOpponentCastle(game);
        distanceFromFrontLine = new DistanceFromFrontLine(game);

        MoveGuard moveGuard = createMoveGard();
        TrainingGuard trainingGuard = createTrainingGard();

        CellNearFrontLineComparator cellNearFrontLineComparator = new CellNearFrontLineComparator(
                distanceFromOpponentCastle, distanceFromFrontLine);

        Comparator<MoveCommand> moveComparator = new MoveCommandComparator(game, cellNearFrontLineComparator);
        Comparator<TrainCommand> trainComparator = new TrainCommandComparator(game, cellNearFrontLineComparator);

        moveCommandSimulator = new MoveCommandSimulator(moveComparator, moveGuard);
        trainCommandSimulator = new TrainCommandSimulator(trainComparator, trainingGuard);

        moveCommandGenerator = new MoveCommandGenerator(game);
        trainCommandGenerator = new TrainCommandGenerator(game);
    }

    private MoveGuard createMoveGard() {
        List<MoveGuard> allGuards = new ArrayList<>();
        allGuards.add(new KeepingTiePositionMoveGuard(game));
        return new AnyMoveGuard(allGuards);
    }

    private TrainingGuard createTrainingGard() {
        List<TrainingGuard> allGuards = new ArrayList<>();
        allGuards.add(new NextToFrontLineTrainingGard());
        allGuards.add(new NoSuicideTrainingGard(game));
        return new AnyTrainingGuard(allGuards);
    }

    @Override
    public Collection<GameCommand> buildCommands() {
        computeDistances();

        commands.clear();
        addMoveCommands();
        addTrainCommands();
        return commands;
    }

    private void computeDistances() {
        distanceFromFrontLine.compute();
        distanceFromOpponentCastle.compute();
    }

    private void addMoveCommands() {
        List<MoveCommand> candidates = moveCommandGenerator.generate();
        moveCommandSimulator.simulate(candidates);
    }

    private void addTrainCommands() {
        List<TrainCommand> candidates = trainCommandGenerator.generate();
        trainCommandSimulator.simulate(candidates);
    }

    private void addCommand(GameCommand command) {
        if (command.isValid()) {
            command.execute();
            commands.add(command);
        }
    }

    private class TrainCommandSimulator extends AbstractCommandSimulator<TrainCommand> {

        private TrainingGuard trainingGuard;

        private TrainCommandSimulator(Comparator<TrainCommand> comparator, TrainingGuard trainingGuard) {
            super(comparator);
            this.trainingGuard = trainingGuard;
        }

        @Override
        protected void runCommand(TrainCommand command) {
            addCommand(command);
        }

        @Override
        protected boolean isUseful(TrainCommand command) {
            return !trainingGuard.isUseless(command);
        }

        @Override
        protected boolean willNeverBeUsefulThisRound(TrainCommand command) {
            return command.willNeverBeValidThisRound();
        }
    }

    private class MoveCommandSimulator extends AbstractCommandSimulator<MoveCommand> {

        private MoveGuard moveGuard;

        private MoveCommandSimulator(Comparator<MoveCommand> moveComparator, MoveGuard moveGuard) {
            super(moveComparator);
            this.moveGuard = moveGuard;
        }

        @Override
        protected void runCommand(MoveCommand command) {
            addCommand(command);
        }

        @Override
        protected boolean isUseful(MoveCommand command) {
            return !moveGuard.isUseless(command);
        }

        @Override
        protected boolean willNeverBeUsefulThisRound(MoveCommand command) {
            return false;
        }
    }

}
