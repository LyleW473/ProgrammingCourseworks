/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.HashSet;
import java.util.ArrayList;

public class CommandWords
{
    // A hashset that holds all of the valid command words.
    private static final HashSet<String> validCommands = new HashSet<String>();

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // Add valid commands to hashset
        validCommands.add("go");
        validCommands.add("quit");
        validCommands.add("help");
        validCommands.add("print out");
        validCommands.add("interact with"); // Able to interact with: "npc"
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {   
        return validCommands.contains(aString);
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll() 
    {      
        System.out.println("All of your command words are:");
        int i = 0;
        int numCommands = validCommands.size(); // Save so this does not need to be calculated at every iteration
        for(String command: validCommands) {
            System.out.print(command);
            if (i < numCommands - 1)
            {
                System.out.print(" | ");
            }
            i ++;
        }
    }

    /**
     * Print all valid commands that can be used right now to System.out.
     * - For example, sometimes the "interact with" should not be shown
     */
    public void showApplicable(Room currentRoom)
    {   
        System.out.println("Applicable commands:");
        
        ArrayList<String> commandsToIgnore = new ArrayList<String>();
        // Has no NPCs (Note: Add && OBJECT condition here once objects are added)
        if (!currentRoom.hasNPC())
        {
            commandsToIgnore.add("interact with");
        }  

        // Output all applicable commands
        int i = 0;
        int numCommands = validCommands.size() - commandsToIgnore.size();
        for (String command: validCommands)
        {
            if (!commandsToIgnore.contains(command))
            {
                System.out.print(command);
                if (i < numCommands - 1)
                {
                    System.out.print(" | ");
                }
                i ++;
            }

        }
    }
}
