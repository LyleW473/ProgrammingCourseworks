import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * A class representing the shared characteristics of all forms of life
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2022.01.06
 */

public abstract class Cell 
{
    private boolean alive;    
    private boolean nextAlive; // The state of the cell in the next iteration
    private Color color = Color.WHITE;
    private List<Cell> neighbours;
    private static final Random rand = new Random();

    /**
     * Create a new cell at location in field.
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     * @param color The color of this cell.
     * @param aliveProbability The probability that this cell will be alive when being created.
     */
    public Cell(int row, int col, Color color, double aliveProbability) {
        alive = true;
        nextAlive = false;
        setColor(color);

        // Chance of setting this cell to dead.
        if (Cell.rand.nextDouble() > aliveProbability)
        {
            this.setDead();
        }
    }


    /**
     * Abstract methods for performing the alive and dead behaviour of all cells.
     */
    abstract protected void performAliveBehaviour();
    abstract protected void performDeadBehaviour();

    /**
     * Make this cell act - that is: the cell decides it's status in the
     * next generation. This method will make use of the performAliveBehaviour
     * and performDeadBehaviour methods.
     */
    abstract public void act();

    /**
     * Check whether the cell is alive or not.
     * @return true if the cell is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the cell is no longer alive.
     */
    protected void setDead() {
        alive = false;
    }

    /**
     * Checks if the cell should be alive next generation.
     * If the number of neighbours is less than the death threshold, then they survive till the next generation, otherwise they die.
     * @param numNeighbours The number of living neighbours currently adjacent to the cell.
     * @param deathThreshold The death threshold for this cell.
     */
    protected void assignNextState(int numNeighbours, int deathThreshold)
    {
        if (numNeighbours < deathThreshold)
        {
            setNextState(true); 
        }
        else 
        {
            setNextState(false);
        }
    }

    /**
     * Indicate that the cell will be alive or dead in the next generation.
     * @param value The boolean to assign to the attribute "nextAlive".
     */
    protected void setNextState(boolean value) {
        nextAlive = value;
    }

    /**
     * Changes the state of the cell.
     */
    public void updateState() {
        alive = nextAlive;
    }

    /**
     * Changes the color of the cell.
     * @param color The color to change the cell into.
     */
    protected void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the cell's color.
     * @return The current color of this cell.
     */
    protected Color getColor() {
        return this.color;
    }

    /**
     * Sets the color of this cell to the reserved color which represents cells being in a dead state.
     */
    protected void setDeadColor()
    {
        setColor(Color.WHITE);
    }
    
    /**
     * Sets the list of the neighbours of this cell to the passed in list.
     * @param neighbours A list containing the neighbours of this cell, alive or dead.
     */
    protected void setAllNeighbours(List<Cell> neighbours)
    {
        this.neighbours = neighbours;
    }

    /**
     * Returns a list containing Cell objects that are living neighbours of this cell.
     * @return A list containing the neighbours of this cell that are alive.
     */
    protected List<Cell> getLivingNeighbours()
    {
        return this.neighbours.stream().filter(cell -> cell.isAlive()).toList();
    }

    /**
     * Returns a list containing Cell objects that are neighbours of this cell.
     * @return A list containing the neighbours of this cell, alive or dead.
     */
    protected List<Cell> getAllNeighbours()
    {
        return neighbours;
    }
}
