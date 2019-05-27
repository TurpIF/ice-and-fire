package fr.pturpin.hackathon.iceandfire.strategy.guard.train;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.BuildCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.OpponentCount;
import fr.pturpin.hackathon.iceandfire.strategy.guard.tower.BuildGuard;

import java.util.HashSet;
import java.util.Set;

public class WithOpponentNearbyGuard implements BuildGuard {

    private final GameRepository gameRepository;

    public WithOpponentNearbyGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isUseless(BuildCommand command) {
        OpponentCount count = getOpponentCount(command.getCell().getPosition());
        return count.score() < 5;
    }

    private OpponentCount getOpponentCount(Position origin) {
        Set<Position> toVisit = new HashSet<>();

        toVisit.add(origin);
        origin.getNeighbors().stream()
                .flatMap(neighbor -> neighbor.getNeighbors().stream())
                .forEach(toVisit::add);

        OpponentCount count = new OpponentCount();
        for (Position position : toVisit) {
            gameRepository.getOpponentUnitAt(position).ifPresent(count::add);
            gameRepository.getOpponentBuildingAt(position).ifPresent(count::add);
        }

        return count;
    }

}
