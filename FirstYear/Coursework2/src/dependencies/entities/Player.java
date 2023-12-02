package dependencies.entities;

import java.util.ArrayList;

public class Player 
{
    private Room currentRoom;
    private ArrayList<Artifact> inventory = new ArrayList<Artifact>();
    private double inventoryWeight = 0.0; // Current total weight of the player's inventory
    public final double INVENTORY_WEIGHT_LIMIT = 5.0; // Total amount of weight that the player can have
    private ArrayList<Room> roomsHistory = new ArrayList<Room>(); // Stores the history of all the rooms the player has visited (in order)

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
}
