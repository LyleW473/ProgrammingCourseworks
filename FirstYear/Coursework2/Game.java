import java.util.Random;
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
        outside.setExit("north", mainHallway);

        kitchen.setExit("east", mainHallway);
        kitchen.setExit("north", diningRoom);

        mainHallway.setExit("west", kitchen);
        mainHallway.setExit("east", bathroom);
        mainHallway.setExit("north", livingRoom);
        mainHallway.setExit("outside", outside);

        bathroom.setExit("west", mainHallway);

        diningRoom.setExit("east", livingRoom);
        diningRoom.setExit("south", kitchen);

        livingRoom.setExit("west", diningRoom);
        livingRoom.setExit("east", storageRoom);
        livingRoom.setExit("south", mainHallway);
        livingRoom.setExit("upstairs", hallway1); // Climb up the stairs

        storageRoom.setExit("west", livingRoom);
        
        hallway1.setExit("north", hallway2);
        hallway1.setExit("downstairs", livingRoom); // Climb down the stairs
        
        bedroom1.setExit("east", hallway2);

        hallway2.setExit("west", bedroom1);
        hallway2.setExit("north", hallway3);
        hallway2.setExit("south", hallway1);

        bedroom2.setExit("west", hallway3);

        hallway3.setExit("east", bedroom2);
        hallway3.setExit("upstairs", attic);
        hallway3.setExit("south", hallway2);

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

            System.out.println("HasNPC: " + currentRoom.hasNPC());
            if (currentRoom.hasNPC())
            {  
                System.out.println("There is an NPC in here");
                NPC currentNPC = currentRoom.getAssignedNPC();
                System.out.println(currentNPC.getName() + ": " + currentNPC.getRandomConversation());
                // for (String c: currentNPC.getPossibleConversations())
                // {
                //     System.out.println(c);
                // }
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        this.textPrinter.outputContentsFile("texts/welcome_message.txt");
        printHelp();
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        System.out.println(); // Print space to space out different terminal outputs
        
        // Unknown command
        boolean wantToQuit = false;
        if(command.isUnknown()) {
            System.out.println("Unknown command, please enter a valid command, type 'help' to see all valid commands.");
            return false;
        }
        
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("Cannot go this way!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
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
