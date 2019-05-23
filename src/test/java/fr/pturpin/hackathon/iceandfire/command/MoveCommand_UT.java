package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class MoveCommand_UT {

    @Mock
    private PlayerUnit playerUnit;

    @Mock
    private GameCell cell;

    private MoveCommand command;

    @Before
    public void setUp() throws Exception {
        command = new MoveCommand(playerUnit, cell);
    }

    @Test
    public void getFormattedCommand_GivenIdAndPosition_ReturnsCommand() throws Exception {
        givenPlayerId(1);
        givenCellPosition(2, 3);

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("MOVE 1 2 3");
    }

    @Test
    public void getFormattedCommand_GivenIdAndPosition_ReturnsCommand2() throws Exception {
        givenPlayerId(2);
        givenCellPosition(3, 4);

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("MOVE 2 3 4");
    }

    @Test
    public void getCell_GivenCell_ReturnsIt() throws Exception {
        GameCell commandCell = command.getCell();

        assertThat(commandCell).isEqualTo(cell);
    }

    @Test
    public void isValid_GivenUnitThatCantMove_ReturnsFalse() throws Exception {
        givenValidMove();

        when(playerUnit.canMove()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void isValid_GivenUnitAtTheSamePositionThanTheCell_ReturnsFalse() throws Exception {
        givenValidMove();

        Position position = new Position(1, 1);
        when(playerUnit.getPosition()).thenReturn(position);
        when(cell.getPosition()).thenReturn(position);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    // FIXME It is possible to give distance greater than one but for the moment, we limit this feature.
    @Test
    public void isValid_GivenDistanceGreaterThanOne_ReturnsFalse() throws Exception {
        givenValidMove();

        when(playerUnit.getPosition()).thenReturn(new Position(1, 1));
        when(cell.getPosition()).thenReturn(new Position(1, 3));

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void isValid_GivenCellContainingAlly_ReturnsFalse() throws Exception {
        givenValidMove();

        when(cell.containsAlly()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void isValid_GivenCellContainingUnbeatableOpponent_ReturnsFalse() throws Exception {
        givenValidMove();

        when(cell.containsBeatableOpponentFor(playerUnit)).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void isValid_GivenWallCell_ReturnsFalse() throws Exception {
        givenValidMove();

        when(cell.isWall()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void isValid_GivenCellProtectedByOpponentTowerAndUnitThatCantReachTower_ReturnsFalse() throws Exception {
        givenValidMove();

        when(cell.isProtectedByOpponentTower()).thenReturn(true);
        when(playerUnit.canReachTower()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void isValid_GivenCellProtectedByOpponentTowerAndUnitThatCanReachTower_ReturnsTrue() throws Exception {
        givenValidMove();

        when(cell.isProtectedByOpponentTower()).thenReturn(true);
        when(playerUnit.canReachTower()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(true);
    }

    @Test
    public void isValid_GivenCellUnprotectedByOpponentTower_ReturnsTrue() throws Exception {
        givenValidMove();

        when(cell.isProtectedByOpponentTower()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(true);
    }

    @Test
    public void isValid_GivenValidCondition_ReturnsTrue() throws Exception {
        givenValidMove();

        boolean valid = command.isValid();

        assertThat(valid).isEqualTo(true);
    }

    private void givenValidMove() {
        when(cell.getPosition()).thenReturn(new Position(2, 1));
        when(playerUnit.getPosition()).thenReturn(new Position(1, 1));
        when(playerUnit.canMove()).thenReturn(true);
        when(cell.containsAlly()).thenReturn(false);
        when(cell.containsBeatableOpponentFor(playerUnit)).thenReturn(true);
        when(cell.isWall()).thenReturn(false);
        when(cell.isProtectedByOpponentTower()).thenReturn(false);
        when(playerUnit.canReachTower()).thenReturn(true);
    }

    private void givenCellPosition(int x, int y) {
        when(cell.getPosition()).thenReturn(new Position(x, y));
    }

    private void givenPlayerId(int unitId) {
        when(playerUnit.getId()).thenReturn(unitId);
    }

}