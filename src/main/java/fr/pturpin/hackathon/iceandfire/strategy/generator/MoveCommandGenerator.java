package fr.pturpin.hackathon.iceandfire.strategy.generator;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveCommandGenerator implements CommandGenerator<MoveCommand> {

    private final GameRepository gameRepository;

    public MoveCommandGenerator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<MoveCommand> generate() {
        return gameRepository.getAllPlayerUnits()
                .flatMap(this::generate)
                .collect(Collectors.toList());
    }

    private Stream<MoveCommand> generate(PlayerUnit unit) {
        Collection<Position> neighbors = unit.getPosition().getNeighbors();

        return neighbors.stream()
                .map(gameRepository::getCell)
                .map(cell -> new MoveCommand(unit, cell));
    }

}
