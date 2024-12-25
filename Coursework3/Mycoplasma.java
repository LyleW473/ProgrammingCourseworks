import javafx.scene.paint.Color; 
import java.util.List;

/**
 * Simplest form of life.
 * Fun Fact: Mycoplasma are one of the simplest forms of life.  A type of
 * bacteria, they only have 500-1000 genes! For comparison, fruit flies have
 * about 14,000 genes.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2022.01.06
 */

public class Mycoplasma extends Cell
{
    private static final Color COLOR = Color.ORANGE; // Color of all Mycoplasma objects
    private static final double ALIVE_PROB = 0.995;

    /**
     * Create a new Mycoplasma.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public Mycoplasma(int row, int col) {
        super(row, col, Mycoplasma.COLOR, Mycoplasma.ALIVE_PROB);
    }

    
    @Override
    /**
     * Performs the behaviour of this cell when alive.
     */
    protected void performAliveBehaviour()
    {
        setNextState(true);
    }

    @Override
    /**
     * Performs the behaviour of this cell when dead.
     */
    protected void performDeadBehaviour()
    {
        setNextState(false);
    }
    
    @Override
    /**
     * Performs the behaviour of Mycoplasma cells/objects.
     */
    public void act() {

        // Find number of living neighbours.
        int numNeighbours = getLivingNeighbours().size();

        // Rule set:

        // Dies if there are fewer than 2 neighbours or more than 3 neighbours.
        if ((numNeighbours < 2) || numNeighbours > 3)
        {
            performDeadBehaviour();
        }
        // 1st case: Lives on if it is alive and has two live neighbours.
        // 2nd case: Lives on if it has 3 live neighbours (regardless of cell's current state).
        else if ((isAlive() && numNeighbours == 2) || numNeighbours == 3)
        {
            performAliveBehaviour();
        }
    }
}
