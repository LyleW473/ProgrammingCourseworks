import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextPrinter 
{
    /**
     * Prints all of the lines inside of a file into the terminal
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

    /**
     * @return ArrayList<String> containing lines (strings) from a text file
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
    
}
