import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Representa una taza en el simulador de torre de apilamiento.
 *
 * Dimensiones:
 *   Altura exterior: (2*i - 1) cm
 *   Ancho exterior:   2*i cm
 *   Grosor pared:     0.5 cm (5 px)
 *
 * Esto garantiza que la copa i quepa exactamente dentro de la copa i+1,
 * pues el ancho interior de i+1 es (2*(i+1) - 1) cm > 2*i cm = ancho copa i.
 *
 * @author AndresMora-DanielCamacho
 * @version 3.0
 */
public class Cup {

    /** Píxeles por centímetro. */
    public static final int PX_PER_CM = 10;

    /** Grosor de pared en píxeles (1 cm). */
    public static final int WALL = PX_PER_CM;

    private static final String[] COLORS = {
        "red", "blue", "green", "magenta", "yellow",
        "orange", "cyan", "pink", "darkGreen", "purple",
        "brown", "teal", "coral"
    };

    private int number;
    private String color;
    private boolean isVisible;
    private boolean upsideDown;
    private int xPosition;
    private int yPosition;

    /**
     * Crea una taza con el número dado, orientación normal (U).
     * @param number número de la taza (>= 1)
     */
    public Cup(int number) {
        this.number     = number;
        this.color      = COLORS[(number - 1) % COLORS.length];
        this.isVisible  = false;
        this.upsideDown = false;
        this.xPosition  = -2000;
        this.yPosition  = -2000;
    }

    /**
     * Voltea la orientación de la copa (U ↔ ∩).
     */
    public void flip() {
        upsideDown = !upsideDown;
        if (isVisible) { erase(); draw(); }
    }

    /** @return true si la copa está boca abajo */
    public boolean isUpsideDown() { return upsideDown; }

    /** @return número de la taza */
    public int getNumber() { return number; }

    /**
     * Altura de la taza en centímetros: i cm.
     * @return altura en cm
     */
    public int getHeightCm() { return 2 * number - 1; }

    /**
     * Ancho exterior de la taza en centímetros: 2*i.
     * @return ancho en cm
     */
    public int getWidthCm() { return 2 * number; }

    /** @return altura en píxeles */
    public int getHeightPx() { return getHeightCm() * PX_PER_CM; }

    /** @return ancho exterior en píxeles */
    public int getWidthPx() { return getWidthCm() * PX_PER_CM; }

    /** @return color de la taza */
    public String getColor() { return color; }

    /**
     * Mueve la copa a la posición dada (esquina superior-izquierda exterior).
     * @param x posición x en píxeles
     * @param y posición y en píxeles
     */
    public void moveTo(int x, int y) {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /** Hace visible la copa. */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /** Hace invisible la copa. */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Forma U normal: paredes + base abajo, abierto arriba.
     * Forma ∩ invertida: paredes + base arriba, abierto abajo.
     */
    private Path2D buildShape() {
        int x = xPosition;
        int y = yPosition;
        int w = getWidthPx();
        int h = getHeightPx();

        Path2D.Float path = new Path2D.Float();
        if (!upsideDown) {
            // Forma U: base abajo
            path.moveTo(x,         y);
            path.lineTo(x,         y + h);
            path.lineTo(x + w,     y + h);
            path.lineTo(x + w,     y);
            path.lineTo(x + w - WALL, y);
            path.lineTo(x + w - WALL, y + h - WALL);
            path.lineTo(x + WALL,     y + h - WALL);
            path.lineTo(x + WALL,     y);
        } else {
            // Forma ∩: base arriba
            path.moveTo(x,         y + h);
            path.lineTo(x,         y);
            path.lineTo(x + w,     y);
            path.lineTo(x + w,     y + h);
            path.lineTo(x + w - WALL, y + h);
            path.lineTo(x + w - WALL, y + WALL);
            path.lineTo(x + WALL,     y + WALL);
            path.lineTo(x + WALL,     y + h);
        }
        path.closePath();
        return path;
    }

    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, buildShape());
            canvas.wait(10);
        }
    }

    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
