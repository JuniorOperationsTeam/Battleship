package iscteiul.ista.battleship;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BarcoTest {

    @Nested
    class CaravelBehavior {
        @Test
        void caravelOccupiesCorrectPositionsAndCanBeSunkAndHandlesRepeatedShots() {
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

            // top/left/right/bottom helpers
            assertEquals(2, caravel.getTopMostPos());
            assertEquals(3, caravel.getBottomMostPos());
            assertEquals(3, caravel.getLeftMostPos());
            assertEquals(3, caravel.getRightMostPos());

            // occupies checks using equal Position instances
            assertTrue(caravel.occupies(new Position(2, 3)));
            assertTrue(caravel.occupies(new Position(3, 3)));
            assertFalse(caravel.occupies(new Position(4, 3)));

            // Initially still floating
            assertTrue(caravel.stillFloating());

            // Shoot one position -> still floating
            caravel.shoot(new Position(2, 3));
            assertTrue(p0.isHit());
            assertTrue(caravel.stillFloating());

            // shooting the same cell twice should not change correctness
            caravel.shoot(new Position(2, 3));
            assertTrue(p0.isHit());
            assertTrue(caravel.stillFloating());

            // Shoot the other position -> should not be floating
            caravel.shoot(new Position(3, 3));
            assertTrue(p1.isHit());
            assertFalse(caravel.stillFloating());

            // tooCloseTo - a barge at (4,3) is adjacent to (3,3)
            Barge nearby = new Barge(Compass.SOUTH, new Position(4, 3));
            assertTrue(caravel.tooCloseTo(nearby));

            // and not too close to a distant barge
            Barge far = new Barge(Compass.EAST, new Position(10, 10));
            assertFalse(caravel.tooCloseTo(far));
        }
    }

    @Nested
    class ShipFactoryAndKinds {
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
}
