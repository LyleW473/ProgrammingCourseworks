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
    private static ArrayList<Room> NPCSpawnableRooms = new ArrayList<Room>(); // Stores all the rooms that NPCs can spawn in.
    private static ArrayList<Room> artifactSpawnableRooms = new ArrayList<Room>(); // Stores all the rooms that artifacts can spawn in.
    private static ArrayList<Room> allRooms = new ArrayList<Room>(); // List of all room objects instantiated.
    private static Room magicTransporterRoom; // Pointer to Room set as the magic transporter room.
    private static Room goalRoom; // Pointer to Room that the player needs to drop all artifacts inside in order to win.

    private String description; // Description of this room.
    private HashMap<String, Room> exits = new HashMap<String, Room>(); // Stores the exits of this room.
    private NPC assignedNPC; // Pointer to NPC assigned to this room.
    private Artifact assignedArtifact; // Pointer to Artifact assigned to this room.

    /**
     * Constructor for Room class. 
     * Creates and initialises a Room object with a description and whether or not NPCs or artifacts can spawn in them.
     * @param description The room's description (e.g., "in the kitchen" or "outside the Smith's residence")
     * @param npcSpawnable Whether or not NPCs can spawn in this room
     * @param artifactSpawnable Whether or not artifacts can spawn  in this room
     */
    public Room(String description, boolean npcSpawnable, boolean artifactSpawnable)
    {
        this.description = description;

        // Add this room to the NPCSpawnable rooms list, if applicable
        if (npcSpawnable == true)
        {
            Room.NPCSpawnableRooms.add(this);
        }

        // Add this room to the artifactSpawnable rooms list, if applicable
        if (artifactSpawnable == true)
        {
            Room.artifactSpawnableRooms.add(this);
        }

        // Add this room to the list of all rooms
        Room.allRooms.add(this);
    }
    
    /**
     * @return A boolean indicating whether or not the room passed in is the magic transporter room
     * @param RoomToCheck The room to check against
     */
    public static boolean isMagicTransporterRoom(Room roomToCheck)
    {
        return roomToCheck.equals(Room.magicTransporterRoom);
    }

    /**
     * @return A boolean indicating whether or not the room passed in is the goal room (i.e., the room that the player needs to drop all artifacts in, to win).
     * @param roomToCheck The room to check against
     */
    public static boolean isGoalRoom(Room roomToCheck)
    {
        return roomToCheck.equals(Room.goalRoom);
    }

    /**
     * @return An ArrayList<Room> containing all of the room (objects) created in the world.
     */
    public static ArrayList<Room> getAllRooms()
    {
        return Room.allRooms;
    }

    /**
     * @return An ArrayList<Room> containing the rooms that NPCs can spawn in.
     */
    public static ArrayList<Room> getNPCSpawnableRooms()
    {
        return Room.NPCSpawnableRooms;
    }

    /**
     * @return An ArrayList<Room> containing the rooms that artifacts can spawn in.
     */
    public static ArrayList<Room> getArtifactSpawnableRooms()
    {
        return Room.artifactSpawnableRooms;
    }

    /**
     * Sets a passed-in room as the magic transporter room.
     * @param selectedRoom The room (object) to set as the magic transporter room
     */
    public static void setMagicTransporterRoom(Room selectedRoom)
    {
        Room.magicTransporterRoom = selectedRoom;
    }

    /**
     * Sets a passed-in room as the goal room.
     * @param selectedRoom The room (object) to set as the goal room
     */
    public static void setGoalRoom(Room selectedRoom)
    {
        Room.goalRoom = selectedRoom;
    }

    /**
     * @return The short description of the room.
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *      You are in the main hallway.
     *      Exits: outside (0) | living room (1) | kitchen (2) | bathroom (3)
     * @return A long description of this room.
     * @param options A list of options (i.e., exits) available to the player.
     */
    public String getLongDescription(ArrayList<String> options)
    {
        return "You are " + description + ".\n" + getExitString(options);
    }

    /**
     * Adds all the options (exits) available to the player, and returns a string containing the exits available to the player when called.
     * @return A string describing the rooms this room connects to / details of the room's exits, e.g.
     *      Exits: outside (0) | living room (1) | kitchen (2) | bathroom (3)
     * @param options A list of options (i.e., exits) available to the player.
     */
    private String getExitString(ArrayList<String> options)
    {
        String returnString = "Exits: ";
        int i = 0;
        int numExits = exits.size(); 

        // For each exit for this room
        for(String exit : exits.keySet()) {
            returnString += exit; // Add the name of the room to the return string
            returnString += " (" + i + ")"; // Add the "option number" to the return string

            options.add(exit); // Add this exit to the available options the player currently has
            
            if (i < numExits - 1)
            {   
                returnString += " | "; // Add a separator to the return string
            }
            i ++;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction.
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * @return A boolean indicating whether an NPC is inside this room or not.
     */
    public boolean hasNPC()
    {
        return (assignedNPC != null);
    }

    /**
     * @return A boolean indicating whether an artifact is inside this room or not.
     */
    public boolean hasArtifact()
    {
        return (assignedArtifact != null);
    }

    /**
     * @return The assigned NPC to this room.
     * (Should only be called if the assigned NPC is not null)
     */
    public NPC getAssignedNPC()
    {   
        return assignedNPC;
    }

    /**
     * @return The assigned artifact to this room.
     * (Should only be called if the assigned artifact is not null)
     */
    public Artifact getAssignedArtifact()
    {
        return assignedArtifact;
    }

    /**
     * Defines an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Assigns a passed-in NPC to this room.
     * @param NPCToAssign The NPC to set as the assignedNPC for this room.
     */
    public void assignNPC(NPC NPCToAssign) 
    {
        assignedNPC = NPCToAssign;
    }

    /**
     * Assigns a passed-in artifact to this room.
     * @param artifactToAssign The artifact to set as the assignedArtifact for this room.
     */
    public void assignArtifact(Artifact artifactToAssign) 
    {
        assignedArtifact = artifactToAssign;
    }
}