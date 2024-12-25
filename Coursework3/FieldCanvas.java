import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color; 

/**
 * Provide a graphical view of the field. This is a custom node for the user interface. 
 *
 * @author Jeffery Raphael
 * @version 2024.02.03
 */

public class FieldCanvas extends Canvas {

    private static final int GRID_VIEW_SCALING_FACTOR = 6;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private int width, height;
    private int xScale, yScale;
    GraphicsContext gc;
    
    private FieldStats stats;
    
    /**
     * Create a new FieldView component.
     */
    public FieldCanvas(int height, int width) {
        super(height, width);
        gc = getGraphicsContext2D();
        this.height = height;
        this.width = width;
        this.stats = new FieldStats();
    }
    
    /**
     * Sets the x and y scale for the FieldCanvas.
     */
    public void setScale(int gridHeight, int gridWidth) {
        xScale = width / gridWidth;
        yScale = height / gridHeight;
    
        if (xScale < 1)
            xScale = GRID_VIEW_SCALING_FACTOR;
    
        if (yScale < 1)
            yScale = GRID_VIEW_SCALING_FACTOR;
    }
  
    /**
     * Paint a rectangle of the given color on the canvas
     */
    private void drawMark(int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
    }

    /**
     * Updates the stats of the field and the canvas itself, drawing the cells and stats onto the screen.
     * @param Field The field to "update" and display.
     * @return A string containing the population details (stats) for it to be drawn on the screen by the SimulatorView.
     */
    public String update(Field field)
    {
        this.stats.reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Cell cell = field.getObjectAt(row, col);
                if (cell != null && cell.isAlive()) 
                {
                    this.stats.incrementCount(cell.getClass());
                    this.drawMark(col, row, cell.getColor());
                }
                else 
                {
                    this.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        this.stats.countFinished();
        return this.stats.getPopulationDetails(field);
    }
}
