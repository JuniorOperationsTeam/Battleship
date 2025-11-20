package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IFleetTest {

    // Stubs simples (podes reaproveitar a mesma lógica do FleetTest)

    static class TestPosition implements IPosition {
        private final int row;
        private final int col;

        TestPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getColumn() {
            return col;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof IPosition)) return false;
            IPosition o = (IPosition) other;
            return row == o.getRow() && col == o.getColumn();
        }

        @Override
        public boolean isAdjacentTo(IPosition other) {
            int dr = Math.abs(this.row - other.getRow());
            int dc = Math.abs(this.col - other.getColumn());
            return (dr <= 1 && dc <= 1) && !(dr == 0 && dc == 0);
        }

        @Override
        public void occupy() { }

        @Override
        public void shoot() { }

        @Override
        public boolean isOccupied() { return false; }

        @Override
        public boolean isHit() { return false; }
    }

    static class TestShip implements IShip {
        private final String category;
        private final List<IPosition> positions;
        private final int top;
        private final int bottom;
        private final int left;
        private final int right;
        private boolean floating = true;

        TestShip(String category, List<IPosition> positions) {
            this.category = category;
            this.positions = positions;

            int minRow = Integer.MAX_VALUE;
            int maxRow = Integer.MIN_VALUE;
            int minCol = Integer.MAX_VALUE;
            int maxCol = Integer.MIN_VALUE;

            for (IPosition p : positions) {
                minRow = Math.min(minRow, p.getRow());
                maxRow = Math.max(maxRow, p.getRow());
                minCol = Math.min(minCol, p.getColumn());
                maxCol = Math.max(maxCol, p.getColumn());
            }

            this.top = minRow;
            this.bottom = maxRow;
            this.left = minCol;
            this.right = maxCol;
        }

        void sink() { floating = false; }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public Integer getSize() {
            return positions.size();
        }

        @Override
        public List<IPosition> getPositions() {
            return positions;
        }

        @Override
        public IPosition getPosition() {
            return positions.get(0);
        }

        @Override
        public Compass getBearing() {
            return Compass.EAST;
        }

        @Override
        public boolean stillFloating() {
            return floating;
        }

        @Override
        public int getTopMostPos() {
            return top;
        }

        @Override
        public int getBottomMostPos() {
            return bottom;
        }

        @Override
        public int getLeftMostPos() {
            return left;
        }

        @Override
        public int getRightMostPos() {
            return right;
        }

        @Override
        public boolean occupies(IPosition pos) {
            return positions.contains(pos);
        }

        @Override
        public boolean tooCloseTo(IShip other) {
            return false;
        }

        @Override
        public boolean tooCloseTo(IPosition pos) {
            return positions.contains(pos);
        }

        @Override
        public void shoot(IPosition pos) { }
    }

    private static TestShip createSingleCellShip(String category, int row, int col) {
        List<IPosition> pos = new ArrayList<>();
        pos.add(new TestPosition(row, col));
        return new TestShip(category, pos);
    }

    @Test
    @DisplayName("getShips retorna lista inicialmente vazia e depois com navios (IFleet)")
    void getShips() {
        IFleet fleet = new Fleet();
        assertTrue(fleet.getShips().isEmpty());

        IShip s = createSingleCellShip("Nau", 0, 0);
        assertTrue(fleet.addShip(s));
        assertEquals(1, fleet.getShips().size());
    }

    @Test
    @DisplayName("addShip adiciona vários navios válidos até ao limite da implementação")
    void addShip() {
        IFleet fleet = new Fleet();

        // A implementação atual permite até FLEET_SIZE + 1 navios
        int maxShips = IFleet.FLEET_SIZE + 1;

        for (int i = 0; i < maxShips; i++) {
            // Garantimos que (row, col) fica sempre dentro do tabuleiro 10x10
            int row = i / IFleet.BOARD_SIZE;   // 0,0,0,...,1,1,...
            int col = i % IFleet.BOARD_SIZE;   // 0..9, 0..9, ...

            assertTrue(
                    fleet.addShip(createSingleCellShip("Nau" + i, row, col)),
                    "O navio " + i + " deveria ter sido adicionado com sucesso"
            );
        }

        // Agora já está no limite da implementação, mais um deve falhar
        assertFalse(
                fleet.addShip(createSingleCellShip("Extra", 9, 9)),
                "Depois de " + maxShips + " navios, não deveria aceitar mais"
        );
    }

    @Test
    @DisplayName("getShipsLike filtra corretamente por categoria (IFleet)")
    void getShipsLike() {
        IFleet fleet = new Fleet();

        fleet.addShip(createSingleCellShip("Nau", 0, 0));
        fleet.addShip(createSingleCellShip("Nau", 0, 1));
        fleet.addShip(createSingleCellShip("Fragata", 1, 1));

        assertEquals(2, fleet.getShipsLike("Nau").size());
        assertEquals(1, fleet.getShipsLike("Fragata").size());
        assertEquals(0, fleet.getShipsLike("Barca").size());
    }

    @Test
    @DisplayName("getFloatingShips devolve apenas navios a flutuar (IFleet)")
    void getFloatingShips() {
        IFleet fleet = new Fleet();

        TestShip s1 = createSingleCellShip("Nau", 0, 0);
        TestShip s2 = createSingleCellShip("Fragata", 1, 1);
        s2.sink();

        fleet.addShip(s1);
        fleet.addShip(s2);

        List<IShip> floating = fleet.getFloatingShips();
        assertEquals(1, floating.size());
        assertTrue(floating.contains(s1));
        assertFalse(floating.contains(s2));
    }

    @Test
    @DisplayName("shipAt devolve navio correto ou null (IFleet)")
    void shipAt() {
        IFleet fleet = new Fleet();

        IShip s1 = createSingleCellShip("Nau", 0, 0);
        IShip s2 = createSingleCellShip("Fragata", 2, 2);

        fleet.addShip(s1);
        fleet.addShip(s2);

        assertEquals(s1, fleet.shipAt(new TestPosition(0, 0)));
        assertEquals(s2, fleet.shipAt(new TestPosition(2, 2)));
        assertNull(fleet.shipAt(new TestPosition(5, 5)));
    }

    @Test
    @DisplayName("printStatus não lança exceção (IFleet)")
    void printStatus() {
        IFleet fleet = new Fleet();
        fleet.addShip(createSingleCellShip("Nau", 0, 0));

        assertDoesNotThrow(fleet::printStatus);
    }
}
