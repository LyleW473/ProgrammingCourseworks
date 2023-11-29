package dependencies.entities;

import java.util.ArrayList;

public class Enemy 
{   
    private static ArrayList<Enemy> allEnemies = new ArrayList<Enemy>(); // List containing all instantiated enemies
    private ArrayList<Room> pathToTraverse = new ArrayList<Room>();
    private int currentPathIndex = 0; // Starts at the first room given in the path

    public Enemy(ArrayList<Room> entireTraversalPath)
    {
        pathToTraverse = entireTraversalPath;

        // Add this enemy to list of all enemies
        Enemy.allEnemies.add(this);
    }

    /**
     * Getter method for accessing a list containing all instantiated enemies
     */
    public static ArrayList<Enemy> getAllEnemies()
    {
        return Enemy.allEnemies;
    }

    /**
     * Getter method for accessing a list containing all instantiated enemies
     */
    public static void moveAllEnemies()
    {
        // Move enemy after every command
        for (Enemy e: Enemy.getAllEnemies())
        {
            System.out.println("<< " + e.returnCurrentRoom().getShortDescription() + " >>");
            e.move();
        }
    }

    /**
     * @return the current room that this enemy is in
     */
    public Room returnCurrentRoom()
    {
        return pathToTraverse.get(currentPathIndex);
    }

    /**
     * Moves the enemy into the next room in their set path to traverse
     */
    public void move()
    {   
        // Increment index 
        currentPathIndex++;

        // If this is the last room in the path, cycle back to the start
        if (currentPathIndex == pathToTraverse.size())
        {
            currentPathIndex = 0;
        }
    }

    /**
     * @return the next room that this enemy will move to
     * - Used as part of the functionality for magic transporter room, to ensure that the player isn't teleported to a location where an enemy is about to go
     */
    public Room getNextRoom()
    {
        return pathToTraverse.get((currentPathIndex + 1) % pathToTraverse.size());
    }
}
