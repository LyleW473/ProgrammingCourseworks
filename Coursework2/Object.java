/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 * 
 * 
 *  This Object class is used to encapsulate common attributes and methods that items within my game may have.
 *  This class defines "name" and "description" attributes and getter methods for each attribute.
 *  For example, artifacts all have a name and a description, hence, the Artifact class inherits this class.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Object
{
    private String name; // Name of this object
    private String description; // Description for this object

    /**
     * Constructor for Object class, initialises each object with a name and description.
     * @param name The name assigned to this object.
     * @param description The description assigned to this object.
     */ 
    public Object(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * @return The name of this object
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return A description of this object
     */
    public String getDescription()
    {
        return description;
    }
}