import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;

/**
 * This class acts as the controller class for the COVID dataset viewer. 
 * 
 * It provides methods for alternating between different panels, selecting a date range and menu options.
 */
public class CovidViewerController
{
    private int panelIndex;
    private ArrayList<Panel> panelList; // Reference to the list of all panels created.
    private Pane currentPane; // The current pane being shown (from the current panel)
    private BorderPane panelPane; // The BorderPane in which the current pane from the current panel is placed into.

    /**
     * Constructor for objects of class CovidViewerController
     */
    public CovidViewerController(ArrayList<Panel> panelList, BorderPane panelPane)
    {
        this.panelIndex = 0;
        this.panelList = panelList;
        this.currentPane = panelList.get(0).getPane();
        this.panelPane = panelPane;
    }
    
    /**
     * Method to set the event listener for changing the start/from date inside of the date picker.
     * @param fromDate The DatePicker object used for selecting the start date.
     */
    public void setFromDateChangeListener(DatePicker fromDate) {
        fromDate.setOnAction(event -> {
            LocalDate original = DateContext.getFromDate();
            DateContext.updateFromDate(fromDate.getValue());
            
            LocalDate selectedFromDate = DateContext.getFromDate();
            LocalDate selectedToDate = DateContext.getToDate();
            
            // Check if "from" date is after "to" date
            if (selectedToDate != null && selectedFromDate != null && selectedFromDate.isAfter(selectedToDate)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Date Range");
                alert.setContentText("The start date cannot be after the end date.");
                alert.showAndWait();
                DateContext.updateFromDate(original);
            }
            
            // Set date to correct date
            fromDate.setValue(DateContext.getFromDate());
            
            // Update stats counter to ensure stats for total deaths are correct. (Should be done before updating panels)
            if (DateContext.getFromDate() != null && DateContext.getToDate() != null)
            {
                StatsCounter.update();
            }
            
            // Update current panel
            Panel currentPanel = this.panelList.get(this.panelIndex);
            currentPanel.update();
        });
    }
    
    /**
     * Method to set the event listener for changing the end/to date inside of the date picker.
     * @param toDate The DatePicker object used for selecting the end date.
     */
    public void setToDateChangeListener(DatePicker toDate) {
        toDate.setOnAction(event -> {
            LocalDate original = DateContext.getToDate();
            DateContext.updateToDate(toDate.getValue());

            LocalDate selectedToDate = DateContext.getToDate();
            LocalDate selectedFromDate = DateContext.getFromDate();
            // Check if "to" date is before "from" date
            if (selectedToDate != null && selectedFromDate != null && selectedFromDate.isAfter(selectedToDate)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Date Range");
                alert.setContentText("The end date cannot be before the start date.");
                alert.showAndWait();
                DateContext.updateToDate(original);
            }
            
            // Set date to correct date
            toDate.setValue(DateContext.getToDate());
            
            // Update death counter to ensure stats for total deaths are correct. (Should be done before updating panels)
            if (DateContext.getFromDate() != null && DateContext.getToDate() != null)
            {
                StatsCounter.update();
            }
            
            // Update current panel
            Panel currentPanel = this.panelList.get(this.panelIndex);
            currentPanel.update();
        });
    }
    
    /**
     * The "Quit" item has been activated.
     */
    public void quitAction(ActionEvent event)
    {
        System.exit(0);
    }
    
    /**
     * Updates the viewer to display the previous panel.
     */
    public void backAction(ActionEvent event)
    {
        // Check for invalid date range
        if (DateContext.getFromDate() == null || DateContext.getToDate() == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date Range");
            alert.setContentText("Please select a start and end date.");
            alert.showAndWait();
            return;
        }
        
        // Update index
        this.panelIndex -= 1;
        if (this.panelIndex < 0)
        {
            this.panelIndex = panelList.size() - 1;
        }
      
        // Update the current panel being shown
        Panel currentPanel = panelList.get(this.panelIndex);
        this.currentPane = currentPanel.getPane();
        this.panelPane.setCenter(this.currentPane);
        currentPanel.update(); 
    }
    
    /**
     * Updates the viewer to display the next panel.
     */
    public void nextAction(ActionEvent event)
    {
        // Check for invalid date range
        if (DateContext.getFromDate() == null || DateContext.getToDate() == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date Range");
            alert.setContentText("Please select a start and end date.");
            alert.showAndWait();
            return;
        }
    
        // Update index
        this.panelIndex += 1;
        
        if (this.panelIndex == panelList.size())
        {
            this.panelIndex = 0;
        }
       
        // Update the current panel being shown
        Panel currentPanel = panelList.get(this.panelIndex);
        this.currentPane = currentPanel.getPane();
        this.panelPane.setCenter(this.currentPane);
        currentPanel.update(); 
    }
}
