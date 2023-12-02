package dependencies.entities;

import java.util.ArrayList;

public class Player 
{
    private Room currentRoom;

    private ArrayList<Artifact> inventory = new ArrayList<Artifact>();
    private double inventoryWeight = 0.0;
    public final double INVENTORY_WEIGHT_LIMIT = 5.0; // Total amount of weight that the player can have

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
}
