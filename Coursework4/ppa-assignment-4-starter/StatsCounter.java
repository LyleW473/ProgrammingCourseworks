import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Comparator;

/**
 * Class to count the stats for each borough within a specified date range. 
 * 
 * Used for button colours based on a borough's death rate during a specific date range.
 * 
 * All hash maps are shared by every StatsCounter instance. Every class with a StatsCounter instance is able to update the current state of the 
 * hashmap (but only the "CovidViewer" is necessary for updating them) and get the associated stats for a borough for a given date range.
 */
public class StatsCounter
{
    private static StatsCalculator statsCalc = new StatsCalculator(); // Calculator class for finding/calculating statistics.
    
    private static HashSet<String> boroughs; // All the boroughs with data within the specified date range.
    
    private static HashMap<String, Double> deathsMap = new HashMap<String, Double>(); // Maps boroughs to total death counts
    private static HashMap<String, Double> avgTotalCasesMap = new HashMap<String, Double>(); // Maps boroughs to average total cases
    private static HashMap<String, Double> avgGMRAcrossPeriodMap = new HashMap<String, Double>(); // Maps GMR types to the average GMR across the period
    
    // A list containing the all of the possible GMR types for the GMR statistics.
    public static final String[] allGMRTypes = new String[] {
                                                            "Retail and Recreation", 
                                                            "Grocery and Pharmacy", 
                                                            "Parks", 
                                                            "Transit", 
                                                            "Workplaces", 
                                                            "Residential"
                                                            };
    
    private static Double highestDeaths; // The highest number of total deaths between all boroughs within the specified date range.
    
    /**
     * Resets the death counter to its initial state to ensure counting accuracy.
     * @param loadedData The data for all boroughs within the specified date range.
     */
    private static void reset(ArrayList<CovidData> loadedData)
    {
        // Reset boroughs hashset
        boroughs = new HashSet<String>();
        
        // Reset the hashmaps to have default values of 0.0
        StatsCounter.deathsMap.clear();
        StatsCounter.avgTotalCasesMap.clear();
        StatsCounter.avgGMRAcrossPeriodMap.clear();
        for (CovidData d: loadedData)
        {
            String boroughName = d.getBorough();
            boroughs.add(boroughName);
        }
        for (String borough: boroughs)
        {
            StatsCounter.deathsMap.put(borough, 0.0);
            StatsCounter.avgTotalCasesMap.put(borough, 0.0);
        }
        for (String GMRType: StatsCounter.allGMRTypes)
        {
            StatsCounter.avgGMRAcrossPeriodMap.put(GMRType, 0.0);
        }
        
        // Reset highest deaths count (Assume worst-case scenario (Should be reset every time this is called))
        StatsCounter.highestDeaths = Double.MIN_VALUE; 
    }
    
    /**
     * Updates the total death count for each of the boroughs within the specified date range.
     */
    public static void update()
    {
        // Loading data
        CovidDataLoader dataLoader = new CovidDataLoader();
        ArrayList<CovidData> loadedData = dataLoader.load();
        
        // Sort the data by date (so that counting the total deaths is correct).
        loadedData.sort(Comparator.comparing(CovidData::getDate));
        
        LocalDate dataStartDate = LocalDate.parse(loadedData.get(0).getDate());
        LocalDate dataEndDate = LocalDate.parse(loadedData.get(loadedData.size() - 1).getDate());
        
        LocalDate startDate = DateContext.getFromDate();
        LocalDate endDate = DateContext.getToDate();
        
        // Reset the stats counter (Should be done so that counts are accurate).
        // Note: Should be reset even if an invalid date range has been specified, so that default values (0.0) are shown
        StatsCounter.reset(loadedData);
        
        // Check for invalid date ranges where there is no data for it
        if (startDate.isAfter(dataEndDate) || endDate.isBefore(dataStartDate))
        {
            return;
        }
        
        // Fill data hashmaps (Passing copies of references to the hashmaps to the function)
        HashMap<String, Integer> startDateDeaths = new HashMap<String, Integer>();
        HashMap<String, Integer> endDateDeaths = new HashMap<String, Integer>();
        HashMap<String, Integer> numRecordedPerBorough = new HashMap<String, Integer>(); // Number of records counted for each borough (used for average calculations)
        StatsCounter.fillDataHashmaps(loadedData, numRecordedPerBorough, startDateDeaths, endDateDeaths, startDate, endDate);
        
        // Find the total deaths for each borough within the date range (and the highest total death amongst all boroughs).
        Double newHighestDeaths = StatsCounter.statsCalc.findTotalDeaths(startDateDeaths, endDateDeaths, StatsCounter.deathsMap, StatsCounter.highestDeaths);
        StatsCounter.highestDeaths = newHighestDeaths;
        
        // Find the average total cases for each borough within the date range
        StatsCounter.statsCalc.findAverageTotalCases(numRecordedPerBorough, StatsCounter.avgTotalCasesMap);
        
        // Find the average GMR across all boroughs within the date range
        StatsCounter.statsCalc.findAverageGMRs(numRecordedPerBorough, StatsCounter.avgGMRAcrossPeriodMap, StatsCounter.allGMRTypes);
    }
    
    /**
     * Fills all of the data hashmaps with data within the specified date range. 
     * This method alters the original hashmaps passed in, by accessing copies of their references and adding data to them.
     * 
     * @param loadedData An array list containing data for all boroughs within the selected date range.
     * @param numRecordedPerBorough A hashmap that maps boroughs to the number of records in the dataset within the selected date range.
     * @param startDateDeaths A hashmap that maps boroughs to the date of the earliest record belonging to that borough, within the selected 
     * date range.
     * @param endDateDeaths A hashmap that maps boroughs to the date of the latest record belonging to that borough, within the selected date range.
     * @param startDate The start date of the COVID dataset.
     * @param endDate The end date of the COVID dataset.
     */
    private static void fillDataHashmaps(
                                        ArrayList<CovidData> loadedData, 
                                        HashMap<String, Integer> numRecordedPerBorough,
                                        HashMap<String, Integer> startDateDeaths, 
                                        HashMap<String, Integer> endDateDeaths,
                                        LocalDate startDate,
                                        LocalDate endDate
                                        )
        {
        // Add information to associated hashmaps
        for (CovidData d: loadedData)
        {
            LocalDate dataDate = LocalDate.parse(d.getDate());
            Boolean withinDates = dataDate.isAfter(startDate) && dataDate.isBefore(endDate);
            Boolean equalsDates = dataDate.isEqual(startDate) || dataDate.isEqual(endDate);
            
            String boroughName = d.getBorough();
            
            // Not within the date range (meaning some boroughs might not be registered in the hashmaps)
            if (!(withinDates || equalsDates))
            {
                continue;
            }
            
            // --- Total deaths  ---
            // Find the earliest point in time
            if (!startDateDeaths.containsKey(boroughName))
            {
                startDateDeaths.put(boroughName, d.getTotalDeaths());
            }
            // Keep updating ending date stats, to get the latest stats
            endDateDeaths.put(boroughName, d.getTotalDeaths()); 

            // --- Average total cases ---
            // Add total cases to the number of total cases for this borough during this period
            double oldTotalCases = StatsCounter.avgTotalCasesMap.getOrDefault(boroughName, 0.0);
            StatsCounter.avgTotalCasesMap.put(boroughName, oldTotalCases + d.getTotalCases());
            
            // GMRs
            for (String GMRType: StatsCounter.allGMRTypes)
            {
                Double oldVal = StatsCounter.avgGMRAcrossPeriodMap.getOrDefault(GMRType, 0.0);
                Double newVal = oldVal + d.getGMRValues(GMRType);
                StatsCounter.avgGMRAcrossPeriodMap.put(GMRType, newVal);
            }

            // Increment number of records per borough
            int oldRecorded = numRecordedPerBorough.getOrDefault(boroughName, 0);
            numRecordedPerBorough.put(boroughName, oldRecorded + 1);
        }
    }
    
    /**
     * @return The normalised version of the passed in death count (value between 0 and 1).
     * @param oldVal The value to normalise.
     * Used by the BoroughButton class to get the normalised values (for death rates).
     */
    private static double normaliseCount(Double oldVal)
    {
        double newVal;
        
        // The highest death count is 0 or is the smallest number possible, meaning all boroughs should have the same colour.
        if (StatsCounter.highestDeaths == 0 || StatsCounter.highestDeaths == Double.MIN_VALUE)
        {
            newVal = 0;
        }
        else
        {
            newVal = oldVal / StatsCounter.highestDeaths;
        }
        
        return newVal;
    }
    
    /**
     * @return The death rate associated with the specified borough within a specified date range.
     * @param borough The borough to find the death rate for.
     * For boroughs that do not have data within the date range, return 0.0 by default.
     */
    public Double getBoroughDeathRate(String borough)
    {
        Double totalDeaths = StatsCounter.deathsMap.getOrDefault(borough, 0.0);
        Double normTotalDeaths = StatsCounter.normaliseCount(totalDeaths);
        return normTotalDeaths;
    }
    
    /**
     * @return The total deaths associated with the specified borough within a specified date range.
     * @param borough The borough to find the total deaths for.
     * For boroughs that do not have data within the date range, return 0.0 by default.
     */
    public Double getBoroughTotalDeaths(String borough)
    {
        return StatsCounter.deathsMap.getOrDefault(borough, 0.0);
    }
    
     /**
     * @return The average total cases associated with the specified borough within a specified date range.
     * @param borough The borough to find the average total cases for.
     * For boroughs that do not have data within the date range, return 0.0 by default.
     */
    public Double getBoroughAvgTotalCases(String borough)
    {
        return StatsCounter.avgTotalCasesMap.getOrDefault(borough, 0.0);
    }
    
    /**
     * @return The average GMR value for a specified GMR type across a specific date range across all boroughs.
     * @param GMRType The GMR type to find the total GMR value for.
     * Returns 0.0 by default (All GMR types are set to 0.0 by default).
     */
    public Double getAvgGMRAcrossBoroughs(String GMRType)
    {
        return StatsCounter.avgGMRAcrossPeriodMap.get(GMRType);
    }
    
    /**
     * @return The boroughs that have COVID data within the specified date range.
     */
    public HashSet<String> getBoroughs()
    {
        return StatsCounter.boroughs;
    }
    
    // Functions used for the testing class:
    
    /**
     * Sets the list of boroughs to the passed in list.
     * @param testBoroughs The list of boroughs to set the current list to.
     */
    public void setBoroughs(HashSet<String> testBoroughs)
    {
        StatsCounter.boroughs = testBoroughs;
    }
    
    /**
     * Sets the total deaths hashmap to the passed in hashmap.
     * @param testMaps The hashmap to set the current total deaths hashmap to.
     */
    public void setDeathsMap(HashMap<String, Double> testMaps)
    {
        this.deathsMap = testMaps;
    }
    
    /**
     * Sets the total cases hashmap to the passed in hashmap.
     * @param testMaps The hashmap to set the current total cases hashmap to.
     */
    public void setCaseMap(HashMap<String, Double> testMaps)
    {
        this.avgTotalCasesMap = testMaps;
    }
    
    /**
     * Sets the highest deaths attribute to the passed in value.
     * @param testVal The value to set the highest deaths to.
     */
    public void setHighestDeaths(double testVal)
    {
        this.highestDeaths = testVal;
    }
}
