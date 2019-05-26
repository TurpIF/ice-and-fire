package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.guard.NoSuicideTrainingGard;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class NoSuicideTrainingGard_UT {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private TrainCommand command;

    @Mock
    private GameCell cell;

    private NoSuicideTrainingGard criteria;

    @Before
    public void setUp() throws Exception {
        when(command.getCell()).thenReturn(cell);

        criteria = new NoSuicideTrainingGard(gameRepository);
    }

    @Test
    public void isUseless_GivenOpponentLevel2InFrontAndLevel1_ReturnsTrue() throws Exception {
        OpponentUnit opponentUnit = new OpponentUnit(2);

        givenTrainedUnit(1);
        when(cell.isInOpponentTerritory()).thenReturn(false);
        when(cell.getPosition()).thenReturn(new Position(4, 1));
        when(gameRepository.getOpponentUnitAt(new Position(4, 2))).thenReturn(Optional.of(opponentUnit));

        boolean useless = criteria.isUseless(command);

        assertThat(useless).isTrue();
    }

    private void givenTrainedUnit(int level) {
        TrainedUnit trainedUnit = new TrainedUnit(level);
        when(command.getTrainedUnit()).thenReturn(trainedUnit);
    }

}