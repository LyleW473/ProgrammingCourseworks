package dependencies.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

import core.TextPrinter;

public class Artifact extends Object
{
    private double weight;
    private static HashMap<String, Artifact> artifactsDetails = new HashMap<String, Artifact>(); // HashMap mapping each artifact name to an Artifact object

    public Artifact(String name, String description, double weight)
    {
        // Inherits from Object class
        super(name, description);
        this.weight = weight;
    }

    /**
     * Initialises a static HashMap<String, Artifact>, mapping each artifact name to an Artifact object
     */
    public static void createArtifactsDetails(TextPrinter textPrinter)
    {      
        // Get lines consisting of "ArtifactName|ArtifactWeight"
        ArrayList<String> lines = textPrinter.returnContentsList("dependencies/texts/artifact_details.txt");
        for (String line: lines)
        {  
            // Extract entities from each line into two separate variables
            String[] detailsPerLine = line.split("@"); // Split using the "@" delimiter
            String name = detailsPerLine[0];
            String description = detailsPerLine[1];
            Double weight = Double.parseDouble(detailsPerLine[2]);
            Artifact createdArtifact = new Artifact(name, description, weight);
            
            // Create mapping
            Artifact.artifactsDetails.put(name, createdArtifact);

            // System.out.println(name + " " + artifactsDetails.get(name));
        }
    }

    /**
    * @return an ArrayList<String> containing all possible artifacts that can be created from the key set of artifactsDetails
    */
    public static ArrayList<String> getAllArtifactNames()
    {
        return new ArrayList<String>(Artifact.artifactsDetails.keySet());
    }

    /**
    * @return the artifact with the corresponding artifact name.
    */
    public static Artifact getArtifact(String nameOfArtifact)
    {   
        Artifact artifact = Artifact.artifactsDetails.get(nameOfArtifact);
        Artifact.artifactsDetails.remove(nameOfArtifact);
        return artifact;
    }

    /**
     * Used to randomly select n rooms (where allowed) to spawn artifacts into
     */
    public static void spawnArtifacts(int numArtifacts)
    {
        for (Room room: Room.artifactSpawnableRooms)
        {
            System.out.println("Item room: " + room.getShortDescription());
        }

        // Generate random indexes between 0 (inclusive) and the number of artifact spawnable rooms (exclusive) there are
        Random randomGen = new Random();
        HashSet<Integer> uniqueIndexes = new HashSet<Integer>(); // Hashset for distinct values
        int generatedIndex;
        int numArtifactSpawnableRooms = Room.artifactSpawnableRooms.size();

        while (uniqueIndexes.size() < numArtifacts) // Continue generating until we have enough indexes for all rooms
        {
            generatedIndex = randomGen.nextInt(numArtifactSpawnableRooms);
            uniqueIndexes.add(generatedIndex);
        }
        
        // Assign artifacts to the randomly selected rooms
        Room roomToAssignArtifact;
        ArrayList<String> assignableArtifacts = Artifact.getAllArtifactNames(); // Ordered list of names of assignable artifacts
        for (int roomIdx: uniqueIndexes)
        {
            // Generate random artifact to assign to this room
            int randomArtifactIndex = randomGen.nextInt(assignableArtifacts.size());
            String artifactName = assignableArtifacts.get(randomArtifactIndex);
            Artifact artifactToAssign = Artifact.getArtifact(artifactName);
            
            // Assign artifact to the room
            roomToAssignArtifact = Room.artifactSpawnableRooms.get(roomIdx);
            roomToAssignArtifact.assignArtifact(artifactToAssign);

            // Remove the artifact selected from the list of assignable artifacts
            assignableArtifacts.remove(randomArtifactIndex);
            System.out.println("Artifact name: " + artifactToAssign.getName() + "\nRoom name: " + roomToAssignArtifact.getShortDescription());
        }
    }
    
    /**
    * @return the weight of this artifact
    */
    public double getWeight()
    {
        return weight;
    }

    /**
    * Prints the name, description and weight of this artifact
    */
    public void printDetails(int itemNumber)
    {
        System.out.println("Name: " + this.getName() + " (" + itemNumber + ")");
        System.out.println("Description: " + this.getDescription());
        System.out.println("Artifact weight: " + this.getWeight());
        System.out.println();
    }
}
