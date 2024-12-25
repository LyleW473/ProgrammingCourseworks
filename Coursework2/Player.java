/**
 *  Student K-number: 22039642
 *  Student full name: Gee-Lyle Wong
 * 
 * 
 *  This class is tbe class for the Player, and contains data relevant to the player.
 *  There are getter methods to modify and retrieve information relevant to the player
 *  as well as "helper" methods that are executed upon a valid command within the Game 
 *  class, for example "dropArtifact" for dropping an artifact out of the player's 
 *  inventory.
 * 
 * It contains the following data:
 *  - The current room the player is in
 *  - History of rooms the player has visited
 *  - Inventory (of collected artifacts)
 *  - Weight of the inventory
 *  - Number of artifacts successfully dropped off (objective).
 *  - The weight limit for the player's inventory.
 * 
 *  The player is created and spawned from within the Game class.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.ArrayList;

public class Player 
{
    private static final double INVENTORY_WEIGHT_LIMIT = 5.0; // Maximum player inventory weight

    private Room currentRoom; // The current room that the player is in.
    private ArrayList<Room> roomsHistory = new ArrayList<Room>(); // List storing a history of all the rooms the player has visited (in order).
    private ArrayList<Artifact> inventory = new ArrayList<Artifact>(); // List containing the artifacts that the player has collected so far.
    private double inventoryWeight = 0.0; // Current total weight of the player's inventory.
    private int numCompletedArtifacts = 0; // Number of artifacts successfully dropped off at the goal room.
    
    /**
     * Constructor for Player class. Initialises the Player object and spawns them in a passed-in spawning location / position.
     * @param spawningLocation The room that this Player object should be spawned in.
     */
    public Player(Room spawningLocation)
    {
        setCurrentRoom(spawningLocation); // Spawn the player in the selected location
    }

    /**
     * @return The number of artifacts "completed", i.e., successfully dropped off at the goal room.
     */
    public int getNumCompletedArtifacts()
    {
        return numCompletedArtifacts;
    }

    /**
     * @return The current room that the player is in.
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * @return The history of all the rooms that the player has visited.
     */
    public ArrayList<Room> getRoomHistory()
    {
        return roomsHistory;
    }

    /**
     * @return The last room inside of the room history (i.e., the previous room the player was in).
     */
    public Room getPreviousRoom()
    {      
        // Create a new room history (every room in the list apart from the previous room)
        int historyLength = roomsHistory.size();
        ArrayList<Room> newHistory = new ArrayList<Room>();
        for (int i = 0; i < historyLength - 1; i++)
        {
            newHistory.add(roomsHistory.get(i));
        }

        // Save the previous room (And then return it after updating the room history)
        Room previousRoom = roomsHistory.get(roomsHistory.size() - 1);
        
        // Update the room history
        roomsHistory = newHistory;

        return previousRoom;
    }

    /**
     * @return The inventory, which is a list containing all of the player's collected artifacts.
     */
    public ArrayList<Artifact> getInventory()
    {
        return inventory;
    }

    /**
     * @return The current total weight of the player's inventory.
     */
    public double getInventoryWeight()
    {
        return inventoryWeight;
    }

    /**
     * Sets the current room the player is in to the selected room.
     * @param selectedRoom The room selected to change the player's current room to
     */
    public void setCurrentRoom(Room selectedRoom)
    {
        currentRoom = selectedRoom;
    }

    /**
     * Adds a room to the room history.
     * @param roomToAdd The room to add to the player's room history
     */
    public void addToRoomHistory(Room roomToAdd)
    {
        roomsHistory.add(roomToAdd);
    }

    /**
     * Clears the history of rooms that the player has visited.
     * - Executed after the player enters the magic attic / transporter room.
     * - This is an intended side effect of teleporting.
     */
    public void clearRoomsHistory()
    {
        roomsHistory.clear();
    }

    /**
     * Prints out in the terminal, the details of each artifact inside of the player's inventory i.e.,:
     * - The current weight of inventory.
     * - The weight limit set for the inventory.
     */
    public void showInventoryContents()
    {
        // Display details for each artifact inside of the inventory
        System.out.println("<< Inventory >>");
        for (int i = 0; i < inventory.size(); i++)
        {   
            inventory.get(i).printDetails(i);
        }
        // Display current total weight of the player's inventory and the set weight limit
        System.out.println("Current total weight: " + inventoryWeight);
        System.out.println("Weight limit: " + INVENTORY_WEIGHT_LIMIT);
        System.out.println();
    }

    /**
     * Adds an artifact to the player's inventory, and updates the inventory weight accordingly.
     * @param artifactToCollect The artifact that the player just collected, which will be added to the inventory.
     * @param newWeight The new weight of the inventory, after adding the artifact to collect.
     */
    public void addToInventory(double newWeight, Artifact artifactToCollect)
    {
        inventoryWeight = newWeight; // Update inventory weight
        inventory.add(artifactToCollect); // Add artifact to inventory
        System.out.println("The '" + artifactToCollect.getName() +  "' artifact has been added to your inventory!\n");
    }
    
    /**
     * Removes an artifact from the player's inventory, and updates the inventory weight accordingly.
     * @param itemIndex The index the player used in their "drop" command, specifying which artifact in their inventory to drop.
     * @return The artifact that was dropped (which can then be assigned to the room that it was dropped in).
     */
    public Artifact removeFromInventory(int itemIndex)
    {
        Artifact artifactToDrop = inventory.get(itemIndex);
        inventoryWeight -= artifactToDrop.getWeight(); // Update inventory weight
        inventory.remove(itemIndex); // Remove artifact from inventory
        System.out.println("Successfully dropped '" + artifactToDrop.getName() + "'!\n");
        return artifactToDrop;
    }

    /**
     * Collects an artifact from the current room the player is in, if one exists.
     * - Called through the Game class whenever a "collect artifact" command is used.
     */
    public void collectArtifact()
    {
        // Check if there is an artifact in this room
        if (!currentRoom.hasArtifact())
        {
            System.out.println("There is no artifact to collect in this room!");
        }
        else
        {   
            Artifact artifactToCollect = currentRoom.getAssignedArtifact(0); // Take the first artifact in the room
            double newTotalWeight = inventoryWeight + artifactToCollect.getWeight();

            // Check if the collecting the artifact will exceed the weight restriction
            if (newTotalWeight > INVENTORY_WEIGHT_LIMIT)
            {   
                System.out.println("Cannot pick up this artifact as you will exceed the weight limit of " + INVENTORY_WEIGHT_LIMIT + "!\nCurrent total weight: " + inventoryWeight + "\n");
                // Exit at the bottom of method 
            }
            else
            {
                // Add artifact to the player's inventory
                this.addToInventory(newTotalWeight, artifactToCollect);

                // Remove artifact from the room
                currentRoom.removeArtifact(artifactToCollect);
            }
        }
    }
    
    /**
     * Drops an artifact from the player's inventory based on the itemIndex defined by the player in their "drop" command.
     * @param secondWord The second word that the player used within their "drop" command, which should be the index of the artifact that the player wants to drop.
     * @param totalArtifactsSpawned The total number of artifacts that were spawned into the game world.
     * @return A boolean indicating whether dropping the artifact specified by the player is possible.
     */
    public boolean dropArtifact(String secondWord, int totalArtifactsSpawned)
    {
        int inventorySize = inventory.size();

        // Check for empty inventory
        if (inventorySize == 0)
        {
            System.out.println("You have no items in your inventory!");
            return false;
        }
        else
        {
            // Check whether the index is in between in the range of the number of items in the inventory.
            int itemIndex = Integer.parseInt(secondWord); // If this fails, then the exception is caught within Game.dropArtifact() method call
            if (itemIndex >= 0 && itemIndex < inventorySize)
            {
                // Drop item into this room and remove from inventory
                Artifact artifactToDrop = this.removeFromInventory(itemIndex);

                // If this is the goal room (i.e., outside)
                if (Room.isGoalRoom(currentRoom))
                {  
                    // Note: Don't re-assign artifact to this room (as it is the goal room)
                    numCompletedArtifacts ++; // Increment number of artifacts dropped off successfully at the goal room
                    System.out.println("<< You have successfully dropped off " + numCompletedArtifacts + "/" + totalArtifactsSpawned + " artifacts! >>\n");
                }
                else 
                {
                    // Re-assign artifact to this room
                    currentRoom.addArtifact(artifactToDrop);
                }
                return true;
            }
        }
        // Case: If the index is out range then skip to bottom of method
        return false;
    }
}