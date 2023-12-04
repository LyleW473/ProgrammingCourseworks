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