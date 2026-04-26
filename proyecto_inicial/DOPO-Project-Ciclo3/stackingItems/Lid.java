import java.awt.*;

/**
 * Representa una tapa en el simulador de torre de apilamiento.
 * Cada tapa tiene el mismo ancho que su taza correspondiente y mide 1 cm de alto.
 *
 * @author AndresMora-DanielCamacho
 * @version 3.0
 */
public class Lid {

    /** Altura fija de cada tapa en centímetros. */
    public static final int HEIGHT_CM = 1;

    private static final String[] COLORS = {
        "red", "blue", "green", "magenta", "yellow",
        "orange", "cyan", "pink", "darkGreen", "purple",
        "brown", "teal", "coral"
    };

    private int number;
    private String color;
    private boolean isVisible;
    private int xPosition;
    private int yPosition;

    /**
     * Crea una tapa con el número dado.
     * @param number número de la tapa (>= 1)
     */
    public Lid(int number) {
        this.number    = number;
        this.isVisible = false;
        this.xPosition = -2000;
        this.yPosition = -2000;
        this.color     = COLORS[(number - 1) % COLORS.length];
    }

    /** @return número de la tapa */
    public int getNumber() { return number; }

    /** @return altura en cm (siempre 1) */
    public int getHeightCm() { return HEIGHT_CM; }

    /**
     * Ancho de la tapa en cm: igual que la taza del mismo número (2*i cm).
     * @return ancho en cm
     */
    public int getWidthCm() { return 2 * number; }

    /**
     * Ancho de la tapa en píxeles.
     * @return ancho en píxeles
     */
    public int getWidthPx() { return getWidthCm() * Cup.PX_PER_CM; }

    /** @return color de la tapa */
    public String getColor() { return color; }

    /**
     * Mueve la tapa a la posición dada.
     * @param x posición x en píxeles
     * @param y posición y en píxeles
     */
    public void moveTo(int x, int y) {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /** Hace visible la tapa. */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /** Hace invisible la tapa. */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, getWidthPx(), HEIGHT_CM * Cup.PX_PER_CM));
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
