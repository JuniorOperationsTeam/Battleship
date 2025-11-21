package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - Barca/Barcaça")
public class BargeTest {

    @Nested
    @DisplayName("Comportamento geral e tiros")
    class BehaviorAndShooting {

        @Test
        @DisplayName("Barca ocupa posições, trata tiros repetidos e pode ser afundada")
        void bargeOccupiesAndHandlesShots() {
            Position start = new Position(5, 5);
            Barge barge = new Barge(Compass.EAST, start);

            // tem posições válidas
            assertNotNull(barge.getPositions());
            assertTrue(barge.getPositions().size() > 0);

            // ocupa a posição inicial
            assertTrue(barge.occupies(start));

            // inicialmente flutuando
            assertTrue(barge.stillFloating());

            // dispara em cada posição da barca e verifica estado final
            for (IPosition p : barge.getPositions()) {
                assertFalse(p.isHit());
                barge.shoot(new Position(p.getRow(), p.getColumn()));
                assertTrue(p.isHit());
            }

            // após atingir todas as posições não deve estar flutuando
            assertFalse(barge.stillFloating());
        }

        @Test
        @DisplayName("Tiros repetidos não alteram estado incorretamente")
        void repeatedShotsDoNotBreakState() {
            Position start = new Position(7, 7);
            Barge barge = new Barge(Compass.SOUTH, start);

            IPosition first = barge.getPositions().get(0);
            assertFalse(first.isHit());

            barge.shoot(new Position(first.getRow(), first.getColumn()));
            assertTrue(first.isHit());
            // repetir o tiro
            barge.shoot(new Position(first.getRow(), first.getColumn()));
            assertTrue(first.isHit());
        }
    }

    @Nested
    @DisplayName("Interacções com outras embarcações")
    class ProximityTests {

        @Test
        @DisplayName("Barca detecta proximidade a uma Caravela próxima")
        void tooCloseToDetectsNearbyCaravel() {
            Barge barge = new Barge(Compass.SOUTH, new Position(4, 3));
            Caravel caravel = new Caravel(Compass.NORTH, new Position(2, 3));
            // caravel ocupa (2,3) e (3,3), barge em (4,3) deve ficar perto de pelo menos uma
            assertTrue(caravel.tooCloseTo(barge));
            assertTrue(barge.tooCloseTo(caravel));
        }

        @Test
        @DisplayName("Barca não considera distante como próxima")
        void notTooCloseToDistantShip() {
            Barge barge = new Barge(Compass.EAST, new Position(10, 10));
            Caravel far = new Caravel(Compass.EAST, new Position(0, 0));
            assertFalse(barge.tooCloseTo(far));
        }
    }

    @Nested
    @DisplayName("Fábrica de navios")
    class FactoryTests {

        @Test
        @DisplayName("Ship.buildShip devolve Barca/Barcaça")
        void buildShipFactoryProducesBarge() {
            Ship s2 = Ship.buildShip("barca", Compass.EAST, new Position(0, 0));
            assertNotNull(s2);
            assertEquals("Barca", s2.getCategory());

            // unknown kind should return null
            Ship s3 = Ship.buildShip("unknown", Compass.EAST, new Position(0, 0));
            assertNull(s3);
        }
    }
}
