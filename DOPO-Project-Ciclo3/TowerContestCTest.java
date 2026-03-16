/**
 * Casos de prueba comunes para TowerContest — Ciclo 3.
 *
 * @author AndresMora-DanielCamacho
 * @version 1.0
 */
public class TowerContestCTest {

    // solve — AM, DC

    /** solve(4,9) retorna permutación válida de 4 alturas. AM */
    public static void testSolveSampleInput1_AM() {
        String r = TowerContest.solve(4, 9);
        assert !r.equals("impossible") : "solve(4,9) debe ser posible";
        assert r.split(" ").length == 4 : "Debe tener 4 alturas";
    }

    /** solve(4,100) es impossible. DC */
    public static void testSolveSampleInput2_DC() {
        assert TowerContest.solve(4, 100).equals("impossible");
    }

    /** h por debajo del mínimo es impossible. AM */
    public static void testSolveImpossibleBelowMin_AM() {
        assert TowerContest.solve(4, 6).equals("impossible");
    }

    /** h por encima del máximo es impossible. DC */
    public static void testSolveImpossibleAboveMax_DC() {
        assert TowerContest.solve(4, 17).equals("impossible");
    }

    /** n=1, h=1: retorna "1". AM */
    public static void testSolveN1_AM() {
        assert TowerContest.solve(1, 1).equals("1");
    }

    /** n=1, h=2: impossible. DC */
    public static void testSolveN1Impossible_DC() {
        assert TowerContest.solve(1, 2).equals("impossible");
    }

    /** n grande, hMin: posible y usa n alturas. AM */
    public static void testSolveLargeN_AM() {
        int n = 200000;
        String r = TowerContest.solve(n, 2L*n-1);
        assert !r.equals("impossible");
        assert r.split(" ").length == n;
    }

    /** n grande, h>n²: impossible. DC */
    public static void testSolveLargeNImpossible_DC() {
        int n = 200000;
        assert TowerContest.solve(n, (long)n*n+1).equals("impossible");
    }

    public static void main(String[] args) {
        testSolveSampleInput1_AM();
        testSolveSampleInput2_DC();
        testSolveImpossibleBelowMin_AM();
        testSolveImpossibleAboveMax_DC();
        testSolveN1_AM();
        testSolveN1Impossible_DC();
        testSolveLargeN_AM();
        testSolveLargeNImpossible_DC();
        System.out.println("Todas las pruebas comunes del Ciclo 3 pasaron.");
    }
}
