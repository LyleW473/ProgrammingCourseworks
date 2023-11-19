package core;

public class CommandResult
{

    public boolean successBool; // A boolean indicating whether this command was executed successfully or not
    public boolean quitBool; // A boolean indicating whether or not the player wanted to quit the game

    public CommandResult(boolean successBool, boolean quitBool)
    {
        this.successBool = successBool;
        this.quitBool = quitBool;
    }

    /**
     * @return Whether the command was executed successfully or not
     */
    public boolean wasSuccessful()
    {
        return successBool;
    }

    /**
     * @return true if the player wanted to quit the game else false
     */
    public boolean wantsToQuit()
    {
        return quitBool;
    }
}