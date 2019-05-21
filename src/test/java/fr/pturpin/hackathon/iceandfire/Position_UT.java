package fr.pturpin.hackathon.iceandfire;

import org.junit.Test;

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

}