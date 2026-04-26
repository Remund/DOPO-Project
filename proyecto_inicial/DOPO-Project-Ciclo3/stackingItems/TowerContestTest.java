import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TowerContest (Ciclo 3).
 *
 * @author AndresMora-DanielCamacho
 * @version 1.0
 */
public class TowerContestTest {

    /** Verifica la altura real de una solución usando Tower. */
    private int measureHeight(String sol, int n) {
        if (sol.equals("impossible")) return -1;
        String[] parts = sol.split(" ");
        Tower t = new Tower(2*n+4, n*n+10);
        t.makeInvisible();
        for (String p : parts) t.pushCup((Integer.parseInt(p)+1)/2);
        return t.height();
    }

    /** Verifica que sea permutación válida de {1,3,...,2n-1}. */
    private void assertPermutation(String sol, int n) {
        assertNotEquals("impossible", sol);
        String[] parts = sol.split(" ");
        assertEquals(n, parts.length);
        boolean[] seen = new boolean[n+1];
        for (String p : parts) {
            int h = Integer.parseInt(p), k = (h+1)/2;
            assertTrue(h%2==1 && k>=1 && k<=n);
            assertFalse(seen[k]); seen[k] = true;
        }
    }

    @Test public void testSampleInput1() {
        String r = TowerContest.solve(4, 9);
        assertPermutation(r, 4);
        assertEquals(9, measureHeight(r, 4));
    }

    @Test public void testSampleInput2() {
        assertEquals("impossible", TowerContest.solve(4, 100));
    }

    @Test public void testMinHeight() {
        for (int n=1; n<=5; n++) {
            String r = TowerContest.solve(n, 2L*n-1);
            assertPermutation(r, n);
            assertEquals(2*n-1, measureHeight(r, n));
        }
    }

    @Test public void testMaxHeight() {
        for (int n=1; n<=5; n++) {
            String r = TowerContest.solve(n, (long)n*n);
            assertPermutation(r, n);
            assertEquals(n*n, measureHeight(r, n));
        }
    }

    @Test public void testN1() {
        assertEquals("1", TowerContest.solve(1, 1));
        assertEquals("impossible", TowerContest.solve(1, 2));
    }

    @Test public void testAllHeightsN3() {
        for (long h=5; h<=9; h++) {
            String r = TowerContest.solve(3, h);
            assertPermutation(r, 3);
            assertEquals((int)h, measureHeight(r, 3));
        }
    }

    @Test public void testAllHeightsN4() {
        for (long h=7; h<=16; h++) {
            String r = TowerContest.solve(4, h);
            assertPermutation(r, 4);
            assertEquals((int)h, measureHeight(r, 4));
        }
    }

    @Test public void testImpossibleBelowMin() { assertEquals("impossible", TowerContest.solve(5, 8)); }
    @Test public void testImpossibleAboveMax() { assertEquals("impossible", TowerContest.solve(5, 26)); }
    @Test public void testImpossibleNegative() { assertEquals("impossible", TowerContest.solve(3, -1)); }

    @Test public void testLargeN() {
        int n = 200000;
        String r = TowerContest.solve(n, 2L*n-1);
        assertNotEquals("impossible", r);
        assertEquals(n, r.split(" ").length);
    }
}
