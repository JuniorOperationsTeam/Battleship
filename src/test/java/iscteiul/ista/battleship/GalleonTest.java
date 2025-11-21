package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GalleonTest {

    private Position p(int row, int col) {
        return new Position(row, col);
    }

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructionTests {

        @Test
        @DisplayName("Construção do Galleon com orientação válida produz 5 posições")
        void buildShip() {
            Galleon g = new Galleon(Compass.NORTH, p(1, 1));
            assertNotNull(g);
            assertEquals(Integer.valueOf(5), g.getSize());
            assertNotNull(g.getPositions());
            assertEquals(5, g.getPositions().size());
        }

        @Test
        @DisplayName("Construtor com bearing nulo lança AssertionError (comportamento atual)")
        void constructorNullBearing() {
            assertThrows(AssertionError.class, () -> new Galleon(null, p(1, 1)));
        }
    }

    @Nested
    @DisplayName("Testes de Getters Básicos")
    class GetterTests {

        @Test
        @DisplayName("getCategory retorna 'Galeao'")
        void getCategory() {
            Galleon g = new Galleon(Compass.WEST, p(2, 2));
            assertEquals("Galeao", g.getCategory());
        }

        @Test
        @DisplayName("getBearing retorna o rumo passado no construtor")
        void getBearing() {
            Galleon g = new Galleon(Compass.EAST, p(3, 3));
            assertEquals(Compass.EAST, g.getBearing());
        }

        @Test
        @DisplayName("getSize retorna 5")
        void getSize() {
            Galleon g = new Galleon(Compass.NORTH, p(5, 5));
            assertEquals(Integer.valueOf(5), g.getSize());
        }
    }

    @Nested
    @DisplayName("Testes de Posições por Orientação")
    class PositionOrientationTests {

        @Test
        @DisplayName("Posições para NORTH estão corretas")
        void getPositionsNorth() {
            Galleon g = new Galleon(Compass.NORTH, p(1, 1));
            List<IPosition> expected = Arrays.asList(
                    p(1, 1),
                    p(1, 2),
                    p(1, 3),
                    p(2, 2),
                    p(3, 2)
            );
            assertEquals(5, g.getPositions().size());
            assertTrue(g.getPositions().containsAll(expected));
            assertTrue(g.occupies(p(1, 2)));
            assertFalse(g.occupies(p(0, 0)));
        }

        @Test
        @DisplayName("Posições para SOUTH estão corretas")
        void getPositionsSouth() {
            Galleon g = new Galleon(Compass.SOUTH, p(1, 2));
            List<IPosition> expected = Arrays.asList(
                    p(1, 2),
                    p(2, 2),
                    p(3, 1),
                    p(3, 2),
                    p(3, 3)
            );
            assertEquals(5, g.getPositions().size());
            assertTrue(g.getPositions().containsAll(expected));
        }

        @Test
        @DisplayName("Posições para EAST estão corretas")
        void getPositionsEast() {
            Galleon g = new Galleon(Compass.EAST, p(2, 3));
            List<IPosition> expected = Arrays.asList(
                    p(2, 3),
                    p(3, 1),
                    p(3, 2),
                    p(3, 3),
                    p(4, 3)
            );
            assertEquals(5, g.getPositions().size());
            assertTrue(g.getPositions().containsAll(expected));
        }

        @Test
        @DisplayName("Posições para WEST estão corretas")
        void getPositionsWest() {
            Galleon g = new Galleon(Compass.WEST, p(2, 2));
            List<IPosition> expected = Arrays.asList(
                    p(2, 2),
                    p(3, 2),
                    p(3, 3),
                    p(3, 4),
                    p(4, 2)
            );
            assertEquals(5, g.getPositions().size());
            assertTrue(g.getPositions().containsAll(expected));
        }
    }
}
