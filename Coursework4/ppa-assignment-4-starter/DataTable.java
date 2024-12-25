import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class to display a borough's data when the button associated with that borough has been clicked. Each borough will have its own DataWindow, which is
 * shown/hidden when the button is clicked.
 */
public class DataTable
{
    private TableView<CovidData> table;
    private ObservableList<CovidData> data;
    private String borough;

    /**
     * Constructor for objects of class DataTable
     * @param borough The name of the borough this button is designated for.
     */
    public DataTable(String borough)
    {
        this.initialiseTable();
        this.borough = borough;
    }
    
    /**
     * Initialises the table, creating the table and its columns for each data feature/variable.
     */
    private void initialiseTable()
    {
        // Create table containing COVID data.
        this.table = new TableView<CovidData>();
        this.data = FXCollections.observableArrayList();
        this.table.setItems(this.data);
        
        // Create columns for each record attribute
        TableColumn<CovidData, String> dateCol = new TableColumn<CovidData, String>("Date");
        TableColumn<CovidData, Integer> newCasesCol = new TableColumn<CovidData, Integer>("New cases");
        TableColumn<CovidData, Integer> totalCasesCol = new TableColumn<CovidData, Integer>("Total cases");
        TableColumn<CovidData, Integer> newDeathsCol = new TableColumn<CovidData, Integer>("New deaths");
        TableColumn<CovidData, Integer> totalDeathsCol = new TableColumn<CovidData, Integer>("Total deaths");
        
        TableColumn<CovidData, Integer> retailRecreationGMRCol = new TableColumn<CovidData, Integer>("retailRecreationGMR");
        TableColumn<CovidData, Integer> groceryPharmacyGMRCol = new TableColumn<CovidData, Integer>("groceryPharmacyGMR");
        TableColumn<CovidData, Integer> parksGMRCol = new TableColumn<CovidData, Integer>("parksGMR");
        TableColumn<CovidData, Integer> transitGMRCol = new TableColumn<CovidData, Integer>("transitGMR");
        TableColumn<CovidData, Integer> workplacesGMRCol = new TableColumn<CovidData, Integer>("workplacesGMR");
        TableColumn<CovidData, Integer> residentialGMRCol = new TableColumn<CovidData, Integer>("residentialGMR");
        
        // Add "listeners" to each data variable
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        newCasesCol.setCellValueFactory(new PropertyValueFactory("newCases"));
        totalCasesCol.setCellValueFactory(new PropertyValueFactory("totalCases"));
        newDeathsCol.setCellValueFactory(new PropertyValueFactory("newDeaths"));
        totalDeathsCol.setCellValueFactory(new PropertyValueFactory("totalDeaths"));
        
        retailRecreationGMRCol.setCellValueFactory(new PropertyValueFactory("retailRecreationGMR"));
        groceryPharmacyGMRCol.setCellValueFactory(new PropertyValueFactory("groceryPharmacyGMR"));  
        parksGMRCol.setCellValueFactory(new PropertyValueFactory("parksGMR"));
        transitGMRCol.setCellValueFactory(new PropertyValueFactory("transitGMR"));
        workplacesGMRCol.setCellValueFactory(new PropertyValueFactory("workplacesGMR"));
        residentialGMRCol.setCellValueFactory(new PropertyValueFactory("residentialGMR"));
        
        // Add to table
        this.table.getColumns().setAll(
                                        dateCol, 
                                        newCasesCol, 
                                        totalCasesCol, 
                                        newDeathsCol, 
                                        totalDeathsCol, 
                                        retailRecreationGMRCol,
                                        groceryPharmacyGMRCol,
                                        parksGMRCol,
                                        transitGMRCol,
                                        workplacesGMRCol,
                                        residentialGMRCol
                                        );
    }
    
    /**
     * Retrieves the data for the current borough within the specified date range, and sorts it by date initially.
     */
    public void retrieveData()
    {
        // Loading data for this borough
        CovidDataLoader dataLoader = new CovidDataLoader();
        ArrayList<CovidData> loadedData = dataLoader.load();
        
        LocalDate startDate = DateContext.getFromDate();
        LocalDate endDate = DateContext.getToDate();
        
        this.data.clear(); // Clear current data
        for (CovidData d: loadedData)
        {
            String boroughName = d.getBorough();
            // Data is for this borough
            if (boroughName.equals(this.borough))
            {
                
                // If the date is equal to or between the start and end dates
                LocalDate dataDate = LocalDate.parse(d.getDate());
                Boolean withinDates = dataDate.isAfter(startDate) && dataDate.isBefore(endDate);
                Boolean equalsDates = dataDate.isEqual(startDate) || dataDate.isEqual(endDate);
                if (withinDates || equalsDates)
                {
                    this.data.add(d);
                }
            }
        }
        // Sort data by date
        this.data.sort(Comparator.comparing(CovidData::getDate));
    }
    
    /**
     * Sorts the data according to a provided sort comparator.
     * @param sortComparator The comparator that should be used to sort the data. (e.g., a sort by date).
     */
    public void sortData(Comparator sortComparator)
    {
        this.data.sort(sortComparator);
    }
    
    /**
     * @return The table containing the data for this borough at a specific date range.
     */
    public TableView<CovidData> getTable()
    {
        return this.table;
    }
}
