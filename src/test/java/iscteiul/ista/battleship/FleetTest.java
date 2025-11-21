package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

    // ----------------- STUBS AUXILIARES -----------------

    static class TestPosition implements IPosition {
        private final int row;
        private final int col;
        private boolean occupied = false;
        private boolean hit = false;

        TestPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override public int getRow() { return row; }
        @Override public int getColumn() { return col; }

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

        @Override public void occupy() { occupied = true; }
        @Override public void shoot() { hit = true; }
        @Override public boolean isOccupied() { return occupied; }
        @Override public boolean isHit() { return hit; }
    }

    static class TestShip implements IShip {
        private final String category;
        private final List<IPosition> positions;
        private boolean floating = true;

        private final int top, bottom, left, right;

        // flag para forçar tooCloseTo a devolver true/false
        private boolean tooCloseFlag = false;

        TestShip(String category, List<IPosition> positions) {
            this.category = category;
            this.positions = positions;

            int minRow = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE;
            int minCol = Integer.MAX_VALUE, maxCol = Integer.MIN_VALUE;

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

        void setTooCloseFlag(boolean v) { this.tooCloseFlag = v; }
        void sink() { this.floating = false; }

        @Override public String getCategory() { return category; }
        @Override public Integer getSize() { return positions.size(); }
        @Override public List<IPosition> getPositions() { return positions; }
        @Override public IPosition getPosition() { return positions.get(0); }
        @Override public Compass getBearing() { return Compass.EAST; }

        @Override public boolean stillFloating() { return floating; }

        @Override public int getTopMostPos() { return top; }
        @Override public int getBottomMostPos() { return bottom; }
        @Override public int getLeftMostPos() { return left; }
        @Override public int getRightMostPos() { return right; }

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
            if (occupies(pos)) pos.shoot();
        }

        @Override
        public String toString() {
            return "Ship(" + category + ")";
        }
    }

    private static TestShip createSingleCellShip(String category, int row, int col) {
        List<IPosition> pos = new ArrayList<>();
        TestPosition p = new TestPosition(row, col);
        p.occupy();
        pos.add(p);
        return new TestShip(category, pos);
    }

    // =====================================================
    //                    TESTES
    // =====================================================

    @Nested
    @DisplayName("getShips()")
    class GetShipsTests {

        @Test
        @DisplayName("Frota nova devolve lista vazia")
        void emptyFleet() {
            Fleet f = new Fleet();
            assertNotNull(f.getShips());
            assertTrue(f.getShips().isEmpty());
        }

        @Test
        @DisplayName("Após addShip, getShips contém o navio")
        void afterAdd() {
            Fleet f = new Fleet();
            IShip s = createSingleCellShip("Nau", 0, 0);
            assertTrue(f.addShip(s));
            assertEquals(1, f.getShips().size());
            assertSame(s, f.getShips().get(0));
        }
    }

    @Nested
    @DisplayName("addShip() - todos os ramos")
    class AddShipTests {

        @Test
        @DisplayName("Adiciona navio válido dentro do tabuleiro e sem colisão")
        void addValidShip() {
            Fleet f = new Fleet();
            IShip s = createSingleCellShip("Fragata", 5, 5);
            assertTrue(f.addShip(s));
        }

        @Test
        @DisplayName("Recusa navio se estiver fora do tabuleiro (left < 0)")
        void rejectLeftOutside() {
            Fleet f = new Fleet();
            IShip s = createSingleCellShip("Nau", 0, -1);
            assertFalse(f.addShip(s));
        }

        @Test
        @DisplayName("Recusa navio se estiver fora do tabuleiro (right > 9)")
        void rejectRightOutside() {
            Fleet f = new Fleet();
            IShip s = createSingleCellShip("Nau", 0, 10);
            assertFalse(f.addShip(s));
        }

        @Test
        @DisplayName("Recusa navio se estiver fora do tabuleiro (top < 0)")
        void rejectTopOutside() {
            Fleet f = new Fleet();
            IShip s = createSingleCellShip("Nau", -1, 0);
            assertFalse(f.addShip(s));
        }

        @Test
        @DisplayName("Recusa navio se estiver fora do tabuleiro (bottom > 9)")
        void rejectBottomOutside() {
            Fleet f = new Fleet();
            IShip s = createSingleCellShip("Nau", 10, 0);
            assertFalse(f.addShip(s));
        }

        @Test
        @DisplayName("Recusa navio se houver risco de colisão")
        void rejectCollisionRisk() {
            Fleet f = new Fleet();

            TestShip existing = createSingleCellShip("Nau", 0, 0);
            existing.setTooCloseFlag(true); // força colisionRisk a devolver true

            assertTrue(f.addShip(existing));

            IShip newShip = createSingleCellShip("Fragata", 3, 3);
            assertFalse(f.addShip(newShip));
        }

        @Test
        @DisplayName("Recusa navio quando frota excede o limite (size > FLEET_SIZE)")
        void rejectWhenFleetTooBig() {
            Fleet f = new Fleet();

            // encher com navios válidos e sem colisão
            int maxAllowedByImpl = IFleet.FLEET_SIZE + 1; // a implementação permite 11

            for (int i = 0; i < maxAllowedByImpl; i++) {
                int row = i / IFleet.BOARD_SIZE;
                int col = i % IFleet.BOARD_SIZE;
                assertTrue(f.addShip(createSingleCellShip("S" + i, row, col)));
            }

            // este já deve falhar porque ships.size() agora é 11 (>10)
            assertFalse(f.addShip(createSingleCellShip("Extra", 9, 9)));
        }
    }

    @Nested
    @DisplayName("getShipsLike()")
    class GetShipsLikeTests {

        @Test
        @DisplayName("Filtra navios corretamente por categoria")
        void filterByCategory() {
            Fleet f = new Fleet();
            IShip s1 = createSingleCellShip("Nau", 0, 0);
            IShip s2 = createSingleCellShip("Nau", 1, 1);
            IShip s3 = createSingleCellShip("Fragata", 2, 2);

            f.addShip(s1);
            f.addShip(s2);
            f.addShip(s3);

            assertEquals(2, f.getShipsLike("Nau").size());
            assertEquals(1, f.getShipsLike("Fragata").size());
            assertTrue(f.getShipsLike("Barca").isEmpty());
        }
    }

    @Nested
    @DisplayName("getFloatingShips()")
    class FloatingShipsTests {

        @Test
        @DisplayName("Devolve apenas navios que ainda flutuam")
        void onlyFloating() {
            Fleet f = new Fleet();
            TestShip s1 = createSingleCellShip("Nau", 0, 0);
            TestShip s2 = createSingleCellShip("Fragata", 1, 1);
            TestShip s3 = createSingleCellShip("Barca", 2, 2);

            f.addShip(s1);
            f.addShip(s2);
            f.addShip(s3);

            s2.sink();

            List<IShip> floating = f.getFloatingShips();
            assertEquals(2, floating.size());
            assertTrue(floating.contains(s1));
            assertTrue(floating.contains(s3));
        }

        @Test
        @DisplayName("Se todos afundarem, devolve lista vazia")
        void noneFloating() {
            Fleet f = new Fleet();
            TestShip s1 = createSingleCellShip("Nau", 0, 0);
            TestShip s2 = createSingleCellShip("Fragata", 1, 1);

            f.addShip(s1);
            f.addShip(s2);

            s1.sink();
            s2.sink();

            assertTrue(f.getFloatingShips().isEmpty());
        }
    }

    @Nested
    @DisplayName("shipAt()")
    class ShipAtTests {

        @Test
        @DisplayName("Devolve navio correto quando existe nessa posição")
        void returnsShipWhenExists() {
            Fleet f = new Fleet();
            IShip s1 = createSingleCellShip("Nau", 3, 3);
            f.addShip(s1);

            assertEquals(s1, f.shipAt(new TestPosition(3, 3)));
        }

        @Test
        @DisplayName("Devolve null quando não existe navio na posição")
        void returnsNullWhenEmpty() {
            Fleet f = new Fleet();
            IShip s1 = createSingleCellShip("Nau", 3, 3);
            f.addShip(s1);

            assertNull(f.shipAt(new TestPosition(9, 9)));
        }
    }

    @Nested
    @DisplayName("Métodos de impressão")
    class PrintTests {

        @Test
        @DisplayName("printShips não lança exceções e imprime algo")
        void printShipsWorks() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(baos));

            List<IShip> list = new ArrayList<>();
            list.add(createSingleCellShip("Nau", 0, 0));

            assertDoesNotThrow(() -> Fleet.printShips(list));
            assertTrue(baos.toString().contains("Nau"));

            System.setOut(old);
        }

        @Test
        @DisplayName("printStatus imprime sem lançar exceções")
        void printStatusWorks() {
            Fleet f = new Fleet();
            f.addShip(createSingleCellShip("Nau", 0, 0));
            f.addShip(createSingleCellShip("Fragata", 1, 1));

            assertDoesNotThrow(f::printStatus);
        }

        @Test
        @DisplayName("printShipsByCategory imprime só essa categoria")
        void printShipsByCategoryWorks() {
            Fleet f = new Fleet();
            f.addShip(createSingleCellShip("Nau", 0, 0));
            f.addShip(createSingleCellShip("Fragata", 1, 1));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(baos));

            f.printShipsByCategory("Nau");

            String out = baos.toString();
            assertTrue(out.contains("Nau"));
            assertFalse(out.contains("Fragata"));

            System.setOut(old);
        }

        @Test
        @DisplayName("printAllShips imprime todos os navios")
        void printAllShipsWorks() {
            Fleet f = new Fleet();
            f.addShip(createSingleCellShip("Nau", 0, 0));
            f.addShip(createSingleCellShip("Fragata", 1, 1));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(baos));

            f.printAllShips();

            String out = baos.toString();
            assertTrue(out.contains("Nau"));
            assertTrue(out.contains("Fragata"));

            System.setOut(old);
        }
    }
}
