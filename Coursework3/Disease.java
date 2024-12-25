/**
 * A class encapsulating an entity that causes cells to behave "differently".
 * When a Cell is inflicted with a Disease, they change into a colour reserved
 * for Cells in a diseased state. Cells infected also periodically lose health,
 * eventually dying. Disease is a permanent effect and cannot be removed in 
 * a cell's life, unless they are revived.
 * The Disease deals bonus damage depending on the DiseaseCell that is inflicting
 * this disease.
 *
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class Disease 
{
    private static final int BASE_DMG = 1;
    private int damage;
    private int bonusDamage;

    /**
     * Create a new Disease, an "entity" that deals damage to cells.
     */
    public Disease(int bonusDamage)
    {
        this.damage = Disease.BASE_DMG + bonusDamage;
    }

    /**
     * Returns the damage that this disease deals to other cells.
     * @return The disease damage.
     */
    public int getDamage()
    {
        return this.damage;
    }

    /**
     * Returns the bonus damage added on top of the damage this disease deals to other cells.
     * @return The bonus disease damage.
     */
    public int getBonusDamage()
    {
        return this.bonusDamage;
    }
}
