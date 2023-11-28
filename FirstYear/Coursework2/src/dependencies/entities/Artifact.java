package dependencies.entities;

import java.util.HashMap;
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
