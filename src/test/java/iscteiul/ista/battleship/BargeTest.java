// src/test/java/iscteiul/ista/battleship/BargeTest.java
package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste da entidade Barge")
class BargeTest {

    @Test
    @DisplayName("getPositions deve ter tamanho igual a getSize")
    void positionsMatchSize() {
        Barge barge = new Barge(Compass.NORTH, new Position(0, 0));
        List<IPosition> positions = barge.getPositions();
        assertEquals(barge.getSize(), positions.size());
        assertTrue(barge.getSize() > 0);
    }

    @Test
    @DisplayName("Posições para NORTH crescem nas linhas mantendo a coluna")
    void positionsForNorth() {
        Position start = new Position(2, 5);
        Barge barge = new Barge(Compass.NORTH, start);
        List<IPosition> positions = barge.getPositions();
        int size = barge.getSize();
        assertEquals(size, positions.size());
        for (int i = 0; i < size; i++) {
            assertEquals(start.getRow() + i, positions.get(i).getRow());
            assertEquals(start.getColumn(), positions.get(i).getColumn());
        }
    }

    @Test
    @DisplayName("Posições para SOUTH crescem nas linhas mantendo a coluna")
    void positionsForSouth() {
        Position start = new Position(1, 0);
        Barge barge = new Barge(Compass.SOUTH, start);
        List<IPosition> positions = barge.getPositions();
        int size = barge.getSize();
        assertEquals(size, positions.size());
        for (int i = 0; i < size; i++) {
            assertEquals(start.getRow() + i, positions.get(i).getRow());
            assertEquals(start.getColumn(), positions.get(i).getColumn());
        }
    }

    @Test
    @DisplayName("Posições para EAST crescem nas colunas mantendo a linha")
    void positionsForEast() {
        Position start = new Position(4, 2);
        Barge barge = new Barge(Compass.EAST, start);
        List<IPosition> positions = barge.getPositions();
        int size = barge.getSize();
        assertEquals(size, positions.size());
        for (int i = 0; i < size; i++) {
            assertEquals(start.getRow(), positions.get(i).getRow());
            assertEquals(start.getColumn() + i, positions.get(i).getColumn());
        }
    }

    @Test
    @DisplayName("Posições para WEST crescem nas colunas mantendo a linha")
    void positionsForWest() {
        Position start = new Position(0, 1);
        Barge barge = new Barge(Compass.WEST, start);
        List<IPosition> positions = barge.getPositions();
        int size = barge.getSize();
        assertEquals(size, positions.size());
        for (int i = 0; i < size; i++) {
            assertEquals(start.getRow(), positions.get(i).getRow());
            assertEquals(start.getColumn() + i, positions.get(i).getColumn());
        }
    }
}