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
    private static Property currentProperty; // Static because only one viewer is required
    
    // Main method for testing + development (Remove later)
    public static void main(String[] args)
    {   
        // Create a property viewer
        PropertyViewer propertyViewer1 = new PropertyViewer();
    }
    
    /**
     * Create a PropertyViewer and display its GUI on screen.
     */
    public PropertyViewer()
    {
        // Instantiate GUI and Portfolio
        gui = new PropertyViewerGUI(this);
        portfolio = new Portfolio("airbnb-london.csv");

        // Initialise property viewer with the first 
        int propertyIndex = 0;
        PropertyViewer.currentProperty = this.portfolio.getProperty(propertyIndex);
        
         // Show property ID at the top of the window
        this.gui.showID(currentProperty);
        // Display property (i.e., the entries of this property)
        this.gui.showProperty(currentProperty);
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
    // Toogle the current property being viewed as favourite
    public void toggleFavourite()
    {
        PropertyViewer.currentProperty.toggleFavourite(); // Set to True if False, False if True.
        this.gui.showFavourite(currentProperty); // Display whether this property is favourited or not (Done after each method call)
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
