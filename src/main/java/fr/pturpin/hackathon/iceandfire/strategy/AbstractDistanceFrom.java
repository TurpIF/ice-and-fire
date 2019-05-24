package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractDistanceFrom {

    protected final GameRepository game;

    // FIXME this class should not be aware of the grid size
    private final int[] distances = new int[12 * 12];
    private final Queue<Position> toVisit = new ArrayDeque<>(12 * 12);
    private final Set<Position> visited = new HashSet<>(12 * 12);

    public AbstractDistanceFrom(GameRepository game) {
        this.game = game;
    }

    public void compute() {
        toVisit.clear();
        visited.clear();

        ignoreWalls();
        getStartingCellsToVisit().forEach(toVisit::add);
        initializeDistances();

        while (!toVisit.isEmpty()) {
            Position current = toVisit.remove();
            if (visited.contains(current)) {
                continue;
            }

            int currentIndex = toIndex(current);

            visited.add(current);

            Collection<Position> neighbors = current.getNeighbors();
            for (Position neighbor : neighbors) {
                toVisit.add(neighbor);
                int neighborIndex = toIndex(neighbor);

                boolean hasFoundShorterPath = distances[neighborIndex] == -1
                        || distances[neighborIndex] > distances[currentIndex];

                if (hasFoundShorterPath) {
                    distances[neighborIndex] = distances[currentIndex] + 1;
                }
            }
        }
    }

    private void initializeDistances() {
        Arrays.fill(distances, -1);
        toVisit.forEach(current -> {
            int index = toIndex(current);
            distances[index] = 0;
        });
    }

    protected abstract Stream<Position> getStartingCellsToVisit();

    private void ignoreWalls() {
        game.getAllCells()
                .filter(GameCell::isWall)
                .map(GameCell::getPosition)
                .forEach(visited::add);
    }

    public int getDistanceOf(Position position) {
        int index = toIndex(position);
        return distances[index];
    }

    // FIXME this class should not be aware of the grid topology
    private int toIndex(Position position) {
        return position.getY() * 12 + position.getX();
    }
}
