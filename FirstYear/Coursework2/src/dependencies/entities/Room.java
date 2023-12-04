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
    private static ArrayList<Room> noteSpawnableRooms = new ArrayList<Room>(); // Stores all the rooms that notes can spawn in.
    private static ArrayList<Room> artifactSpawnableRooms = new ArrayList<Room>(); // Stores all the rooms that artifacts can spawn in.
    private static ArrayList<Room> allRooms = new ArrayList<Room>(); // List of all room objects instantiated.
    private static Room magicTransporterRoom; // Pointer to Room set as the magic transporter room.
    private static Room goalRoom; // Pointer to Room that the player needs to drop all artifacts inside in order to win.

    private String description; // Description of this room.
    private HashMap<String, Room> exits = new HashMap<String, Room>(); // Stores the exits of this room.
    private Note assignedNote; // Pointer to Note assigned to this room.
    private ArrayList<Artifact> artifactsInRoom = new ArrayList<Artifact>(); // A list of artifacts that are in this room.

    /**
     * Constructor for Room class. 
     * Creates and initialises a Room object with a description and whether or not notes or artifacts can spawn in them.
     * @param description The room's description (e.g., "in the kitchen" or "outside the Smith's residence").
     * @param noteSpawnable Whether or not notes can spawn in this room.
     * @param artifactSpawnable Whether or not artifacts can spawn in this room.
     */
    public Room(String description, boolean noteSpawnable, boolean artifactSpawnable)
    {
        this.description = description;

        // Add this room to the noteSpawnable rooms list, if applicable
        if (noteSpawnable == true)
        {
            Room.noteSpawnableRooms.add(this);
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
     * @return An ArrayList<Room> containing the rooms that notes can spawn in.
     */
    public static ArrayList<Room> getNoteSpawnableRooms()
    {
        return Room.noteSpawnableRooms;
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
     * @return A boolean indicating whether a note is inside this room or not.
     */
    public boolean hasNote()
    {
        return (assignedNote != null);
    }

    /**
     * @return A boolean indicating whether an artifact is inside this room or not.
     */
    public boolean hasArtifact()
    {
        return (artifactsInRoom.size() > 0);
    }

    /**
     * @return The assigned note to this room.
     * (Should only be called if the assigned note is not null)
     */
    public Note getAssignedNote()
    {   
        return assignedNote;
    }

    /**
     * @return The number of artifacts in this room.
     */
    public int getNumArtifactsInRoom()
    {
        return artifactsInRoom.size();
    }

    /**
     * @return The assigned artifact to this room at the specified artifactIndex.
     * @param artifactIndex The index of the artifact to retrieve within the list of artifacts in this room.

     */
    public Artifact getAssignedArtifact(int artifactIndex)
    {
        
        return artifactsInRoom.get(artifactIndex);
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
     * Assigns a passed-in note to this room.
     * @param noteToAssign The note to set as the assignedNote for this room.
     */
    public void assignNote(Note noteToAssign) 
    {
        assignedNote = noteToAssign;
    }

    /**
     * Adds a passed-in artifact to the list of artifacts in this room.
     * @param artifactToAdd The artifact to add into the list of artifacts in this room.
     */
    public void addArtifact(Artifact artifactToAdd) 
    {
        artifactsInRoom.add(artifactToAdd);
    }

    /**
     * Adds a passed-in artifact to the list of artifacts in this room.
     * @param artifactToRemove The artifact to remove from the list of artifacts in this room.
     */
    public void removeArtifact(Artifact artifactToRemove)
    {
        artifactsInRoom.remove(artifactToRemove);
    }
}