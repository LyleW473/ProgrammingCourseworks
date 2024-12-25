import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority; // Import for Priority
import java.util.ArrayList;

/**
 * The main class used for viewing the COVID dataset. 
 * 
 * This class encapsulates the code for creating the interface for the COVID dataset viewer.
 * 
 * It provides DatePicker objects for selecting a date range. 
 * There are also navigation buttons for switching between different panels. Each panel is displayed in the center of the screen.
 * The following panels can be shown are as follows: WelcomePanel, MapPanel, StatisticsPanel, ChartPanel
 * 
 */
public class CovidViewer extends Application {
    private ArrayList<Panel> panelList = new ArrayList<Panel>(); // List containing all the panels.
    private CovidViewerController controller; // Controller class for navigating through the viewer.
    
    /**
     * Initialisation function for creating the entire interface for the COVID dataset viewer.
     */
    @Override
    public void start(Stage stage) {
        
        // Creating and adding all panels to the panel list
        WelcomePanel welcomePanel = new WelcomePanel();
        MapPanel mapPanel = new MapPanel();
        StatsPanel statsPanel = new StatsPanel();
        ChartPanel chartPanel = new ChartPanel();
        
        panelList.add(welcomePanel);
        panelList.add(mapPanel);
        panelList.add(statsPanel);
        panelList.add(chartPanel);
        
        // Set current pane
        Pane currentPane = panelList.get(0).getPane();
    
        // Date box
        // Create date picker objects
        DatePicker fromDatePicker = new DatePicker();
        DatePicker toDatePicker = new DatePicker();
        HBox dateBox = this.createDateBox(fromDatePicker, toDatePicker);
        
        // Create bottom box for navigation buttons
        Button backButton = new Button("<");
        backButton.getStyleClass().add("back-next-btns");
        Button nextButton = new Button(">");
        nextButton.getStyleClass().add("back-next-btns");
        HBox bottomBox = this.createNavigationBox(backButton, nextButton);
        
        // Set components in the panel pane
        BorderPane panelPane = new BorderPane();
        panelPane.setTop(dateBox);
        panelPane.setCenter(currentPane);
        panelPane.setBottom(bottomBox);
        
        // Create controller
        this.controller = new CovidViewerController(this.panelList, panelPane);
        
        // Add action methods to components.
        this.controller.setFromDateChangeListener(fromDatePicker);
        this.controller.setToDateChangeListener(toDatePicker);
        backButton.setOnAction(this.controller::backAction);
        nextButton.setOnAction(this.controller::nextAction);
        
        // Root pane
        VBox root = new VBox();
        this.makeMenuBar(root);
        root.getChildren().add(panelPane); // Add borderPane to root VBox
        
        Scene scene = new Scene(root, 1000, 720);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Load the CSS file
        
        stage.setTitle("Covid Viewer");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Creates the main frame's menu bar.
     * @param parent The pane that the menu bar should be added to.
     */
    private void makeMenuBar(Pane parent)
    {
        MenuBar menubar = new MenuBar();
        parent.getChildren().add(menubar);
        
        // create the File menu
        Menu fileMenu = new Menu("File");
        
        MenuItem openItem = new MenuItem("About");

        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(this.controller::quitAction);

        fileMenu.getItems().addAll(openItem, quitItem);
        
        menubar.getMenus().addAll(fileMenu);
    }
    
    /**
     * Creates the HBox that contains the date picker objects for selecting a date range.
     */
    private HBox createDateBox(DatePicker fromDatePicker, DatePicker toDatePicker)
    {
        HBox dateBox = new HBox();
        dateBox.getStyleClass().add("date-box"); // Add a CSS class to the date box
        Label fromLabel = new Label("From:");
        Label toLabel = new Label("To:");
        fromLabel.getStyleClass().add("date-label");
        toLabel.getStyleClass().add("date-label");
        
        // Add date box components
        Pane dateBoxSpace = new Pane();
        HBox.setHgrow(dateBoxSpace, Priority.ALWAYS); // Set spacer to grow horizontally
        dateBoxSpace.setMinWidth(Region.USE_PREF_SIZE);        
        dateBox.getChildren().addAll(dateBoxSpace,fromLabel, fromDatePicker, toLabel, toDatePicker);
        return dateBox;
    }
    
    /**
     * Creates the HBox that contains the navigation buttons for switching between panels.
     */
    private HBox createNavigationBox(Button backButton, Button nextButton)
    {
        HBox navigationBox = new HBox();
        navigationBox.getStyleClass().add("bottom-box"); // Add a CSS class to the bottom box

        
        // Panel spacing
        Pane buttonPanelSpace = new Pane();
        HBox.setHgrow(buttonPanelSpace,Priority.ALWAYS);
        buttonPanelSpace.setMinWidth(Region.USE_PREF_SIZE);
        
        navigationBox.getChildren().addAll(backButton, buttonPanelSpace, nextButton);
        return navigationBox;
    }
}
