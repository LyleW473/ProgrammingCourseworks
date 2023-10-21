import java.awt.Desktop;
import java.beans.PropertyEditor;
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
    private PropertyViewerGUI gui; // The Graphical User Interface
    private Portfolio portfolio; // Portfolio
    
    private int propertyIndex = 0; // Index to keep track of which property we're looking at
    private int numPropertiesViewed = 0; // Total properties viewed since the applicaton started
    private int propertiesPriceSum = 0; // Total sum of the price of all the properties viewed since the applicaton started
    private final int NUM_PROPERTIES; // Constant to keep track of how many total properties there are
    
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

        // Initialise property viewer with the first property
        this.gui.setCurrentProperty(this.portfolio.getProperty(this.propertyIndex));

        // Update statistics 
        // Note: Initialises the number of properties viewed as 1 and price sum as the price of the current property being looked at
        this.updateStatistics();

        // Update GUI with the current property's information (i.e., the first property)
        this.updateGUI();
    }

    /**
     * Moves to the next property and updates the GUI with its information.
     */
    public void nextProperty()
    {  
        // Increase the property index, looping back to the first property if clicking next on the last property in the "list"
        this.propertyIndex = (this.propertyIndex + 1) % (NUM_PROPERTIES);
        
        // Update currentProperty with the "next" property
        this.gui.setCurrentProperty(this.portfolio.getProperty(this.propertyIndex));
        
        // Update statistics
        this.updateStatistics();

        // Update GUI with the current property's information
        this.updateGUI();
    }

    /**
     * Moves to the previous property and updates the GUI with its information.
     */
    public void previousProperty()
    {   
        // Decrease the property index
        this.propertyIndex --;
        
        // Loop back to the last property if clicking previous on the first property in the "list"
        if (this.propertyIndex < 0)
            {
            this.propertyIndex += NUM_PROPERTIES;
            }

        // Update currentProperty with the "previous" property
        this.gui.setCurrentProperty(this.portfolio.getProperty(this.propertyIndex));

        // Update statistics
        this.updateStatistics();

        // Update GUI with the current property's information
        this.updateGUI();
    }

    /**
     * Favourites / unfavourites a property and displays if its favourited on the GUI.
     */
    public void toggleFavourite()
    {
        this.gui.getCurrentProperty().toggleFavourite(); // Set to True if False, False if True.
        this.gui.showFavourite(this.gui.getCurrentProperty()); // Display whether this property is favourited or not (Done after each method call)
    }

    //----- Additional methods for code readability -----

    /**
     * Updates the GUI with the current property's: property ID, the details used for the entries of the GUI, whether it has been favourited.
     */
    public void updateGUI()
    {
        // Show property ID at the top of the window
        this.gui.showID(this.gui.getCurrentProperty());

        // Display property (i.e., the entries/fields of this property)
        this.gui.showProperty(this.gui.getCurrentProperty());

        // Display whether this property is favourited
        this.gui.showFavourite(this.gui.getCurrentProperty());

    }

    /**
     * Updates the values of the statistics when viewing different properties
     */
    public void updateStatistics()
    {
        // Increase number of properties viewed since the application started
        numPropertiesViewed ++;
            
        // Increase total value of all properties viewed so far
        this.propertiesPriceSum += this.gui.getCurrentProperty().getPrice();
    }

    //----- methods for challenge tasks -----
    
    /**
     * This method opens the system's default internet browser
     * The Google maps page should show the current properties location on the map.
     */
    public void viewMap() throws Exception
    {
       double latitude = this.gui.getCurrentProperty().getLatitude();
       double longitude = this.gui.getCurrentProperty().getLongitude();
       
       URI uri = new URI("https://www.google.com/maps/place/" + latitude + "," + longitude);
       java.awt.Desktop.getDesktop().browse(uri); 
    }
    
    /**
     * Returns the number of properties viewed since the application was started
     */
    public int getNumberOfPropertiesViewed()
    {
        return this.numPropertiesViewed;
    }
    
    /**
     * Returns the average price of all of the properties viewed since the application was started
     */
    public int averagePropertyPrice()
    {   
        return this.propertiesPriceSum / this.numPropertiesViewed;
    }
    
    /**
     * Returns the sum of all of the prices of the properties viewed since the application was started
     */
    public int getPropertiesPriceSum()
    {
        return this.propertiesPriceSum;
    }

}
