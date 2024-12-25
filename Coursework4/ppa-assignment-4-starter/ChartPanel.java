import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.ArrayList;
import javafx.event.ActionEvent;

/**
 * Panel that displays statistics for selected boroughs in a specified date range in a bar chart format. Users can select multiple boroughs simultaneously 
 * to compare between them.
 * 
 * The panel provides buttons to navigate between different statistics and an additional refresh button that is used to update the entire panel based on
 * the selected boroughs.
 */
public class ChartPanel extends Panel{
    private DataBarChart dataBarChart;
    private BoroughSelectionGrid boroughSelectionGrid;
    private ArrayList<String> toggledBoroughs; // A list of all the toggled boroughs that will be shared with the grid and the chart for updating.
    
    public ChartPanel() {
        this.toggledBoroughs = new ArrayList<String>();
        
        BorderPane pane = new BorderPane();
        int panelWidth = 1000;
        int panelHeight = 720;
        
        pane.setPrefSize(panelWidth, panelHeight);
        
        // Create pane for bar chart
        BorderPane chartPane = new BorderPane();
        this.dataBarChart = new DataBarChart(this.toggledBoroughs);
        chartPane.setCenter(this.dataBarChart.getChart());
        
         // Create buttons for switching between statistics
        Button nextButton = new Button(">");
        nextButton.setOnAction(this.dataBarChart::nextStatistic);
        nextButton.getStyleClass().add("back-next-btns");
        
        Button prevButton = new Button("<");
        prevButton.setOnAction(this.dataBarChart::prevStatistic);
        prevButton.getStyleClass().add("back-next-btns");
    
        HBox bottomBox = new HBox();
        HBox buttonsPanel = new HBox();
        Pane buttonSpacing = new Pane();
        
        
        // Button for borough selection grid:
        Button boroughSelectionButton = new Button("Borough selection grid");
        this.boroughSelectionGrid = new BoroughSelectionGrid(this.toggledBoroughs);
        boroughSelectionButton.setOnAction(this.boroughSelectionGrid::toggleWindow);
        boroughSelectionButton.getStyleClass().add("back-next-btns");
        
        // Button for refreshing the panel
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(this::refresh);
        refreshButton.getStyleClass().add("back-next-btns");
        
        // Adding to button panel
        HBox.setHgrow(buttonSpacing,Priority.ALWAYS);
        buttonSpacing.setMinWidth(Region.USE_PREF_SIZE);
        buttonsPanel.getChildren().addAll(prevButton, boroughSelectionButton, refreshButton, buttonSpacing, nextButton);
        buttonsPanel.getStyleClass().add("bottom-box");
        buttonsPanel.setSpacing(20);
        
        
        // Add other to main pane
        pane.setCenter(chartPane);
        pane.setTop(buttonsPanel);
        BorderPane.setAlignment(chartPane, Pos.CENTER);
        BorderPane.setMargin(buttonsPanel, new Insets(10, 0, 0, 0)); // Add separation padding
        this.pane = pane;
    }

    /**
     * Calls the update function, refreshing the entire panel with the selected data.
     * - This has been defined because the only other time the entire panel is updated is when a new valid date range is selected.
     */
    private void refresh(ActionEvent event)
    {
        this.update();
    }
    
    /** 
     * Identifies selected/toggled boroughs and updates the bar chart accordingly.
     */
    public void update()
    {
        this.boroughSelectionGrid.findToggledBoroughs();
        this.dataBarChart.update();
    }
}

