import javafx.scene.control.Button;
import javafx.event.ActionEvent;

/**
 * Generic button designated for each borough.
 */
public abstract class BoroughButton extends Button
{
    private String borough;
    
    /**
     * Constructor for objects of class BoroughButton
     * @param borough The name of the borough this button is designated for.
     */
    public BoroughButton(String borough)
    {
        super(borough); // Pass in borough name as text.
        this.borough = borough;
        this.setOnAction(this::interact);
    }
    
    /**
     * Executes the callback method/function when clicking on the button.
     */
    protected abstract void interact(ActionEvent event);
    
    /**
     * Sets the colour of the button.
     */
    protected abstract void setColour();
    
    /**
     * Calls the update method for the button.
     */
    protected abstract void update();
}
