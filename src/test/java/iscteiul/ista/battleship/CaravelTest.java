package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - Caravela")
public class CaravelTest {

    @Nested
    @DisplayName("Comportamento de posição e tiros")
    class PositionAndShooting {

        @Test
        @DisplayName("Caravela ocupa posições corretas, regista hits e afunda após todos os impactos")
        void caravelOccupiesCorrectPositionsAndHandlesHits() {
            Position start = new Position(2, 3);
            Caravel caravel = new Caravel(Compass.NORTH, start);

            assertEquals(2, caravel.getSize());
            assertEquals(2, caravel.getPositions().size());

            IPosition p0 = caravel.getPositions().get(0);
            IPosition p1 = caravel.getPositions().get(1);

            // posições calculadas
            assertEquals(new Position(2, 3), p0);
            assertEquals(new Position(3, 3), p1);

            // hits repetidos não quebram nada
            assertTrue(caravel.stillFloating());
            caravel.shoot(new Position(2, 3));
            caravel.shoot(new Position(2, 3));
            assertTrue(p0.isHit());
            assertTrue(caravel.stillFloating());

            caravel.shoot(new Position(3, 3));
            assertTrue(p1.isHit());
            assertFalse(caravel.stillFloating());
        }

        @Test
        @DisplayName("Caravela não afunda se levar apenas um hit")
        void caravelDoesNotSinkAfterOneHit() {
            Caravel caravel = new Caravel(Compass.NORTH, new Position(0, 0));
            caravel.shoot(new Position(0, 0));
            assertTrue(caravel.stillFloating());
        }

        @Test
        @DisplayName("Métodos top/bottom/left/right retornam valores corretos")
        void topBottomLeftRightAreCorrect() {
            Caravel caravel = new Caravel(Compass.NORTH, new Position(5, 7));

            assertEquals(5, caravel.getTopMostPos());
            assertEquals(6, caravel.getBottomMostPos());
            assertEquals(7, caravel.getLeftMostPos());
            assertEquals(7, caravel.getRightMostPos());
        }
    }

    @Nested
    @DisplayName("Fábrica de navios")
    class FactoryTests {

        @Test
        @DisplayName("Ship.buildShip devolve Caravela quando pedida")
        void buildShipFactoryProducesCaravel() {
            Ship s1 = Ship.buildShip("caravela", Compass.EAST, new Position(0, 0));
            assertNotNull(s1);
            assertEquals("Caravela", s1.getCategory());
            assertInstanceOf(Caravel.class, s1);
        }

        @Test
        @DisplayName("Ship.buildShip retorna null ou lança exceção para tipo inválido")
        void buildShipInvalidType() {
            assertNull(Ship.buildShip("navio_inexistente", Compass.WEST, new Position(1, 1)));
        }
    }

    @Nested
    @DisplayName("Orientações EAST/WEST e tiros fora")
    class OrientationAndNegativeShots {

        @Test
        @DisplayName("EAST orientation ocupa colunas consecutivas (x, y) e (x, y+1)")
        void eastOrientationOccupiesColumns() {
            Caravel c = new Caravel(Compass.EAST, new Position(1, 2));
            assertEquals(new Position(1, 2), c.getPositions().get(0));
            assertEquals(new Position(1, 3), c.getPositions().get(1));
        }

        @Test
        @DisplayName("WEST orientation ocupa colunas consecutivas (x, y) e (x, y+1)")
        void westOrientationOccupiesColumns() {
            Caravel c = new Caravel(Compass.WEST, new Position(0, 5));
            assertEquals(new Position(0, 5), c.getPositions().get(0));
            assertEquals(new Position(0, 6), c.getPositions().get(1));
        }

        @Test
        @DisplayName("Atirar fora das posições da Caravela não marca hits")
        void shootingOutsideDoesNotMarkHits() {
            Caravel c = new Caravel(Compass.NORTH, new Position(4, 4));
            c.shoot(new Position(0, 0)); // tiro fora

            for (IPosition p : c.getPositions()) {
                assertFalse(p.isHit());
            }
            assertTrue(c.stillFloating());
        }
    }

    @Nested
    @DisplayName("Validações e exceções do construtor")
    class ExceptionsAndValidations {

        @Test
        @DisplayName("Construtor com bearing null lança AssertionError")
        void constructorWithNullBearingThrows() {
            assertThrows(AssertionError.class, () -> new Caravel(null, new Position(0, 0)));
        }

        @Test
        @DisplayName("Construtor com posição null lança AssertionError")
        void constructorWithNullPositionThrows() {
            assertThrows(AssertionError.class, () -> new Caravel(Compass.NORTH, null));
        }

        @Test
        @DisplayName("Construtor com valores perto dos limites do tabuleiro não falha")
        void constructorNearEdgesDoesNotThrow() {
            assertDoesNotThrow(() -> new Caravel(Compass.EAST, new Position(99, 99)));
        }
    }
}
