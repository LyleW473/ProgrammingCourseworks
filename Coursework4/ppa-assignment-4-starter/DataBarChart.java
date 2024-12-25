import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;

/**
 * Chart which displays data for boroughs that the user has selected inside of the BoroughSelectionGrid.
 * Note: Named "DataBarChart" instead of "BarChart" to avoid clashes with bar chart import.
 */
public class DataBarChart
{
    private BarChart<String, Number> barChart;
    private StatsCounter statsCounter = new StatsCounter();
    private HashMap<String, Series<String, Number>> boroughDataSeries = new HashMap<String, Series<String,Number>>();
    
    private String[] statistics = {"Total Deaths", "Average Total Cases"};
    private int statisticsIndex = 0;
    private ArrayList<String> toggledBoroughs;

    /**
     * Constructor for objects of class DataBarChart
     * @param toggledBoroughs A reference to the actual toggledBoroughs list created in the chart panel. 
     */
    public DataBarChart(ArrayList<String> toggledBoroughs)
    {
        this.toggledBoroughs = toggledBoroughs;
        
        // Create x and y axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Boroughs");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Data"); 
        
        this.barChart = new BarChart<String, Number>(xAxis, yAxis);
        this.barChart.setTitle("Statistics Chart");
    }
    
    /**
     * Updates the chart to display the previous statistic.
     */
    public void prevStatistic(ActionEvent event)
    {
      // Update index
      this.statisticsIndex -= 1;
      if (this.statisticsIndex < 0)
      {
          this.statisticsIndex = this.statistics.length - 1;
      }
      
      // Update the chart
      this.update();
    }
    
    /**
     * Updates the chart to display the next statistic.
     */
    public void nextStatistic(ActionEvent event)
    {
       // Update index
       this.statisticsIndex += 1;
       if (this.statisticsIndex == this.statistics.length)
       {
           this.statisticsIndex = 0;
       }
       
       // Update the chart
       this.update();
    }
    
    /**
     * Creates and adds a series for each borough that has been selected to the hashmap that maps boroughs to its series.
     */
    private void addSeriesData()
    {
        this.boroughDataSeries = new HashMap<String, Series<String,Number>>();
        
        // Map boroughs to their respective data series
        for (String borough: this.toggledBoroughs)
        {
            Series<String, Number> series = new Series<>();
            series.setName(borough);
            
            // Find data
            Double result;
            if (statisticsIndex == 0)
            {
                result = this.statsCounter.getBoroughTotalDeaths(borough);
            }
            else if (statisticsIndex == 1)
            {
                result = this.statsCounter.getBoroughAvgTotalCases(borough);
            }
            else
            {
                result = -1.0;
            }
            
            // Add data
            String dataVar = this.statistics[this.statisticsIndex];
            Data<String, Number> boroughData = new Data<>("", result); // Empty string for the data name (not needed)
            series.getData().add(boroughData);
            this.boroughDataSeries.put(borough, series);
            
            // Alter the label for the Y axis, depending on the statistic being shown
            this.barChart.getYAxis().setLabel(dataVar);
        }
    }
    
    /**
     * @return The BarChart object which contains all of the Series objects created for each selected borough.
     */
    public BarChart<String, Number> getChart()
    {
        return this.barChart;
    }
    
    /**
     * Updates the chart according to which boroughs were selected in the BoroughSelectionGrid.
     */
    public void update()
    {
        this.addSeriesData();
        
        // Clear data and set to new data.
        // Note: New data is created instead of re-used due to lag when switching between statistics.
        this.barChart.setData(FXCollections.observableArrayList());
        
        // Add toggled boroughs' series to the chart
        for (String borough: this.toggledBoroughs)
        {
            Series<String, Number> series = this.boroughDataSeries.get(borough);
            this.barChart.getData().add(series);
        }
    }
}
