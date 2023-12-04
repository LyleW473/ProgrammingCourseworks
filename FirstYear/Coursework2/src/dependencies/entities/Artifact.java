package dependencies.entities;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

import core.TextPrinter;

public class Artifact extends Object // Inherit from the Object class
{
    private static HashMap<String, Artifact> artifactsDetails = new HashMap<String, Artifact>(); // Maps each artifact name to an Artifact object
    private static final int NUM_ARTIFACTS = 3; // Number of artifacts to spawn in the world

    private double weight; // Weight assigned to this artifact

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
    }

    /**
     * Initialises a static HashMap<String, Artifact>, mapping each artifact name to an Artifact object
     * - The Artifact object will have the "details" e.g., the description and weight associated with each artifact name.
     * - The Artifact object can then be retrieved using the artifact name through Artifact.artifactDetails.
     */
    public static void createArtifactsDetails(TextPrinter textPrinter)
    {
        // Get lines consisting of "ArtifactName|ArtifactWeight" e.g., "Golden Trophy|3.0"
        ArrayList<String> lines = textPrinter.returnContentsList("dependencies/texts/artifact_details.txt");
        for (String line: lines)
        {  
            // Extract entities from each line into separate variables (name, description and weight)
            String[] detailsPerLine = line.split("@"); // Split using the "@" delimiter
            String name = detailsPerLine[0];
            String description = detailsPerLine[1];
            Double weight = Double.parseDouble(detailsPerLine[2]);
            Artifact createdArtifact = new Artifact(name, description, weight);
            
            // Create mapping
            Artifact.artifactsDetails.put(name, createdArtifact);
        }
    }

    /**
    * @return An ArrayList<String> containing the names of all the artifacts that can be created / instantiated
    */
    public static ArrayList<String> getAllArtifactNames()
    {
        return new ArrayList<String>(Artifact.artifactsDetails.keySet());
    }

    /**
    * @return The artifact with the corresponding artifact name, to be assigned to a random room
    * @param nameOfArtifact The name of the artifact to spawn into the game world (i.e., to be assigned to a random room).
    */
    public static Artifact getArtifact(String nameOfArtifact)
    {   
        Artifact artifact = Artifact.artifactsDetails.get(nameOfArtifact);
        Artifact.artifactsDetails.remove(nameOfArtifact); // Remove from artifactDetails, meaning that this artifact cannot be spawned again
        return artifact;
    }

    /**
     * Randomly selects "Artifact.NUM_ARTIFACTS" rooms to spawn artifacts into.
     * - Selects random rooms that can have artifacts spawning in them and then randomly assigns artifacts to each room
     */
    public static void spawnArtifacts()
    {
        ArrayList<Room> artifactSpawnableRooms = Room.getArtifactSpawnableRooms();

        // Generate random indexes between 0 (inclusive) and the number of artifact spawnable rooms (exclusive) there are
        Random randomGen = new Random();
        ArrayList<Integer> roomIndexes = new ArrayList<Integer>(); // The same room can have multiple artifacts spawning in them
        int generatedIndex;
        int numArtifactSpawnableRooms = artifactSpawnableRooms.size();

        while (roomIndexes.size() < Artifact.NUM_ARTIFACTS) // Continue generating until we have enough indexes for all rooms
        {
            generatedIndex = randomGen.nextInt(numArtifactSpawnableRooms);
            roomIndexes.add(generatedIndex);
        }
        
        // Assign artifacts to the randomly selected rooms
        Room roomToAssignArtifact;
        ArrayList<String> assignableArtifacts = Artifact.getAllArtifactNames(); // Ordered list of names of assignable artifacts
        for (int roomIdx: roomIndexes)
        {
            // Generate random artifact to assign to this room
            int randomArtifactIndex = randomGen.nextInt(assignableArtifacts.size());
            String artifactName = assignableArtifacts.get(randomArtifactIndex);
            Artifact artifactToAssign = Artifact.getArtifact(artifactName);
            
            // Assign artifact to the room
            roomToAssignArtifact = artifactSpawnableRooms.get(roomIdx);
            roomToAssignArtifact.addArtifact(artifactToAssign);

            // Remove the artifact selected from the list of assignable artifacts
            assignableArtifacts.remove(randomArtifactIndex);
        }
    }

    /**
    * @return A constant representing the number of artifacts that should be spawned inside of the game world
    */
    public static int getNumArtifacts()
    {
        return Artifact.NUM_ARTIFACTS;
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