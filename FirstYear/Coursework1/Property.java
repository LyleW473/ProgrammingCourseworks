import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

/**
 * Property is a class that defines a property for display.
 * 
 * @author  Michael Kölling and Josh Murphy
 * @version 2.0
 */
public class Property
{

    private String id;
    private String description;
    private String hostID;
    private String hostName;
    private String neighbourhood;
    private double latitude;
    private double longitude;
    private String roomType;
    private int price;
    private int minimumNights;
    private int numReviews;
    private String dateLastReview;
    private double reviewsPerMonth;
    private int availability365;
    private boolean isFavourite;
    
    /**
     * Create a new property with specified initial values.
     */
    public Property(String id, String name, String hostID, String hostName, 
            String neighbourhood, double latitude, double longitude, String roomType, 
            int price, int minimumNights, int numReviews, String dateLastReview, double reviewsPerMonth, int availability365){
        
		this.id = id;
        this.description = name;
        this.hostID = hostID;
        this.hostName = hostName;
        this.neighbourhood = neighbourhood;
        this.latitude = latitude;
        this.longitude  = longitude;
        this.roomType = roomType;
        this.price = price;
        this.minimumNights = minimumNights;
        this.numReviews = numReviews;
        this.dateLastReview = dateLastReview;
        this.reviewsPerMonth = reviewsPerMonth;
        this.availability365 = availability365;
        
        this.isFavourite = false;
    }
    
    /**
     * Return the Id of this property.
     */
    public String getID()
    {
        return this.id;
    }
    
    /**
     * Return the hostId of this property.
     */
    public String getHostID()
    {
        return this.hostID;
    }
    
    /**
     * Return the latitude of this property.
     */
    public double getLatitude()
    {
        return this.latitude;
    }
    
    /**
     * Return the longitude of this property.
     */
    public double getLongitude()
    {
        return this.longitude;
    }
    
    /**
     * Return the price of this property.
     */
    public int getPrice()
    {
        return this.price;
    }
    
    /**
     * Returns true if this property is currently marked as a favourite, false otherwise.
     */
    public boolean isFavourite()
    {
        return this.isFavourite;
    }
    
    /**
     * Return the host name of this property.
     */
    public String getHostName()
    {
        return this.hostName;
    }
    
    /**
     * Return the neighbourhood of this property.
     */
    public String getNeighbourhood()
    {
        return this.neighbourhood;
    }
    
    /**
     * Return the room type of this property.
     */
    public String getRoomType()
    {
        return this.roomType;
    }
    
    /**
     * Return the minimum number of nights this property can be booked for.
     */
    public String getMinNights()
    {
        return "" + this.minimumNights;
    }
    
    /**
     * Return the description of this property.
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * Toggles whether this property is marked as a favourite or not.
     */
    public void toggleFavourite()
    {
        this.isFavourite = !this.isFavourite;
    }

    // ---- Additional entries/fields for each property ----

    /**
     * Returns the total number of reviews for this property.
     */
    public int getNumReviews()
    {
        return this.numReviews;
    }

    /**
     * Returns the date of the last review for this property
     */
    public String getDateLastReview()
    {
        return this.dateLastReview;
    }

    /**
     * Returns the number of reviews per month for this property
     */
    public double getReviewsPerMonth()
    {
        return this.reviewsPerMonth;
    }

    /**
     * Returns the year-round availiability of the property in days.
     */
    public int getAvailability365()
    {
        return this.availability365;
    }
}
