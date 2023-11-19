package dependencies.entities;

public class Object
{
    private String name;
    private String description;

    public Object(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * @return the name of this object
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return a description of this object
     */
    public String getDescription()
    {
        return description;
    }
}
