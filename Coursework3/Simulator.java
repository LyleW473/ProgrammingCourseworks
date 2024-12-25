import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * A Life (Game of Life) simulator, first described by British mathematician
 * John Horton Conway in 1970.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2024.02.03
 */

public class Simulator {

    public static final int GRID_WIDTH = 50; //100;
    public static final int GRID_HEIGHT = 40; //80; 
    private List<Cell> cells;
    private Field field;
    private int generation;
    private CellCreator cellCreator;
    private Random rand;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(Simulator.GRID_HEIGHT, Simulator.GRID_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        cells = new ArrayList<>();
        rand = new Random();
        field = new Field(rand, depth, width);
        cellCreator = new CellCreator(rand);
        reset();
    }

    /**
     * Run the simulation from its current state for a single generation.
     * Iterate over the whole field updating the state of each life form.
     */
    public void simOneGeneration() {
        generation++;
        for (Cell cell : cells) {
          cell.act();
          cell.updateState();
        }
        field.updateCellNeighbours();
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        generation = 0;
        cells.clear();
        cellCreator.reset();
        populate();
    }

    /**
     * Randomly populate the field live/dead life forms
     */
    private void populate() {
      field.clear();
    
      int idx = 0;
      Location[] locations = new Location[field.getDepth() * field.getWidth()];
      for (int row = 0; row < field.getDepth(); row++) {
        for (int col = 0; col < field.getWidth(); col++) {
          locations[idx] = new Location(row, col);
          idx ++;
        }
      }

      Collections.shuffle(Arrays.asList(locations));
      for (Location loc: locations)
      {
        int row = loc.getRow();
        int col = loc.getCol();
        Cell cell = cellCreator.generateRandomCell(row, col); // Generate a cell of a random type
        cells.add(cell);
        field.place(cell, row, col); // Place cell onto the field
      }
      field.updateCellNeighbours(); // Update cell neighbours
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
    /**
     * Return the shared Field object.
     * @return The shared Field object.
     */
    public Field getField() {
        return field;
    }

    /**
     * Return the current generation (number).
     * @return The current generation (number).
     */
    public int getGeneration() {
        return generation;
    }

}
