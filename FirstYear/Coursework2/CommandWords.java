/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 * 
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 * It also contains methods to check whether a user command is valid or repeatable.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.HashSet;

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
     * @param commandWord The command word (first word) in the user's command
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String commandWord)
    {   
        return validCommands.contains(commandWord);
    }

    /**
     * Check whether a command is repeatable or not.
     * @param commandWord The command word (first word) in the user's command
     * @return true if it is, false if it isn't.
     */
    public boolean isRepeatable(String commandWord)
    {   
        return !cannotRepeatCommands.contains(commandWord);
    }
}