import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The test class StatsCounterTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class StatsCounterTest
{
    StatsPanel statsPanel = new StatsPanel();
    StatsCounter statsCounter = new StatsCounter();
    
    LocalDate startDate;
    LocalDate endDate;
    
    LocalDate latestDate = LocalDate.parse("2023-02-09");       //Latest date recorded in covid_london.csv
    LocalDate earliestDate = LocalDate.parse("2020-02-03");     //Earliest date recorded in covid_london.csv
    
    HashSet<String> testBoroughs = new HashSet<>();         //Test HashSet used for testing purposes
    HashMap<String, Double> testMaps = new HashMap<>();     //Test HashMap used for testing purposes
    
    public double testVal;      //Test double value used for testing purposes
    
    /**
     * Default constructor for test class StatsCounterTest
     */
    public StatsCounterTest()
    {
    }
    
    /**
     * Test whether the total deaths will return 0.0 if a date range exceeds 
     * the earliest/latest dates recorded in covid_london.csv.
     */
    @Test
    public void outOfRangeDates_TotalDeaths()
    {
        startDate = LocalDate.parse("2020-01-01");
        endDate = LocalDate.parse("2020-01-15");
        
        boolean isBefore = endDate.isBefore(earliestDate);      //Checks if the end date is before the earliest date in the csv
        boolean isBeforeAlt = startDate.isBefore(earliestDate); //Checks if the start date is before the latest date in the csv
        
        boolean isAfterAlt = endDate.isAfter(latestDate);       //Checks if the end date is before the latest date in the csv
        boolean isAfter = startDate.isAfter(latestDate);        //Checks if the start date is after the latest date in the csv
        
        // Add data into the test HashMap and HashSet
        testBoroughs.add("Harrow");
        testBoroughs.add("Croydon");
        
        testMaps.put("Harrow", 0.0);
        testMaps.put("Croydon", 0.0);
        
        // Set the test HashMap and HashSet so that the "getAllDeaths" method will run on the given data.
        statsCounter.setBoroughs(testBoroughs);
        statsCounter.setDeathsMap(testMaps);
        
        // Actual return value when the method is run.
        double totalDeaths = statsPanel.getController().getAllDeaths();
        
        if((isAfter && isAfterAlt) || (isBefore && isBeforeAlt))
        {
            assertEquals(0.0, totalDeaths);
        }
    }
    
    /**
     * Test whether the average total cases will return 0 if a date range exceeds
     * the earliest/latest dates recorded in covid_london.csv.
     */
    @Test
    public void outOfRangeDates_AvgCases()
    {
        startDate = LocalDate.parse("2024-10-15");
        endDate = LocalDate.parse("2024-10-15");
        
        boolean isBefore = endDate.isBefore(earliestDate);      //Checks if the end date is before the earliest date in the csv
        boolean isBeforeAlt = startDate.isBefore(earliestDate); //Checks if the start date is before the latest date in the csv
        
        boolean isAfterAlt = endDate.isAfter(latestDate);       //Checks if the end date is before the latest date in the csv
        boolean isAfter = startDate.isAfter(latestDate);        //Checks if the start date is after the latest date in the csv
        
        // Add data into the test HashMap and HashSet
        testBoroughs.add("Croydon");
        testBoroughs.add("Lambeth");
        
        testMaps.put("Croydon", 0.0);
        testMaps.put("Lambeth", 0.0);
        
        // Set the test HashMap and HashSet so that the "getAvgCases" method will run on the given data.
        statsCounter.setBoroughs(testBoroughs);
        statsCounter.setCaseMap(testMaps);
        
        // Actual return value when the method is run.
        String avgCases = statsPanel.getController().getAvgCases();
        
        if((isAfter && isAfterAlt) || (isBefore && isBeforeAlt))
        {
            assertEquals("0", avgCases);
        }
    }
    
    /**
     * Test whether total deaths will return the correct value.
     */
    @Test
    public void inRange_TotalDeaths()
    {
        startDate = LocalDate.parse("2022-10-15");
        endDate = LocalDate.parse("2022-10-20");
        
        boolean isBefore = endDate.isBefore(earliestDate);      //Checks if the end date is before the earliest date in the csv
        boolean isBeforeAlt = startDate.isBefore(earliestDate); //Checks if the start date is before the latest date in the csv
        
        boolean isAfterAlt = endDate.isAfter(latestDate);       //Checks if the end date is before the latest date in the csv
        boolean isAfter = startDate.isAfter(latestDate);        //Checks if the start date is after the latest date in the csv
        
        // Add data into the test HashMap and HashSet
        testBoroughs.add("Harrow");
        testBoroughs.add("Islington");
        //testBoroughs.add("Croydon");
        
        testMaps.put("Harrow", 80.0);
        testMaps.put("Islington", 60.0);
        testMaps.put("Croydon", 70.0);
        
        // Set the test HashMap and HashSet so that the "getAllDeaths" method will run on the given data.
        statsCounter.setBoroughs(testBoroughs);
        statsCounter.setDeathsMap(testMaps);
        
        // Actual return value when the method is run.
        double totalDeaths = statsPanel.getController().getAllDeaths();
        
        if(!isAfter || !isBefore || !isAfterAlt || !isBeforeAlt)
        {
            assertEquals(140.0, totalDeaths);
        }
    }
    
    /**
     * Test whether average number of cases will return the correct value.
     */
    @Test
    public void inRangeDates_AvgCases()
    {
        startDate = LocalDate.parse("2022-10-15");
        endDate = LocalDate.parse("2022-10-16");
        
        boolean isBefore = endDate.isBefore(earliestDate);      //Checks if the end date is before the earliest date in the csv
        boolean isBeforeAlt = startDate.isBefore(earliestDate); //Checks if the start date is before the latest date in the csv
        
        boolean isAfterAlt = endDate.isAfter(latestDate);       //Checks if the end date is before the latest date in the csv
        boolean isAfter = startDate.isAfter(latestDate);        //Checks if the start date is after the latest date in the csv
        
        // Add data into the test HashMap and HashSet
        testBoroughs.add("Croydon");
        testBoroughs.add("Lambeth");
        //testBoroughs.add("Harrow");
        
        testMaps.put("Croydon", 12.0);
        testMaps.put("Lambeth", 8.0);
        testMaps.put("Harrow", 20.0);
        
        // Set the test HashMap and HashSet so that the "getAvgCases" method will run on the given data.
        statsCounter.setBoroughs(testBoroughs);
        statsCounter.setCaseMap(testMaps);
        
        // Actual return string when the method is run.
        String avgCases = statsPanel.getController().getAvgCases();
        
        if(!(isAfter && isAfterAlt) || !(isBefore && isBeforeAlt))
        {
            assertEquals("10", avgCases);
        }
    }
    
    /**
     * Test whether getBoroughs() returns the correct HashSet
     */
    @Test
    public void testGetBoroughs()
    {
        // Create an extra test HashSet to see whether the right HashSet is returned.
        HashSet<String> dummySet = new HashSet<>();
        dummySet.add("Croydon");
        dummySet.add("Lambeth");
        dummySet.add("Islington");
        
        // Add data into the test HashSet.
        testBoroughs.add("Harrow");
        testBoroughs.add("Lambeth");
        testBoroughs.add("Westminster");
        
        // Set the test HashSet so that the "getBoroughs" method will run on the given data.
        statsCounter.setBoroughs(testBoroughs);
        
        // Actual return result when the method is run.
        HashSet<String> actualHashSet = statsCounter.getBoroughs();
        
        assertEquals(testBoroughs, actualHashSet);
    }

        /**
     * Test whether the death rate will return the correct value.
     */
    @Test 
    public void testDeathRate()
    {
        // Add data into the test value and HashSet.
        testMaps.put("Islington", 60.0);
        testMaps.put("Harrow", 30.0);
        testMaps.put("Westminster", 50.0);
        testVal = 3.0;
        
        // Set the test HashSet and test value so that the "getBoroughs" method will run on the given data.
        statsCounter.setDeathsMap(testMaps);
        statsCounter.setHighestDeaths(testVal);
        
        // Actual return result when the method is run.
        double actualRate = statsCounter.getBoroughDeathRate("Islington");
        
        assertEquals(20,actualRate);
    }
}
