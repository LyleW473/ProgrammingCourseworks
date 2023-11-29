import java.util.Random;

import core.Command;
import core.CommandResult;
import core.Parser;
import core.TextPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import dependencies.entities.Room;
import dependencies.entities.NPC;
import dependencies.entities.Artifact;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private TextPrinter textPrinter;

    /** 
     * List containing the names/ identities of the options available to the player (So that commands like "go 0" can be used)
     * Uses: Names of rooms, Names of items
    */
    private ArrayList<String> currentOptions = new ArrayList<String>();
    private Room currentRoom;
    private Command previousCommand = null; // Holds the previous command that was successfully executed (erased after a failed command)

    private ArrayList<Artifact> inventory = new ArrayList<Artifact>();
    private double totalWeight = 0.0;
    public final double WEIGHT_LIMIT = 5.0; // Total amount of weight that the player can have

    public final int NUM_ARTIFACTS = 3;
    public final int NUM_NPCS = 2;
    
    /**
     * Main method, for development
     */
    public static void main(String[] args)
    {   
        // Start game
        Game myGame = new Game();
        myGame.play();
    }
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {   
        // Text printer and parser
        textPrinter = new TextPrinter();
        parser = new Parser();

        // Initialise static collections for NPC class
        NPC.createConversationsList(textPrinter);
        NPC.createNamesList(textPrinter);

        // Initialise details for all artifacts
        Artifact.createArtifactsDetails(textPrinter);

        // Create game world
        createRooms();
        spawnArtifacts();
        spawnNPCs();
        
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // Create all rooms
        Room
        outside,
        gamesRoom, kitchen, mainHallway, bathroom,
        artRoom, diningRoom, livingRoom, storageRoom,
        hallway1,
        bedroom1, hallway2,
        hallway3, bedroom2,
        attic;

        // Parameters: description, can spawn NPCs in this room?, can spawn artifacts in this room?
        outside = new Room("outside the Smith's residence", true, false);

        gamesRoom = new Room("in the games room", false, true);
        kitchen = new Room("in the kitchen", false, true);
        mainHallway = new Room("in the main hallway", false, false);
        bathroom = new Room("in the bathroom", true, true);

        artRoom = new Room("in the art room", false, true);
        diningRoom = new Room("in the dining room", false, false);
        livingRoom = new Room("in the living room", false, true);
        storageRoom = new Room("in the storage room", true, true);

        hallway1 = new Room("in the hallway (1)", false, false);

        bedroom1 = new Room("in bedroom 1", true, true);
        hallway2 = new Room("in the hallway (2)", false, false);

        hallway3 = new Room("in the hallway (3)", false, false);
        bedroom2 = new Room("in bedroom 2", true, true);

        attic = new Room("in the attic", false, false);

        // Initialise exits for each room
        outside.setExit("main hallway", mainHallway);

        gamesRoom.setExit("art room", artRoom);
        gamesRoom.setExit("kitchen", kitchen);

        kitchen.setExit("games room", gamesRoom);
        kitchen.setExit("dining room", diningRoom);
        kitchen.setExit("main hallway", mainHallway);

        mainHallway.setExit("kitchen", kitchen);
        mainHallway.setExit("living room", livingRoom);
        mainHallway.setExit("bathroom", bathroom);
        mainHallway.setExit("outside", outside);

        bathroom.setExit("main hallway", mainHallway);

        artRoom.setExit("dining room", diningRoom);
        artRoom.setExit("games room", gamesRoom);

        diningRoom.setExit("art room", artRoom);
        diningRoom.setExit("living room", livingRoom);
        diningRoom.setExit("kitchen", kitchen);

        livingRoom.setExit("dining room", diningRoom);
        livingRoom.setExit("upstairs", hallway1); // Climb up the stairs
        livingRoom.setExit("storage room", storageRoom);
        livingRoom.setExit("main hallway", mainHallway);

        storageRoom.setExit("living room", livingRoom);
        
        hallway1.setExit("forwards", hallway2);
        hallway1.setExit("downstairs", livingRoom); // Climb down the stairs
        
        bedroom1.setExit("hallway 2", hallway2);

        hallway2.setExit("bedroom 1", bedroom1);
        hallway2.setExit("forwards", hallway3);
        hallway2.setExit("backwards", hallway1);

        hallway3.setExit("upstairs", attic);
        hallway3.setExit("bedroom 2", bedroom2);
        hallway3.setExit("backwards", hallway2);

        bedroom2.setExit("bedroom 2", hallway3);

        attic.setExit("downstairs", hallway3);

        // Spawn the player outside
        currentRoom = outside;
    }

    /**
     * Used to randomly select n rooms (where allowed) to spawn the NPCs into
     */
    public void spawnNPCs()
    {
        for (Room room: Room.NPCSpawnableRooms)
        {
            System.out.println("NPC room: " + room.getShortDescription());
        }

        // Generate random indexes between 0 (inclusive) and the number of NPC spawnable rooms (exclusive) there are
        Random randomGen = new Random();
        HashSet<Integer> uniqueIndexes = new HashSet<Integer>();
        int generatedIndex;
        int numNPCSpawnableRooms = Room.NPCSpawnableRooms.size();

        while (uniqueIndexes.size() < NUM_NPCS)
        {
            generatedIndex = randomGen.nextInt(numNPCSpawnableRooms);
            uniqueIndexes.add(generatedIndex);
        }

        // Assign NPCs to the randomly selected rooms
        Room roomToAssignNPC;
        for (int idx: uniqueIndexes)
        {
            System.out.println(idx);
            roomToAssignNPC = Room.NPCSpawnableRooms.get(idx);
            roomToAssignNPC.assignNPC(new NPC());
        }
    }

    /**
     * Used to randomly select n rooms (where allowed) to spawn artifacts into
     */
    public void spawnArtifacts()
    {
        for (Room room: Room.artifactSpawnableRooms)
        {
            System.out.println("Item room: " + room.getShortDescription());
        }

        // Generate random indexes between 0 (inclusive) and the number of artifact spawnable rooms (exclusive) there are
        Random randomGen = new Random();
        HashSet<Integer> uniqueIndexes = new HashSet<Integer>(); // Hashset for distinct values
        int generatedIndex;
        int numArtifactSpawnableRooms = Room.artifactSpawnableRooms.size();

        while (uniqueIndexes.size() < NUM_ARTIFACTS) // Continue generating until we have enough indexes for all rooms
        {
            generatedIndex = randomGen.nextInt(numArtifactSpawnableRooms);
            uniqueIndexes.add(generatedIndex);
        }
        
        // Assign artifacts to the randomly selected rooms
        Room roomToAssignArtifact;
        ArrayList<String> assignableArtifacts = Artifact.getAllArtifactNames(); // Ordered list of names of assignable artifacts
        for (int roomIdx: uniqueIndexes)
        {
            // Generate random artifact to assign to this room
            int randomArtifactIndex = randomGen.nextInt(assignableArtifacts.size());
            String artifactName = assignableArtifacts.get(randomArtifactIndex);
            Artifact artifactToAssign = Artifact.getArtifact(artifactName);
            
            // Assign artifact to the room
            roomToAssignArtifact = Room.artifactSpawnableRooms.get(roomIdx);
            roomToAssignArtifact.assignArtifact(artifactToAssign);

            // Remove the artifact selected from the list of assignable artifacts
            assignableArtifacts.remove(randomArtifactIndex);
            System.out.println("Artifact name: " + artifactToAssign.getName() + "\nRoom name: " + roomToAssignArtifact.getShortDescription());
        }
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        //  Main game loop, which will process commands until the player "quits" the game
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();

            // Space out command inputs
            System.out.println();
            System.out.println("--------------------------------------------");

            finished = processCommand(command).wantsToQuit();
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        this.textPrinter.outputContentsFile("dependencies/texts/welcome_message.txt");
        printHelp(true);
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private CommandResult processCommand(Command command) 
    {   
        // Result variables
        boolean wantToQuit = false;
        boolean successfulCommand = false;

        // Unknown command
        if(command.isUnknown()) {
            System.out.println("Unknown command, please enter a valid command, type 'help' to see all valid commands.");
            return new CommandResult(successfulCommand, wantToQuit);
        }
        
        String commandWord = command.getCommandWord();

        if (commandWord.equals("help")) 
        {
            successfulCommand = printHelp(false);
        }
        else if (commandWord.equals("go")) 
        {
            successfulCommand = goRoom(command);
        }
        else if (commandWord.equals("quit")) 
        {
            successfulCommand = wantToQuit = quit(command);
        }
        else if (commandWord.equals("print out"))
        {  
            System.out.println(command.getSecondWord());
            System.out.println("Three word command working!" + " " + command.getSecondWord());
        }
        else if (commandWord.equals("interact with"))
        {   
            successfulCommand = interactWithNPC(command);
        }
        else if (commandWord.equals("collect" ))
        {      
            successfulCommand = collectArtifact(command);
        }
        else if (commandWord.equals("back"))
        {   
            successfulCommand = goBack();
        }
        else if (commandWord.equals("repeat"))
        {
            successfulCommand = repeatPrevCommand();
        }
        else if (commandWord.equals("show"))
        {
            successfulCommand = showInventory(command);
        }
        else if (commandWord.equals("drop"))
        {
            successfulCommand = dropArtifact(command);
        }

        // If the command was successful and the command can be repeated, save it as the previous command
        // Note: If the command is "repeat", the previousCommand that was executed successfully will not be overwritten (i.e., to repeat the last successful command that can be repeated)
        if (successfulCommand == true){

            // Repeatable command (includes "repeat" command)
            if (parser.getCommandWords().isRepeatable(commandWord))
            {   
                // New previous command to repeat
                if (!command.getCommandWord().equals("repeat"))
                {
                    previousCommand = command;
                }
                // Otherwise, just keep the last previous command to repeat again

            }
            /** Cannot repeat this command.
             * - Will always replace previousCommand
             * For example: Following a sequence of commands like: help, go 0, repeat, "help" should not be used again after a command that cannot be repeated ("go 0")
             */
            else
            {
                previousCommand = null;
            }
        }

        // Return the result of this command
        return new CommandResult(successfulCommand, wantToQuit);
    }

    // implementations of user commands:
    /**
     * All user commands will return a boolean, representing whether the command was successful or not.
     * @return true if the command was successful.
     * @return false if the command was unsuccessful.
     * Returning true will allow the command to be repeated if the "repeat" command is used (given that it is a repeatable command)
     */

    /**
     * Prints out:
     * - All of the commands that the user can use
     * - All of the commands the user can use at the time of this being called.
     * - Information about the room and its contents
     * "isInitialCall" is used so that when "printHelp" is called in "printWelcome", it will add elements to the currentOptions list (when currentRoom.getLongDescription is called)
     * In any other case where "printHelp" is called, more options shouldn't be added to the currentOptions list 
     */
    private boolean printHelp(boolean isInitialCall) 
    {   
        // Used in the welcome message
        if (isInitialCall == true)
        {
            System.out.println("Use the 'help' command for additional information and guidance!");
        }
        
        // Used whenever the help command is called manually by the player
        else
        {
            parser.showAllCommands();
            System.out.println("\n");
            parser.showApplicableCommands(this.currentRoom);
            System.out.println("\n");
            System.out.println("<< For commands: 'go', you can use the option number in the command e.g., 'go 1' instead of 'go dining room' >>");
        }

        System.out.println();
        checkForNPC();
        checkForArtifact();
        System.out.println(currentRoom.getLongDescription(currentOptions, isInitialCall));
        return true;
    }

    /** 
     * Try to enter the room with the corresponding "selectedRoomName". If there is an exit, try to enter the room, otherwise print an error message.
     */
    private boolean goRoom(Command command) 
    {   
        // No location given by the player to go to
        if(!command.hasSecondWord()) 
        {
            System.out.println("Go where? Missing location to go to.");
            System.out.println("Use the 'help' command for additional guidance.");
            return false;
        }

        String selectedRoomName = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(selectedRoomName);

        // Check whether the user entered an index for the currentOptions (e.g., "go 0")
        if (nextRoom == null)
        {   
            try {
                int optionIndex = Integer.parseInt(selectedRoomName);
                
                // Check whether the index is in between in the range of options
                if (optionIndex >= 0 && optionIndex < currentOptions.size())
                    {
                    String roomName = currentOptions.get(optionIndex);
                    nextRoom = currentRoom.getExit(roomName);
                    }
                // Case: If the index is out range then skip to bottom of method
                }
            // Case: The second word was not an index or a valid location
            catch (NumberFormatException e)
            {}
        }

        // Changing rooms if possible (Chek again in case the identifier was an index)
        if (nextRoom != null) 
        {   
            Room.addToRoomHistory(currentRoom); // Add to room history before moving rooms
            currentRoom = nextRoom;
            currentOptions.clear(); // Empty the list of options available to the player
            checkForNPC();
            checkForArtifact();
            System.out.println(currentRoom.getLongDescription(currentOptions, true));
            return true;
        }

        // Cases: Index out of range or Room is not reachable
        System.out.println("Invalid input! A room with the name/identifier '" + selectedRoomName + "' does not exist or is not accessible from this room!");
        System.out.println("Use the 'help' command for additional guidance.");
        return false;
    }

    /** 
     * Try to go back to the previous room that the player was in
     */
    public boolean goBack()
    {   
        // If there is a previous room to go to
        if (Room.getRoomHistory().size() > 0)
        {
            currentRoom = Room.returnPrevious();
            checkForNPC();
            checkForArtifact();

            // Refresh all the options available to the player
            currentOptions.clear();
            System.out.println(currentRoom.getLongDescription(currentOptions, true));
            return true;
        }

        // Otherwise:
        System.out.println("There are currently no rooms to go back to!");
        System.out.println("Use the 'help' command for additional guidance.");
        return false;
    }

    /** 
     * Try to repeat the previous command executed if possible
     */
    public boolean repeatPrevCommand()
    {   
        // If there was a previous command
        if (!(previousCommand == null))
        {   
            return processCommand(previousCommand).wasSuccessful(); // Return whether the repeat command was successful executed or not
        }

        // Otherwise:
        System.out.println("There is no previous command to repeat / execute again or the previous command cannot be repeateed!");
        System.out.println("Use the 'help' command for additional guidance.");
        return false;
    }

    /**
     * Prints the conversation from the assigned NPC in the current room when interacted with, if there is an NPC in this room
     */
    public boolean interactWithNPC(Command command)
    {
        String secondWord = command.getSecondWord();

        // Check if the player used an invalid command
        if (secondWord == null || !secondWord.equalsIgnoreCase("NPC"))
            {
                System.out.println("Cannot interact with '" + secondWord + "', use the 'interact with npc' command to interact with NPCs.");
            }
        else{      
            // Check if this room has an NPC
            if (!currentRoom.hasNPC()) 
                {
                    System.out.println("There is no NPC to interact with in this room!");
                    return true; // Allow for repeated commands
                }
            else
                {
                NPC currentNPC = currentRoom.getAssignedNPC();
                System.out.println(currentNPC.getName() + ": " + currentNPC.getRandomConversation());
                // for (String c: currentNPC.getPossibleConversations())
                // {
                //     System.out.println(c);
                // }
                return true;
                }
            }
        return false;
    }

    /**
     * Method to collect an artifact if the requirements are satisfied
     */
    public boolean collectArtifact(Command command)
    {  
        String secondWord = command.getSecondWord();

        // Check if the player used an invalid command
        if (secondWord == null || !secondWord.equals("artifact"))
            {
                System.out.println("Invalid command, use the 'collect artifact' command to collect artifacts!");
            }
        else
        {   
            // Check if there is an artifact in this room
            if (!currentRoom.hasArtifact())
            {
                System.out.println("There is no artifact to collect in this room!");
            }
            else
            {   
                Artifact artifactToCollect = currentRoom.getAssignedArtifact();
                double newTotalWeight = totalWeight + artifactToCollect.getWeight();

                 // Check if the player will exceed the weight restriction
                if (newTotalWeight > WEIGHT_LIMIT)
                {   
                    System.out.println("Cannot pick up this artifact as you exceeding the weight limit of " + WEIGHT_LIMIT + "!\nCurrent total weight: " + totalWeight);
                    // Exit at the bottom of method 
                }
                else
                {
                    // Add artifact to the player's inventory
                    totalWeight = newTotalWeight;
                    inventory.add(artifactToCollect);

                    // Remove artifact from the room
                    currentRoom.assignArtifact(null);
                    for (Object o: inventory)
                    {
                        System.out.println(o.getClass());
                    }
                    System.out.println("The '" + artifactToCollect.getName() +  "' artifact has been added to your inventory!");
                    return true;
                }
            }
        }
        return false;
    }
   
    /**
     * Method to drop an artifact if the requirements are satisfied
     */
    public boolean dropArtifact(Command command)
    {
        String secondWord = command.getSecondWord();

        // Check if there is a second word
        if (secondWord != null)
            {
                // Only acceptable input is the item number
                try {
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
                            int itemIndex = Integer.parseInt(secondWord);
                            if (itemIndex >= 0 && itemIndex < inventorySize)
                                {
                                    // Drop item into this room and remove from inventory
                                    Artifact artifactToDrop = inventory.get(itemIndex);
                                    totalWeight -= artifactToDrop.getWeight();
                                    inventory.remove(itemIndex);
                                    currentRoom.assignArtifact(artifactToDrop);
                                    System.out.println("Successfully dropped '" + artifactToDrop.getName() + "'!");
                                    return true;
                                }
                            // Case: If the index is out range then skip to bottom of method
                        }
                    }
                // Case: The second word was not an index, then skip to the bottom of the method
                catch (NumberFormatException e)
                {}
            }

        // Invalid command
        System.out.println("Invalid command, use the 'drop {itemNumber}' command to collect artifacts!");
        return false;
    }
    
    /**
     * Shows the contents of the player's inventory inside of the terminal
     */
    public boolean showInventory(Command command)
    {
        String secondWord = command.getSecondWord();

        // Check if the player used an invalid command
        if (secondWord == null || !secondWord.equalsIgnoreCase("inventory"))
            {
                System.out.println("Cannot show '" + secondWord + "', use the 'show inventory' command to inspect your inventory.");
                return false;
            }
        
        // Empty inventory
        if (inventory.size() == 0)
        {
            System.out.println("Your inventory is empty. No artifacts have been collected yet.");
        }
        else
        {  
            // Display details for each artifact inside of the inventory
            System.out.println("<< Inventory >>");
            for (int i = 0; i < inventory.size(); i++)
            {   
                inventory.get(i).printDetails(i);
            }
            // Display current total weight of the player's inventory and the set weight limit
            System.out.println("Current total weight: " + totalWeight);
            System.out.println("Weight limit: " + WEIGHT_LIMIT);
        }
        return true;
    }

    /**
     * Check if this room has an NPC, output a comment 
     */
    public void checkForNPC()
    {
        // Check if there is an NPC in this room
            if (currentRoom.hasNPC())
                {
                    System.out.println("<< There is an NPC in this room! >>");
                }
    }
    
    /**
     * Check if this room has an artifact, output a comment 
     */
    public void checkForArtifact()
    {
        // Check if there is an artifact in this room
            if (currentRoom.hasArtifact())
                {
                    System.out.println("<< There is an artifact in this room! >>");
                    System.out.println(currentRoom.getAssignedArtifact().getName());
                }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}