package dependencies.entities;

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
    private static ArrayList<Room> NPCSpawnableRooms = new ArrayList<Room>(); // Stores all the rooms that NPCs can spawn in
    private static ArrayList<Room> artifactSpawnableRooms = new ArrayList<Room>(); // Stores all the rooms that artifacts can spawn in
    private static ArrayList<Room> allRooms = new ArrayList<Room>(); // List of all rooms
    private static Room magicTransporterRoom; // Pointer to Room set as the magic transporter room
    private static Room goalRoom; // Pointer to Room that the player needs to drop all artifacts inside in order to win

    private String description;
    private HashMap<String, Room> exits = new HashMap<String, Room>(); // Stores the exits of this room.
    private NPC assignedNPC; // Pointer to NPC assigned to this room
    private Artifact assignedArtifact; // Pointer to Artifact assigned to this room

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, boolean npcSpawnable, boolean artifactSpawnable)
    {
        this.description = description;

        // Add this room to the NPCSpawnable rooms list if applicable
        if (npcSpawnable == true)
        {
            Room.NPCSpawnableRooms.add(this);
        }

        // Add this room to the artifactSpawnable rooms list if applicable
        if (artifactSpawnable == true)
        {
            Room.artifactSpawnableRooms.add(this);
        }

        // Add to list of all rooms (To teleport the player when entering the magic attic)
        Room.allRooms.add(this);
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
    public String getLongDescription(ArrayList<String> options, boolean shouldAdd)
    {
        return "You are " + description + ".\n" + getExitString(options, shouldAdd);
    }

    /**
     * @return a string describing the rooms this room connects to / details of the room's exits, e.g.
     * Exits: dining room (0) | upstairs (1) | storage room (2) | main hallway (3)
     * Takes in a list of options available to the player, and adds exits (the names of the rooms) to that list
     */
    private String getExitString(ArrayList<String> options, boolean shouldAdd)
    {
        String returnString = "Exits: ";
        int i = 0;
        int numExits = exits.size(); 

        for(String exit : exits.keySet()) {
            returnString += exit;
            returnString += " (" + i + ")";

            // Should only add when specified (When the "help" command is used, there is no need to add more items)
            if (shouldAdd == true)
            {
                options.add(exit);
            }
            
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
     * @return A boolean indicating whether an artifact is inside this room or not
     */
    public boolean hasArtifact()
    {
        return (assignedArtifact != null);
    }

    /**
     * @return the assigned NPC to this room (should only be called if the assigned NPC is not null)
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

    /**
     * Called to assign an artifact to this room
     */
    public void assignArtifact(Artifact artifactToAssign) 
    {
        assignedArtifact = artifactToAssign;
    }

    /**
     * @return the assigned artifact to this room (should only be called if the assigned artifact is not null)
     */
    public Artifact getAssignedArtifact()
    {
        return assignedArtifact;
    }
        
    /**
     * Sets a room as the magic transporter room
     */
    public static void setMagicTransporterRoom(Room selectedRoom)
    {
        Room.magicTransporterRoom = selectedRoom;
    }

    /**
     * Method used to check if a passed in room is the magic transporter room
     */
    public static boolean isMagicTransporterRoom(Room roomToCheck)
    {
        return roomToCheck.equals(Room.magicTransporterRoom);
    }

    /**
     * @return a list containing all of the room objects created
     */
    public static ArrayList<Room> getAllRooms()
    {
        return Room.allRooms;
    }

    /**
     * Sets a room as the goal room
     */
    public static void setGoalRoom(Room selectedRoom)
    {
        Room.goalRoom = selectedRoom;
    }

    /**
     * Method used to check if a passed in room is the goal room
     */
    public static boolean isGoalRoom(Room roomToCheck)
    {
        return roomToCheck.equals(Room.goalRoom);
    }

    /**
     * @return an ArrayList<Room> containing the rooms that NPCs can spawn in
     */
    public static ArrayList<Room> getNPCSpawnableRooms()
    {
        return Room.NPCSpawnableRooms;
    }

    /**
     * @return an ArrayList<Room> containing the rooms that artifacts can spawn in
     */
    public static ArrayList<Room> getArtifactSpawnableRooms()
    {
        return Room.artifactSpawnableRooms;
    }
}