import java.util.Random;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.stream.Stream;

/**
 * These cells have a light pink color and exhibit chaotic, non-deterministic behavior influenced by random chance when alive.
 * The behavior includes self-destruction, reviving dead neighbours, exterminating living neighbours, and normal functioning.
 * These actions occur based on a predefined probability distribution.
 * Additionally, the cell may revive itself when dead, depending on a defined probability. 
 * The implementation involves handling disease-induced color resets and performing its defined rule sets based on whether
 * it is alive or dead.
 * 
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class ChaosCell extends NonImmuneCell
{
    private static final Color COLOR = Color.LIGHTPINK;
    private static final int DEATH_THRESHOLD = 5;
    private static final double ALIVE_PROB = 0.25;
    private static final int INITIAL_HEALTH = 5;
    private static final Random rand = new Random();

    /**
     * Create a new ChaosCell.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public ChaosCell(int row, int col)
    {
        super(row, col, ChaosCell.COLOR, ChaosCell.ALIVE_PROB, ChaosCell.INITIAL_HEALTH);
    }

    @Override
    /**
     * Returns the death threshold for ChaosCell cells.
     * @return The death threshold.
     */
    protected int getDeathThreshold()
    {  
        int deathThreshold = ChaosCell.DEATH_THRESHOLD;

        // Infected with a disease.
        if (isInfected())
        {
            deathThreshold -= 1;
        }
        return deathThreshold;
    }

    /**
     * Revives all of the neighbours of this cell that are currently dead.
     */
    private void reviveNeighbours()
    {
        // Get all dead neighbours
        List<NonImmuneCell> deadNeighbours = getAllNeighbours()
                                            .stream()
                                            .filter(cell -> (cell instanceof NonImmuneCell) && !cell.isAlive())
                                            .map(cell -> (NonImmuneCell) cell)
                                            .toList();
        // Revive all dead neighbours                    
        for (NonImmuneCell neighbour: deadNeighbours)
        {   
            neighbour.setNextState(true);
        }
    }

    /**
     * Exterminates/kills all of the neighbours of this cell that are currently alive.
     */
    private void exterminateNeighbours()
    {
        // Get and exterminate all living neighbours.
        for (Object neighbour: getLivingNeighbours())
        {  
            if (neighbour instanceof NonImmuneCell)
            {
                NonImmuneCell nonImmuneCell = (NonImmuneCell) neighbour;
                nonImmuneCell.takeDamage(nonImmuneCell.getHealth());
            }
        }
    }
    
    /**
     * Resets the color of this cell to its default setting.
     */
    private void resetColor()
    {
        this.setColor(ChaosCell.COLOR);
    }

    @Override
    /**
     * Performs the behaviour of this cell when alive.
     */
    protected void performAliveBehaviour()
    {
        int randNum = ChaosCell.rand.nextInt(100);
        int numNeighbours = getLivingNeighbours().size();
        
        // 10% chance of self-destructing
        if (0 <= randNum && randNum < 10)
        {
            super.setNextState(false);
        }
        // 20% chance of reviving all of its neighbours, as long as they can be infected with disease.
        else if (10 <= randNum && randNum < 30)
        {   
            reviveNeighbours();
            super.assignNextState(numNeighbours, this.getDeathThreshold());
        }
        // 20% chance of killing all of its neighbours, as long as they can be infected with disease.
        else if (30 <= randNum && randNum < 50)
        {   
            exterminateNeighbours();
            super.assignNextState(numNeighbours, this.getDeathThreshold());
        }
        // 50% chance of behaving as normal (doing nothing extra)
        else
        {
            // Assign next state, dies if exceeding the death threshold.
            super.assignNextState(numNeighbours, this.getDeathThreshold()); 
        }
    }

    @Override
    /**
     * Performs the behaviour of this cell when dead.
     */
    protected void performDeadBehaviour()
    {
        int randNum = ChaosCell.rand.nextInt(100);
        int numNeighbours = getLivingNeighbours().size();
        
        // 25% chance of reviving itself
        if (0 <= randNum && randNum < 25)
        {
            super.setNextState(true);
            // Reset health
            while( getHealth() < ChaosCell.INITIAL_HEALTH)
            {
                increaseHealth(ChaosCell.INITIAL_HEALTH);
            }
        }
    }

    @Override
    /**
     * Performs the behaviour of ChaosCell cells.
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
            super.handleDiseaseState(this, (cell) -> {((ChaosCell) cell).resetColor();});
            super.act();

            performAliveBehaviour();
        }
    }
}
