/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 * 
 * 
 *  This class is tbe class for the notes in my game, which is the uncollectible item in my 
 *  game.
 * 
 *  Each note has an associated owner (which is just the name of the "owner" of this note)
 *  as well as a text (the contents of the note).
 * 
 *  Notes cannot be collected but they can interacted with as long as the player is in the
 *  same room as the note and the "interact with note" command was used.

 *  All notes are created and spawned from within the Game class using the defined static
 *  methods. (Each created note is randomly assigned an owner from the defined owners and 
 *  texts within this class).
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.ArrayList;
import java.util.Random;

public class Note
{   
    // List of strings containing the possible contents that a Note object can be assigned.
    private static ArrayList<String> allTexts = new ArrayList<String>()
                                                                        {{
                                                                        add("That maid is really scary... I'm afraid to move in case she might find me! Who knows what she might do to me...");
                                                                        add("Sometimes at night, I hear screams... I think its the thieves that got caught stealing! I'm not trying to be the next victim...");
                                                                        add("Ever since the Smiths hired that maid, its become impossible to even steal slippers from this house!");
                                                                        add("Are these artifacts really worth it?...");
                                                                        add("I want to leave already. I don't even care about the artifacts anymore.");
                                                                        add("Whoever gets this note, tread carefully, these maids have no mercy!");
                                                                        }};
    // List of strings containing the possible owners that a Note object can be assigned.
    private static ArrayList<String> allOwners = new ArrayList<String>()
                                                                        {{
                                                                        add("Ava");
                                                                        add("William");
                                                                        add("Carl");
                                                                        add("Jimmy");
                                                                        add("John");
                                                                        add("Albert");
                                                                        }};
    private static Random randomGen = new Random(); // Random number generator
    
    private String owner; // The note that this belongs to (the name of the person).
    private String contents; // The contents of the note.
    private double weight = 0.0; // Weight of the note (There is none as it is uncollectible)

    /**
     * Constructor for Note class. 
     * - Initialises the Note object with a randomly selected owner and randomly selected contents.
     */
    public Note()
    {  
        // Initialises the "owner" attribute
        Note.assignRandomOwner(this);

        // Initialises the "contents" attribute
        Note.assignRandomContents(this);
    }  

    /**
     * Randomly assigns an owner (a string) to the Note object.
     * @param subjectNote The Note to assign an owner to.
     */
    public static void assignRandomOwner(Note subjectNote)
    {
        int numOwnersLeft = allOwners.size();
        int ownerIndex = Note.randomGen.nextInt(numOwnersLeft); // Generate random index
        String randomName = allOwners.get(ownerIndex);
        subjectNote.setOwner(randomName); // Set the "owner" attribute of this note to this owner.
        allOwners.remove(ownerIndex); // Remove this owner from the possible owners that other notes can have assigned.
    }

    /**
     * Randomly assigns one of the possible texts that a note can contain to the Note object.
     * @param subjectNote The Note to assign contents to.
     */
    public static void assignRandomContents(Note subjectNote)
    {
        int numTextsLeft = allTexts.size();
        int contentsIndex = Note.randomGen.nextInt(numTextsLeft); // Generate random index
        String randomText = allTexts.get(contentsIndex);
        subjectNote.setContents(randomText); // Set the "contents" attribute of this note to the selected text.
        allTexts.remove(contentsIndex); // Remove this text from the possible texts that other notes can have assigned.
    }

    /**
     * @return The owner of this note.
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * @return The contents of this note.
     */
    public String getContents()
    {
        return contents;
    }

    /**
     * Prints a string containing the owner that this note belongs to, along with its contents into the terminal
     * E.g.,
     *      
     */
    public void printDescription()
    {
        System.out.println(owner + "'s note: " + contents + "\n");
    }

    /**
     * Sets the owner of this note to the passed-in owner
     * @param selectedOwner The owner that should be assigned to this note
     */
    public void setOwner(String selectedOwner)
    {
        owner = selectedOwner;
    }

    /**
     * Sets the contents of this note to the passed-in contents
     * @param selectedContents The contents that should be assigned to this note
     */
    public void setContents(String selectedContents)
    {
        contents = selectedContents;
    }
}