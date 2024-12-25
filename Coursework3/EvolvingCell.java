import java.util.List;
import javafx.scene.paint.Color; 

/**
 *  The EvolvingCell class extends from NonImmuneCell and represents a type of cell in a simulation. 
 *  These cells have a life cycle with different color stages, can die if specific death thresholds are exceeded,
 *  and transitions between maturity levels as they age.
 *  There are 3 maturity levels; young, mature and elderly. Greater maturity levels reap benefits including higher
 *  death thresholds, behaving differently as time passes.
 *  Each maturity level has its own corresponding color. The color transitions and death thresholds are influenced 
 *  by the cell's age.
 *  
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class EvolvingCell extends NonImmuneCell
{
    private static final double ALIVE_PROB = 0.25;
    private static final Color[] COLORS = {Color.CYAN, Color.DEEPSKYBLUE, Color.PURPLE};
    private static final int MATURE_AGE = 25; // Age to be considered as a mature cell
    private static final int ELDERLY_AGE = 75; // Elderly cell age (also the max age of EvolvingCell)
    private static final int[] DEATH_THRESHOLDS = {2, 4, 7}; // The number of other cells that need to be adjacent to it for it to die.
    private static final int INITIAL_HEALTH = 2;

    private int generationsLived = 0;
    
    /**
     * Create a new EvolvingCell.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public EvolvingCell(int row, int col)
    {
        super(row, col, EvolvingCell.COLORS[0], EvolvingCell.ALIVE_PROB, EvolvingCell.INITIAL_HEALTH);
    }  

    /**
     * Returns a boolean indicating whether the cell is an elderly cell or not.
     * @return Boolean indicating whether the cell is an elderly cell or not.
     */
    private boolean isElderly()
    {
        return (this.generationsLived == EvolvingCell.ELDERLY_AGE);
    }

    /**
     * Resets the color of this cell to its default setting.
     */
    private void resetColor()
    {
        this.setColor(EvolvingCell.COLORS[0]);
        mature();
    }
    
    /**
     * "Matures" the cell, (turning it from a young cell to a mature cell or a mature cell to an elderly cell). Changes the color of the cell.
     */
    private void mature()
    {   
        if (0 <= this.generationsLived && this.generationsLived < EvolvingCell.MATURE_AGE)
        {
            this.setColor(EvolvingCell.COLORS[0]); // Young
        }
        else if (EvolvingCell.MATURE_AGE <= this.generationsLived && this.generationsLived < EvolvingCell.ELDERLY_AGE)
        {
            this.setColor(EvolvingCell.COLORS[1]); // Mature
        }
        else
        {
            this.setColor(EvolvingCell.COLORS[2]); // Elderly
        }
    }

    @Override
    /**
     * Returns the death threshold for EvolvingCell cells, dependent on the age of the cell.
     * @return The death threshold.
     */
    protected int getDeathThreshold()
    {  
        int deathThreshold;

        if (0 <= this.generationsLived && this.generationsLived < EvolvingCell.MATURE_AGE)
        {
            deathThreshold = EvolvingCell.DEATH_THRESHOLDS[0]; // Young
        }
        else if (EvolvingCell.MATURE_AGE <= this.generationsLived && this.generationsLived < EvolvingCell.ELDERLY_AGE)
        {
            deathThreshold = EvolvingCell.DEATH_THRESHOLDS[1]; // Mature
        }
        else
        {
            deathThreshold = EvolvingCell.DEATH_THRESHOLDS[2]; // Elderly
        }

        // Infected with a disease.
        if (isInfected())
        {
            deathThreshold -= 1;
        }
        return deathThreshold;
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

        // Not an elderly cell
        if (!isElderly())
        {   
            generationsLived ++; 
            
            // Only "mature" when not infected (as it changes colour of the cell)
            if (!isInfected())
            {
                // Check if the cell can mature
                mature();
            }
        }
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
     * Performs the behaviour of evolving cells.
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
            super.handleDiseaseState(this, (cell) -> {((EvolvingCell) cell).resetColor();});
            super.act();

            performAliveBehaviour();
        }
    }
}
