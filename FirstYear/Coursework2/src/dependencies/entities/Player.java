package dependencies.entities;

import java.util.ArrayList;

public class Player 
{
    private Room currentRoom;

    private ArrayList<Artifact> inventory = new ArrayList<Artifact>();
    private double inventoryWeight = 0.0;
    public final double INVENTORY_WEIGHT_LIMIT = 5.0; // Total amount of weight that the player can have
    private ArrayList<Room> roomsHistory = new ArrayList<Room>(); // Stores the history of all the rooms the player has visited (in order)

    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    public void setCurrentRoom(Room selectedRoom)
    {
        currentRoom = selectedRoom;
    }

    public ArrayList<Artifact> getInventory()
    {
        return inventory;
    }

    public double getInventoryWeight()
    {
        return inventoryWeight;
    }

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
    
    public void addToInventory(double newWeight, Artifact artifactToCollect)
    {
        inventoryWeight = newWeight;
        inventory.add(artifactToCollect);
        System.out.println("The '" + artifactToCollect.getName() +  "' artifact has been added to your inventory!");
    }
    
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
     * @return the last room inside of the room history (i.e., the last room the player was in)
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
     * Clears the player's room history
     * - Executed after the player enters the magic attic / transporter room.
     * - This is an intended side effect of teleporting.
     */
    public void clearRoomsHistory()
    {
        roomsHistory.clear();
    }
}
