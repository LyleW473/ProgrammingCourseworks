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

    // Following attributes are static because only one PropertyViewer is required for the program
    private static Property currentProperty;
    private static int propertyIndex = 0;
    private final int NUM_PROPERTIES; // Non-static because cannot assign to static in constructor after initialisation
    
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

        // Initialise additional attributes
        NUM_PROPERTIES = portfolio.numberOfProperties();

        // Initialise property viewer with the first 
        PropertyViewer.currentProperty = this.portfolio.getProperty(PropertyViewer.propertyIndex);

         // Show property ID at the top of the window
        this.gui.showID(PropertyViewer.currentProperty);
        // Display property (i.e., the entries of this property)
        this.gui.showProperty(PropertyViewer.currentProperty);
    }

    /**
     *
     */
    public void nextProperty()
    {  
        System.out.println(this.portfolio.numberOfProperties());
        System.out.println("Before" +  PropertyViewer.propertyIndex);
        // Increase the property index, looping back to the first property if clicking next on the last property in the "list"
        PropertyViewer.propertyIndex = (PropertyViewer.propertyIndex + 1) % (NUM_PROPERTIES);
        System.out.println("After" + PropertyViewer.propertyIndex);
        
        // Update current property
        PropertyViewer.currentProperty = this.portfolio.getProperty(PropertyViewer.propertyIndex);
        // Update GUI
        this.gui.showID(PropertyViewer.currentProperty);
        this.gui.showProperty(PropertyViewer.currentProperty);
        this.gui.showFavourite(currentProperty);
    }

    /**
     * 
     */
    public void previousProperty()
    {   
        System.out.println("Before" +  PropertyViewer.propertyIndex);
        // Decrease the property index
        PropertyViewer.propertyIndex --;
        
        // Loop back to the last property if clicking previous on the first property in the "list"
        if (PropertyViewer.propertyIndex < 0)
            {
            PropertyViewer.propertyIndex += NUM_PROPERTIES;
            }
        System.out.println("After" + PropertyViewer.propertyIndex);

        // Update current property
        PropertyViewer.currentProperty = this.portfolio.getProperty(PropertyViewer.propertyIndex);
        // Update GUI
        this.gui.showID(PropertyViewer.currentProperty);
        this.gui.showProperty(PropertyViewer.currentProperty);
        this.gui.showFavourite(currentProperty);
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
