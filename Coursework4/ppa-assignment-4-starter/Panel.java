import javafx.scene.layout.Pane;

/**
 * Generic Panel class which contains the attributes and methods that all panels have in common.
 */
public abstract class Panel
{
    protected Pane pane;

    public abstract void update();
    
    public Pane getPane() {
        return pane;
    }    
}
