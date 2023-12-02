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
     * Method for moving all of their enemies to their next location in their set traversal path
     */
    public static void moveAllEnemies()
    {
        // Move enemy after every command
        for (Enemy e: Enemy.getAllEnemies())
        {
            e.move();
        }
    }

    /**
     * Method for displaying the current locations of all enemies
     */
    public static void displayEnemyLocations()
    {
        // Move enemy after every command
        for (int i = 0; i < Enemy.allEnemies.size(); i++)
        {   
            Enemy e = Enemy.allEnemies.get(i);
            System.out.println("<< Maid " + (i + 1) + " is " + e.returnCurrentRoom().getShortDescription() + "! >>");
        }
    }

    /**
     * Creates enemies, passing in a defined path to traverse for each enemy (manually created for each enemy).
     */
    public static void spawnEnemies(Room gamesRoom, Room artRoom, Room diningRoom, Room livingRoom, Room mainHallway, Room kitchen, Room attic, Room hallway3, Room bedroom2, Room hallway2, Room bedroom1)
    {
        // Define traversal paths for each enemy
        ArrayList<Room> enemy1Path = new ArrayList<Room>()
                                                        {{
                                                            add(mainHallway);
                                                            add(kitchen);
                                                            add(gamesRoom);
                                                            add(artRoom);
                                                            add(diningRoom);
                                                            add(livingRoom);
                                                        }};
        ArrayList<Room> enemy2Path = new ArrayList<Room>()
                                                        {{
                                                            add(artRoom);
                                                            add(diningRoom);
                                                            add(livingRoom);
                                                            add(mainHallway);
                                                            add(kitchen); 
                                                            add(gamesRoom);
                                                        }};
        ArrayList<Room> enemy3Path = new ArrayList<Room>()
                                                        {{
                                                            add(attic);
                                                            add(hallway3);
                                                            add(bedroom2); 
                                                            add(hallway3);
                                                            add(hallway2);
                                                            add(bedroom1);
                                                            add(hallway2);
                                                            add(hallway3);
                                                            add(bedroom2);
                                                            add(hallway3);
                                                        }};

        // Create/instantiate enemies
        new Enemy(enemy1Path);
        new Enemy(enemy2Path);
        new Enemy(enemy3Path);
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
