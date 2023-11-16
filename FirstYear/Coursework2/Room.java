import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits = new HashMap<String, Room>(); // Stores the exits of this room.
    public static ArrayList<Room> NPCSpawnableRooms = new ArrayList<Room>(); // Stores the number of rooms that NPCs 
    private NPC assignedNPC; // Pointer to NPC assigned to this room

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, boolean npcSpawnable)
    {
        this.description = description;

        // Add this room to the NPCSpawnable rooms list if applicable
        if (npcSpawnable == true)
        {
            Room.NPCSpawnableRooms.add(this);
        }
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits: ";
        int i = 0;
        int numExits = exits.size(); 

        for(String exit : exits.keySet()) {
            returnString += exit;
            if (i < numExits - 1)
            {
                returnString += " | ";
            }
            i ++;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * @return A boolean indicating whether an NPC is inside this room or not
     */
    public boolean hasNPC()
    {
        return (assignedNPC != null);
    }

    /**
     * @return The assigned NPC to this room (should only be called if the assigned NPC is not null)
     */
    public NPC getAssignedNPC()
    {   
        return assignedNPC;
    }

    /**
     * Called to assign an NPC to this room
     */
    public void assignNPC(NPC NPCToAssign) 
    {
        assignedNPC = NPCToAssign;
    }

}

