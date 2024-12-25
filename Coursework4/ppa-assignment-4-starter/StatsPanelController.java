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
 * This class acts as the controller class for the statistics panel.
 * 
 * It provides methods to interact with the statistics panel interface such as:
 * - Alternating between different statistics.
 * - Selecting a Google Mobility statistic.
 * - Finding/calculating the statistics.
 */
public class StatsPanelController
{
    private int currentIndex = 0;

    private Label statsType;
    private Label statsInfo;
    private ComboBox<String> GMRComboBox;
    
    private final String[] statsTypes = {"Total Number of Deaths", "Average of Total Cases", "Google Mobility Average"};
    private StatsCounter statsCounter = new StatsCounter();
    private DecimalFormat doubleValue = new DecimalFormat("#.##"); // Set to 2 d.p
    
    /**
     * Constructor for objects of class StatsPanelController
     */
    public StatsPanelController(Label statsType, Label statsInfo, ComboBox<String> GMRComboBox)
    {
        this.statsType = statsType;
        this.statsType.setText(statsTypes[0]);
        
        this.statsInfo = statsInfo;
        
        this.GMRComboBox = GMRComboBox;
        ObservableList<String> GMRTypes = FXCollections.observableArrayList(StatsCounter.allGMRTypes); // Take from StatsCounter
        this.GMRComboBox.setItems(GMRTypes);
    }

    /**
     * Action method for when the forwards navigation button is clicked.
     * Updates the current index to the next one in the statsTitles list and change the text on the statsTitle label.
     */
    public void forwardButton(ActionEvent event)
    {
        this.currentIndex ++;
        if (this.currentIndex == this.statsTypes.length)
        {
            this.currentIndex = 0;
        }
        this.updateStats();
    }

    /**
     * Action method for when the back navigation button is clicked.
     * Updates the current index to the previous one in the statsTitles list and changes the text on the statsTitle label.
     */
    public void backButton(ActionEvent event)
    {
       this.currentIndex --;
       if (this.currentIndex < 0)
       {
            this.currentIndex = this.statsTypes.length - 1;
       }
       this.updateStats();
    }
    
    /**
     * Sets the contents of the label for the statistic to the correct value, depending on which statistic is currently being shown.
     */
    public void update()
    {   
        // Total deaths
        if(this.currentIndex == 0)
        {
            this.statsInfo.setText(String.valueOf(this.getAllDeaths()));
        }
        // Avg total cases
        else if(this.currentIndex == 1)
        {
            this.statsInfo.setText(this.getAvgCases());
        }
        // GMR average
        else
        {
            this.setGMRAvg();       
        }
    }
    
    /**
     * Updates the text indicating which statistic is currently showing.
     * - Displays the GMR drop-down bar if the current statistic being shown is the Average Google Mobility Measure.
     */
    public void updateStats()
    {
        this.statsType.setText(statsTypes[currentIndex]);
        this.GMRComboBox.setVisible(currentIndex == (this.statsTypes.length - 1)); // Only display GMR combo box when showing that statistic
        this.update();
    }
    
    //  -- Total number of deaths --
    /**
     * @return The total number of deaths amongst all boroughs within the specified date range.
     */
    public double getAllDeaths()
    {
        double totalDeaths = 0.0;
        HashSet<String> boroughs = this.statsCounter.getBoroughs();
        for(String borough : boroughs)
        {
            totalDeaths += this.statsCounter.getBoroughTotalDeaths(borough);
        }

        return totalDeaths;
    }
    
    // -- Average of total cases --
    /**
     * @return The average number of total cases amongst all boroughs within the specified date range.
     */
    public String getAvgCases()
    {
        HashSet<String> boroughs = this.statsCounter.getBoroughs();
        int numBoroughs = boroughs.size();
        double totalCases = 0.0;
        
        // Find sum of all average cases per borough
        for(String borough : boroughs)
        {
            totalCases += this.statsCounter.getBoroughAvgTotalCases(borough);
        }

        // Find average cases amongst all boroughs
        double avgCases = totalCases / numBoroughs;
        String strAvgCases = doubleValue.format(avgCases);
        return strAvgCases;
    }
    
    // -- Google Mobility Average --
    /**
     * Sets the stats label to the average GMR value amongst all boroughs within the specified date range for the selected Google Mobility statistic.
     */
    public void setGMRAvg() 
    {
        String GMR = GMRComboBox.getSelectionModel().getSelectedItem();
        
        // A GMR statistic has not been chosen
        if (GMR == null)
        {
            statsInfo.setText("N/A");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("GMR Input");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Google Mobility measure!");
            alert.showAndWait();
        }
        
        else
        {
            double avgGMR = this.statsCounter.getAvgGMRAcrossBoroughs(GMR);
            String strAvg = doubleValue.format(avgGMR);
            statsInfo.setText(strAvg);
        }
    }
}
