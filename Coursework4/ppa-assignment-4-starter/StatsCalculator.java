import java.util.HashMap;

/**
 * Class containing the methods for finding and calculating specific statistics for the StatsCounter class.
 */
public class StatsCalculator
{
    /**
     * Finds the total deaths for each borough based on the total deaths on the starting and end dates for that borough.
     * @param startDateDeaths Hashmap that maps the borough to the total deaths on the start date.
     * @param endDateDeaths Hashmap that maps the borough to the total deaths on the end date.
     * @param deathsMap Reference to the hashmap that maps boroughs to the total number of deaths for that borough within the specified date range.
     * @return The highest number of total deaths between the selected date range across all boroughs.
     */
    public double findTotalDeaths(
                                HashMap<String, Integer> startDateDeaths, 
                                HashMap<String, Integer> endDateDeaths,
                                HashMap<String, Double> deathsMap,
                                double highestDeaths
                                )
    {
        // Find the total deaths for each borough within the date range
        for (String borough: startDateDeaths.keySet())
        {
            int startDeaths = startDateDeaths.get(borough);
            int endDeaths = endDateDeaths.get(borough);
            double totalDeaths = endDeaths - startDeaths;
            
            // Check for lowest and highest deaths
            if (totalDeaths > highestDeaths)
            {
                highestDeaths = totalDeaths;
            }
            
            deathsMap.put(borough, totalDeaths);
        }
        
        return highestDeaths;
    }
    
    /**
     * Finds the average total cases for each borough based on the cumulative total cases between that period, divided by the length of the date range.
     * @param numRecordedPerBorough A hashmap mapping boroughs to the number of records belonging to it between the date range.
     * @param avgTotalCasesMap Reference to the hashmap that maps boroughs to the average total cases for that borough within the specified date range.
     */
    public void findAverageTotalCases(
                                      HashMap<String, Integer> numRecordedPerBorough,
                                      HashMap<String, Double> avgTotalCasesMap
                                      )
    {
        for (String borough: avgTotalCasesMap.keySet())
        {
            double sumTotalCasesBetweenPeriod = avgTotalCasesMap.get(borough);
            int numRecordsForBorough = numRecordedPerBorough.getOrDefault(borough, 0);
            double avgTotalCases = sumTotalCasesBetweenPeriod / numRecordsForBorough;
            avgTotalCasesMap.put(borough, avgTotalCases);
        }
    }
    
    /**
     * Finds the average GMR values amongst all boroughs for all GMR types between the specified date range.
     * @param numRecordedPerBorough A hashmap mapping boroughs to the number of records belonging to it between the date range.
     */
    public void findAverageGMRs(
                                HashMap<String, Integer> numRecordedPerBorough,
                                HashMap<String, Double> avgGMRAcrossPeriodMap,
                                String[] allGMRTypes
                                )
    {
        // Find the total number of records between the specified date range, across all boroughs.
        int totalRecords = numRecordedPerBorough.values().stream().mapToInt(Integer::intValue).sum();
        
        for (String GMRType: allGMRTypes)
        {
            double sumGMR = avgGMRAcrossPeriodMap.get(GMRType);
            double avgGMRAcrossBoroughs = sumGMR / totalRecords;
            avgGMRAcrossPeriodMap.put(GMRType, avgGMRAcrossBoroughs);
        }
    }
}
