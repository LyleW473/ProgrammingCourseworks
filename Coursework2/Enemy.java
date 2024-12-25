/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 * 
 * 
 *  This class is tbe class for the enemies in my game (i.e., the maids).
 * 
 *  Each enemy has a move interval and counter, and will only move after 
 *  a successful command from the player.
 * 
 *  All enemies are given a defined path to traverse (which is a list of 
 *  Room objects), which is set when all of the Enemy objects are created. 
 *  All traversal paths are cycles, so the maidspatrol a certain area "endlessly".
 * 
 *  Enemies are spawned within the Game class.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.ArrayList;

public class Enemy 
{   
    private static ArrayList<Enemy> allEnemies = new ArrayList<Enemy>(); // List containing all instantiated enemies

    private ArrayList<Room> pathToTraverse; // A defined path (a list of rooms) that this Enemy will traverse
    private int currentPathIndex = 0; // Starts at the first room given in the path
    private int moveCounter = 1; // Counter which will be updated to move at the defined moveInterval
    private int moveInterval; // How often the enemy will move

    /**
     * Constructor for Enemy class. Initialises the Enemy object with a defined traversal path and an interval for them to move in.
     * @param entireTraversalPath An ordered list of rooms which the enemy will traverse.
     * @param moveInterval The interval in which the Enemy will move in, i.e., how often the enemy will move (e.g., after every 2 turns, 3 turns, etc...).
     * - A "turn" is defined as a successful command from the user / player.
     */
    public Enemy(ArrayList<Room> entireTraversalPath, int moveInterval)
    {  
        // Initialise attributes
        pathToTraverse = entireTraversalPath;
        this.moveInterval = moveInterval;

        // Add this enemy to list of all enemies
        Enemy.allEnemies.add(this);
    }

    /**
     * @return An ArrayList<Enemy> containing all instantiated Enemy objects.
     */
    public static ArrayList<Enemy> getAllEnemies()
    {
        return Enemy.allEnemies;
    }

    /**
     * Method for moving all instantiated Enemy objects to their next location in their respective traversal paths.
     */
    public static void moveAllEnemies()
    {
        // Move all enemies
        for (Enemy e: Enemy.getAllEnemies())
        {
            e.move();
        }
    }
    
    /**
     * Method for displaying the current locations of all enemies.
     * E.g.
     *      << Maid 1 is in the main hallway! >>
     *      << Maid 2 is in the art room! >>
     *      << Maid 3 is in the attic! >>
     */
    public static void displayEnemyLocations()
    {
        // Move enemy after every command
        for (int i = 0; i < Enemy.allEnemies.size(); i++)
        {   
            Enemy e = Enemy.allEnemies.get(i);
            System.out.println("<< Maid " + (i + 1) + " is " + e.getCurrentRoom().getShortDescription() + "! >>");
        }
        System.out.println();
    }

    /**
     * @return The current room that this enemy is in.
     */
    public Room getCurrentRoom()
    {
        return pathToTraverse.get(currentPathIndex);
    }

    /**
     * Moves the enemy into the next room in their defined traversal path.
     */
    public void move()
    {   
        // If enough turns have passed for this enemy to move
        if (moveCounter == moveInterval)
        {   
            // Reset move counter
            moveCounter = 1;

            // Increment index 
            currentPathIndex++;

            // If this is the last room in the path, cycle back to the start of the traversal path
            if (currentPathIndex == pathToTraverse.size())
            {
                currentPathIndex = 0;
            }
        }
        else
        {
            // Increment the move counter
            moveCounter ++;
        }
    }

    /**
     * @return The next room that this enemy will move to.
     * - Used as part of the functionality for magic transporter room, to ensure that the player isn't teleported to a location where an enemy is about to go
     */
    public Room getNextRoom()
    {
        return pathToTraverse.get((currentPathIndex + 1) % pathToTraverse.size());
    }
}