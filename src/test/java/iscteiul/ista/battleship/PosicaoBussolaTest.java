package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PosicaoBussolaTest {

    @Test
    void positionBasicsAndEquality() {
        Position p1 = new Position(5, 7);
        Position p2 = new Position(5, 7);
        Position p3 = new Position(6, 7);

        assertEquals(5, p1.getRow());
        assertEquals(7, p1.getColumn());

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);

        assertFalse(p1.isHit());
        assertFalse(p1.isOccupied());

        p1.occupy();
        assertTrue(p1.isOccupied());

        p1.shoot();
        assertTrue(p1.isHit());
    }

    @Test
    void compassValuesAndConversions() {
        assertEquals(Compass.NORTH, Compass.charToCompass('n'));
        assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
        assertEquals(Compass.EAST, Compass.charToCompass('e'));
        assertEquals(Compass.WEST, Compass.charToCompass('o'));

        assertEquals('n', Compass.NORTH.getDirection());
        assertEquals("n", Compass.NORTH.toString());
    }
}

