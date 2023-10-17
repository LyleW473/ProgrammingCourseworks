import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This project implements a simple application. Properties from a fixed
 * file can be displayed. 
 * 
 * 
 * @author Michael Kölling and Josh Murphy
 * @version 1.0
 */
public class PropertyViewer
{    
    private PropertyViewerGUI gui;     // the Graphical User Interface
    private Portfolio portfolio;
    
    /**
     * Create a PropertyViewer and display its GUI on screen.
     */
    public PropertyViewer()
    {
        gui = new PropertyViewerGUI(this);
        portfolio = new Portfolio("airbnb-london.csv");
    }

    /**
     *
     */
    public void nextProperty()
    {
        
    }

    /**
     * 
     */
    public void previousProperty()
    {
        
    }

    /**
     * 
     */
    public void toggleFavourite()
    {

    }
    

    //----- methods for challenge tasks -----
    
    /**
     * This method opens the system's default internet browser
     * The Google maps page should show the current properties location on the map.
     */
    public void viewMap() throws Exception
    {
       double latitude = 51.512793;
       double longitude = -0.117149;
       
       URI uri = new URI("https://www.google.com/maps/place/" + latitude + "," + longitude);
       java.awt.Desktop.getDesktop().browse(uri); 
    }
    
    /**
     * 
     */
    public int getNumberOfPropertiesViewed()
    {
        return 0;
    }
    
    /**
     * 
     */
    public int averagePropertyPrice()
    {
        return 0;
    }
}
