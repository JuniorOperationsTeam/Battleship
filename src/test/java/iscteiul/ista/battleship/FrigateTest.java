// java
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
    @DisplayName("Construtor com bearing nulo lança NullPointerException")
    void constructorNullBearing() {
        assertThrows(NullPointerException.class, () -> new Frigate(null, p(1, 1)));
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
    @DisplayName("Posições para WEST estão corretas (implementação atual usa mesma lógica de colunas)")
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
        assertEquals(p(2, 3), f.getTopMostPos());
        assertEquals(p(5, 3), f.getBottomMostPos());
    }

    @Test
    @DisplayName("getLeftMostPos e getRightMostPos funcionam conforme posições")
    void getLeftRightMostPos() {
        Frigate f = new Frigate(Compass.EAST, p(6, 7));
        assertEquals(p(6, 7), f.getLeftMostPos());
        assertEquals(p(6, 10), f.getRightMostPos());
    }

    @Test
    @DisplayName("tooCloseTo retorna false quando distante e true quando perto")
    void tooCloseTo() {
        Frigate f = new Frigate(Compass.NORTH, p(2, 2));
        Frigate otherFar = new Frigate(Compass.NORTH, p(10, 10));
        Frigate otherNear = new Frigate(Compass.NORTH, p(5, 2)); // overlap/adjacent
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
