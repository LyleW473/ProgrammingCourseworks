import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextPrinter 
{
    // Note: No custom constructor as no attributes needed.

    /**
     * @return ArrayList<String> containing strings which are the contents (lines) of a text file.
     * @param pathOfFile The path of the text file to read from.
     */
    public ArrayList<String> returnContentsList(String pathOfFile)
    {
        ArrayList<String> lines = new ArrayList<String>();

        try 
        {
            // Initialise reader and line
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathOfFile));
            String line;

            // While there are still lines in the file to read
            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }

            // Close reader
            bufferedReader.close();

            return lines;
        } 

        // Catch any exceptions
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return lines;
    }
    
    /**
     * Prints all of the contents (lines) inside of a text file into the terminal.
     * @param pathOfFile The path of the text file to read from.
     */
    public void outputContentsFile(String pathOfFile)
    {   
        try 
        {
            // Initialise reader and line
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathOfFile));
            String line;

            // While there are still lines in the file to read
            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(line);
            }

            // Close reader
            bufferedReader.close();
        } 

        // Catch any exceptions
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}