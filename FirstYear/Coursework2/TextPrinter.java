import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextPrinter 
{
    public void outputContentsFile(String pathOfFile)
    {   
        System.out.println("Entered");
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
