package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BarcoTest {

    @Test
    void caravelOccupiesCorrectPositionsAndCanBeSunk() {
        Position start = new Position(2, 3);
        Caravel caravel = new Caravel(Compass.NORTH, start);

        // Caravel size is 2 and when bearing NORTH occupies rows 2 and 3 in same column
        assertEquals(2, caravel.getSize());
        assertEquals(2, caravel.getPositions().size());

        IPosition p0 = caravel.getPositions().get(0);
        IPosition p1 = caravel.getPositions().get(1);

        assertEquals(2, p0.getRow());
        assertEquals(3, p0.getColumn());
        assertEquals(3, p1.getRow());
        assertEquals(3, p1.getColumn());

        // occupies checks
        assertTrue(caravel.occupies(new Position(2, 3)));
        assertTrue(caravel.occupies(new Position(3, 3)));
        assertFalse(caravel.occupies(new Position(4, 3)));

        // Initially still floating
        assertTrue(caravel.stillFloating());

        // Shoot one position -> still floating
        caravel.shoot(new Position(2, 3));
        assertTrue(p0.isHit());
        assertTrue(caravel.stillFloating());

        // Shoot the other position -> should not be floating
        caravel.shoot(new Position(3, 3));
        assertTrue(p1.isHit());
        assertFalse(caravel.stillFloating());
    }

    @Test
    void buildShipFactoryProducesDifferentKinds() {
        Ship s1 = Ship.buildShip("caravela", Compass.EAST, new Position(0, 0));
        assertNotNull(s1);
        assertEquals("Caravela", s1.getCategory());

        Ship s2 = Ship.buildShip("barca", Compass.EAST, new Position(0, 0));
        assertNotNull(s2);
        assertEquals("Barca", s2.getCategory());

        // Unknown kind should return null
        Ship s3 = Ship.buildShip("unknown", Compass.EAST, new Position(0, 0));
        assertNull(s3);
    }
}

