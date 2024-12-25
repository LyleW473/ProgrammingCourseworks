import javafx.scene.paint.Color; 
import java.util.List;

/**
 * The HealthCell class is an abstract class that extends Cell and represents cells in a simulation with health. 
 * It introduces the concept of health to the cells, which can be influenced by various factors in the simulation. 
 * Key functionalities include defining a death threshold, performing the behavior of health cells, 
 * and providing methods to access and modify the health of the cell. 
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public abstract class HealthCell extends Cell
{
    private int health;

    /**
     * Create a new HealthCell, a cell with health.
     *
     * @param row The row index of the field that this cell will be placed in.
     * @param col The column index of the field that this cell will be placed in.
     * @param color The color of this cell.
     * @param aliveProbability The probability that this cell will be alive when being created.
     * @param initialHealth The initial health of this cell.
     */
    public HealthCell(int row, int col, Color color, double aliveProbability, int initialHealth)
    {
        super(row, col, color, aliveProbability);
        this.health = initialHealth;
    }

    /**
     * Return the defined death threshold for this HealthCell.
     * @return the defined death threshold for this HealthCell (should be implemented for subclasses).
     */
    abstract protected int getDeathThreshold();


    /**
     * Return the current health of this cell.
     * @return The health of this cell, i.e., an integer greater than 0.
     */
    protected int getHealth()
    {
        return this.health;
    }

    /**
     * Increases the current health of this cell by one, as long as it is below the passed in health limit.
     * @param healthLimit The health limit defined for the cell calling this method.
     */
    protected void increaseHealth(int healthLimit)
    {   
        if (this.health < healthLimit)
        {
            this.health ++;
        }
    }

    /**
     * Decreases the amount of health this cell has by the passed in damage number.
     * @param damage The damage number to decrease the health by.
     */
    protected void takeDamage(int damage)
    {   
        this.health -= damage;

        // Cap health at minimum of 0
        if (this.health < 0)
        {
            this.health = 0;
        }
    }

    /**
     * Returns a boolean indicating whether or not the cell is currently alive.
     * @return The boolean indicating whether or not the cell is alive.
     */
    protected boolean isAlive()
    {
        return (super.isAlive() == true && this.health > 0);
    }
}
