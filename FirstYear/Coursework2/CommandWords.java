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
}