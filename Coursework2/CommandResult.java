/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 * 
 * 
 *  This class is used to define objects that can hold additional information about the result of a command.
 *  It has been defined because upon a successful command, I wanted to make it so that the command be repeated 
 *  via the "repeat" command as long as the command itself is repeatable. For example, if the player used a 
 *  "help" command followed by a "repeat" command, it should repeat the "help" command again.
 * 
 *  Each CommandResult object will hold boolean attributes, relating to the result of a command created from
 *  the user's input.
 * 
 *  CommandResult objects are created within the user command methods within the Game class.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class CommandResult
{
    private boolean successBool; // A boolean indicating whether this command was executed successfully or not.
    private boolean quitBool; // A boolean indicating whether or not the player wanted to quit the game.

    /**
     * Constructor for CommandResult class.
     * @param successBool A boolean indicating whether this command was executed successfully or not.
     * @param quitBool A boolean indicating whether or not the player wanted to quit the game.
     */    
    public CommandResult(boolean successBool, boolean quitBool)
    {
        this.successBool = successBool;
        this.quitBool = quitBool;
    }
    
    /**
     * @return A boolean indicating whether or not the command was executed successfully.
     */
    public boolean wasSuccessful()
    {
        return successBool;
    }

    /**
     * @return A boolean indicating whether or not the player wants to quit the game (after a successful "quit" command).
     */
    public boolean wantsToQuit()
    {
        return quitBool;
    }
}