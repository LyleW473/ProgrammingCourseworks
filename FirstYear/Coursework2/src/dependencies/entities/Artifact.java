package dependencies.entities;

public class Artifact extends Object
{
    private double weight;
    
    public Artifact(String name, String description, double weight)
    {
        // Inherits from Object class
        super(name, description);
        this.weight = weight;
    }
    
    /**
    * @return the weight of this artifact
    */
    public double getWeight()
    {
        return weight;
    }
}
