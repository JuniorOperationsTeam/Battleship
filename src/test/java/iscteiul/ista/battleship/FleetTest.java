package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

    // ----------------- STUBS AUXILIARES -----------------

    // Implementação simples de IPosition só para testes
    static class TestPosition implements IPosition {
        private final int row;
        private final int col;
        private boolean occupied = false;
        private boolean hit = false;

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
        public void occupy() {
            occupied = true;
        }

        @Override
        public void shoot() {
            hit = true;
        }

        @Override
        public boolean isOccupied() {
            return occupied;
        }

        @Override
        public boolean isHit() {
            return hit;
        }
    }

    // Implementação simples de IShip só para testes
    static class TestShip implements IShip {
        private final String category;
        private final List<IPosition> positions;
        private boolean floating = true;
        private final int top;
        private final int bottom;
        private final int left;
        private final int right;
        private boolean tooCloseFlag = false;

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

        void setTooCloseFlag(boolean v) {
            this.tooCloseFlag = v;
        }

        void sink() {
            this.floating = false;
        }

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
            // Não interessa para estes testes
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
            return tooCloseFlag;
        }

        @Override
        public boolean tooCloseTo(IPosition pos) {
            return positions.contains(pos);
        }

        @Override
        public void shoot(IPosition pos) {
            if (occupies(pos)) {
                pos.shoot();
            }
        }

        @Override
        public String toString() {
            // Para podermos testar a impressão com o nome da categoria
            return "Ship(" + category + ")";
        }
    }

    private static TestShip createSingleCellShip(String category, int row, int col) {
        List<IPosition> pos = new ArrayList<>();
        pos.add(new TestPosition(row, col));
        for (IPosition p : pos) {
            p.occupy();
        }
        return new TestShip(category, pos);
    }

    // ----------------- TESTES -----------------

    @Test
    @DisplayName("printShips imprime os navios sem lançar exceções")
    void printShips() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));

        List<IShip> list = new ArrayList<>();
        list.add(createSingleCellShip("Nau", 0, 0));

        assertDoesNotThrow(() -> Fleet.printShips(list));

        String output = baos.toString();
        assertTrue(output.contains("Nau")); // porque o toString devolve Ship(Nau)

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("getShips devolve lista inicialmente vazia e depois com navios")
    void getShips() {
        Fleet fleet = new Fleet();
        assertNotNull(fleet.getShips());
        assertTrue(fleet.getShips().isEmpty());

        IShip s = createSingleCellShip("Nau", 0, 0);
        assertTrue(fleet.addShip(s));
        assertEquals(1, fleet.getShips().size());
        assertSame(s, fleet.getShips().get(0));
    }

    @Test
    @DisplayName("addShip adiciona navio válido e recusa navio fora do tabuleiro")
    void addShip() {
        Fleet fleet = new Fleet();

        // dentro do tabuleiro
        IShip inside = createSingleCellShip("Fragata", 5, 5);
        assertTrue(fleet.addShip(inside));
        assertEquals(1, fleet.getShips().size());

        // fora do tabuleiro (coluna negativa)
        List<IPosition> pos = new ArrayList<>();
        pos.add(new TestPosition(0, -1));
        IShip outside = new TestShip("Nau", pos);
        assertFalse(fleet.addShip(outside));
        assertEquals(1, fleet.getShips().size());
    }

    @Test
    @DisplayName("getShipsLike filtra navios pela categoria")
    void getShipsLike() {
        Fleet fleet = new Fleet();
        IShip s1 = createSingleCellShip("Nau", 0, 0);
        IShip s2 = createSingleCellShip("Nau", 1, 1);
        IShip s3 = createSingleCellShip("Fragata", 2, 2);

        fleet.addShip(s1);
        fleet.addShip(s2);
        fleet.addShip(s3);

        List<IShip> naus = fleet.getShipsLike("Nau");
        assertEquals(2, naus.size());
        assertTrue(naus.contains(s1));
        assertTrue(naus.contains(s2));

        List<IShip> fragatas = fleet.getShipsLike("Fragata");
        assertEquals(1, fragatas.size());
        assertTrue(fragatas.contains(s3));

        List<IShip> barcas = fleet.getShipsLike("Barca");
        assertTrue(barcas.isEmpty());
    }

    @Test
    @DisplayName("getFloatingShips devolve apenas os navios ainda a flutuar")
    void getFloatingShips() {
        Fleet fleet = new Fleet();

        TestShip s1 = createSingleCellShip("Nau", 0, 0);
        TestShip s2 = createSingleCellShip("Fragata", 1, 1);
        TestShip s3 = createSingleCellShip("Barca", 2, 2);

        fleet.addShip(s1);
        fleet.addShip(s2);
        fleet.addShip(s3);

        // Afundar um dos navios
        s2.sink();

        List<IShip> floating = fleet.getFloatingShips();
        assertEquals(2, floating.size());
        assertTrue(floating.contains(s1));
        assertTrue(floating.contains(s3));
        assertFalse(floating.contains(s2));
    }

    @Test
    @DisplayName("shipAt devolve o navio na posição ou null se não houver")
    void shipAt() {
        Fleet fleet = new Fleet();

        TestShip s1 = createSingleCellShip("Nau", 0, 0);
        TestShip s2 = createSingleCellShip("Fragata", 3, 3);

        fleet.addShip(s1);
        fleet.addShip(s2);

        IPosition pos1 = new TestPosition(0, 0);
        IPosition pos2 = new TestPosition(3, 3);
        IPosition posEmpty = new TestPosition(5, 5);

        assertEquals(s1, fleet.shipAt(pos1));
        assertEquals(s2, fleet.shipAt(pos2));
        assertNull(fleet.shipAt(posEmpty));
    }

    @Test
    @DisplayName("printStatus imprime o estado da frota sem lançar exceções")
    void printStatus() {
        Fleet fleet = new Fleet();
        fleet.addShip(createSingleCellShip("Nau", 0, 0));
        fleet.addShip(createSingleCellShip("Fragata", 1, 1));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));

        assertDoesNotThrow(fleet::printStatus);

        String output = baos.toString();
        assertTrue(output.contains("Nau"));
        assertTrue(output.contains("Fragata"));

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("printShipsByCategory imprime apenas navios da categoria indicada")
    void printShipsByCategory() {
        Fleet fleet = new Fleet();
        fleet.addShip(createSingleCellShip("Nau", 0, 0));
        fleet.addShip(createSingleCellShip("Fragata", 1, 1));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));

        fleet.printShipsByCategory("Nau");

        String output = baos.toString();
        assertTrue(output.contains("Nau"));
        assertFalse(output.contains("Fragata"));

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("printFloatingShips imprime apenas navios a flutuar")
    void printFloatingShips() {
        Fleet fleet = new Fleet();
        TestShip s1 = createSingleCellShip("Nau", 0, 0);
        TestShip s2 = createSingleCellShip("Fragata", 1, 1);
        s2.sink();

        fleet.addShip(s1);
        fleet.addShip(s2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));

        fleet.printFloatingShips();

        String output = baos.toString();
        assertTrue(output.contains("Nau"));
        assertFalse(output.contains("Fragata"));

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("printAllShips imprime todos os navios da frota")
    void printAllShips() {
        Fleet fleet = new Fleet();
        fleet.addShip(createSingleCellShip("Nau", 0, 0));
        fleet.addShip(createSingleCellShip("Fragata", 1, 1));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));

        // método package-private, mas estamos no mesmo package
        fleet.printAllShips();

        String output = baos.toString();
        assertTrue(output.contains("Nau"));
        assertTrue(output.contains("Fragata"));

        System.setOut(originalOut);
    }
}
