import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * PropertyViewerGUI provides the GUI for the project. It displays the property
 * and strings, and it listens to button clicks.

 * @author Michael Kölling, David J Barnes, and Josh Murphy 
 * @version 3.0
 */
public class PropertyViewerGUI
{
    // Porfolio viewer GUI
    private JFrame frame;
    private JPanel propertyPanel;
    private Property currentProperty;
    private PropertyViewer viewer;
    private boolean fixedSize;

    // Labels for portfolio viewer GUI for saving the fields from the currentProperty
    private JLabel idLabel;
    private JLabel favouriteLabel;
    private JTextField hostIDLabel;
    private JTextField hostNameLabel;
    private JTextField neighbourhoodLabel;
    private JTextField roomTypeLabel;
    private JTextField priceLabel;
    private JTextField minNightsLabel;
    private JTextArea descriptionLabel;
    
    // Labels for the statistics window
    private JTextField numPropertiesViewedLabel;
    private JTextField propertiesPriceSumLabel;
    private JTextField averagePropertyPriceLabel;

    // Boolean value as to whether the statistics window is currently being displayed or not
    private boolean isShowingStatisticsWindow = false;


    /**
     * Create a PropertyViewer and display its GUI on screen.
     */
    public PropertyViewerGUI(PropertyViewer viewer)
    {
        currentProperty = null;
        this.viewer = viewer;
        fixedSize = false;
        makeFrame();
        this.setPropertyViewSize(400, 250);
    }

    // ---- public view functions ----

    /**
     * Sets the current property to the property passed in
     */
    public void setCurrentProperty(Property propertyToChangeTo)
    {
        currentProperty = propertyToChangeTo;
    }

    /**
     * Returns the current property being viewed
     */
    public Property getCurrentProperty()
    {
        return currentProperty;
    }

    /**
     * Display a given property
     */
    public void showProperty(Property property)
    {
        hostIDLabel.setText(property.getHostID());
        hostNameLabel.setText(property.getHostName());
        neighbourhoodLabel.setText(property.getNeighbourhood());
        roomTypeLabel.setText(property.getRoomType());
        priceLabel.setText("£" + property.getPrice());
        minNightsLabel.setText(property.getMinNights());
        // descriptionLabel.setText(property.getDescription());
        
        // If the window to display the statistics is showing
        if (isShowingStatistics() == true)
        {
            updateStatisticsWindow();
        }
    }
    
    /**
     * Set a fixed size for the property display. If set, this size will be used for all properties.
     * If not set, the GUI will resize for each property.
     */
    public void setPropertyViewSize(int width, int height)
    {
        propertyPanel.setPreferredSize(new Dimension(width, height));
        frame.pack();
        fixedSize = true;
    }
    
    /**
     * Show a message in the status bar at the bottom of the screen indicating whether it has been favourited or not.
     */
    public void showFavourite(Property property)
    {
        String favouriteText = " ";
        if (property.isFavourite()){
            favouriteText += "This is one of your favourite properties!";
        }
        favouriteLabel.setText(favouriteText);
    }
    
    /**
     * Show the ID in the top of the screen.
     */
    public void showID(Property property)
    {
        idLabel.setText("Current Property ID:" + property.getID());
    }
    
    // ---- implementation of button functions ----
    
    /**
     * Called when the 'Next' button was clicked.
     */
    private void nextButton()
    {
        viewer.nextProperty();
    }

    /**
     * Called when the 'Previous' button was clicked.
     */
    private void previousButton()
    {
        viewer.previousProperty();
    }
    
    /**
     * Called when the 'View on Map' button was clicked.
     */
    private void viewOnMapsButton()
    {
        try{
         viewer.viewMap();
        }
        catch(Exception e){
            System.out.println("URL INVALID");
        }
        
    }
    
    /**
     * Called when the 'Toggle Favourite' button was clicked.
     */
    private void toggleFavouriteButton()
    {
        viewer.toggleFavourite();     
    }
    
    // ---- Methods for displaying statistics ----

    /**
     * Returns a boolean indicating whether the statistics window is being displayed currently or not
     */
    public boolean isShowingStatistics()
    {
        return isShowingStatisticsWindow;
    }

    /**
     * Updates the labels for the statistics window with the correct values for each statistic
     */
    public void updateStatisticsWindow()
    {
        // Note: Integer.toString to convert from "int" to "String"
        numPropertiesViewedLabel.setText(Integer.toString(viewer.getNumberOfPropertiesViewed()));
        averagePropertyPriceLabel.setText(Integer.toString(viewer.averagePropertyPrice()));
        propertiesPriceSumLabel.setText(Integer.toString(viewer.getPropertiesPriceSum()));
    }

    /**
     * Used to toggle the attribute used to show/hide the statistics window when the 'Display statistics' button (or the exit button on the window) is clicked.
     */
    public void toggleShowingStatistics()
    {
        isShowingStatisticsWindow = !isShowingStatisticsWindow;
    }

    /**
     * Called when the 'Display statistics' button was clicked.
     */
    private void displayStatisticsButton(){
        
        // If the window isn't already showing, create a new one
        if (isShowingStatistics() == false)
        {
            // Create frame and content pane for the statistics window
            frame = new JFrame("Statistics");
            JPanel contentPane = (JPanel)frame.getContentPane();
            contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));

            // Specify the layout manager with nice spacing
            contentPane.setLayout(new BorderLayout(2, 2));

            // Create the property pane in the center
            propertyPanel = new JPanel();
            propertyPanel.setLayout(new GridLayout(3,2)); // First number should be the number of labels we want

            // ---- Labels for each statistic ----
            
            // Number of properties viewed
            propertyPanel.add(new JLabel("Number of properties viewed: "));
            numPropertiesViewedLabel = new JTextField("default");
            numPropertiesViewedLabel.setEditable(false);
            propertyPanel.add(numPropertiesViewedLabel);
        
            // Total sum of the prices of the properties viewed
            propertyPanel.add(new JLabel("Total sum of property prices: "));
            propertiesPriceSumLabel = new JTextField("default");
            propertiesPriceSumLabel.setEditable(false);
            propertyPanel.add(propertiesPriceSumLabel);

            // Average property price
            propertyPanel.add(new JLabel("Average property price: "));
            averagePropertyPriceLabel = new JTextField("default");
            averagePropertyPriceLabel.setEditable(false);
            propertyPanel.add(averagePropertyPriceLabel);
            
            // Add property panel to the content pane
            propertyPanel.setBorder(new EtchedBorder());
            contentPane.add(propertyPanel, BorderLayout.CENTER);

            // ----------------------------------
            
            // building is done - arrange the components     
            frame.pack();
            
            // Place and show the frame at the center of the screen
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
            frame.setVisible(true);
            
            // Add a window listener to the frame for when the close button is clicked to call the toggle statistics method
            frame.addWindowListener(new java.awt.event.WindowAdapter() 
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                toggleShowingStatistics();
                }
            }
            );
            
            // Set the statistics frame to always be on top of all other frames
            frame.setAlwaysOnTop(true);

        }

        // If the window is already showing when the display statistics button was clicked
        if (isShowingStatistics() == true)
        {
            // Set the frame "invisible"
            frame.setVisible(false);
            
        }

        // Show/Hide statistics window
        toggleShowingStatistics();

        // Update the statistics window with the current state (values) of the statistics
        updateStatisticsWindow();
    }

    // ---- swing stuff to build the frame and all its components ----
    
    /**
     * Create the Swing frame and its content.
     */
    private void makeFrame()
    {
        // ---- Initialise window for portfolio viewer application ----
        frame = new JFrame("Portfolio Viewer Application");
        JPanel contentPane = (JPanel)frame.getContentPane();
        contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));

        // Specify the layout manager with nice spacing
        contentPane.setLayout(new BorderLayout(6, 6));

        // ---- Labels for each field for each property ----

        // Create the property pane in the center
        propertyPanel = new JPanel();
        propertyPanel.setLayout(new GridLayout(6,2));
        
        // Host ID
        propertyPanel.add(new JLabel("HostID: "));
        hostIDLabel = new JTextField("default");
        hostIDLabel.setEditable(false);
        propertyPanel.add(hostIDLabel);

        // Host Name
        propertyPanel.add(new JLabel("Host Name: "));
        hostNameLabel = new JTextField("default");
        hostNameLabel.setEditable(false);
        propertyPanel.add(hostNameLabel);
        
        // Neighbourhood
        propertyPanel.add(new JLabel("Neighbourhood: "));
        neighbourhoodLabel = new JTextField("default");
        neighbourhoodLabel.setEditable(false);
        propertyPanel.add(neighbourhoodLabel);
        
        // Room type
        propertyPanel.add(new JLabel("Room type: "));
        roomTypeLabel = new JTextField("default");
        roomTypeLabel.setEditable(false);
        propertyPanel.add(roomTypeLabel);
        
        // Price
        propertyPanel.add(new JLabel("Price: "));
        priceLabel = new JTextField("default");
        priceLabel.setEditable(false);
        propertyPanel.add(priceLabel);
        
        // Minimum nights
        propertyPanel.add(new JLabel("Minimum nights: "));
        minNightsLabel = new JTextField("default");
        minNightsLabel.setEditable(false);
        propertyPanel.add(minNightsLabel);

        // Add property panel to the content pane
        propertyPanel.setBorder(new EtchedBorder());
        contentPane.add(propertyPanel, BorderLayout.CENTER);
        
        // ----------------------------------
        
        // Create two labels at top and bottom for the file name and status message
        idLabel = new JLabel("default");
        contentPane.add(idLabel, BorderLayout.NORTH);

        // Label for whether the current property being viewed has been favourited or not
        favouriteLabel = new JLabel(" ");
        contentPane.add(favouriteLabel, BorderLayout.SOUTH);

        // ---- Toolbar for buttons ----
        
        // Create the toolbar with the buttons
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(0, 1));
        
        // Next button
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { nextButton(); }
                           });
        toolbar.add(nextButton);
        
        // Previous button
        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { previousButton(); }
                           });
        toolbar.add(previousButton);
        
        // Map button
        JButton mapButton = new JButton("View Property on Map");
        mapButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { viewOnMapsButton(); }
                           });
        toolbar.add(mapButton);
                        
        // Favourite button
        JButton favouriteButton = new JButton("Toggle Favourite");
        favouriteButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { toggleFavouriteButton(); }
                           });
        toolbar.add(favouriteButton);
        
        // Statistics button
        JButton statisticsButton = new JButton("Display statistics");
        statisticsButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { displayStatisticsButton(); }
                           });
        toolbar.add(statisticsButton);

        // Add toolbar into panel with flow layout for spacing
        JPanel flow = new JPanel();
        flow.add(toolbar);
        
        contentPane.add(flow, BorderLayout.WEST);

        // ----------------------------------
        
        // building is done - arrange the components     
        frame.pack();
        
        // place the frame at the center of the screen and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        frame.setVisible(true);
    }    
}
