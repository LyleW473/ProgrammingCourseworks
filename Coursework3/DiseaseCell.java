import javafx.scene.paint.Color;
import java.util.ArrayList;


/**
 * The DiseaseCell class represents a cell in a simulation capable of spreading disease.
 * The presence of a “DiseaseCell” will cause adjacent cells (living neighbours of this cell) to become sick with “Disease”.
 * It extends from HealthCell and has an initial health of 5, a 10% probability of being alive, and a red color. 
 * The cell can infect its living neighbors with a disease, updating the list of infected cells, 
 * and has a maximum age of 250, after which it dies. 
 * The implementation includes methods for infection, updating infected cells, determining the bonus damage of the disease, 
 * tracking the cell's current age.
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

 public class DiseaseCell extends HealthCell
{
    private static final int INITIAL_HEALTH = 5;
    private static final double ALIVE_PROB = 0.9;
    private static final Color COLOR = Color.RED;
    private static final int DEATH_THRESHOLD = 8;
    private static final int MAXIMUM_AGE = 250;
    public static final int MAXIMUM_NUM_CELLS = 80; // Maximum number of DiseaseCell instances
    private static int numCellsCreated = 0; // Number of DiseaseCell instances created in the current simulation / environment.

    private ArrayList<HealthCell> infectedCells = new ArrayList<HealthCell>();
    private int age;
    
    /**
     * Create a new DiseaseCell, a cell that can spread disease.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     */
    public DiseaseCell(int row, int col)
    {
        super(row, col, DiseaseCell.COLOR, DiseaseCell.ALIVE_PROB, DiseaseCell.INITIAL_HEALTH);
        DiseaseCell.numCellsCreated ++; 
    }

    /**
     * Returns a boolean indicating whether the maximum number of DiseaseCell cells have been created or not.
     * @return A boolean indicating whether the maximum number of DiseaseCell cells have been created or not.
     */
    public static boolean generatedMaximumCells()
    {
        return DiseaseCell.numCellsCreated == DiseaseCell.MAXIMUM_NUM_CELLS;
    }

    /**
     * Infects the living neighbours with the disease.
     */
    private void infectNeighbours()
    {
        for (Object cell: getLivingNeighbours())
        {   
            if ((cell instanceof NonImmuneCell)) // Is a neighbour that can be infected with a disease.
            {
                NonImmuneCell neighbour = (NonImmuneCell) cell;
                if (neighbour.isInfected() == false) // Is not already infected with a disease.
                {
                    // Infect them with a disease.
                    neighbour.setDisease(new Disease(getBonusDamage()));

                    // Add this cell to the list of cells this DiseaseCell has infected.
                    this.infectedCells.add(neighbour);
                }
            }
        }
    }

    /**
     * Updates the list of cells that this cell currently has infected with its disease.
     */
    private void updateInfectedCellsList()
    {
        ArrayList<HealthCell> newInfectedCells = new ArrayList<HealthCell>();
        for (HealthCell cell: this.infectedCells)
        {   
            // Cell is alive and infected with the disease
            if (cell.getHealth() > 0)
            {
                newInfectedCells.add(cell);
            }
        }
        // Update list of infected cells
        this.infectedCells = newInfectedCells;
    }
    
    /**
     * Return the bonus damage of the disease that this cell inflicts onto other cells.
     * Dependent on the number of cells that this DiseaseCell currently has infected with its "Disease".
     * Returns 1 if requirement is met, 0 if not.
     * @return The bonus damage of this cell's disease.
     */
    private int getBonusDamage()
    {
        return (this.infectedCells.size() >= 3) ? 1 : 0;
    }

    /**
     * Return the current age of this DiseaseCell.
     */
    private int getAge()
    {
        return this.age;
    }

    /**
     * Checks if this cell has exceeded the maximum defined age of DiseaseCell cells.
     * Lives on if it has not reached the maximum age.
     * Dies if it has exceeded the defined maximum age.
     */
    private void mature()
    {
        if (getAge() > DiseaseCell.MAXIMUM_AGE)
        {
            setNextState(false); // Die as this cell has reached its maximum age.
        }
        else
        {
            this.age ++;
            setNextState(true); // Live on to the next generation.
        }
    }

    @Override
    /**
     * Returns the death threshold for DiseaseCell cells.
     * @return The death threshold.
     */
    protected int getDeathThreshold()
    {  
        int deathThreshold = DiseaseCell.DEATH_THRESHOLD;
        return deathThreshold;
    }

    @Override
    /**
     * Performs the behaviour of this cell when alive.
     */
    protected void performAliveBehaviour()
    {
        infectNeighbours();
        updateInfectedCellsList(); 
        mature();
    }

    @Override
    /**
     * Performs the behaviour of this cell when dead.
     */
    protected void performDeadBehaviour()
    {
        super.setDeadColor();
    }

    @Override
    /**
     * Performs the behaviour of DiseaseCell cells/objects.
     */
    public void act()
    {
        if (super.isAlive())
        {
            performAliveBehaviour();
        }
        else 
        {
            performDeadBehaviour();
        }
    }
}
