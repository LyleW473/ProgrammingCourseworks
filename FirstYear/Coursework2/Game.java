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

import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

public class Game 
{
    private final int NUM_ARTIFACTS = 3; // Number of artifacts to spawn into the game world.
    private final int NUM_NOTES = 3; // The number of notes that should be spawned into the game world.

    private Parser parser; // Parser for reading and interpreting user input.
    private Random randomGen = new Random(); // Random number generator for spawning of entities.
    private ArrayList<String> currentOptions = new ArrayList<String>(); // List containing the names/identities of the options available to the player,
    private Command previousCommand = null; // Holds the previous command that was successfully executed (erased after a failed command),
    private Player player1; // Pointer to the Player object.

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
     * Constructor for Game class.
     * Creates and initialises the game world.
     */
    public Game() 
    {   
        // Create parser to read user input
        parser = new Parser();

        // Create game world
        createGameWorld();
    }

    /**
     * Checks if the player has lost the game.
     * @return true if the player lost.
     * @return false if the player has not lost.
     */
    public boolean checkGameLoss()
    {
        // Check if the player is in the same room as any of the enemies
        Room playerRoom = player1.getCurrentRoom();
        for (Enemy e: Enemy.getAllEnemies())
        {
            if (playerRoom.equals(e.getCurrentRoom()))
            {
                System.out.println("--------------------------------------------");
                System.out.println("<<<< One of the maids have caught you red-handed! You have lost the game! >>>> ");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the player has won the game.
     * @return true if the player won (Meaning the player successfully retrieved all artifacts and dropped them off in the goal room).
     * @return false if the player has not won.
     */
    public boolean checkGameWin()
    {
        boolean wonGame = (player1.getNumCompletedArtifacts() == NUM_ARTIFACTS);
        if (wonGame)
        {
            System.out.println();
            System.out.println("--------------------------------------------");
            System.out.println("<<<< Congratulations you have successfully completed the game! Hope it was fun! >>>>");
        }
        return wonGame;
    }

    /**
     * Displays a message indicating that there is a note in the room if there is one.
     */
    public void checkForNote()
    {
        // Check if there is an note in this room
        if (player1.getCurrentRoom().hasNote())
            {
                System.out.println("<< There is a note in this room! Use the 'interact with note' command to interact with the note! >>\n");
            }
    }
    
    /**
     * Displays a message indicating that there is an artifact in the room if there is one.
     */
    public void checkForArtifact()
    {
        // Check if there is an artifact in this room
        Room playerRoom = player1.getCurrentRoom();
        if (playerRoom.hasArtifact())
            {   
                int numArtifactsInRoom = playerRoom.getNumArtifactsInRoom();
                if (numArtifactsInRoom == 1)
                {
                    System.out.print("<< There is " + numArtifactsInRoom + " artifact in this room! Use the 'collect artifact' command to add it to your inventory! >>\n");
                }
                else
                {
                    System.out.print("<< There are " + numArtifactsInRoom + " artifacts in this room! Use the 'collect artifact' command to add them one-by-one to your inventory! >>\n");
                }
                for (int idx = 0; idx < numArtifactsInRoom; idx++)
                {
                    String artifactName = playerRoom.getAssignedArtifact(idx).getName();
                    System.out.println("    Artifact " + (idx + 1) + ": The '" + artifactName + "' artifact");
                }
                System.out.println();
            }
    }
    
    /**
     * Main play routine. Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Main game loop, which will process commands until the player "quits" the game
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();

            // Space out command inputs
            System.out.println();
            System.out.println("--------------------------------------------");

            // Process player command
            finished = processCommand(command).wantsToQuit();
            
            // Check if the player has lost or won the game
            if (!finished)
            {
                finished = (checkGameLoss() || checkGameWin());
            }
        }
        System.out.println("Thank you for playing! Goodbye!");
    }

    /**
     * Initialises the game world, creating all the rooms, exits for each room, selection of special rooms and spawning of entities
     */
    private void createGameWorld()
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

        // Parameters: description, can spawn notes in this room?, can spawn artifacts in this room?
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

        // Set the attic as the magic transporter room
        Room.setMagicTransporterRoom(attic);
    
        // Set "outside" as the drop-off point for all the artifacts (for the player to win the game)
        Room.setGoalRoom(outside);

        // Spawn entities into the world
        spawnEnemies(gamesRoom, artRoom, diningRoom, livingRoom, mainHallway, kitchen, attic, hallway3, bedroom2, hallway2, bedroom1);

        Artifact.createArtifactsDetails(); // Initialise details for all artifacts
        spawnArtifacts();
        
        spawnNotes();
        
        // Create the player, spawning them outside
        player1 = new Player(outside);
    }

    /**
     * Spawns enemies, passing in a defined path to traverse for each enemy (manually created for each enemy) and a moveInterval.
     * - Enemies are spawned in the first room defined in their traversal path
     * Parameters used: The rooms that are used in the traversal paths of any enemy.
     */
    public void spawnEnemies(Room gamesRoom, Room artRoom, Room diningRoom, Room livingRoom, Room mainHallway, Room kitchen, Room attic, Room hallway3, Room bedroom2, Room hallway2, Room bedroom1)
    {
        // Define traversal paths for each enemy
        ArrayList<Room> enemy1Path = new ArrayList<Room>()
                                                        {{
                                                            add(mainHallway);
                                                            add(kitchen);
                                                            add(gamesRoom);
                                                            add(artRoom);
                                                            add(diningRoom);
                                                            add(livingRoom);
                                                        }};
        ArrayList<Room> enemy2Path = new ArrayList<Room>()
                                                        {{
                                                            add(artRoom);
                                                            add(diningRoom);
                                                            add(livingRoom);
                                                            add(mainHallway);
                                                            add(kitchen); 
                                                            add(gamesRoom);
                                                        }};
        ArrayList<Room> enemy3Path = new ArrayList<Room>()
                                                        {{
                                                            add(attic);
                                                            add(hallway3);
                                                            add(bedroom2); 
                                                            add(hallway3);
                                                            add(hallway2);
                                                            add(bedroom1);
                                                            add(hallway2);
                                                            add(hallway3);
                                                            add(bedroom2);
                                                            add(hallway3);
                                                        }};

        // Create/instantiate enemies (passing in their traversal path and a moveInterval)
        new Enemy(enemy1Path, 1);
        new Enemy(enemy2Path, 1);
        new Enemy(enemy3Path, 2);
    }

    /**
     * Randomly selects "Artifact.NUM_ARTIFACTS" rooms to spawn artifacts into.
     * - Selects random rooms that can have artifacts spawning in them and then randomly assigns artifacts to each room
     */
    public void spawnArtifacts()
    {
        ArrayList<Room> artifactSpawnableRooms = Room.getArtifactSpawnableRooms();

        // Generate random indexes between 0 (inclusive) and the number of artifact spawnable rooms (exclusive) there are
        ArrayList<Integer> roomIndexes = new ArrayList<Integer>(); // The same room can have multiple artifacts spawning in them
        int generatedIndex;
        int numArtifactSpawnableRooms = artifactSpawnableRooms.size();

        while (roomIndexes.size() < NUM_ARTIFACTS) // Continue generating until we have enough indexes for all rooms
        {
            generatedIndex = randomGen.nextInt(numArtifactSpawnableRooms);
            roomIndexes.add(generatedIndex);
        }
        
        // Assign artifacts to the randomly selected rooms
        Room roomToAssignArtifact;
        ArrayList<String> assignableArtifacts = Artifact.getAllArtifactNames(); // Ordered list of names of assignable artifacts
        for (int roomIdx: roomIndexes)
        {
            // Generate random artifact to assign to this room
            int randomArtifactIndex = randomGen.nextInt(assignableArtifacts.size());
            String artifactName = assignableArtifacts.get(randomArtifactIndex);
            Artifact artifactToAssign = Artifact.getArtifact(artifactName);
            
            // Assign artifact to the room
            roomToAssignArtifact = artifactSpawnableRooms.get(roomIdx);
            roomToAssignArtifact.addArtifact(artifactToAssign);

            // Remove the artifact selected from the list of assignable artifacts
            assignableArtifacts.remove(randomArtifactIndex);
        }
    }

    /**
     * Randomly selects "Note.NUM_NOTES" rooms and spawns notes into them.
     * - Only spawns notes into rooms that can have notes spawning in them (i.e., Room.noteSpawnable == true)
     */
    public void spawnNotes()
    {
        ArrayList<Room> noteSpawnableRooms = Room.getNoteSpawnableRooms();

        // For each note: Generate random indexes between 0 (inclusive) and the number of note spawnable rooms (exclusive)
        HashSet<Integer> uniqueIndexes = new HashSet<Integer>(); // HashSet for unique indexes
        int randomRoomIndex;
        int numNoteSpawnableRooms = noteSpawnableRooms.size();
        while (uniqueIndexes.size() < NUM_NOTES)
        {
            randomRoomIndex = randomGen.nextInt(numNoteSpawnableRooms);
            uniqueIndexes.add(randomRoomIndex);
        }

        // Assign notes to the randomly selected rooms
        Room roomToAssignNote;
        for (int idx: uniqueIndexes)
        {
            roomToAssignNote = noteSpawnableRooms.get(idx); // Get the randomly selected room
            roomToAssignNote.assignNote(new Note()); // Assign note to the room
        }
    }

    /**
     *  Teleports the player to a random room (functionality intended for magic transporter room).
     * - Does not add the magic transporter room to the player's room history.
     * - Selects a room as long as it is not the magic transporter room or a room that any enemy is about to move to.
     */
    public void teleportPlayer()
    {   
        System.out.println("<< You have entered the magic attic! You have been teleported to another room! >>");
        System.out.println("<< Teleporting has cleared your room history! >>");

        Random randomGen = new Random();
        int generatedIndex;
        ArrayList<Room> allRooms = Room.getAllRooms(); // List of all rooms
        ArrayList<Enemy> allEnemies = Enemy.getAllEnemies(); // List of all enemies
        int numRooms = allRooms.size();
        boolean foundRoom = false;
        boolean isValidRoom = false;
        Room selectedRoom = null;

        while (foundRoom == false)
        {
            // Select a random room to teleport the player to
            generatedIndex = randomGen.nextInt(numRooms);
            selectedRoom = allRooms.get(generatedIndex);

            // For all enemies, check if the selected room is a magic transporter room or if it is a room that it is about to move to
            isValidRoom = true; // Assume that the room selected is valid until proven false
            for (Enemy e: allEnemies)
            {
                if (selectedRoom.equals(e.getNextRoom()) || Room.isMagicTransporterRoom(selectedRoom))
                {      
                    // Exit, and try a different room index
                    isValidRoom = false;
                    break; 
                }
            }

            // If the selected room is not any of the enemies' next rooms to move to and also not the magic transporter room, then this room is valid
            if (isValidRoom == true)
            {
                foundRoom = true;
            }
        }
        
        // Teleport the player
        player1.setCurrentRoom(selectedRoom);

        // Clear the rooms history after teleporting (This is an intended side effect of the magic attic)
        player1.clearRoomsHistory();
    }

    /**
     * Print out the opening/welcome message for the player.
     */
    private void printWelcome()
    {
        System.out.println("It is a quiet night outside the Smith residence.");
        System.out.println("Inside the residence are some precious artifacts which will set you for life!");
        System.out.println("However, I've heard that the Smith's have hired maids to patrol the house.\n");
        System.out.println("Objective:");
        System.out.println("- Find artifacts inside of the house and return them to me, I will be waiting outside for you.");
        System.out.println("- Avoid getting caught by the maids.\n");
        System.out.println("--------------------------------------------");
        printHelp(true);
    }

    /** 
     * Prints all of valid commands that the player can use into the terminal.
     */
    public void showAllValidCommands()
    {
        System.out.println("All user commands:\ngo {location / optionNumber} | back | repeat | help | quit | show inventory | drop {itemNumber} | interact with note | collect artifact\n");
        System.out.println("For the 'go' command, you can use the the corresponding room / option number in the command, e.g., 'go 1' instead of 'go dining room'");
        System.out.println("For the 'drop' command, you can have to use the itemNumber (displayed in the inventory) in the command, e.g., 'drop 1' to drop the 2nd artifact in your inventory\n");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return CommandResult(successfulCommand, wantToQuit), with each parameter being a boolean describing whether:
     * - The command executed successfully.
     * - The player wants to quit the game.
     */
    private CommandResult processCommand(Command command) 
    {   
        // Result variables
        boolean wantToQuit = false; // The user wants to quit
        boolean successfulCommand = false; // The command executed successfully (i.e., valid command)

        // Unknown command
        if(command.isUnknown()) 
        {
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
        else if (commandWord.equals("interact with"))
        {   
            successfulCommand = interactWithNote(command);
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

        // If the command was successful
        if (successfulCommand == true)
        {   
            boolean isRepeatCommand = commandWord.equalsIgnoreCase("repeat");

            // If the command is repeatable, set previousCommand as the current command
            if (parser.getCommandWords().isRepeatable(commandWord))
            {   
                previousCommand = command; // Sets this command as repeatable
            }
            
            // If the command is "repeat", the previousCommand that was executed successfully will not be overwritten (i.e., in order to repeat the last successful command that can be repeated), so do nothing
            else if (isRepeatCommand)
            {}

            /** Cannot repeat this command.
             * - Will always replace previousCommand
             * For example: Following a sequence of commands like: help, go 0, repeat, "help" should not be used again after a command that cannot be repeated ("go 0")
             */
            else
            {
                previousCommand = null;
            }  

            /** The following should only be performed once instead of twice (i.e., should not be executed within a "repeat" command)
             * - Moving enemies if the command was successful and the command isn't "repeat" (because using "repeat" will move enemies twice)
             */ 
            if (!isRepeatCommand)
            {
                Enemy.moveAllEnemies(); 
                checkForNote();
                checkForArtifact();
                Enemy.displayEnemyLocations();
                currentOptions.clear(); // Empty the list of options available to the player
                System.out.println(player1.getCurrentRoom().getLongDescription(currentOptions)); // Refresh the list of options available to the player
            }
        }

        // Return the result of this command
        return new CommandResult(successfulCommand, wantToQuit);
    }

    // Implementations of user commands:
    /**
     * All user commands will return a boolean, representing whether the command was successful or not.
     * @return true if the command was successful.
     * @return false if the command was unsuccessful.
     * If any command returns true, it will allow the command to be repeated if the "repeat" command is used (given that it is a repeatable command).
     */

    /**
     * Prints out:
     * - All of the commands that the user can use.
     * - All of the commands the user can use in the current game state.
     * - Information about the room and its contents (e.g., exits).
     * @param isInitialCall Boolean used to display different messages to the player (The initial call will be within printWelcome).
     */
    private boolean printHelp(boolean isInitialCall) 
    {   
        // Used in the welcome message
        if (isInitialCall == true)
        {
            System.out.println("Use the 'help' command for additional information and guidance!");

            // Show initial information and options to player
            checkForNote();
            checkForArtifact();
            Enemy.displayEnemyLocations();
            currentOptions.clear(); // Empty the list of options available to the player
            System.out.println(player1.getCurrentRoom().getLongDescription(currentOptions)); // Refresh the list of options available to the player
        }
        
        // Used whenever the help command is called manually by the player
        else
        {
            showAllValidCommands();
        }
        return true;
    }

    /** 
     * Try to enter the room with the corresponding "selectedRoomName". Otherwise print an error message.
     */
    private boolean goRoom(Command command) 
    {   
        // No location given by the player to go to
        if(!command.hasSecondWord()) 
        {
            System.out.println("Go where? Your command is missing a location to go to.");
            System.out.println("Use the 'help' command for additional guidance.");
            return false;
        }

        String selectedRoomName = command.getSecondWord();

        // Try to leave current room.
        Room playerRoom = player1.getCurrentRoom();
        Room nextRoom = playerRoom.getExit(selectedRoomName);

        // Check whether the user entered an index for the currentOptions (e.g., "go 0")
        if (nextRoom == null)
        {   
            try {
                int optionIndex = Integer.parseInt(selectedRoomName);
                
                // Check whether the index is in between the range of options
                if (optionIndex >= 0 && optionIndex < currentOptions.size())
                    {
                    // Retrieve room exit via the optionIndex
                    String roomName = currentOptions.get(optionIndex);
                    nextRoom = playerRoom.getExit(roomName);
                    }
                // Case: If the index is out of range then skip to bottom of method
                }
            // Case: The second word was not an index or a valid location
            catch (NumberFormatException e)
            {}
        }

        // Try changing rooms if possible
        if (nextRoom != null) 
        {   
            // Check if the player tried entering the magic transporter room
            if (Room.isMagicTransporterRoom(nextRoom))
            {   
                // Teleport player to a random room (that all enemies will not go into next)
                teleportPlayer();
            }
            else
            {
                player1.addToRoomHistory(playerRoom); // Add to room history before moving rooms
                player1.setCurrentRoom(nextRoom);
            }
            return true;
        }

        // Cases: Index out of range, room is not reachable or room does not exist.
        System.out.println("Invalid input! A room with the name/identifier '" + selectedRoomName + "' does not exist or is not accessible from this room!");
        System.out.println("Use the 'help' command for additional guidance.");
        return false;
    }

    /** 
     * Try to go back to the previous room that the player was in.
     */
    public boolean goBack()
    {   
        // If there is a previous room to go to
        if (player1.getRoomHistory().size() > 0)
        {
            player1.setCurrentRoom(player1.getPreviousRoom()); // Go to the previous room
            return true;
        }

        // Otherwise:
        System.out.println("There are currently no rooms to go back to!");
        System.out.println("Use the 'help' command for additional guidance.");
        return false;
    }

    /** 
     * Try to repeat the previous command executed if possible.
     */
    public boolean repeatPrevCommand()
    {   
        // If there was a previous command
        if (!(previousCommand == null))
        {   
            // Try repeating the previous command
            return processCommand(previousCommand).wasSuccessful(); // Return whether the repeat command was successful executed or not
        }

        // Otherwise:
        System.out.println("There is no previous command to repeat / execute again or the previous command cannot be repeated!");
        System.out.println("Use the 'help' command for additional guidance.");
        return false;
    }

    /**
     * Prints the conversation from the assigned note in the current room when interacted with, if there is a note in this room.
     * @param command The command created from the user's input into the terminal.
     */
    public boolean interactWithNote(Command command)
    {
        String secondWord = command.getSecondWord();

        // Check if the player used an invalid command
        if (secondWord == null || !secondWord.equalsIgnoreCase("note"))
            {
                System.out.println("Cannot interact with '" + secondWord + "', use the 'interact with note' command to interact with notes.");
            }
        else{      
            // Check if this room has a note
            Room playerRoom = player1.getCurrentRoom();
            if (!playerRoom.hasNote()) 
                {
                    System.out.println("There is no note to interact with in this room!");
                    return true; // Allow for repeated commands
                }
            else
                {
                Note currentNote = playerRoom.getAssignedNote();
                currentNote.printDescription();
                return true;
                }
            }
        return false;
    }

    /**
     * Method to collect an artifact if the requirements are satisfied
     * @param command The command created from the user's input into the terminal.
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
            player1.collectArtifact();
            return true;
        }
        return false;
    }
   
    /**
     * Method to drop an artifact if the requirements are satisfied
     * @param command The command created from the user's input into the terminal.
     */
    public boolean dropArtifact(Command command)
    {
        String secondWord = command.getSecondWord();

        // Check if there is a second word
        if (secondWord != null)
        {
            // Only acceptable input is the item number / index
            try {
                    // Try dropping the artifact with the specified index
                    if (player1.dropArtifact(secondWord, NUM_ARTIFACTS) == true)
                    {
                        return true;
                    }
                }
            // Case: The second word was not an index/number, then skip to the bottom of the method
            catch (NumberFormatException e)
            {}
        }

        // Invalid command
        System.out.println("Invalid command, use the 'drop {itemNumber}' command to collect artifacts!");
        return false;
    }

    /**
     * Shows the contents of the player's inventory inside of the terminal.
     * @param command The command created from the user's input into the terminal.
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
        if (player1.getInventoryWeight() == 0)
        {
            System.out.println("Your inventory is empty. No artifacts have been collected yet.\n");
        }
        else
        {  
            player1.showInventoryContents();
        }
        return true;
    }

    /** 
     * If the "quit" command was used, quit the game. (Without a second word)
     * @return true if the command is valid (i.e., "quit" is the only acceptable input, with any lettercase).
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