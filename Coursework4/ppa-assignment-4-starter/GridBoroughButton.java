import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The buttons that users can click on to display that statistics for the designated borough inside of the chart panel.
 */
public class GridBoroughButton extends BoroughButton
{
    private static int NUM_TOGGLED = 0; // Number buttons that are currently toggled on.
    private static final int MAXIMUM_NUM_TOGGLED = 8; // Maximum number of buttons that can be toggled at the same time
    
    private String borough;
    private Boolean toggled = false; // Indication of whether this button has been selected or not.
    
    /**
     * Constructor for objects of class GridBoroughButton
     * @param borough The name of the borough this button is designated for.
     */
    public GridBoroughButton(String borough)
    {
        super(borough); // Pass in borough name as text.
        this.borough = borough;
        this.setOnAction(this::interact);
    }
    
    /**
     * Alternates between the "toggled" attribute, updating the number of buttons toggled and sets the button to the correct colours.
     */
    protected void interact(ActionEvent event)
    {
        
        // Alternate between toggled and not toggled
        if (toggled)
        {
            toggled = false;
            GridBoroughButton.NUM_TOGGLED --;
        }
        else
        {
            // Enforce restriction for maximum number of buttons that can be toggled onsimultaneously.
            if (GridBoroughButton.NUM_TOGGLED == GridBoroughButton.MAXIMUM_NUM_TOGGLED)
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Maximum number of selected boroughs reached!");
                alert.setContentText("You can only select up to " + GridBoroughButton.MAXIMUM_NUM_TOGGLED + " at a time. Deselect boroughs to choose others!");
                alert.showAndWait();
                return;
            }
            
            toggled = true;
            GridBoroughButton.NUM_TOGGLED ++;
        }
        
        // Change colour
        this.setColour();
    }
    
    /**
     * Sets the colour of the button when clicked on.
     */
    protected void setColour()
    {
        if (toggled)
        {
            // Set base colour to blue
            this.setStyle("-fx-base: #50C4ED;");
            this.setTextFill(Color.WHITE);
        }
        else
        {
            // Set back to default state
            this.setStyle("");
            this.setTextFill(Color.BLACK);
        }
    }
    
    /**
     * @return Whether or not this button has been activated / toggled on.
     */
    public Boolean isToggled()
    {
        return this.toggled;
    }
    
    /**
     * @return The borough that this button "belongs" to".
     */
    public String getBorough()
    {
        return this.borough;
    }
    
    /**
     * Calls the update method. 
     */
    public void update()
    {
    }
}
