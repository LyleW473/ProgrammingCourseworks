import java.time.LocalDate;

/**
 * A shared class between the entire program to access the currently selected from and to dates.
 */
public class DateContext
{
    // Selected start/from and end/to dates.
    private static LocalDate selectedFromDate;
    private static LocalDate selectedToDate;

    public static LocalDate getFromDate()
    {
        return selectedFromDate;
    }
    
    public static LocalDate getToDate()
    {
        return selectedToDate;
    }
    
    public static void updateFromDate(LocalDate fromDate)
    {
        selectedFromDate = fromDate;
    }
    
    public static void updateToDate(LocalDate toDate)
    {
        selectedToDate = toDate;
    }
}
