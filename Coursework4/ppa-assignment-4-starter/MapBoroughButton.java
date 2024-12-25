import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.time.LocalDate;
import javafx.scene.paint.Color;

/**
 * The buttons that users can click on to access borough data during a specific date range. Each borough will have its own button, and when clicked will
 * create a new DataWindow to view the data.
 */
public class MapBoroughButton extends BoroughButton
{
    private String borough;
    private DataWindow dataWindow;
    private StatsCounter statsCounter;
    
    /**
     * Constructor for objects of class MapBoroughButton
     * @param borough The name of the borough this button is designated for.
     */
    public MapBoroughButton(String borough)
    {
        super(borough); // Pass in borough name as text.
        
        this.borough = borough;
        this.setOnAction(this::interact);
        this.dataWindow = new DataWindow(borough);
        this.statsCounter = new StatsCounter();
    }
    
    /**
     * Method that toggles the data window to show/hide when interacting with the button.
     */
    protected void interact(ActionEvent event)
    {
        this.dataWindow.toggleWindow();
    }
    
    /**
     * Sets the colour of the button, which is dependent on the number of deaths compared to other boroughs within a specific date range.
     */
    protected void setColour()
    {
        Double totalDeaths = this.statsCounter.getBoroughDeathRate(this.borough);
        
        // Setting the colours, from darkest to lightest (lower value is lighter shade of green).
        if (totalDeaths > 0.75)
        {
            this.setStyle("-fx-base: #618264;");
        }
        else if (totalDeaths > 0.5)
        {
            this.setStyle("-fx-base: #79AC78;");
        }
        else if (totalDeaths > 0.25)
        {
            this.setStyle("-fx-base: #B0D9B1;");
        }
        else
        {
            this.setStyle("-fx-base: #D0E7D2;");
        }
        
        // Set text colour to white
        this.setTextFill(Color.WHITE);
    }
    
    /**
     * Calls the update function, setting the colour of the button based on the death rate of the borough during the specified date range.
     */
    public void update()
    {
        this.setColour();
    }
}
