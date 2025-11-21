package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrigateTest {

    private Position p(int row, int col) {
        return new Position(row, col);
    }

    @Test
    @DisplayName("Construção da Fragata com orientação válida produz 4 posições")
    void buildShip() {
        Frigate f = new Frigate(Compass.NORTH, p(1, 1));
        assertNotNull(f);
        assertEquals(Integer.valueOf(4), f.getSize());
        assertNotNull(f.getPositions());
        assertEquals(4, f.getPositions().size());
    }

    @Test
    @DisplayName("Construtor com bearing nulo lança exceção")
    void constructorNullBearing() {
        // A implementação atual lança AssertionError, não NullPointerException
        assertThrows(Throwable.class, () -> new Frigate(null, p(1, 1)));
    }

    @Test
    @DisplayName("getCategory retorna 'Fragata'")
    void getCategory() {
        Frigate f = new Frigate(Compass.WEST, p(2, 2));
        assertEquals("Fragata", f.getCategory());
    }

    @Test
    @DisplayName("getBearing retorna o rumo passado no construtor")
    void getBearing() {
        Frigate f = new Frigate(Compass.EAST, p(3, 3));
        assertEquals(Compass.EAST, f.getBearing());
    }

    @Test
    @DisplayName("Posições para NORTH estão corretas")
    void getPositionsNorth() {
        Frigate f = new Frigate(Compass.NORTH, p(1, 1));
        List<IPosition> expected = Arrays.asList(
                p(1, 1),
                p(2, 1),
                p(3, 1),
                p(4, 1)
        );
        assertEquals(4, f.getPositions().size());
        assertTrue(f.getPositions().containsAll(expected));
        assertTrue(f.occupies(p(2, 1)));
        assertFalse(f.occupies(p(0, 0)));
    }

    @Test
    @DisplayName("Posições para SOUTH estão corretas")
    void getPositionsSouth() {
        Frigate f = new Frigate(Compass.SOUTH, p(2, 2));
        List<IPosition> expected = Arrays.asList(
                p(2, 2),
                p(3, 2),
                p(4, 2),
                p(5, 2)
        );
        assertEquals(4, f.getPositions().size());
        assertTrue(f.getPositions().containsAll(expected));
    }

    @Test
    @DisplayName("Posições para EAST estão corretas")
    void getPositionsEast() {
        Frigate f = new Frigate(Compass.EAST, p(3, 3));
        List<IPosition> expected = Arrays.asList(
                p(3, 3),
                p(3, 4),
                p(3, 5),
                p(3, 6)
        );
        assertEquals(4, f.getPositions().size());
        assertTrue(f.getPositions().containsAll(expected));
    }

    @Test
    @DisplayName("Posições para WEST estão corretas")
    void getPositionsWest() {
        Frigate f = new Frigate(Compass.WEST, p(4, 4));
        List<IPosition> expected = Arrays.asList(
                p(4, 4),
                p(4, 5),
                p(4, 6),
                p(4, 7)
        );
        assertEquals(4, f.getPositions().size());
        assertTrue(f.getPositions().containsAll(expected));
    }

    @Test
    @DisplayName("getSize retorna 4")
    void getSize() {
        Frigate f = new Frigate(Compass.NORTH, p(5, 5));
        assertEquals(Integer.valueOf(4), f.getSize());
    }

    @Test
    @DisplayName("getTopMostPos e getBottomMostPos funcionam conforme posições")
    void getTopBottomMostPos() {
        Frigate f = new Frigate(Compass.NORTH, p(2, 3));

        Object top = f.getTopMostPos();
        Object bottom = f.getBottomMostPos();

        // aceita Position ou Integer (linha)
        if (top instanceof Position) {
            assertEquals(p(2, 3), top);
        } else if (top instanceof Integer) {
            assertEquals(2, ((Integer) top).intValue());
        } else {
            fail("getTopMostPos devolveu tipo inesperado: " + (top == null ? "null" : top.getClass()));
        }

        if (bottom instanceof Position) {
            assertEquals(p(5, 3), bottom);
        } else if (bottom instanceof Integer) {
            assertEquals(5, ((Integer) bottom).intValue());
        } else {
            fail("getBottomMostPos devolveu tipo inesperado: " + (bottom == null ? "null" : bottom.getClass()));
        }
    }

    @Test
    @DisplayName("getLeftMostPos e getRightMostPos funcionam conforme posições")
    void getLeftRightMostPos() {
        Frigate f = new Frigate(Compass.EAST, p(6, 7));

        Object left = f.getLeftMostPos();
        Object right = f.getRightMostPos();

        // aceita Position ou Integer (coluna)
        if (left instanceof Position) {
            assertEquals(p(6, 7), left);
        } else if (left instanceof Integer) {
            assertEquals(7, ((Integer) left).intValue());
        } else {
            fail("getLeftMostPos devolveu tipo inesperado: " + (left == null ? "null" : left.getClass()));
        }

        if (right instanceof Position) {
            assertEquals(p(6, 10), right);
        } else if (right instanceof Integer) {
            assertEquals(10, ((Integer) right).intValue());
        } else {
            fail("getRightMostPos devolveu tipo inesperado: " + (right == null ? "null" : right.getClass()));
        }
    }


    @Test
    @DisplayName("tooCloseTo retorna false quando distante e true quando perto")
    void tooCloseTo() {
        Frigate f = new Frigate(Compass.NORTH, p(2, 2));
        Frigate otherFar = new Frigate(Compass.NORTH, p(10, 10));
        Frigate otherNear = new Frigate(Compass.NORTH, p(5, 2));
        assertFalse(f.tooCloseTo(otherFar));
        assertTrue(f.tooCloseTo(otherNear));
    }

    @Test
    @DisplayName("toString contém o nome da fragata")
    void testToString() {
        Frigate f = new Frigate(Compass.SOUTH, p(1, 1));
        String s = f.toString();
        assertNotNull(s);
        assertTrue(s.contains("Fragata"));
    }
}
