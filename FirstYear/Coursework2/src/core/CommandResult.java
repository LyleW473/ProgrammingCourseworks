package core;

public class CommandResult
{
    public boolean successBool; // A boolean indicating whether this command was executed successfully or not.
    public boolean quitBool; // A boolean indicating whether or not the player wanted to quit the game.

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