package fr.pturpin.hackathon.iceandfire.cell;

import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class Position_UT {

    @Test
    public void new_GivenAllowedCoordinates_ReturnsNewPosition() {
        assertThatCode(() -> new Position(11, 11)).doesNotThrowAnyException();
    }

    @Test
    public void new_GivenAllowedCoordinates_ReturnsNewPosition2() {
        assertThatCode(() -> new Position(0, 0)).doesNotThrowAnyException();
    }

    @Test
    public void new_GivenForbiddenCoordinates_ThrowsIllegalArgument() {
        assertThatCode(() -> new Position(12, 11)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void new_GivenForbiddenCoordinates_ThrowsIllegalArgument2() {
        assertThatCode(() -> new Position(10, 12)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void new_GivenForbiddenCoordinates_ThrowsIllegalArgument3() {
        assertThatCode(() -> new Position(-1, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void new_GivenForbiddenCoordinates_ThrowsIllegalArgument4() {
        assertThatCode(() -> new Position(0, -1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void distanceTo_GivenSamePosition_Returns0() throws Exception {
        Position position = new Position(0, 0);

        int distance = position.distanceTo(position);

        assertThat(distance).isEqualTo(0);
    }

    @Test
    public void distanceTo_GivenOtherPosition_ReturnsL1Distance() throws Exception {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(4, 5);

        int distance1 = position2.distanceTo(position1);
        int distance2 = position1.distanceTo(position2);

        assertThat(distance1).isEqualTo(distance2).isEqualTo(9);
    }

    @Test
    public void getNeighbors_GivenCenteredPosition_Returns4PositionsNextToGivenOne() throws Exception {
        Position position = new Position(5, 5);

        Collection<Position> neighbors = position.getNeighbors();

        assertThat(neighbors).containsExactlyInAnyOrder(
                new Position(4, 5),
                new Position(5, 4),
                new Position(6, 5),
                new Position(5, 6));
    }

    @Test
    public void getNeighbors_GivenPositionNearEdges_ReturnsOnlyValidNeighbors() throws Exception {
        Position position = new Position(0, 1);

        Collection<Position> neighbors = position.getNeighbors();

        assertThat(neighbors).containsExactlyInAnyOrder(
                new Position(0, 0),
                new Position(1, 1),
                new Position(0, 2));
    }

    @Test
    public void getNeighbors_GivenPositionNearEdges_ReturnsOnlyValidNeighbors2() throws Exception {
        Position position = new Position(0, 0);

        Collection<Position> neighbors = position.getNeighbors();

        assertThat(neighbors).containsExactlyInAnyOrder(
                new Position(1, 0),
                new Position(0, 1));
    }

    @Test
    public void getNeighbors_GivenPositionNearEdges_ReturnsOnlyValidNeighbors3() throws Exception {
        Position position = new Position(11, 11);

        Collection<Position> neighbors = position.getNeighbors();

        assertThat(neighbors).containsExactlyInAnyOrder(
                new Position(10, 11),
                new Position(11, 10));
    }

}