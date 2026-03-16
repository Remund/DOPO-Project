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
 * Tower(cups : int). Crea tazas 1, 2, 3, ... cups. La más grande queda en la base.
 * swap. Los objetos se identifican por su tipo y su número. Ej, {"cup","4"} o {"lid","4"}
 * swapToReduce. Los objetos a intercambiar se indentifican por su tipo y su número. Ej: {{"cup","4"},{"lid","4"}}
 *
 * @author AndresMora-DanielCamacho
 * @version 4.0
 */
public class Tower {

    private static final int PX_PER_CM = Cup.PX_PER_CM;
    private static final int TOWER_X   = 30;
    private static final int TOWER_TOP = 10;
    private static final int RULER_W   = 8;
    private static final int TICK_H    = 2;
    private static final int MAX_ITEMS = 500;

    private int width;
    private int maxHeight;

    private Object[] items;
    private int itemCount;

    private Rectangle   background;
    private Rectangle[] rulerTicks;

    private boolean visible;
    private boolean lastOk;

    private int centerX;
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
        if (fitsOnCanvas()) { visible = true; showBackground(); }
        else visible = false;
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
     * Invierte el orden actual de todos los elementos de la torre.
     */
    public void reverseTower() {
        hideAll();
        reverseItems();
        trimToFit();
        lastOk = true;
        redraw();
    }

    // ---------------------------------------------------------------
    // CICLO 2
    // ---------------------------------------------------------------

    /**
     * Constructor que crea una torre con copas del 1 al cups, con la más grande en la base.
     * Ej: cups=4 crea las tazas 1, 2, 3 y 4 (altura 1, 3, 5, 7 cm).
     *
     * @param cups número de tazas a crear (>= 1)
     */
    public Tower(int cups) {
        this(20, 50);
        for (int i = cups; i >= 1; i--) {
            items[itemCount++] = new Cup(i);
        }
        lastOk = true;
        redraw();
    }

    /**
     * Intercambia la posición de dos objetos en la torre.
     * Forma amigable: swap("cup", 4, "lid", 4)
     *
     * @param type1 tipo del primer objeto ("cup" o "lid")
     * @param num1  número del primer objeto
     * @param type2 tipo del segundo objeto ("cup" o "lid")
     * @param num2  número del segundo objeto
     */
    public void swap(String type1, int num1, String type2, int num2) {
        swap(new String[]{type1, String.valueOf(num1)},
             new String[]{type2, String.valueOf(num2)});
    }

    /**
     * Intercambia la posición de dos objetos en la torre.
     * Los objetos se identifican por tipo y número: {"cup","4"} o {"lid","4"}.
     *
     * @param o1 identificador del primer objeto  {tipo, número}
     * @param o2 identificador del segundo objeto {tipo, número}
     */
    public void swap(String[] o1, String[] o2) {
        int idx1 = findItem(o1);
        int idx2 = findItem(o2);
        if (idx1 == -1) { fail("Elemento " + o1[0] + " " + o1[1] + " no está en la torre."); return; }
        if (idx2 == -1) { fail("Elemento " + o2[0] + " " + o2[1] + " no está en la torre."); return; }
        Object tmp  = items[idx1];
        items[idx1] = items[idx2];
        items[idx2] = tmp;
        lastOk = true;
        redraw();
    }

    /**
     * Coloca la tapa de la taza t inmediatamente encima de ella en la lista.
     * Si la tapa ya existe en otro lugar, se quita y se reinserta en cupIdx+1.
     * Una taza está tapada cuando su tapa ocupa el índice inmediatamente siguiente.
     *
     * @param t número de la taza a tapar
     */
    public void cover(int t) {
        int cupIdx = findCup(t);
        if (cupIdx == -1) { fail("Taza " + t + " no está en la torre."); return; }
        if (isCovered(cupIdx)) { lastOk = true; return; }

        // Si la tapa ya existe en otro lugar, quitarla primero
        int lidIdx = findLid(t);
        if (lidIdx != -1) {
            removeAtIndex(lidIdx);
            cupIdx = findCup(t); // recalcular tras la eliminación
        }

        // Insertar la tapa en cupIdx + 1
        insertAt(new Lid(t), cupIdx + 1);
        lastOk = true;
        redraw();
    }

    /**
     * Consulta qué intercambio de dos elementos reduciría la altura de la torre.
     * Prueba todos los pares posibles y retorna el par que produce la mayor reducción.
     * Si ningún intercambio reduce la altura, retorna un arreglo vacío.
     *
     * @return String[][] con los dos identificadores {tipo,número}, o vacío si no hay mejora.
     *         Ej: {{"cup","4"},{"lid","4"}}
     */
    public String[][] swapToReduce() {
        int currentH = stackHeight();
        int bestGain = 0;
        int bestI = -1, bestJ = -1;
        for (int i = 0; i < itemCount; i++) {
            for (int j = i + 1; j < itemCount; j++) {
                Object tmp = items[i]; items[i] = items[j]; items[j] = tmp;
                int gain = currentH - stackHeight();
                tmp = items[i]; items[i] = items[j]; items[j] = tmp;
                if (gain > bestGain) { bestGain = gain; bestI = i; bestJ = j; }
            }
        }
        if (bestI == -1) return new String[0][0];
        return new String[][] {
            { typeOf(bestI), String.valueOf(numberOf(bestI)) },
            { typeOf(bestJ), String.valueOf(numberOf(bestJ)) }
        };
    }

    // ---------------------------------------------------------------
    // CONSULTAS
    // ---------------------------------------------------------------

    /**
     * Retorna la altura efectiva de la torre en cm.
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
        for (int i = 0; i < itemCount; i++)
            if (items[i] instanceof Cup && isCovered(i)) count++;
        int[] result = new int[count];
        int   pos    = 0;
        for (int i = 0; i < itemCount; i++)
            if (items[i] instanceof Cup && isCovered(i))
                result[pos++] = ((Cup) items[i]).getNumber();
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
    // DIBUJO
    // ---------------------------------------------------------------

    private void redraw() {
        hideAll();
        if (!visible) return;
        int bottomPx = TOWER_TOP + maxHeight * PX_PER_CM;
        int wallPx   = Cup.WALL;
        int[] stkBaseY = new int[MAX_ITEMS], stkTopY = new int[MAX_ITEMS];
        int[] stkWidth = new int[MAX_ITEMS];
        boolean[] stkIsCup = new boolean[MAX_ITEMS];
        int stkSize = 0, highTopY = bottomPx;

        for (int i = 0; i < itemCount; i++) {
            int w = widthPxOf(i), h = heightPxOf(i);
            int lastPoppedTopY = -1;
            while (stkSize > 0) {
                int interior = stkIsCup[stkSize-1] ? stkWidth[stkSize-1] - 2*wallPx : 0;
                if (w > interior) { lastPoppedTopY = stkTopY[stkSize-1]; stkSize--; }
                else break;
            }
            int baseY;
            if      (lastPoppedTopY != -1 && stkSize > 0) baseY = lastPoppedTopY;
            else if (stkSize > 0)                          baseY = stkBaseY[stkSize-1] - wallPx;
            else if (lastPoppedTopY != -1)                 baseY = highTopY;
            else                                           baseY = bottomPx;
            int topY = baseY - h;
            placeElement(i, centerX - w/2, topY);
            if (topY < highTopY) highTopY = topY;
            stkBaseY[stkSize] = baseY; stkTopY[stkSize] = topY;
            stkWidth[stkSize] = w; stkIsCup[stkSize] = (items[i] instanceof Cup);
            stkSize++;
        }
    }

    private void placeElement(int i, int x, int y) {
        if (items[i] instanceof Cup) { ((Cup)items[i]).moveTo(x, y); ((Cup)items[i]).makeVisible(); }
        else { ((Lid)items[i]).moveTo(x, y); ((Lid)items[i]).makeVisible(); }
    }

    // ---------------------------------------------------------------
    // ALTURA
    // ---------------------------------------------------------------

    private int stackHeight() {
        if (itemCount == 0) return 0;
        int[] stkBase = new int[MAX_ITEMS], stkTop = new int[MAX_ITEMS];
        int[] stkWidth = new int[MAX_ITEMS];
        boolean[] stkIsCup = new boolean[MAX_ITEMS];
        int stkSize = 0, maxTop = 0;
        for (int i = 0; i < itemCount; i++) {
            int w = widthCmOf(i), h = heightCmOf(i), wall = 1;
            int lastPopped = -1;
            while (stkSize > 0) {
                int interior = stkIsCup[stkSize-1] ? stkWidth[stkSize-1] - 2*wall : 0;
                if (w > interior) { lastPopped = stkTop[stkSize-1]; stkSize--; }
                else break;
            }
            int base;
            if      (lastPopped != -1 && stkSize > 0) base = lastPopped;
            else if (stkSize > 0)                      base = stkBase[stkSize-1] + wall;
            else if (lastPopped != -1)                 base = maxTop;
            else                                       base = maxTop;
            int top = base + h;
            if (top > maxTop) maxTop = top;
            stkBase[stkSize]=base; stkTop[stkSize]=top;
            stkWidth[stkSize]=w; stkIsCup[stkSize]=(items[i] instanceof Cup);
            stkSize++;
        }
        return maxTop;
    }

    // ---------------------------------------------------------------
    // PRIVADOS
    // ---------------------------------------------------------------

    private int widthPxOf(int i)  { return (items[i] instanceof Cup) ? ((Cup)items[i]).getWidthPx()  : ((Lid)items[i]).getWidthPx(); }
    private int heightPxOf(int i) { return (items[i] instanceof Cup) ? ((Cup)items[i]).getHeightPx() : Lid.HEIGHT_CM * PX_PER_CM; }
    private int widthCmOf(int i)  { return (items[i] instanceof Cup) ? ((Cup)items[i]).getWidthCm()  : ((Lid)items[i]).getWidthCm(); }
    private int heightCmOf(int i) { return (items[i] instanceof Cup) ? ((Cup)items[i]).getHeightCm() : Lid.HEIGHT_CM; }

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
     * Inserta un elemento en la posición dada, desplazando los demás.
     */
    private void insertAt(Object element, int idx) {
        if (itemCount >= MAX_ITEMS) return;
        for (int i = itemCount; i > idx; i--) items[i] = items[i-1];
        items[idx] = element;
        itemCount++;
    }

    private void sortByNumberDescending() {
        int cupCount = 0, lidCount = 0;
        Cup[] cups = new Cup[itemCount]; Lid[] lids = new Lid[itemCount];
        for (int i = 0; i < itemCount; i++) {
            if (items[i] instanceof Cup) cups[cupCount++] = (Cup)items[i];
            else                          lids[lidCount++] = (Lid)items[i];
        }
        for (int i = 1; i < cupCount; i++) { Cup k=cups[i]; int j=i-1; while(j>=0&&cups[j].getNumber()<k.getNumber()){cups[j+1]=cups[j];j--;} cups[j+1]=k; }
        for (int i = 1; i < lidCount; i++) { Lid k=lids[i]; int j=i-1; while(j>=0&&lids[j].getNumber()<k.getNumber()){lids[j+1]=lids[j];j--;} lids[j+1]=k; }
        boolean[] lidUsed = new boolean[lidCount];
        itemCount = 0; int li = 0;
        for (int ci = 0; ci < cupCount; ci++) {
            Cup cup = cups[ci];
            while (li < lidCount && !lidUsed[li]) {
                boolean belongs = false;
                for (int k=0;k<cupCount;k++) if(cups[k].getNumber()==lids[li].getNumber()){belongs=true;break;}
                if (belongs) { li++; continue; }
                if (lids[li].getNumber() > cup.getNumber()) { items[itemCount++]=lids[li]; lidUsed[li]=true; li++; }
                else break;
            }
            items[itemCount++] = cup;
            for (int k=0;k<lidCount;k++) if(!lidUsed[k]&&lids[k].getNumber()==cup.getNumber()){items[itemCount++]=lids[k];lidUsed[k]=true;break;}
        }
        for (int k=0;k<lidCount;k++) if(!lidUsed[k]) items[itemCount++]=lids[k];
    }

    private void reverseItems() {
        for (int i = 0; i < itemCount/2; i++) {
            Object tmp = items[i]; items[i] = items[itemCount-1-i]; items[itemCount-1-i] = tmp;
        }
    }

    private void trimToFit() {
        while (stackHeight() > maxHeight && itemCount > 0) items[--itemCount] = null;
    }

    private void buildBackground() {
        int maxCupNum = (maxHeight + 1) / 2;
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
            tick.changeSize(TICK_H, RULER_W); tick.changeColor("white");
            tick.moveHorizontal((TOWER_X + bgWidthPx) - 70);
            tick.moveVertical((TOWER_TOP + hPx - cm * PX_PER_CM) - 15);
            rulerTicks[cm-1] = tick;
        }
    }

    private void showBackground() { background.makeVisible(); for (Rectangle t : rulerTicks) { if (t!=null) t.makeVisible(); } }
    private void hideAll() { for (int i=0;i<itemCount;i++) { if(items[i] instanceof Cup)((Cup)items[i]).makeInvisible(); else if(items[i] instanceof Lid)((Lid)items[i]).makeInvisible(); } }

    private void removeAtIndex(int idx) {
        if (idx < 0 || idx >= itemCount) return;
        if (items[idx] instanceof Cup) ((Cup)items[idx]).makeInvisible();
        else if (items[idx] instanceof Lid) ((Lid)items[idx]).makeInvisible();
        for (int i=idx; i<itemCount-1; i++) items[i]=items[i+1];
        items[--itemCount] = null;
    }

    private int findCup(int n) { for(int i=0;i<itemCount;i++) if(items[i] instanceof Cup&&((Cup)items[i]).getNumber()==n) return i; return -1; }
    private int findLid(int n) { for(int i=0;i<itemCount;i++) if(items[i] instanceof Lid&&((Lid)items[i]).getNumber()==n) return i; return -1; }
    private int findItem(String[] id) { int n=Integer.parseInt(id[1]); if("cup".equals(id[0])) return findCup(n); if("lid".equals(id[0])) return findLid(n); return -1; }
    private String typeOf(int i) { return (items[i] instanceof Cup) ? "cup" : "lid"; }
    private int numberOf(int i) { return (items[i] instanceof Cup) ? ((Cup)items[i]).getNumber() : ((Lid)items[i]).getNumber(); }
    private int lastCupIndex() { for(int i=itemCount-1;i>=0;i--) if(items[i] instanceof Cup) return i; return -1; }
    private int lastLidIndex() { for(int i=itemCount-1;i>=0;i--) if(items[i] instanceof Lid) return i; return -1; }
    private boolean fitsOnCanvas() { return (maxHeight*PX_PER_CM+TOWER_TOP)<=600 && (bgWidthPx+TOWER_X+RULER_W)<=600; }
    private void fail(String msg) { lastOk=false; if(visible) JOptionPane.showMessageDialog(null,msg,"Simulador de Torre",JOptionPane.WARNING_MESSAGE); }
}
