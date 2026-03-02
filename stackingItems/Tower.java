import javax.swing.JOptionPane;
import java.util.Arrays;

/**
 * Simula una torre de apilamiento de tazas y tapas.
 *
 * MODELO DE APILAMIENTO:
 * La torre mantiene un "nivel de piso" (floorY) que representa la base
 * del elemento actual. Cuando se apila un elemento:
 *
 *   - Si es más ancho que el elemento anterior (o es el primero):
 *     va encima del elemento anterior (floorY sube).
 *   - Si es más angosto que el elemento anterior (cabe dentro):
 *     su base queda al mismo nivel que la base del contenedor más
 *     cercano que lo puede contener. Visualmente está DENTRO de ese contenedor.
 *
 * La altura de la torre es la distancia desde la base del elemento más
 * externo hasta el punto más alto de cualquier elemento.
 *
 * @author AndresMora-DanielCamacho
 * @version 3.0
 */
public class Tower {

    private static final int PX_PER_CM = Cup.PX_PER_CM;
    private static final int TOWER_X   = 30;   // margen izquierdo px
    private static final int TOWER_TOP = 10;   // margen superior px
    private static final int RULER_W   = 8;    // ancho regla px
    private static final int TICK_H    = 2;    // alto marca regla px
    private static final int MAX_ITEMS = 500;

    private int width;
    private int maxHeight;

    /** Lista de elementos en orden de apilamiento (índice 0 = fondo de la torre). */
    private Object[] items;
    private int itemCount;

    private Rectangle   background;
    private Rectangle[] rulerTicks;

    private boolean visible;
    private boolean lastOk;

    /** Centro X de la torre en píxeles (calculado según la copa más grande posible). */
    private int centerX;
    /** Ancho del fondo en píxeles. */
    private int bgWidthPx;

    /**
     * Crea una torre con el ancho y la altura máxima dados.
     *
     * @param width     ancho mínimo en cm
     * @param maxHeight altura máxima en cm
     */
    public Tower(int width, int maxHeight) {
        this.width      = width;
        this.maxHeight  = maxHeight;
        this.lastOk     = true;
        this.items      = new Object[MAX_ITEMS];
        this.itemCount  = 0;
        this.rulerTicks = new Rectangle[maxHeight];

        buildBackground();

        if (fitsOnCanvas()) {
            visible = true;
            showBackground();
        } else {
            visible = false;
        }
    }

    // ---------------------------------------------------------------
    // TAZAS
    // ---------------------------------------------------------------

    /**
     * Agrega una taza a la cima de la torre.
     *
     * @param t número de la taza
     */
    public void pushCup(int t) {
        if (findCup(t) != -1) { fail("Taza " + t + " ya está en la torre."); return; }
        if (stackHeight() + new Cup(t).getHeightCm() > maxHeight) {
            fail("Taza " + t + " no cabe en la torre."); return;
        }
        items[itemCount++] = new Cup(t);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la taza más alta de la lista (y su tapa pegada si la tiene).
     */
    public void popCup() {
        int idx = lastCupIndex();
        if (idx == -1) { fail("No hay tazas en la torre."); return; }
        if (isCovered(idx)) removeAtIndex(idx + 1);
        removeAtIndex(lastCupIndex());
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la taza con el número dado (y su tapa pegada si la tiene).
     *
     * @param t número de la taza
     */
    public void removeCup(int t) {
        int idx = findCup(t);
        if (idx == -1) { fail("Taza " + t + " no está en la torre."); return; }
        if (isCovered(idx)) removeAtIndex(idx + 1);
        removeAtIndex(findCup(t));
        lastOk = true;
        redraw();
    }

    // ---------------------------------------------------------------
    // TAPAS
    // ---------------------------------------------------------------

    /**
     * Agrega una tapa a la cima de la torre.
     *
     * @param t número de la tapa
     */
    public void pushLid(int t) {
        if (findLid(t) != -1) { fail("Tapa " + t + " ya está en la torre."); return; }
        if (stackHeight() + Lid.HEIGHT_CM > maxHeight) {
            fail("Tapa " + t + " no cabe en la torre."); return;
        }
        items[itemCount++] = new Lid(t);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la tapa más alta de la lista.
     */
    public void popLid() {
        int idx = lastLidIndex();
        if (idx == -1) { fail("No hay tapas en la torre."); return; }
        removeAtIndex(idx);
        lastOk = true;
        redraw();
    }

    /**
     * Elimina la tapa con el número dado.
     *
     * @param t número de la tapa
     */
    public void removeLid(int t) {
        int idx = findLid(t);
        if (idx == -1) { fail("Tapa " + t + " no está en la torre."); return; }
        removeAtIndex(idx);
        lastOk = true;
        redraw();
    }

    // ---------------------------------------------------------------
    // REORGANIZAR
    // ---------------------------------------------------------------

    /**
     * Ordena los elementos de mayor a menor número (el menor queda en cima).
     * Si una taza tiene su tapa en la lista, la tapa se pega encima de la taza.
     */
    public void orderTower() {
        hideAll();
        sortByNumberDescending();
        trimToFit();
        lastOk = true;
        redraw();
    }

    /**
     * Invierte el orden actual de todos los elementos.
     */
    public void reverseTower() {
        hideAll();
        reverseItems();
        trimToFit();
        lastOk = true;
        redraw();
    }

    // ---------------------------------------------------------------
    // CONSULTAS
    // ---------------------------------------------------------------

    /**
     * Retorna la altura efectiva de la torre en cm.
     * Es la altura desde la base de la torre hasta el punto más alto.
     *
     * @return altura en cm
     */
    public int height() { return stackHeight(); }

    /**
     * Retorna los números de las tazas que tienen su tapa pegada
     * (tapa inmediatamente en posición i+1), ordenados de menor a mayor.
     *
     * @return int[] con los números de tazas tapadas
     */
    public int[] lidedCups() {
        int count = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i] instanceof Cup && isCovered(i)) count++;
        }
        int[] result = new int[count];
        int   pos    = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i] instanceof Cup && isCovered(i))
                result[pos++] = ((Cup) items[i]).getNumber();
        }
        Arrays.sort(result);
        return result;
    }

    /**
     * Retorna los elementos apilados como {tipo, número}, de base a cima.
     *
     * @return String[][] de elementos
     */
    public String[][] stackingItems() {
        String[][] result = new String[itemCount][2];
        for (int i = 0; i < itemCount; i++) {
            if (items[i] instanceof Cup) {
                result[i][0] = "cup"; result[i][1] = String.valueOf(((Cup)items[i]).getNumber());
            } else {
                result[i][0] = "lid"; result[i][1] = String.valueOf(((Lid)items[i]).getNumber());
            }
        }
        return result;
    }

    // ---------------------------------------------------------------
    // VISIBILIDAD
    // ---------------------------------------------------------------

    /** Hace visible el simulador. */
    public void makeVisible() {
        if (!fitsOnCanvas()) return;
        visible = true;
        showBackground();
        redraw();
    }

    /** Hace invisible el simulador. */
    public void makeInvisible() {
        visible = false;
        hideAll();
        background.makeInvisible();
        for (Rectangle t : rulerTicks) { if (t != null) t.makeInvisible(); }
    }

    /** Termina el simulador. */
    public void exit() { System.exit(0); }

    /**
     * Indica si la última operación fue exitosa.
     * @return true si tuvo éxito
     */
    public boolean ok() { return lastOk; }

    // ---------------------------------------------------------------
    // DIBUJO — núcleo del simulador
    // ---------------------------------------------------------------

    /**
     * Redibuja todos los elementos con la lógica correcta de apilamiento:
     *
     * Se recorre la lista de fondo (0) a cima (itemCount-1).
     * Se mantiene una pila de elementos activos, cada uno con:
     *   - baseY: píxel de su base exterior (donde apoya)
     *   - topY:  píxel de su borde superior
     *   - widthPx: ancho exterior
     *
     * Para cada elemento nuevo:
     *   - Si su ancho <= ancho interior del tope de la pila (cabe dentro):
     *       su baseY = baseY del tope + WALL (1cm de grosor de base)
     *       se agrega a la pila
     *   - Si su ancho > ancho interior del tope (no cabe):
     *       sacar de la pila hasta encontrar uno que lo contenga o hasta vaciarla
     *       si vacía: baseY = topY más alto visto (va encima de todo)
     *       si encontró contenedor: baseY = baseY del contenedor + WALL
     *       se agrega a la pila
     */
    private void redraw() {
        hideAll();
        if (!visible) return;

        int bottomPx = TOWER_TOP + maxHeight * PX_PER_CM;

        // Pila: baseY, topY, widthPx, esCopa de cada contenedor activo
        int[]     stkBaseY  = new int[MAX_ITEMS];
        int[]     stkTopY   = new int[MAX_ITEMS];
        int[]     stkWidth  = new int[MAX_ITEMS];
        boolean[] stkIsCup  = new boolean[MAX_ITEMS];
        int   stkSize  = 0;
        int   highestTopY = bottomPx;

        for (int i = 0; i < itemCount; i++) {
            int w = widthPxOf(i);
            int h = heightPxOf(i);
            int wallPx = Cup.WALL;

            // Interior del contenedor en el tope:
            // copa → width - 2*WALL; tapa → 0 (sólida, nada cabe dentro)
            int lastPoppedTopY = -1;
            while (stkSize > 0) {
                int interior = stkIsCup[stkSize - 1] ? stkWidth[stkSize - 1] - 2 * wallPx : 0;
                if (w > interior) {
                    lastPoppedTopY = stkTopY[stkSize - 1];
                    stkSize--;
                } else break;
            }

            int baseY;
            if (lastPoppedTopY != -1) {
                // Se sacaron elementos: el nuevo va encima del último sacado
                // (ya sea que haya contenedor debajo o no)
                baseY = lastPoppedTopY;
            } else if (stkSize > 0) {
                // Nadie fue sacado: es el primer elemento dentro de este contenedor
                baseY = stkBaseY[stkSize - 1] - wallPx;
            } else {
                // Pila vacía desde el inicio: va en el fondo de la torre
                baseY = bottomPx;
            }

            int topY = baseY - h;
            int x    = centerX - w / 2;
            placeElement(i, x, topY);

            if (topY < highestTopY) highestTopY = topY;

            stkBaseY [stkSize] = baseY;
            stkTopY  [stkSize] = topY;
            stkWidth [stkSize] = w;
            stkIsCup [stkSize] = (items[i] instanceof Cup);
            stkSize++;
        }
    }

    /** Mueve y hace visible el elemento i en la posición dada. */
    private void placeElement(int i, int x, int y) {
        if (items[i] instanceof Cup) {
            Cup c = (Cup) items[i];
            c.moveTo(x, y);
            c.makeVisible();
        } else {
            Lid l = (Lid) items[i];
            l.moveTo(x, y);
            l.makeVisible();
        }
    }

    // ---------------------------------------------------------------
    // ALTURA EFECTIVA DE LA TORRE
    // ---------------------------------------------------------------

    /**
     * Calcula la altura efectiva de la torre en cm usando la misma
     * lógica de contenedores que redraw().
     *
     * @return altura de la torre en cm
     */
    private int stackHeight() {
        if (itemCount == 0) return 0;

        int[] stkBase  = new int[MAX_ITEMS];
        int[] stkTop   = new int[MAX_ITEMS];
        int[] stkWidth = new int[MAX_ITEMS];
        boolean[] stkIsCup = new boolean[MAX_ITEMS];
        int stkSize = 0;
        int maxTop  = 0;

        for (int i = 0; i < itemCount; i++) {
            int w    = widthCmOf(i);
            int h    = heightCmOf(i);
            int wall = 1;

            int lastPoppedTop = -1;
            while (stkSize > 0) {
                int interior = stkIsCup[stkSize-1] ? stkWidth[stkSize-1] - 2*wall : 0;
                if (w > interior) {
                    lastPoppedTop = stkTop[stkSize-1];
                    stkSize--;
                } else break;
            }

            int base;
            if      (lastPoppedTop != -1) base = lastPoppedTop;
            else if (stkSize > 0)         base = stkBase[stkSize-1] + wall;
            else                          base = maxTop;

            int top = base + h;
            if (top > maxTop) maxTop = top;

            stkBase [stkSize] = base;
            stkTop  [stkSize] = top;
            stkWidth[stkSize] = w;
            stkIsCup[stkSize] = (items[i] instanceof Cup);
            stkSize++;
        }
        return maxTop;
    }

    // ---------------------------------------------------------------
    // MÉTODOS PRIVADOS DE APOYO
    // ---------------------------------------------------------------

    /** Ancho en píxeles del elemento i. */
    private int widthPxOf(int i) {
        if (items[i] instanceof Cup) return ((Cup) items[i]).getWidthPx();
        return ((Lid) items[i]).getWidthPx();
    }

    /** Altura en píxeles del elemento i. */
    private int heightPxOf(int i) {
        if (items[i] instanceof Cup) return ((Cup) items[i]).getHeightPx();
        return Lid.HEIGHT_CM * PX_PER_CM;
    }

    /** Ancho en cm del elemento i. */
    private int widthCmOf(int i) {
        if (items[i] instanceof Cup) return ((Cup) items[i]).getWidthCm();
        return ((Lid) items[i]).getWidthCm();
    }

    /** Altura en cm del elemento i. */
    private int heightCmOf(int i) {
        if (items[i] instanceof Cup) return ((Cup) items[i]).getHeightCm();
        return Lid.HEIGHT_CM;
    }

    /**
     * Indica si la taza en idx está tapada (tapa del mismo número en idx+1).
     *
     * @param idx índice de la taza
     * @return true si está tapada
     */
    private boolean isCovered(int idx) {
        if (idx + 1 >= itemCount)          return false;
        if (!(items[idx]     instanceof Cup)) return false;
        if (!(items[idx + 1] instanceof Lid)) return false;
        return ((Cup)items[idx]).getNumber() == ((Lid)items[idx+1]).getNumber();
    }

    /**
     * Ordena la lista de mayor a menor número usando inserción.
     * Las tapas que pertenecen a tazas presentes se pegan encima de su taza.
     */
    private void sortByNumberDescending() {
        int cupCount = 0, lidCount = 0;
        Cup[] cups = new Cup[itemCount];
        Lid[] lids = new Lid[itemCount];
        for (int i = 0; i < itemCount; i++) {
            if (items[i] instanceof Cup) cups[cupCount++] = (Cup) items[i];
            else                          lids[lidCount++] = (Lid) items[i];
        }

        // Ordenar tazas descendente
        for (int i = 1; i < cupCount; i++) {
            Cup key = cups[i]; int j = i - 1;
            while (j >= 0 && cups[j].getNumber() < key.getNumber()) { cups[j+1]=cups[j]; j--; }
            cups[j+1] = key;
        }
        // Ordenar tapas descendente
        for (int i = 1; i < lidCount; i++) {
            Lid key = lids[i]; int j = i - 1;
            while (j >= 0 && lids[j].getNumber() < key.getNumber()) { lids[j+1]=lids[j]; j--; }
            lids[j+1] = key;
        }

        // Reconstruir: tazas de mayor a menor, pegando su tapa si existe
        boolean[] lidUsed = new boolean[lidCount];
        itemCount = 0;
        int li = 0;
        for (int ci = 0; ci < cupCount; ci++) {
            Cup cup = cups[ci];
            // Tapas sueltas más grandes que esta taza van antes
            while (li < lidCount && !lidUsed[li]) {
                boolean belongsToCup = false;
                for (int k = 0; k < cupCount; k++) {
                    if (cups[k].getNumber() == lids[li].getNumber()) { belongsToCup = true; break; }
                }
                if (belongsToCup) { li++; continue; }
                if (lids[li].getNumber() > cup.getNumber()) {
                    items[itemCount++] = lids[li]; lidUsed[li] = true; li++;
                } else break;
            }
            items[itemCount++] = cup;
            // Pegar tapa si existe
            for (int k = 0; k < lidCount; k++) {
                if (!lidUsed[k] && lids[k].getNumber() == cup.getNumber()) {
                    items[itemCount++] = lids[k]; lidUsed[k] = true; break;
                }
            }
        }
        // Tapas restantes sin taza
        for (int k = 0; k < lidCount; k++) {
            if (!lidUsed[k]) items[itemCount++] = lids[k];
        }
    }

    /** Invierte el arreglo de items. */
    private void reverseItems() {
        for (int i = 0; i < itemCount / 2; i++) {
            Object tmp = items[i]; items[i] = items[itemCount-1-i]; items[itemCount-1-i] = tmp;
        }
    }

    /** Elimina elementos desde la cima hasta que la altura quepa. */
    private void trimToFit() {
        while (stackHeight() > maxHeight && itemCount > 0) items[--itemCount] = null;
    }

    /** Construye el fondo y la regla. El ancho se adapta a la copa más grande posible. */
    private void buildBackground() {
        // Copa más grande que cabe: número n donde 2n-1 <= maxHeight → n = (maxHeight+1)/2
        int maxCupNum = (maxHeight + 1) / 2;
        // Ancho de esa copa en px
        bgWidthPx = Math.max(width * PX_PER_CM, maxCupNum * 2 * PX_PER_CM);
        centerX   = TOWER_X + bgWidthPx / 2;
        int hPx   = maxHeight * PX_PER_CM;

        background = new Rectangle();
        background.changeSize(hPx, bgWidthPx + RULER_W);
        background.changeColor("black");
        background.moveHorizontal(TOWER_X - 70);
        background.moveVertical(TOWER_TOP - 15);

        for (int cm = 1; cm <= maxHeight; cm++) {
            Rectangle tick = new Rectangle();
            tick.changeSize(TICK_H, RULER_W);
            tick.changeColor("white");
            tick.moveHorizontal((TOWER_X + bgWidthPx) - 70);
            tick.moveVertical((TOWER_TOP + hPx - cm * PX_PER_CM) - 15);
            rulerTicks[cm - 1] = tick;
        }
    }

    /** Muestra fondo y regla. */
    private void showBackground() {
        background.makeVisible();
        for (Rectangle t : rulerTicks) { if (t != null) t.makeVisible(); }
    }

    /** Oculta todos los elementos. */
    private void hideAll() {
        for (int i = 0; i < itemCount; i++) {
            if      (items[i] instanceof Cup) ((Cup)items[i]).makeInvisible();
            else if (items[i] instanceof Lid) ((Lid)items[i]).makeInvisible();
        }
    }

    /** Elimina el elemento en idx y compacta el arreglo. */
    private void removeAtIndex(int idx) {
        if (idx < 0 || idx >= itemCount) return;
        if      (items[idx] instanceof Cup) ((Cup)items[idx]).makeInvisible();
        else if (items[idx] instanceof Lid) ((Lid)items[idx]).makeInvisible();
        for (int i = idx; i < itemCount - 1; i++) items[i] = items[i+1];
        items[--itemCount] = null;
    }

    /** Índice de la taza con el número dado, o -1. */
    private int findCup(int n) {
        for (int i = 0; i < itemCount; i++)
            if (items[i] instanceof Cup && ((Cup)items[i]).getNumber() == n) return i;
        return -1;
    }

    /** Índice de la tapa con el número dado, o -1. */
    private int findLid(int n) {
        for (int i = 0; i < itemCount; i++)
            if (items[i] instanceof Lid && ((Lid)items[i]).getNumber() == n) return i;
        return -1;
    }

    /** Índice de la última taza, o -1. */
    private int lastCupIndex() {
        for (int i = itemCount - 1; i >= 0; i--)
            if (items[i] instanceof Cup) return i;
        return -1;
    }

    /** Índice de la última tapa, o -1. */
    private int lastLidIndex() {
        for (int i = itemCount - 1; i >= 0; i--)
            if (items[i] instanceof Lid) return i;
        return -1;
    }

    /** ¿Cabe la torre en el canvas? */
    private boolean fitsOnCanvas() {
        return (maxHeight * PX_PER_CM + TOWER_TOP) <= 600
            && (bgWidthPx + TOWER_X + RULER_W)     <= 600;
    }

    /** Marca lastOk=false y muestra mensaje si visible. */
    private void fail(String msg) {
        lastOk = false;
        if (visible) JOptionPane.showMessageDialog(null, msg,
            "Simulador de Torre", JOptionPane.WARNING_MESSAGE);
    }
}
