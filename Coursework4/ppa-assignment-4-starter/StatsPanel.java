import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;
import java.time.*;
import javafx.collections.*;
import java.text.*;

/**
 * Panel that displays statistics amongst all boroughs for a selected date range. 
 * The statistics that this panel displays are:
 * - Total number of deaths
 * - Average of total cases
 * - Average of two Google Mobility measures
 */
public class StatsPanel extends Panel
{
    private Label statsType = new Label(); // Label containing the type of statistic being displayed 
    private Label statsInfo = new Label(); // Label containing the actual value of the displayed statistic.
    private ComboBox<String> GMRComboBox; // Box that contains the selections for Google Mobility statistics.
    private StatsPanelController controller; // Controller for navigating the stats panel.
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    public StatsPanel()
    {   
        // Set the label text font and size
        statsType.setFont(Font.font("Times New Roman", FontWeight.BOLD, 19));
        statsInfo.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
        
        // Create the dropbox for selecting GMR Types.
        GMRComboBox = new ComboBox<>();
        
        // Disable the dropboxes until user is on the right section of the Statistics Panel
        GMRComboBox.setVisible(false);

        // Create navigation buttons
        Button nextButton = new Button(">");
        nextButton.setPrefSize(100, 570);
        
        Button prevButton = new Button("<");
        prevButton.setPrefSize(100, 570);
        
        // Add the controls to the wrappers
        VBox nextButtonWrapper = new VBox();    
        VBox prevButtonWrapper = new VBox(); 
        nextButtonWrapper.getChildren().add(nextButton);
        nextButtonWrapper.setPadding(new Insets(15, 20, 0, 0));
        prevButtonWrapper.getChildren().add(prevButton);
        prevButtonWrapper.setPadding(new Insets(15, 0, 0, 20));
        
        // Create controller
        this.controller = new StatsPanelController(this.statsType, this.statsInfo, this.GMRComboBox);
        GMRComboBox.setOnAction((ActionEvent event) -> this.controller.setGMRAvg());
        nextButton.setOnAction(this.controller::forwardButton);
        prevButton.setOnAction(this.controller::backButton);
        
        // Add elements to their respective VBox/HBox/Pane.
        VBox dropdown = new VBox();
        dropdown.setSpacing(5);
        dropdown.getChildren().addAll(GMRComboBox);
        
        BorderPane statsInfoWrapper = new BorderPane(statsInfo, statsType, null, null, dropdown); 
        BorderPane.setAlignment(statsInfoWrapper.getCenter(), Pos.CENTER);
        
        statsInfo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(statsInfo, Priority.ALWAYS);
        
        BorderPane.setMargin(dropdown, new Insets(10, 160, 10, 15));
        BorderPane.setMargin(statsType, new Insets(15, 0, 0, 0));
        BorderPane.setAlignment(statsType, Pos.CENTER);
        this.pane = new BorderPane(statsInfoWrapper, null, nextButtonWrapper, null, prevButtonWrapper);
        this.pane.setPrefSize(0,720);
    }
     
    /**
     * Sets the contents of the label for the statistic to the correct value, depending on which statistic is currently being shown.
     */
    public void update()
    {   
        this.controller.update();
    }
    
    /**
     * @return The controller used for interacting with the panel.
     * - Used mainly for testing class.
     */
    public StatsPanelController getController()
    {
        return this.controller;
    }
}
