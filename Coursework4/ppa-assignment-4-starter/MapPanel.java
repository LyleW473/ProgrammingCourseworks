import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;

/**
 * Panel that contains all of the components associated with the map.
 */
public class MapPanel extends Panel {
    private Map map;

    public MapPanel() {
        BorderPane pane = new BorderPane();
        
        int panelWidth = 1000;
        int panelHeight = 720;
        pane.setPrefSize(panelWidth, panelHeight);
        
        int mapWidth = 800;
        int mapHeight = 450;
        map = new Map(panelWidth);
        GridPane mapPane = map.getPane();
        
        pane.setCenter(mapPane);
        BorderPane.setAlignment(mapPane, Pos.CENTER);
        this.pane = pane;
        
    }


    /** 
     * Calls the update function of the shown map.
     */
    public void update()
    {
        this.map.update();
    }
}