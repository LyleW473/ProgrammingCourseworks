package dependencies.entities;

import java.util.ArrayList;

public class Player 
{
    private static final double INVENTORY_WEIGHT_LIMIT = 5.0; // Total amount of weight that the player can have

    private Room currentRoom;
    private ArrayList<Artifact> inventory = new ArrayList<Artifact>();
    private double inventoryWeight = 0.0; // Current total weight of the player's inventory
    private ArrayList<Room> roomsHistory = new ArrayList<Room>(); // Stores the history of all the rooms the player has visited (in order)
    private int numCompletedArtifacts = 0; // Number of artifacts successfully dropped off at the goal room
    
    /**
     * @return the number of artifacts "completed", i.e., successfully dropped off at the goal room.
     */
    public int getNumCompletedArtifacts()
    {
        return numCompletedArtifacts;
    }

    /**
     * @return the current room that the player is in
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Sets the current room the player is in to the selected room
     * @param selectedRoom is the room which is selected to be the current room the player is in
     */
    public void setCurrentRoom(Room selectedRoom)
    {
        currentRoom = selectedRoom;
    }

    /**
     * @return the history of all the rooms that the player has visited
     */
    public ArrayList<Room> getRoomHistory()
    {
        return roomsHistory;
    }

    /**
     * Adds a room to the room history
     */
    public void addToRoomHistory(Room roomToAdd)
    {
        roomsHistory.add(roomToAdd);
    }

    /**
     * @return the last room inside of the room history (i.e., the previous room the player was in)
     */
    public Room returnPrevious()
    {      
        // Create a new room history (every room in the list apart from the previous room)
        int historyLength = roomsHistory.size();
        ArrayList<Room> newHistory = new ArrayList<Room>();
        for (int i = 0; i < historyLength - 1; i++)
        {
            newHistory.add(roomsHistory.get(i));
        }

        // Save the previous room (And then return it after updating history)
        Room previousRoom = roomsHistory.get(roomsHistory.size() - 1);
        
        // Update the room history
        roomsHistory = newHistory;

        return previousRoom;
    }

    /**
     * Clears the history of rooms that the player has visited
     * - Executed after the player enters the magic attic / transporter room.
     * - This is an intended side effect of teleporting.
     */
    public void clearRoomsHistory()
    {
        roomsHistory.clear();
    }

    /**
     * @return the inventory containing all of the player's collected artifacts
     */
    public ArrayList<Artifact> getInventory()
    {
        return inventory;
    }

    /**
     * @return the current total weight of the player's inventory
     */
    public double getInventoryWeight()
    {
        return inventoryWeight;
    }

    /**
     * Prints out: the details of each artifact inside of the player's inventory, current weight of inventory, weight limit set for the inventory, in the terminal
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
    }

    /**
     * Adds an artifact to the player's inventory, and updates the inventory weight accordingly
     * @param artifactToCollect is the artifact that the player just collected, which will be added to the inventory
     * @param newWeight is the new weight of the inventory, after adding the artifact to collect
     */
    public void addToInventory(double newWeight, Artifact artifactToCollect)
    {
        inventoryWeight = newWeight;
        inventory.add(artifactToCollect);
        System.out.println("The '" + artifactToCollect.getName() +  "' artifact has been added to your inventory!");
    }
    
    /**
     * Removes an artifact from the player's inventory, and updates the inventory weight accordingly
     * @param itemIndex is the index the player used in their "drop" command, specifying which artifact to drop.
     */
    public Artifact removeFromInventory(int itemIndex)
    {
        Artifact artifactToDrop = inventory.get(itemIndex);
        inventoryWeight -= artifactToDrop.getWeight();
        inventory.remove(itemIndex);
        System.out.println("Successfully dropped '" + artifactToDrop.getName() + "'!");
        return artifactToDrop;
    }

    /**
     * Collects an artifact from the current room the player is in if one exists.
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
            Artifact artifactToCollect = currentRoom.getAssignedArtifact();
            double newTotalWeight = inventoryWeight + artifactToCollect.getWeight();

                // Check if the player will exceed the weight restriction
            if (newTotalWeight > INVENTORY_WEIGHT_LIMIT)
            {   
                System.out.println("Cannot pick up this artifact as you exceeding the weight limit of " + INVENTORY_WEIGHT_LIMIT + "!\nCurrent total weight: " + inventoryWeight);
                // Exit at the bottom of method 
            }
            else
            {
                // Add artifact to the player's inventory
                this.addToInventory(newTotalWeight, artifactToCollect);

                // Remove artifact from the room
                currentRoom.assignArtifact(null);
            }
        }
    }
    
    /**
     * Drops an artifact from the player's inventory based on the itemIndex provided.
     * @param secondWord The second word that the player used within their "drop" command, which should be the index of the artifact that the player wants to drop.
     * @return A boolean indicating whether dropping the artifact specified by the player is possible.
     */
    public boolean dropArtifact(String secondWord)
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
            // Cannot drop an artifact in this room if there is an artifact already in this room
            if (currentRoom.getAssignedArtifact() != null)
            {
                System.out.println("Cannot drop another artifact in this room, there is already an artifact in this room!");
                return false;
            }

            // Check whether the index is in between in the range of the number of items in the inventory
            int itemIndex = Integer.parseInt(secondWord); // The index of the artifact within the player's inventory that the player wants to drop / remove.
            if (itemIndex >= 0 && itemIndex < inventorySize)
            {
                // Drop item into this room and remove from inventory
                Artifact artifactToDrop = this.removeFromInventory(itemIndex);

                // If this is the goal room (i.e., outside)
                if (Room.isGoalRoom(currentRoom))
                {  
                    // Note: Don't re-assign artifact to this room
                    numCompletedArtifacts ++; // Increment number of artifacts dropped off successfully at the goal room
                    System.out.println("<< You have successfully dropped off " + numCompletedArtifacts + "/" + Artifact.getNumArtifacts() + " artifacts! >>");
                }
                else 
                {
                    // Re-assign artifact to this room
                    currentRoom.assignArtifact(artifactToDrop);
                }
                return true;
            }
        }
        // Case: If the index is out range then skip to bottom of method
        return false;
    }
}