package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.CellNearFrontLineComparator;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.MoveCommandComparator;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.TrainCommandComparator;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromFrontLine;
import fr.pturpin.hackathon.iceandfire.strategy.distance.DistanceFromOpponentCastle;
import fr.pturpin.hackathon.iceandfire.strategy.guard.*;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameStrategyImpl implements GameStrategy {

    private final GameRepository game;
    private final DistanceFromFrontLine distanceFromFrontLine;
    private final DistanceFromOpponentCastle distanceFromOpponentCastle;

    private final MoveGuard moveGuard;
    private final TrainingGuard trainingGuard;

    private final Comparator<MoveCommand> moveComparator;
    private final Comparator<TrainCommand> trainComparator;

    private List<GameCommand> commands = new ArrayList<>();

    public GameStrategyImpl(GameRepository game) {
        this.game = game;

        distanceFromOpponentCastle = new DistanceFromOpponentCastle(game);
        distanceFromFrontLine = new DistanceFromFrontLine(game);
        moveGuard = createMoveGard();
        trainingGuard = createTrainingGard();

        CellNearFrontLineComparator cellNearFrontLineComparator = new CellNearFrontLineComparator(
                distanceFromOpponentCastle, distanceFromFrontLine);

        moveComparator = new MoveCommandComparator(game, cellNearFrontLineComparator);
        trainComparator = new TrainCommandComparator(game, cellNearFrontLineComparator);
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

    private void addCommand(GameCommand command) {
        if (command.isValid()) {
            command.execute();
            commands.add(command);
        }
    }

    private void computeDistances() {
        distanceFromFrontLine.compute();
        distanceFromOpponentCastle.compute();
    }

    private void addMoveCommands() {
        game.getAllPlayerUnits()
                .sorted(Comparator.comparingInt(playerUnit -> -distanceFromFrontLine.getDistanceOf(playerUnit.getPosition())))
                .forEach(this::addMoveCommand);
    }

    private void addMoveCommand(PlayerUnit unit) {
        Collection<Position> neighbors = unit.getPosition().getNeighbors();
        List<MoveCommand> candidates = new ArrayList<>();

        for (Position neighbor : neighbors) {
            GameCell neighborCell = game.getCell(neighbor);
            MoveCommand command = new MoveCommand(unit, neighborCell);

            if (command.isValid() && isMoveUseful(command)) {
                candidates.add(command);
            }
        }

        candidates.stream()
                .max(moveComparator)
                .ifPresent(this::addCommand);
    }

    private boolean isMoveUseful(MoveCommand command) {
        return !moveGuard.isUseless(command);
    }

    private boolean isTrainingUseful(TrainCommand command) {
        return !trainingGuard.isUseless(command);
    }

    private void addTrainCommands() {
        List<TrainCommand> candidates = generateTrainCommands();

        candidates.sort(trainComparator);

        List<TrainCommand> ignored = new ArrayList<>();

        while (!candidates.isEmpty()) {
            TrainCommand command = candidates.remove(candidates.size() - 1);

            if (command.isValid() && isTrainingUseful(command)) {
                addCommand(command);

                candidates.addAll(ignored);
                candidates.sort(trainComparator);

                ignored.clear();
            } else if (!command.willNeverBeValidThisRound()) {
                ignored.add(command);
            }
        }
    }

    private List<TrainCommand> generateTrainCommands() {
        TrainedUnit trainedUnit1 = new TrainedUnit(1);
        TrainedUnit trainedUnit2 = new TrainedUnit(2);
        TrainedUnit trainedUnit3 = new TrainedUnit(3);

        return game.getAllCells()
                .flatMap(cell -> Stream.of(
                        new TrainCommand(trainedUnit1, cell, game),
                        new TrainCommand(trainedUnit2, cell, game),
                        new TrainCommand(trainedUnit3, cell, game)))
                .filter(command -> !command.willNeverBeValidThisRound())
                .collect(Collectors.toList());
    }

}
