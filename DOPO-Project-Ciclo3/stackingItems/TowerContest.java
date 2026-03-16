import java.util.ArrayList;
import java.util.List;

/**
 * Resuelve y simula el problema "Stacking Cups" del
 * 49° Campeonato Mundial ICPC (Problema J, Bakú 2025).
 *
 * ALGORITMO — solve():
 * Altura mínima = 2n-1 (todas dentro de la copa n).
 * Altura máxima = n² (todas contribuyen su altura).
 * Para h en [2n-1, n²]: deficit = n² - h. Greedy de mayor a menor:
 * si (2k-1) <= deficit, ocultar copa k (va después de n, contribuye 0).
 * Las copas visibles van antes de n en orden creciente.
 *
 * solve. La entrada y la salida corresponden a lo definido en el problema de la maratón.
 * simulate. La entrada corresponde a lo definido en el problema de la maratón. La salida es
 * la imagen de la solución, si existe y es posible graficarla; en caso contrario, presenta
 * un mensaje indicándolo.
 *
 * @author AndresMora-DanielCamacho
 * @version 1.0
 */
public class TowerContest {

    /**
     * Resuelve el problema: dado n copas y altura objetivo h,
     * retorna las alturas en orden de colocación separadas por espacio,
     * o "impossible" si no es factible.
     *
     * @param n número de copas (copas 1..n con alturas 1,3,5,...,2n-1)
     * @param h altura objetivo deseada
     * @return alturas separadas por espacio en orden de colocación, o "impossible"
     */
    public static String solve(int n, long h) {
        long hMin = 2L * n - 1;
        long hMax = (long) n * n;
        if (h < hMin || h > hMax) return "impossible";

        boolean[] hidden = new boolean[n + 1];
        long deficit = hMax - h;
        for (int k = n - 1; k >= 1 && deficit > 0; k--) {
            long contrib = 2L * k - 1;
            if (contrib <= deficit) { hidden[k] = true; deficit -= contrib; }
        }
        if (deficit != 0) return "impossible";

        // Parte 1: visibles menores que n en orden creciente (contribuyen su altura)
        // Parte 2: copa n
        // Parte 3: ocultas en orden decreciente (caen dentro de n, contribuyen 0)
        List<Integer> order = new ArrayList<>();
        for (int k = 1; k < n; k++) if (!hidden[k]) order.add(2*k-1);
        order.add(2*n-1);
        for (int k = n-1; k >= 1; k--) if (hidden[k]) order.add(2*k-1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < order.size(); i++) { if (i>0) sb.append(" "); sb.append(order.get(i)); }
        return sb.toString();
    }

    /**
     * Simula visualmente la solución usando la clase Tower.
     * Si no existe solución, muestra un mensaje.
     * La clase Tower se usa solo para simular, nunca para resolver.
     *
     * @param n número de copas
     * @param h altura objetivo
     */
    public static void simulate(int n, int h) {
        String result = solve(n, (long) h);
        if (result.equals("impossible")) {
            System.out.println("No es posible construir una torre de " + h + " cm con " + n + " copas.");
            javax.swing.JOptionPane.showMessageDialog(null,
                "No es posible construir una torre de " + h + " cm con " + n + " copas.",
                "Stacking Cups — Imposible", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] parts = result.split(" ");
        int[] cups = new int[parts.length];
        for (int i = 0; i < parts.length; i++) cups[i] = (Integer.parseInt(parts[i]) + 1) / 2;

        Tower tower = new Tower(2 * n, h + 4);
        tower.makeVisible();
        System.out.println("Simulando solución: " + result);
        for (int i = 0; i < cups.length; i++) {
            tower.pushCup(cups[i]);
            System.out.println("  Paso " + (i+1) + ": copa " + cups[i]
                + " (h=" + (2*cups[i]-1) + " cm) — torre: " + tower.height() + " cm");
            Canvas.getCanvas().wait(600);
        }
        System.out.println("Simulación completa. Altura final: " + tower.height() + " cm.");
    }
}
