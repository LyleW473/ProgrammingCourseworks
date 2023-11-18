import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

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
    private Room currentRoom;
    private TextPrinter textPrinter;

    /** 
     * List containing the names/ identities of the options available to the player (So that commands like "go 0" can be used)
     * Uses: Names of rooms, Names of items
    */
    private ArrayList<String> currentOptions = new ArrayList<String>();
    
    /**
     * Main method, for development
     */
    public static void main(String[] args)
    {   
        TextPrinter universalTextPrinter = new TextPrinter();

        // Start game
        Game myGame = new Game(universalTextPrinter);
        myGame.play();
    }
    /**
     * Create the game and initialise its internal map.
     */
    public Game(TextPrinter textPrinter) 
    {
        // Initialise static collections for NPC class
        NPC.createConversationsList(textPrinter);
        NPC.createNamesList(textPrinter);

        createRooms();
        parser = new Parser();
        this.textPrinter = textPrinter;

    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // Create all rooms
        Room outside, kitchen, mainHallway, bathroom, diningRoom, livingRoom, storageRoom, hallway1, bedroom1, hallway2, hallway3, bedroom2, attic;

        // description, can spawn NPCs in this room?, can spawn objects in this room?
        outside = new Room("outside the Smith's residence", true);
        kitchen = new Room("in the kitchen", false);
        mainHallway = new Room("in the main hallway", false);
        bathroom = new Room("in the bathroom", true);
        diningRoom = new Room("in the dining room", false);
        livingRoom = new Room("in the living room", false);
        storageRoom = new Room("in the storage room", true);
        hallway1 = new Room("in the hallway (1)", false);
        bedroom1 = new Room("in bedroom 1", true);
        hallway2 = new Room("in the hallway (2)", false);
        hallway3 = new Room("in the hallway (3)", false);
        bedroom2 = new Room("in bedroom 2", true);
        attic = new Room("in the attic", false);

        // Initialise exits for each room
        outside.setExit("main hallway", mainHallway);

        kitchen.setExit("main hallway", mainHallway);
        kitchen.setExit("dining room", diningRoom);

        mainHallway.setExit("kitchen", kitchen);
        mainHallway.setExit("bathroom", bathroom);
        mainHallway.setExit("living room", livingRoom);
        mainHallway.setExit("outside", outside);

        bathroom.setExit("main hallway", mainHallway);

        diningRoom.setExit("living room", livingRoom);
        diningRoom.setExit("kitchen", kitchen);

        livingRoom.setExit("dining room", diningRoom);
        livingRoom.setExit("storage room", storageRoom);
        livingRoom.setExit("main hallway", mainHallway);
        livingRoom.setExit("upstairs", hallway1); // Climb up the stairs

        storageRoom.setExit("living room", livingRoom);
        
        hallway1.setExit("forwards", hallway2);
        hallway1.setExit("downstairs", livingRoom); // Climb down the stairs
        
        bedroom1.setExit("hallway 2", hallway2);

        hallway2.setExit("bedroom 1", bedroom1);
        hallway2.setExit("forwards", hallway3);
        hallway2.setExit("backwards", hallway1);

        bedroom2.setExit("bedroom 2", hallway3);

        hallway3.setExit("bedroom 2", bedroom2);
        hallway3.setExit("upstairs", attic);
        hallway3.setExit("backwards", hallway2);

        attic.setExit("downstairs", hallway3);

        // Randomly spawn NPCs into rooms that they can be spawned into
        spawnNPCs();

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
            System.out.println(room.getShortDescription());
        }

        // Generate random indexes between 0 (inclusive) and the number of NPC spawnable rooms (exclusive) there are
        Random randomGen = new Random();
        HashSet<Integer> uniqueIndexes = new HashSet<Integer>();
        int numNPCs = 2;
        int generatedIndex;
        int numNPCSpawnableRooms = Room.NPCSpawnableRooms.size();

        while (uniqueIndexes.size() < numNPCs)
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
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        //  Main game loop, which will process commands until the player "quits" the game
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        this.textPrinter.outputContentsFile("texts/welcome_message.txt");
        printHelp(true);
        // System.out.println();
        // System.out.println(currentRoom.getLongDescription(currentOptions));
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {   
        // Space out command inputs
        System.out.println();
        System.out.println("--------------------------------------------");
        
        // Unknown command
        boolean wantToQuit = false;
        if(command.isUnknown()) {
            System.out.println("Unknown command, please enter a valid command, type 'help' to see all valid commands.");
            return false;
        }
        
        String commandWord = command.getCommandWord();
        String secondWord = command.getSecondWord();

        if (commandWord.equals("help")) 
        {
            printHelp(false);
        }
        else if (commandWord.equals("go")) 
        {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) 
        {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("print out"))
        {  
            System.out.println(command.getSecondWord());
            System.out.println("Three word command working!" + " " + command.getSecondWord());
        }
        else if (commandWord.equals("interact with"))
        {  
            // Interacting with NPCs
            if (secondWord.equalsIgnoreCase("NPC"))
            {
                printNPCConversation();
            }
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Prints out all of the commands that the user can use and then all of the commands the user can use at the time of this being called.
     * "isInitialCall" is used so that when "printHelp" is called in "printWelcome", it will add elements to the currentOptions list
     * In any other case where "printHelp" is called, more options shouldn't be added to the currentOptions list
     */
    private void printHelp(boolean isInitialCall) 
    {
        parser.showAllCommands();
        System.out.println("\n");
        parser.showApplicableCommands(this.currentRoom);
        System.out.println("\n");
        System.out.println("For commands: 'go', you can use the option number in the command e.g., 'go 1' instead of 'go bedroom1' >>");
        System.out.println();
        System.out.println(currentRoom.getLongDescription(currentOptions, isInitialCall));
    }

    /** 
     * Try to enter the room with the corresponding "selectedRoomName". If there is an exit, try to enter the room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            // System.out.println(currentRoom.getLongDescription(currentOptions, false));
            return;
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
                }
            catch (NumberFormatException e){} // Do nothing
        }

        // Changing rooms if possible
        if (nextRoom != null) 
        {
            currentRoom = nextRoom;
            currentOptions.clear(); // Empty the list of options available to the player

            // Check if there is an NPC in this room
            if (currentRoom.hasNPC())
                {
                    System.out.println("There is an NPC in this room!");
                }
            System.out.println(currentRoom.getLongDescription(currentOptions, true));
        }
        else 
        {
            System.out.println("Invalid input!");
            System.out.println("Use the 'help' command for additional guidance.");
        }
    }

    /**
     * Prints the conversation from the NPC in the current room, if there is an NPC in this room
     */
    public void printNPCConversation()
    {
        if (currentRoom.hasNPC())
        {  
            NPC currentNPC = currentRoom.getAssignedNPC();
            System.out.println(currentNPC.getName() + ": " + currentNPC.getRandomConversation());
            // for (String c: currentNPC.getPossibleConversations())
            // {
            //     System.out.println(c);
            // }
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
