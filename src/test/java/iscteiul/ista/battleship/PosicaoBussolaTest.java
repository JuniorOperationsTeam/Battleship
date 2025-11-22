package iscteiul.ista.battleship;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PosicaoBussolaTest {

    @Nested
    class PositionTests {
        @Test
        void positionBasicsAndEqualityAndHashcode() {
            Position p1 = new Position(5, 7);
            Position p2 = new Position(5, 7);
            Position p3 = new Position(6, 7);

            assertEquals(5, p1.getRow());
            assertEquals(7, p1.getColumn());

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
            assertNotEquals(p1, p3);

            assertFalse(p1.isHit());
            assertFalse(p1.isOccupied());

            p1.occupy();
            assertTrue(p1.isOccupied());

            p1.shoot();
            assertTrue(p1.isHit());

            // usage in HashSet
            Set<Position> set = new HashSet<>();
            set.add(p2);
            assertTrue(set.contains(new Position(5,7)));
        }
    }

    @Nested
    class CompassTests {
        @Test
        void compassValuesAndConversions() {
            assertEquals(Compass.NORTH, Compass.charToCompass('n'));
            assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
            assertEquals(Compass.EAST, Compass.charToCompass('e'));
            assertEquals(Compass.WEST, Compass.charToCompass('o'));

            assertEquals('n', Compass.NORTH.getDirection());
            assertEquals("n", Compass.NORTH.toString());

            // unknown char returns UNKNOWN
            assertEquals(Compass.UNKNOWN, Compass.charToCompass('x'));
        }
    }
}
