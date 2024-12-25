import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * The ChameleonCell class represents a type of cell in a simulation, extending from NonImmuneCell. 
 * These cells can change color randomly from a set of predefined colors. 
 * These ChameleonCells die if the defined death threshold is exceeded, and can be revived if a certain
 * number of ChameleonCells are near it.
 *  
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class ChameleonCell extends NonImmuneCell
{
    // Colors that the Chameleon cells can shift into.
    private static final ArrayList<Color> COLORS = new ArrayList<Color>(){{
                                                                        add(Color.CHARTREUSE);
                                                                        add(Color.LIME);
                                                                        add(Color.DARKGREEN);
                                                                        }}; 
    private static final int numColors = COLORS.size();
    private static final int NUM_REVIVAL_CELLS_REQUIRED = 2; // The number of chameleon cells that need to be near this cell for it to be revived.
    private static final double ALIVE_PROB = 0.5;
    private static final int INITIAL_HEALTH = 3;
    private static final Random rand = new Random();
    private static final int DEATH_THRESHOLD = 5;

    /**
     * Create a new ChameleonCell.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public ChameleonCell(int row, int col)
    {
        super(row, col, ChameleonCell.COLORS.get(0), ChameleonCell.ALIVE_PROB, ChameleonCell.INITIAL_HEALTH);
    }

    /**
     * Generates and returns a random color from the allowed colors for chameleon cells.
     * @return A random color from the allowed colors.
     */
    private Color generateRandomColor()
    {   
        int randIndex = ChameleonCell.rand.nextInt(ChameleonCell.numColors);
        return ChameleonCell.COLORS.get(randIndex);
    }

    /**
     * Shifts the color of this ChameleonCell to a different color (from the allowed colors).
     */
    private void shiftColor()
    {
        Color newColor = this.getColor();
        while (newColor.equals(this.getColor()))
        {
            newColor = generateRandomColor();
        }
        this.setColor(newColor);
    }

    @Override
    /**
     * Returns the death threshold for ChameleonCell cells.
     * @return The death threshold.
     */
    protected int getDeathThreshold()
    {  
        int deathThreshold = ChameleonCell.DEATH_THRESHOLD;
        
        // Infected with a disease.
        if (isInfected())
        {
            deathThreshold -= 1;
        }
        return deathThreshold;
    }

    /**
     * Attempts to revive this ChameleonCell if the requirement is met
     * Requirement: At least NUM_REVIVAL_CELLS_REQUIRED living neighbours.
     */
    private void attemptRevival()
    {
        int numChameleons = 0;
        for (Object neighbour: getLivingNeighbours())
        {   
            boolean isChameleon = (neighbour instanceof ChameleonCell);
            numChameleons += isChameleon ? 1: 0;

            // If the requirement has been met, revive the cell.
            if (numChameleons >= NUM_REVIVAL_CELLS_REQUIRED)
            {
                setNextState(true);
                
                // Reset health
                while( getHealth() < ChameleonCell.INITIAL_HEALTH)
                {
                    increaseHealth(ChameleonCell.INITIAL_HEALTH);
                }
                return;
            }
        }
    }

    /**
     * Resets the color of this cell to its default setting.
     */
    private void resetColor()
    {
        shiftColor();
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

        // Should not shift color when in a diseased state
        if (!isInfected())
        {
            shiftColor();
        }
    }

    @Override
    /**
     * Performs the behaviour of this cell when dead.
     */
    protected void performDeadBehaviour()
    {
        // If the cell is already dead, check whether it can be revived.
        attemptRevival();
    }

    @Override
    /**
    * Performs the behaviour of Chameleon cells.
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
            super.handleDiseaseState(this, (cell) -> {((ChameleonCell) cell).resetColor();});
            super.act();

            performAliveBehaviour();
        }
    }
}
