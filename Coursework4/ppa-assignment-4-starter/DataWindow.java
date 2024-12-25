import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;

/**
 * Class to display a borough's data when the button associated with that borough has been clicked. Each borough will have its own DataWindow, which is
 * shown/hidden when the button is clicked.
 */
public class DataWindow
{

    private Stage window;
    private String borough;
    private DataTable dataTable; // Table containing all of the borough's data
    
    /**
     * Constructor for objects of class DataWindow
     * @param borough The name of the borough this button is designated for.
     */
    public DataWindow(String borough)
    {
        
        this.borough = borough;
        this.window = new Stage();
        
        // Defining contents
        BorderPane root = new BorderPane();
        
        // Create menus for exiting window and sorting data
        MenuBar menuBar = this.createMenuBar();
    
        // Add data table
        this.dataTable = new DataTable(this.borough);
        BorderPane infoPanel = new BorderPane();
        infoPanel.setCenter(this.dataTable.getTable());
    
        // Adding to root and scene
        root.setTop(menuBar);
        root.setCenter(infoPanel);
        Scene scene = new Scene(root, 1000, 720);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Load the CSS file
        window.setScene(scene);
        
        window.setTitle(borough);
    }
    
    /**
     * Toggles the window to show/hide. When showing the data, the data table will be updated again, to ensure that data is accurate and up to date.
     */
    public void toggleWindow()
    {
        if (window.isShowing())
        {
            window.hide();
            
        }
        else
        {
            this.dataTable.retrieveData();
            window.show(); 
        }
    }
    
    /**
     * Creates the menu bar that the users can use to exit the window or sort the data table based on a specific column.
     */
    private MenuBar createMenuBar()
    {
        MenuBar menuBar = new MenuBar();
        
        // File menu (Used for quitting only)
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction((ActionEvent event) -> {this.toggleWindow();});
        fileMenu.getItems().addAll(quitItem);
        
        // Data sorting menu
        Menu sortByMenu = new Menu("Sort by");
        
        MenuItem dateItem = new MenuItem("Date");
        MenuItem newCasesItem = new MenuItem("New cases");
        MenuItem totalCasesItem = new MenuItem("Total cases");
        MenuItem newDeathsItem = new MenuItem("New deaths");
        MenuItem totalDeathsItem = new MenuItem("Total deaths");
        
        MenuItem retailRecreationGMRItem = new MenuItem("Retail Recreation GMR");
        MenuItem groceryPharmacyGMRItem = new MenuItem("Grocery Pharmacy GMR");
        MenuItem parksGMRItem = new MenuItem("Parks GMR");
        MenuItem transitGMRItem = new MenuItem("Transit GMR");
        MenuItem workplacesGMRItem = new MenuItem("Workplaces GMR");
        MenuItem residentialGMRItem = new MenuItem("Residential GMR");
        
        // Adding action events for each sort
        dateItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByDate());});
        newCasesItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByNewCases());});
        totalCasesItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByTotalCases());});
        newDeathsItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByNewDeaths());});
        totalDeathsItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByTotalDeaths());});
        
        retailRecreationGMRItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByRetailRecreationGMR());});
        groceryPharmacyGMRItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByGroceryPharmacyGMR());});
        parksGMRItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByParksGMR());});
        transitGMRItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByTransitGMR());});
        workplacesGMRItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByWorkplacesGMR());});
        residentialGMRItem.setOnAction((ActionEvent event) -> {this.dataTable.sortData(DataSorts.sortByResidentialGMR());});
        
        sortByMenu.getItems().addAll(
                                    dateItem,
                                    newCasesItem, 
                                    totalCasesItem, 
                                    newDeathsItem, 
                                    totalDeathsItem, 
                                    retailRecreationGMRItem, 
                                    groceryPharmacyGMRItem,
                                    parksGMRItem, 
                                    transitGMRItem,
                                    workplacesGMRItem,
                                    residentialGMRItem
                                    );
        
        // Add all menus to the menu bar
        menuBar.getMenus().addAll(fileMenu, sortByMenu);
        return menuBar;
    }
}
