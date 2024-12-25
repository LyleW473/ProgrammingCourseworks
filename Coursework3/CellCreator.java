import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class responsible for creating "Cell" objects and objects from all subclasses of "Cell".
 * Gives precedence to certain cells to ensure that behaviour is consistent and working as 
 * intended, for example, ensuring that all "DiseaseCell" cells "act" first, to ensure that
 * disease spreads.
 *
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

public class CellCreator 
{
    private ArrayList<Method> createMethods; // List containing every method to create cells
    private int numCreateMethods;
    private Random rand; // Shared random number generator
    private int numDiseaseCellsCreated;

    /**
     * Instantiate a CellCreator, to create Cell objects (and objects of its subclasses).
     *
     * @param rand The shared random number generator.
     */
    public CellCreator(Random rand)
    {
        this.createMethods = CellCreator.findCreateMethods();
        this.numCreateMethods = createMethods.size();
        this.rand = rand;
        this.numDiseaseCellsCreated = 0;
    }

    /**
     * Finds and stores all methods in the CellCreator class which are used to create cells inside an ArrayList<Method>
     * Gives precedence to the "createDiseaseCell" method, to ensure that "Disease" is working as intended.
     * @return A list of Method objects which will be invoked to create cell objects of any defined type.
     */
    public static ArrayList<Method> findCreateMethods()
    {
        Method[] methods = CellCreator.class.getDeclaredMethods();
        ArrayList<Method> createMethods = new ArrayList<Method>();

        // Add methods in the defined order: 
        // (allows for particular behaviour, e.g., cells getting infected by
        // DiseaseCell cells before other cells perform their own behaviour)
        String[] methodNames = new String[] {
                                            "createDiseaseCell",
                                            "createPurgerCell",
                                            "createChaosCell",
                                            "createCleansingCell",
                                            "createChameleonCell",
                                            "createEvolvingCell"
                                            };
                                            
        for (String methodName: methodNames)
        {
            for (int i = 0; i < methods.length; i++)
            {
                Method method = methods[i];
                if ((method.getName().equals(methodName)))
                {
                    createMethods.add(method);
                    break;
                }
            }
        }
        return createMethods;
    }

    /**
     * Resets CellCreator to starting position.
     */
    public void reset()
    {
        this.numDiseaseCellsCreated = 0;
    }
    /**
     * Generates and returns a random Cell object (including any subclasses of Cell) using the stored create methods.
     * @param row Row number of the location that the cell will be spawned in.
     * @param col Column number of the location that the cell will be spawned in.
     */
    public Cell generateRandomCell(int row, int col)
    {      
        int index;
        Cell cell;
        
        // Have not generated the maximum number of DiseaseCell instances
        if (numDiseaseCellsCreated < DiseaseCell.MAXIMUM_NUM_CELLS)
        {
            index = 0; // Index for "createDiseaseCell" method.
            numDiseaseCellsCreated ++;
        }
        else
        {
            index = rand.nextInt(numCreateMethods - 1) + 1; // Not DiseaseCell 
        }

        Method createMethod = createMethods.get(index);

        // Try create a cell.
        try 
        {
            cell = (Cell) createMethod.invoke(this, row, col); // Cast to Cell from Object
        }
        // Cannot make this cell, then by default create Mycoplasma.
        catch (Exception e)
        {  
            e.printStackTrace();
            cell = createMycoplasma(row, col);
        }
        return cell;

    }  

    /**
     * Creates and returns a Mycoplasma object.
     * @return The Mycoplasma object.
     */
    public Mycoplasma createMycoplasma(int row, int col)
    {
        return new Mycoplasma(row, col);
    }   

    /**
     * Creates and returns a ChameleonCell object.
     * @return The ChameleonCell object.
     */
    public ChameleonCell createChameleonCell(int row, int col)
    {
        return new ChameleonCell(row, col);
    }

    /**
     * Creates and returns a EvolvingCell object.
     * @return The EvolvingCell object.
     */
    public EvolvingCell createEvolvingCell(int row, int col)
    {
        return new EvolvingCell(row, col);
    }

    /**
     * Creates and returns a DiseaseCell object.
     * @return The DiseaseCell object.
     */
    public DiseaseCell createDiseaseCell(int row, int col)
    {
        return new DiseaseCell(row, col);
    }

    /**
     * Creates and returns a ChaosCell object.
     * @return The ChaosCell object.
     */
    public ChaosCell createChaosCell(int row, int col)
    {
        return new ChaosCell(row, col);
    }

    /**
     * Creates and returns a PurgerCell object.
     * @return The PurgerCell object.
     */
    public PurgerCell createPurgerCell(int row, int col)
    {
        return new PurgerCell(row, col);
    }

    /**
     * Creates and returns a CleansingCell object.
     * @return The CleansingCell object.
     */
    public CleansingCell createCleansingCell(int row, int col)
    {
        return new CleansingCell(row, col);
    }
}
