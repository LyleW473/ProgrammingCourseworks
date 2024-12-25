import java.util.Random;
import javafx.scene.paint.Color;

/**
 * The CleansingCell class is a type of cell in a simulation that extends from NonImmuneCell. 
 * These cells have a blue color.
 * The cell's behavior includes handling disease-induced color resets, evaluating if its death threshold 
 * has been exceeded and cleansing themselves and their living neighbours from "Disease".
 * 
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class CleansingCell extends NonImmuneCell
{
    private static final Color COLOR = Color.BLUE;
    private static final int DEATH_THRESHOLD = 4;
    private static final double ALIVE_PROB = 0.03;
    private static final int INITIAL_HEALTH = 4;
    private static final Random rand = new Random();

    /**
     * Create a new CleansingCell.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public CleansingCell(int row, int col)
    {
        super(row, col, CleansingCell.COLOR, CleansingCell.ALIVE_PROB, CleansingCell.INITIAL_HEALTH);
    }

    @Override
    /**
     * Returns the death threshold for CleansingCell cells.
     * @return The death threshold.
     */
    protected int getDeathThreshold()
    {  
        int deathThreshold = CleansingCell.DEATH_THRESHOLD;

        // Infected with a disease.
        if (isInfected())
        {
            deathThreshold -= 1;
        }
        return deathThreshold;
    }

    /**
     * Cleanses all living neighbours and itself from "Disease".
     */
    private void cleanse()
    {      
        // Cleanse this cell from the disease
        super.setDisease(null);

        // Cleanse all neighbours from the disease
        for (Object cell: getLivingNeighbours())
        {   
            if (!(cell instanceof NonImmuneCell)) // Not a NonImmuneCell (i.e., is not infectable with a disease)
            {
                continue;
            }
            NonImmuneCell neighbour = (NonImmuneCell) cell;
            neighbour.setDisease(null);
        }
    }
    
    /**
     * Resets the color of this cell to its default setting.
     */
    private void resetColor()
    {
        this.setColor(CleansingCell.COLOR);
    }

    @Override
    /**
     * Performs the behaviour of this cell when alive.
     */
    protected void performAliveBehaviour()
    {
        int numNeighbours = getLivingNeighbours().size();

        // Assign next state, dies if exceeding the death threshold.
        super.assignNextState(numNeighbours, this.getDeathThreshold());

        cleanse(); // Cleanse itself and its neighbours from any disease.
    }

    @Override
    /**
     * Performs the behaviour of this cell when dead.
     */
    protected void performDeadBehaviour()
    {
        // Do nothing
    }

    @Override
    /**
     * Performs the behaviour of CleansingCell cells.
     */
    public void act() 
    {
        if (!(super.isAlive()))
        {
            setDeadColor();
            setDisease(null);
            performDeadBehaviour();
            return;
        }
        // Is alive
        else
        {
            // Handle diseased state
            super.handleDiseaseState(this, (cell) -> {((CleansingCell) cell).resetColor();});
            super.act();

            performAliveBehaviour();
        }
    }
}
