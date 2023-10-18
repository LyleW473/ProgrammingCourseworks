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
    // fields:
    private JFrame frame;
    private JPanel propertyPanel;
    private JLabel idLabel;
    private JLabel favouriteLabel;
    
    private JTextField hostIDLabel;
    private JTextField hostNameLabel;
    private JTextField neighbourhoodLabel;
    private JTextField roomTypeLabel;
    private JTextField priceLabel;
    private JTextField minNightsLabel;
    private JTextArea descriptionLabel;


    // Statistics window
    private JTextField numPropertiesViewedLabel;
    private JTextField propertiesPriceSumLabel;
    private JTextField averagePropertyPriceLabel;
    private boolean isShowingStatisticsWindow = false;
    
    private Property currentProperty;
    private PropertyViewer viewer;
    private boolean fixedSize;
        
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
        //descriptionLabel.setText(property.getDescription());
        
        // If the window to display the statistics is showing
        System.out.println(isShowingStatistics());
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
     * Show a message in the status bar at the bottom of the screen.
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
    public void showID(Property property){
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
    private void toggleFavouriteButton(){
        viewer.toggleFavourite();     
    }
    
    // ---- Methods for displaying statistics ----

    /**
     * Returns a boolean as to whether the statistics window is being displayed or not
     */
    public boolean isShowingStatistics()
    {
        return isShowingStatisticsWindow;
    }

    /**
     * Updates the statistics window with the correct values for each statistic
     */
    public void updateStatisticsWindow()
    {
        numPropertiesViewedLabel.setText(Integer.toString(viewer.getNumberOfPropertiesViewed()));
        averagePropertyPriceLabel.setText(Integer.toString(viewer.averagePropertyPrice()));
        propertiesPriceSumLabel.setText(Integer.toString(viewer.getPropertiesPriceSum()));
    }

    /**
     * Changes the showing 
     * Called when the 'Display statistics' button is clicked, or when the close button is clicked on the statistics window, to change 
     */
    public void toggleShowingStatistics()
    {
        isShowingStatisticsWindow = !isShowingStatisticsWindow;
    }

    /**
     * Called when the 'Display statistics' button was clicked.
     */
    private void displayStatisticsButton(){
        System.out.println("Showing stats");
        
        // If the window isn't already showing, create a new one
        if (isShowingStatistics() == false)
        {
            frame = new JFrame("Statistics");
            JPanel contentPane = (JPanel)frame.getContentPane();
            contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));

            // Specify the layout manager with nice spacing
            contentPane.setLayout(new BorderLayout(2, 2));

            // Create the property pane in the center
            propertyPanel = new JPanel();
            propertyPanel.setLayout(new GridLayout(3,2)); // First number should be the number of labels we want
            
            // Number of properties viewed
            propertyPanel.add(new JLabel("Number of properties viewed: "));
            System.out.println(viewer.getNumberOfPropertiesViewed());
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
        
            propertyPanel.setBorder(new EtchedBorder());
            contentPane.add(propertyPanel, BorderLayout.CENTER);
            
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
                System.out.println(isShowingStatistics());
                }
            });

        }
        
        // Only set the showing statistics attribute to false if it isn't being shown already (i.e., only set to back to false when the exit button is clicked)
        if (isShowingStatistics() == false)
        {
            toggleShowingStatistics();
        }

        // Update the statistics window with the current state (values) of the statistics
        updateStatisticsWindow();
    }

    // ---- swing stuff to build the frame and all its components ----
    
    /**
     * Create the Swing frame and its content.
     */
    private void makeFrame()
    {
        frame = new JFrame("Portfolio Viewer Application");
        JPanel contentPane = (JPanel)frame.getContentPane();
        contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));

        // Specify the layout manager with nice spacing
        contentPane.setLayout(new BorderLayout(6, 6));

        // Create the property pane in the center
        propertyPanel = new JPanel();
        propertyPanel.setLayout(new GridLayout(6,2));
        
        propertyPanel.add(new JLabel("HostID: "));
        hostIDLabel = new JTextField("default");
        hostIDLabel.setEditable(false);
        propertyPanel.add(hostIDLabel);
        
        propertyPanel.add(new JLabel("Host Name: "));
        hostNameLabel = new JTextField("default");
        hostNameLabel.setEditable(false);
        propertyPanel.add(hostNameLabel);
        
        propertyPanel.add(new JLabel("Neighbourhood: "));
        neighbourhoodLabel = new JTextField("default");
        neighbourhoodLabel.setEditable(false);
        propertyPanel.add(neighbourhoodLabel);
        
        propertyPanel.add(new JLabel("Room type: "));
        roomTypeLabel = new JTextField("default");
        roomTypeLabel.setEditable(false);
        propertyPanel.add(roomTypeLabel);
        
        propertyPanel.add(new JLabel("Price: "));
        priceLabel = new JTextField("default");
        priceLabel.setEditable(false);
        propertyPanel.add(priceLabel);
        
        propertyPanel.add(new JLabel("Minimum nights: "));
        minNightsLabel = new JTextField("default");
        minNightsLabel.setEditable(false);
        propertyPanel.add(minNightsLabel);

        propertyPanel.setBorder(new EtchedBorder());
        contentPane.add(propertyPanel, BorderLayout.CENTER);
        
        // Create two labels at top and bottom for the file name and status message
        idLabel = new JLabel("default");
        contentPane.add(idLabel, BorderLayout.NORTH);

        favouriteLabel = new JLabel(" ");
        contentPane.add(favouriteLabel, BorderLayout.SOUTH);
        
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
        
        // building is done - arrange the components     
        frame.pack();
        
        // place the frame at the center of the screen and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        frame.setVisible(true);
    }    
}
