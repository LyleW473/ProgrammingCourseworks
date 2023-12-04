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
    // A HashSet that holds all of the valid command words that can be used.
    private final HashSet<String> validCommands = new HashSet<String>() 
                                                                            {{
                                                                                add("go");
                                                                                add("quit");
                                                                                add("help");
                                                                                add("interact with"); // Able to interact with: "note"
                                                                                add("back");
                                                                                add("repeat");
                                                                                add("collect");
                                                                                add("show");
                                                                                add("drop");
                                                                            }};
    // A HashSet containing the command words that cannot be repeated via the "repeat" command.                                        
    private final HashSet<String> cannotRepeatCommands = new HashSet<String>() 
                                                                                    {{
                                                                                        add("go");
                                                                                        add("collect");
                                                                                        add("drop");
                                                                                        add("repeat"); // Cannot repeat a "repeat" command
                                                                                    }};

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
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
     * Check whether a command is repeatable or not.
     * @return true if it is, false if it isn't.
     */
    public boolean isRepeatable(String commandWord)
    {   
        return !cannotRepeatCommands.contains(commandWord);
    }

    /**
     * Prints all valid commands that the player can use to the terminal.
     */
    public void showAllCommands() 
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
     * Prints all valid commands that the player can use in the current game state to the terminal.
     * - For example, "collect artifact" will not show up as an applicable command in a room that does not contain an artifact.
     * @param chosenPlayer The player object, used to access its attributes to identify which commands are applicable in the current game state.
     */
    public void showApplicableCommands(Player chosenPlayer)
    {   
        System.out.println("Applicable commands:");
        
        ArrayList<String> commandsToIgnore = new ArrayList<String>();

        // Current room has no note
        if (!chosenPlayer.getCurrentRoom().hasNote())
        {
            commandsToIgnore.add("interact with");
        }  
        
        // Current room has no artifact
        if (!chosenPlayer.getCurrentRoom().hasArtifact())
        {
            commandsToIgnore.add("collect");
        }  

        // There is no room history
        if (chosenPlayer.getRoomHistory().size() == 0)
        {
            commandsToIgnore.add("back");
        }

        // Empty inventory
        if (chosenPlayer.getInventoryWeight() == 0)
        {
            commandsToIgnore.add("drop");
        }  

        // Output all applicable commands
        int i = 0;
        int numCommands = validCommands.size() - commandsToIgnore.size();
        for (String command: validCommands)
        {
            if (!commandsToIgnore.contains(command))
            {
                System.out.print(command);

                // Printing separator between each command
                if (i < numCommands - 1)
                {
                    System.out.print(" | ");
                }
                i ++;
            }

        }
    }
}
