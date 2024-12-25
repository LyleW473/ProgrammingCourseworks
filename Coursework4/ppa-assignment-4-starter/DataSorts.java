    import java.util.Comparator;
    
    /**
     * Interface that provides methods to retrieve the comparators for sorting data by specific columns.
     */
    public interface DataSorts
    {
        
        public static Comparator sortByDate()
        {
            return Comparator.comparing(CovidData::getDate);
        }
        
        public static Comparator sortByNewCases()
        {
            return Comparator.comparing(CovidData::getNewCases);
        }
        
        public static Comparator sortByTotalCases()
        {
            return Comparator.comparing(CovidData::getTotalCases);
        }
        
        public static Comparator sortByNewDeaths()
        {
            return Comparator.comparing(CovidData::getNewDeaths);
        }
        
        public static Comparator sortByTotalDeaths()
        {
            return Comparator.comparing(CovidData::getTotalDeaths);
        }
        
        public static Comparator sortByRetailRecreationGMR()
        {
            return Comparator.comparing(CovidData::getRetailRecreationGMR);
        }
        
        public static Comparator sortByGroceryPharmacyGMR()
        {
            return Comparator.comparing(CovidData::getGroceryPharmacyGMR);
        }
        
        public static Comparator sortByParksGMR()
        {
            return Comparator.comparing(CovidData::getParksGMR);
        }
    
        public static Comparator sortByTransitGMR()
        {
            return Comparator.comparing(CovidData::getTransitGMR);
        }
        
        public static Comparator sortByWorkplacesGMR()
        {
            return Comparator.comparing(CovidData::getWorkplacesGMR);
        }
        
        public static Comparator sortByResidentialGMR()
        {
            return Comparator.comparing(CovidData::getResidentialGMR);
        }
    }
