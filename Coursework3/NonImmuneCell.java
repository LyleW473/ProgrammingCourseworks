import java.util.List;
import javafx.scene.paint.Color; 

/**
 * The NonImmuneCell class is an abstract class representing cells in a simulation that have health and can be infected with a disease. 
 * It extends from HealthCell. Key functionalities include handling the diseased state by resetting colors and updating the cell's state based on disease damage. 
 * The class also provides methods for checking if a cell is infected, setting and getting the disease, and becoming infected based on the status of neighbouring cells,
 * allowing for "Disease" to spread amongst NonImmuneCell cells.
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public abstract class NonImmuneCell extends HealthCell
{
    private static final Color INFECTED_COLOR = Color.YELLOW; // Color for cells in the "diseased" state.
    private Disease disease; // The disease that this cell is infected with. Null if it isn't inflicted with one.

    /**
     * Create a new NonImmuneCell, a cell with health that can be infected with disease.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     * @param color The color of this cell.
     * @param aliveProbability The probability that this cell will be alive when being created.
     * @param initialHealth The initial health of this cell.
     */
    public NonImmuneCell(int row, int col, Color color, double aliveProbability, int initialHealth)
    {
        super(row, col, color, aliveProbability, initialHealth);
        this.disease = null;
    }

    /**
     * Handles the diseased state, setting the cell to the correct color so that its current state is represented correctly.
     * @param cell Cell to reset its color back to its original state.
     * @param colorResetter A functional interface that invokes a customised method for resetting the color of the passed in NonImmuneCell back to its default state.
     */
    protected void handleDiseaseState(NonImmuneCell cell, ColorResetter colorResetter)
    {      
        // Is infected
        if (isInfected())
        {
            this.setColor(NonImmuneCell.INFECTED_COLOR);
        }
        else 
        {
            colorResetter.resetColor(cell); // Go back to "default" color state.
        }
    }

    /**
     * Returns a boolean indicating whether the cell has been infected by a disease.
     * @return The boolean indicating whether or not the cell is currently infected.
     */
    protected boolean isInfected()
    {
        return (this.disease != null);
    }

    /**
     * Sets the disease that this cell is inflicted with.
     * @param disease The disease that this cell has been inflicted with.
     */
    protected void setDisease(Disease disease)
    {
        this.disease = disease;
    }

    /**
     * Returns the disease that is infecting this cell.
     * @return The disease infecting this cell.
     */
    private Disease getDisease()
    {
        int bonusDamage = this.disease.getBonusDamage();
        return new Disease(bonusDamage);
    }

    /**
     * Checks if any of its living neighbours is currently infected with disease. If they are, this cell should become infected.
     */
    private void becomeInfected()
    {   
        // Get all of the living neighbours that are a NonImmuneCell and infected with a disease.
        List<NonImmuneCell> infectedNeighbours = this.getLivingNeighbours()
                                                    .stream()
                                                    .filter(cell -> (cell instanceof NonImmuneCell)) // Is a NonImmuneCell
                                                    .map(cell -> (NonImmuneCell) cell) // Cast to NonImmuneCell
                                                    .filter(cell -> (cell.isInfected())) // Is infected
                                                    .toList(); // Convert to List<NonImmuneCell>
        
        // If any of this cell's neighbours are infected with a disease, the disease should spread to this cell.
        if (infectedNeighbours.size() > 0)
        {
            this.setDisease(infectedNeighbours.get(0).getDisease()); // Just choose the first cell's disease
        }

    }

    @Override
    /**
     * Indicate that the cell will be alive or dead in the next generation.
     * Additionally, this cell cannot have a disease if it is dead.
     * @param value The boolean to assign to the attribute "nextAlive".
     */
    protected void setNextState(boolean value) {
        super.setNextState(value);
        if (value == false)
        {
            this.disease = null;
        }
    }

    @Override
    /**
     * Performs the behaviour of all NonImmuneCell objects.
     */
    public void act()
    {      
        if (isInfected())
        {
            // Take disease damage if infected.
            this.takeDamage(this.disease.getDamage());
        }
        else
        {
            // Become infected if any of this cell's neighbours are infected
            this.becomeInfected();
        }
    }
}
