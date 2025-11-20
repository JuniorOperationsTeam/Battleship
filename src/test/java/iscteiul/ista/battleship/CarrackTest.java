package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste da entidade Carrack")
class CarrackTest {

    @Test
    @DisplayName("getSize deve retornar 3")
    void getSize() {
        Carrack carrack = new Carrack(Compass.NORTH, new Position(0, 0));
        assertEquals(3, carrack.getSize());
    }

    @Test
    @DisplayName("Posições para NORTH crescem nas linhas mantendo a coluna")
    void positionsForNorth() {
        Carrack carrack = new Carrack(Compass.NORTH, new Position(2, 5));
        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());
        assertEquals(2, positions.get(0).getRow());
        assertEquals(5, positions.get(0).getColumn());
        assertEquals(3, positions.get(1).getRow());
        assertEquals(5, positions.get(1).getColumn());
        assertEquals(4, positions.get(2).getRow());
        assertEquals(5, positions.get(2).getColumn());
    }

    @Test
    @DisplayName("Posições para SOUTH crescem nas linhas mantendo a coluna")
    void positionsForSouth() {
        Carrack carrack = new Carrack(Compass.SOUTH, new Position(1, 0));
        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());
        assertEquals(1, positions.get(0).getRow());
        assertEquals(0, positions.get(0).getColumn());
        assertEquals(2, positions.get(1).getRow());
        assertEquals(0, positions.get(1).getColumn());
        assertEquals(3, positions.get(2).getRow());
        assertEquals(0, positions.get(2).getColumn());
    }

    @Test
    @DisplayName("Posições para EAST crescem nas colunas mantendo a linha")
    void positionsForEast() {
        Carrack carrack = new Carrack(Compass.EAST, new Position(4, 2));
        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());
        assertEquals(4, positions.get(0).getRow());
        assertEquals(2, positions.get(0).getColumn());
        assertEquals(4, positions.get(1).getRow());
        assertEquals(3, positions.get(1).getColumn());
        assertEquals(4, positions.get(2).getRow());
        assertEquals(4, positions.get(2).getColumn());
    }

    @Test
    @DisplayName("Posições para WEST crescem nas colunas mantendo a linha")
    void positionsForWest() {
        Carrack carrack = new Carrack(Compass.WEST, new Position(0, 1));
        List<IPosition> positions = carrack.getPositions();
        assertEquals(3, positions.size());
        assertEquals(0, positions.get(0).getRow());
        assertEquals(1, positions.get(0).getColumn());
        assertEquals(0, positions.get(1).getRow());
        assertEquals(2, positions.get(1).getColumn());
        assertEquals(0, positions.get(2).getRow());
        assertEquals(3, positions.get(2).getColumn());
    }
}