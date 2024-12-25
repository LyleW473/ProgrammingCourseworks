import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import java.util.HashSet;
import java.util.ArrayList;
import javafx.scene.control.Button;

/**
 * Grid used to display buttons for each borough. When a button for a borough is clicked, it will become toggled. After refreshing the panel, the changes
 * will be reflected in the displayed bar chart.
 */
public class BoroughSelectionGrid
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 6;
    private static final int CELL_WIDTH = WIDTH / NUM_COLS;
    private static final int CELL_HEIGHT = HEIGHT / NUM_ROWS;
    
    private Stage window;
    private StatsCounter statsCounter = new StatsCounter();
    private GridPane buttonGrid;
    
    private ArrayList<GridBoroughButton> buttonsList = new ArrayList<GridBoroughButton>(); // List of all buttons
    private ArrayList<String> toggledBoroughs;
    
    /**
     * Constructor for objects of class BoroughSelectionGrid
     * @param toggledBoroughs A reference to the actual toggledBoroughs list created in the chart panel. 
     */
    public BoroughSelectionGrid(ArrayList<String> toggledBoroughs)
    {
        this.toggledBoroughs = toggledBoroughs;
        
        this.window = new Stage();
        
        // Defining contents
        BorderPane root = new BorderPane();    
        
        this.buttonGrid = new GridPane();
        this.buttonGrid.setPrefSize(BoroughSelectionGrid.WIDTH, BoroughSelectionGrid.HEIGHT);
    
        // Adding to root and scene
        root.setCenter(buttonGrid);
        Scene scene = new Scene(root, BoroughSelectionGrid.WIDTH, BoroughSelectionGrid.WIDTH);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        window.setScene(scene);
        
        window.setTitle("Borough Selection Grid");
    }

    /**
     * Creates buttons for all London boroughs and places them inside of the main grid pane.
     */
    private void addButtons()
    {
        // Find the boroughs and add them to an array list (So that we can index them).
        HashSet<String> boroughs = this.statsCounter.getBoroughs();
        ArrayList<String> boroughsList = new ArrayList<String>();
        for (String borough: boroughs)
        {
            this.buttonsList.add(new GridBoroughButton(borough));
            boroughsList.add(borough);
        }
        
        // Add buttons to the grid
        int numAdded = 0;
        int numButtons = this.buttonsList.size();
        for (int i = 0; i < BoroughSelectionGrid.NUM_ROWS; i ++)
        {
            for (int j = 0; j < BoroughSelectionGrid.NUM_COLS; j++)
            {
                // Have not added all of the buttons
                if (numAdded < numButtons)
                {
                    String borough = boroughsList.get(numAdded);
                    GridBoroughButton boroughButton = this.buttonsList.get(numAdded);
                    
                    boroughButton.setMinSize(BoroughSelectionGrid.CELL_WIDTH, BoroughSelectionGrid.CELL_HEIGHT);
                    boroughButton.setMaxSize(BoroughSelectionGrid.CELL_WIDTH, BoroughSelectionGrid.CELL_HEIGHT);
                    boroughButton.setPrefSize(BoroughSelectionGrid.CELL_WIDTH, BoroughSelectionGrid.CELL_HEIGHT);
                    
                    this.buttonGrid.add(boroughButton, j, i);
                    numAdded ++;
                }
            }
        }
    }
    
     /**
     * Clears the current list of toggled boroughs and adds all of the selected boroughs in the grid.
     */
    public void findToggledBoroughs()
    {
        this.toggledBoroughs.clear();
        for (GridBoroughButton button: this.buttonsList)
        {
            if (button.isToggled())
            {
                this.toggledBoroughs.add(button.getBorough());
            }
        }
    }
    
    /**
     * Toggles the window to show/hide.
     */
    public void toggleWindow(ActionEvent event)
    {
        // If no buttons have been added yet
        if (this.buttonsList.size() == 0)
        {
            this.addButtons();
        }
        
        // Toggling the window
        if (window.isShowing())
        {
            window.hide();
            
        }
        else
        {
            window.show(); 
        }
    }
}
