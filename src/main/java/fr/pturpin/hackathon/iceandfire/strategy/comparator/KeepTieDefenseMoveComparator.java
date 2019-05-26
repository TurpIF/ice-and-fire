package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

import java.util.Collection;
import java.util.Comparator;

public class KeepTieDefenseMoveComparator implements Comparator<MoveCommand> {

    private final GameRepository gameRepository;

    public KeepTieDefenseMoveComparator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public int compare(MoveCommand o1, MoveCommand o2) {
        return Comparator.comparingInt(this::evaluate).compare(o1, o2);
    }

    private int evaluate(MoveCommand command) {
        return canKeepTieDefense(command) ? 1 : 0;
    }

    private boolean canKeepTieDefense(MoveCommand command) {
        GameCell cell = command.getCell();

        if (!cell.isInMyTerritory()) {
            return false;
        }

        PlayerUnit playerUnit = command.getPlayerUnit();
        Collection<Position> neighbors = cell.getPosition().getNeighbors();

        for (Position neighbor : neighbors) {
            if (gameRepository.getCellType(neighbor) == CellType.INACTIVE_THEIR) {
                continue;
            }

            boolean isTie = gameRepository.getOpponentUnitAt(neighbor)
                    .filter(opponentUnit -> isTie(opponentUnit, playerUnit))
                    .isPresent();

            if (isTie) {
                return true;
            }
        }

        return false;
    }

    private boolean isTie(OpponentUnit opponentUnit, PlayerUnit playerUnit) {
        return !opponentUnit.canBeat(playerUnit) && !playerUnit.canBeat(opponentUnit);
    }

}
