package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

import java.util.Collection;
import java.util.Optional;

public class KeepingTiePositionMoveCriteria implements MoveUsefulnessCriteria {

    private final GameRepository game;

    public KeepingTiePositionMoveCriteria(GameRepository game) {
        this.game = game;
    }

    @Override
    public boolean isUseless(MoveCommand command) {
        PlayerUnit playerUnit = command.getPlayerUnit();

        // If I'm next to an opponent that can't beat me and that I can't beat, then stay.
        Collection<Position> neighbors = playerUnit.getPosition().getNeighbors();
        for (Position neighbor : neighbors) {
            Optional<OpponentUnit> optOpponentUnit = game.getOpponentUnitAt(neighbor);
            if (optOpponentUnit.isPresent()) {
                OpponentUnit opponentUnit = optOpponentUnit.get();
                if (isTie(playerUnit, opponentUnit)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isTie(PlayerUnit playerUnit, OpponentUnit opponentUnit) {
        return !playerUnit.canBeat(opponentUnit) && !opponentUnit.canBeat(playerUnit);
    }
}
