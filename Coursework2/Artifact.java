/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 *  
 * 
 *  This class defines a collectible item that players should find and collect,
 *  in order to win the game. Each artifact has an associated weight, name and 
 *  description.
 *  
 *  This class also contains static methods for the creation and spawning of artifact 
 *  objects as well as instance methods for retrieving information about each Artifact 
 *  object.
 *  
 *  Artifacts are spawned from the Game class by creating the artifacts' details and 
 *  then spawning the artifacts in randomly selected rooms.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.HashMap;

public class Artifact extends Object // Inherit from the Object class
{
    private static HashMap<String, Artifact> artifactsDetails = new HashMap<String, Artifact>(); // Maps each artifact name to an Artifact object.
    private static int numArtifactsSpawned = 0; // Keeps track of the number of artifacts spawned into the world.
    private double weight; // Weight assigned to this artifact.

    /**
     * Constructor for Artifact class. Initialises the Artifact object with a passed-in name, description and weight.
     * @param name The name of the artifact
     * @param description A description of the artifact 
     * @param weight The weight of the artifact
     */
    public Artifact(String name, String description, double weight)
    {
        // Initialise all attributes
        super(name, description); // Inherits from the Object class
        this.weight = weight;

        // Increment the number of artifacts spawned into the game
        Artifact.numArtifactsSpawned ++;
    }

    /**
     * Initialises a static HashMap<String, Artifact>, mapping each artifact name to an Artifact object.
     * - The Artifact object will have the "details" e.g., the description and weight associated with each artifact name.
     * - The Artifact object can then be retrieved using the artifact name through Artifact.artifactDetails.
     */
    public static void createArtifactsDetails()
    {
        // Create artifacts (Artifact objects), giving each artifact a name, description and a weight.
        String artifactName1 = "Golden Trophy";
        String goldenTrophyDescription = "A golden trophy with a shiny finish.";
        double goldenTrophyWeight = 3.0;
        Artifact goldenTrophy = new Artifact(artifactName1, goldenTrophyDescription, goldenTrophyWeight);

        String artifactName2 = "Expensive Painting";
        String expensivePaintingDescription = "A painting worth a large fortune.";
        double expensivePaintingWeight = 2.0;
        Artifact expensivePainting = new Artifact(artifactName2, expensivePaintingDescription, expensivePaintingWeight);

        String artifactName3 = "Treasure Chest";
        String treasureChestDescription = "A treasure chest containing precious gems!";
        double treasureChestWeight = 5.0;
        Artifact treasureChest = new Artifact(artifactName3, treasureChestDescription, treasureChestWeight);

        // Create mappings for each artifact
        Artifact.artifactsDetails.put(artifactName1, goldenTrophy);
        Artifact.artifactsDetails.put(artifactName2, expensivePainting);
        Artifact.artifactsDetails.put(artifactName3, treasureChest);
    }

    /**
    * @return A HashMap<String, Artifact> mapping the names of each artifact to the Artifact object created.
    */
    public static HashMap<String, Artifact> getArtifactDetails()
    {
        return Artifact.artifactsDetails;
    }

    /**
    * @return The artifact with the corresponding artifact name, to be assigned to a random room.
    * @param nameOfArtifact The name of the artifact to spawn into the game world (i.e., to be assigned to a random room).
    */
    public static Artifact getArtifact(String nameOfArtifact)
    {   
        Artifact artifact = Artifact.artifactsDetails.get(nameOfArtifact);
        Artifact.artifactsDetails.remove(nameOfArtifact); // Remove from artifactDetails, meaning that this artifact cannot be spawned again
        return artifact;
    }

    /**
    * @return The total number of artifacts that have been created in the game / spawned.
    */
    public static int getNumArtifactsSpawned()
    {
        return Artifact.numArtifactsSpawned;
    }

    /**
    * @return The weight of this artifact
    */
    public double getWeight()
    {
        return weight;
    }

    /**
    * Prints the name, description, weight of this artifact and the itemNumber.
    @param itemNumber A number representing which item this is in the inventory, e.g., if it is the first item then its itemNumber would be 1.
    */
    public void printDetails(int itemNumber)
    {
        System.out.println("Name: " + this.getName() + " (" + itemNumber + ")");
        System.out.println("Description: " + this.getDescription());
        System.out.println("Artifact weight: " + this.getWeight());
        System.out.println();
    }
}