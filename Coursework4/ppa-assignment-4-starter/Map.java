import java.util.HashMap;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import java.util.ArrayList;

/**
 * Class that encapsulates the creation of the map interface and the buttons used to interact with boroughs.
 */
public class Map
{
    private final static int NUM_ROWS = 7;
    private final static int NUM_COLS = 8;
    private final static int WIDTH = 800; // Width of the map
    private final static int HEIGHT = 450; // Height of the map
    private final static int CELL_WIDTH = WIDTH / NUM_COLS;
    private final static int CELL_HEIGHT = HEIGHT / NUM_ROWS;
    
    private GridPane map; // The pane that holds the map and its contents
    private ArrayList<MapBoroughButton> allButtons = new ArrayList<MapBoroughButton>(); // Holds a list of buttons which can be updated when a new date range is selected

    /**
     * Constructor for objects of class Map
     * @param panelWidth Width of the parent panel, used to calculate padding.
     */
    public Map(int panelWidth)
    {
        
        this.map = new GridPane();
        this.map.setPrefSize(Map.WIDTH, Map.HEIGHT);
        
        // Padding
        double padding_x = (panelWidth - Map.WIDTH) / 2;
        double padding_y = 75;
        Insets mapPadding = new Insets(padding_y, padding_x, 0, padding_x); // T, R, B, L
        this.map.setPadding(mapPadding);
        
        // Create buttons and the design for the map itself
        this.createInterface();
    }
    
    /**
     * Creates a hashmap containing the locations of where each borough button should be placed.
     * @return The hashmap containing the borough locations.
     */
    private HashMap<String, String> createLocations()
    {
        HashMap<String, String> locations = new HashMap<String, String>();
        
        locations.put("4,0", "Enfield");
        
        locations.put("2,1", "Barnet");
        locations.put("3,1", "Haringey");
        locations.put("4,1", "Waltham Forest");
        
        locations.put("1,2", "Harrow");
        locations.put("2,2", "Brent");
        locations.put("3,2", "Camden");
        locations.put("4,2", "Islington");
        locations.put("5,2", "Hackney");
        locations.put("6,2", "Redbridge");
        locations.put("7,2", "Havering");
        
        locations.put("0,3", "Hillingdon");
        locations.put("1,3", "Ealing");
        locations.put("2,3", "Kensington And Chelsea");
        locations.put("3,3", "Westminster");
        locations.put("4,3", "Tower Hamlets");
        locations.put("5,3", "Newham");
        locations.put("6,3", "Barking And Dagenham");
        
        locations.put("1,4", "Hounslow");
        locations.put("2,4", "Hammersmith And Fulham");
        locations.put("3,4", "Wandsworth");
        locations.put("4,4", "City Of London");
        locations.put("5,4", "Greenwich");
        locations.put("6,4", "Bexley");
        
        locations.put("2,5", "Richmond Upon Thames");
        locations.put("3,5", "Merton");
        locations.put("4,5", "Lambeth");
        locations.put("5,5", "Southwark");
        locations.put("6,5", "Lewisham");
        
        locations.put("3,6", "Kingston Upon Thames");
        locations.put("4,6", "Sutton");
        locations.put("5,6", "Croydon");
        locations.put("6,6", "Bromley");
        
        return locations;
    }
    
    /*
     * Creates the buttons and actual design for the map.
     */
    private void createInterface()
    {
        // Create locations to create the interface
        HashMap<String, String> locations = this.createLocations();
        
        for (int row = 0; row < Map.NUM_ROWS; row++)
        {
            for (int col = 0; col < Map.NUM_COLS; col++)
            {
                String locationString = "" + col + "," + row;
                
                if (locations.containsKey(locationString))
                {
                    // Create button for borough
                    String boroughName = locations.get(locationString);
                    MapBoroughButton boroughButton = new MapBoroughButton(boroughName);
                    boroughButton.setMinSize(Map.CELL_WIDTH, Map.CELL_HEIGHT);
                    boroughButton.setMaxSize(Map.CELL_WIDTH, Map.CELL_HEIGHT);
                    boroughButton.setPrefSize(Map.CELL_WIDTH, Map.CELL_HEIGHT);
                    map.add(boroughButton, col, row);
                    
                    allButtons.add(boroughButton);
                }
                
                else
                {
                    // Create spacing pane
                    Pane spacingPane = new Pane();
                    spacingPane.setMinSize(Map.CELL_WIDTH, Map.CELL_HEIGHT);
                    spacingPane.setMaxSize(Map.CELL_WIDTH, Map.CELL_HEIGHT);
                    spacingPane.setPrefSize(Map.CELL_WIDTH, Map.CELL_HEIGHT);
                    this.map.add(spacingPane, col, row);
                }
            }
        }
    }
    
    /** 
     * @return The map and its contents (i.e., the buttons and spacing).
     */
    public GridPane getPane()
    {
        return this.map;
    }
    
    /** 
     * Calls the update function of all buttons inside of the map.
     */
    public void update()
    {
        for (MapBoroughButton button : this.allButtons)
        {
            button.update();
        }
    }
}
