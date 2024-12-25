import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * The PurgerCell class represents a cell in a simulation with the ability to eliminate threats, extending from NonImmuneCell.
 * It has a high probability of being alive (90%), a blue-violet color, and an initial health of 5.
 * The cell's death threshold is 5, and it can exterminate neighboring cells that are instances of either DiseaseCell or ChaosCell.
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class PurgerCell extends NonImmuneCell
{
    private static final Color COLOR = Color.BLACK;
    private static final int DEATH_THRESHOLD = 5;
    private static final double ALIVE_PROB = 0.8;
    private static final int INITIAL_HEALTH = 5;
    private static final int MAX_HEALTH = 10;
    
    /**
     * Create a new PurgerCell.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public PurgerCell(int row, int col)
    {
        super(row, col, PurgerCell.COLOR, PurgerCell.ALIVE_PROB, PurgerCell.INITIAL_HEALTH);
    }  
    
    @Override
    /**
     * Returns the death threshold for PurgerCell cells, dependent on the age of the cell.
     * @return The death threshold.
     */
    protected int getDeathThreshold()
    {  
        int deathThreshold = PurgerCell.DEATH_THRESHOLD;
        return deathThreshold;
    }

    /**
     * Resets the color of this cell to its default setting.
     */
    private void resetColor()
    {
        this.setColor(PurgerCell.COLOR);
    }

    /**
     * Exterminates/kills all of the neighbours of this cell that are currently alive and are "DiseaseCell" or "ChaosCell" cells.
     */
    private void exterminateThreats()
    {   
        // Kill all threats
        for (Object cell: getLivingNeighbours())
        {   
            if (cell instanceof DiseaseCell)
            {
                DiseaseCell threat = (DiseaseCell) cell;
                threat.takeDamage(threat.getHealth());
                super.increaseHealth(PurgerCell.MAX_HEALTH); // Increase resistance to Disease (in the form of additional health).
            }
            else if (cell instanceof ChaosCell)
            {
                ChaosCell threat = (ChaosCell) cell;
                threat.takeDamage(threat.getHealth());
                super.increaseHealth(PurgerCell.MAX_HEALTH); // Reward for killing ChaosCell cells (for maintaining stability in the environment).

            }
        }
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

        // Exterminate all living threats (neighbours that are "threats")
        exterminateThreats();
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
     * Performs the behaviour of PurgerCell cells.
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
            super.handleDiseaseState(this, (cell) -> {((PurgerCell) cell).resetColor();});
            super.act();

            performAliveBehaviour();
        }
    }

}
