package dependencies.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import core.TextPrinter;

public class NPC 
{
    private static ArrayList<String> allConversations; // List containing all of the possible conversations that NPCs can use when interacted with.
    private static ArrayList<String> allNames; // List containing all of the names that NPCs can pssibly have.
    private static final int NUM_CONVOS_PER_NPC = 2; // The number of possible conversations each NPC should have assigned.
    private static final int NUM_NPCS = 2; // The number of NPCs that should be spawned into the game world.
    private static Random randomGen = new Random(); // Random number generator
    
    private String name; // Name assigned to the NPC.
    private ArrayList<String> possibleConversations = new ArrayList<String>(); // List of possible conversations that it can use when interacted with.

    /**
     * Constructor for NPC class. 
     * - Initialises the NPC object with a list of possible conversations that it can use and a name.
     */
    public NPC()
    {  
        // Randomly assign conversations that this NPC can use
        NPC.assignNPCConversations(this);

        // Randomly assign name
        NPC.assignNPCName(this);
    }

    /**
     * Initialises a static ArrayList<String> containing all of the possible conversations that NPCs can use.
     * @param textPrinter A TextPrinter object that will read and return the contents of a *.txt file as a ArrayList<String>.
     */
    public static void createConversationsList(TextPrinter textPrinter)
    {  
        NPC.allConversations = textPrinter.returnContentsList("dependencies/texts/npc_texts.txt");
        for (String conversation: NPC.allConversations){
            System.out.println("Text: " + conversation);
        }
    }

    /**
     * Initialises a static ArrayList<String> containing all of the possible names that NPCs can be assigned.
     * @param textPrinter A TextPrinter object that will read and return the contents of a *.txt file as a ArrayList<String>.
     */
    public static void createNamesList(TextPrinter textPrinter)
    {  
        NPC.allNames = textPrinter.returnContentsList("dependencies/texts/npc_names.txt");
        for (String name: NPC.allNames)
        {
            System.out.println("Name: " + name);
        }
    }

    /**
     * Assigns conversations that the passed-in NPC can use, using the stored conversations in the static NPC.allConversations list.
     * @param subjectNPC The NPC to assign a list of possible conversations to.
     */
    public static void assignNPCConversations(NPC subjectNPC)
    {
        int generatedIndex;
        int numConvosLeft;   
        String conversation;

        // Assign "NPC.NUM_CONVOS_PER_NPC" conversation lines to the subjectNPC (unless we run out of conversation lines)
        for (int i = 0; i < NPC.NUM_CONVOS_PER_NPC; i++)
        {   
            numConvosLeft = NPC.allConversations.size();
            // If there are no more conversation lines left to assign (ran out), then exit the method
            if (numConvosLeft == 0)
            {
                break;
            }

            // Add one randomly selected conversation to this NPC's list of possible conversations
            generatedIndex = NPC.randomGen.nextInt(numConvosLeft);
            conversation = NPC.allConversations.get(generatedIndex);
            subjectNPC.possibleConversations.add(conversation);

            // Remove the conversations assigned to this NPC from the allConversations list (to ensure there NPCs have unique conversation lines).
            ArrayList<String> remainingConversations = new ArrayList<String>();
            for (int j = 0; j < NPC.allConversations.size(); j++)
            {
                if (j != generatedIndex)
                {
                    remainingConversations.add(NPC.allConversations.get(j));
                }
            }
            NPC.allConversations = remainingConversations; // Update original list with the list without duplicates.
        }
    }

    /**
     * Randomly selects "NPC.NUM_NPCS" rooms and spawns NPCs into them.
     * - Only spawns NPCs into rooms that can have NPCs spawning in them (i.e., Room.NPCSpawnable == true)
     */
    public static void spawnNPCs()
    {
        ArrayList<Room> NPCSpawnableRooms = Room.getNPCSpawnableRooms();

        // For each NPC: Generate random indexes between 0 (inclusive) and the number of NPC spawnable rooms (exclusive)
        HashSet<Integer> uniqueIndexes = new HashSet<Integer>(); // HashSet for unique indexes
        int randomRoomIndex;
        int numNPCSpawnableRooms = NPCSpawnableRooms.size();
        while (uniqueIndexes.size() < NPC.NUM_NPCS)
        {
            randomRoomIndex = NPC.randomGen.nextInt(numNPCSpawnableRooms);
            uniqueIndexes.add(randomRoomIndex);
        }

        // Assign NPCs to the randomly selected rooms
        Room roomToAssignNPC;
        for (int idx: uniqueIndexes)
        {
            System.out.println(idx);
            roomToAssignNPC = NPCSpawnableRooms.get(idx); // Get the randomly selected room
            roomToAssignNPC.assignNPC(new NPC()); // Assign NPC to the room
        }
    }

    /**
     * @return A random conversation (line) out of the possible conversations this NPC can use.
     */
    public String getRandomConversation()
    {
        return possibleConversations.get(NPC.randomGen.nextInt(possibleConversations.size()));
    }

    /**
     * @return All the possible conversations that this NPC can use.
     */
    public ArrayList<String> getPossibleConversations()
    {
        return possibleConversations;
    }

    /**
     * @return The assigned name of this NPC
     */
    public String getName()
    {
        return name;
    }

    /**
     * Randomly assigns a name to a passed-in NPC, using the stored names in the static NPC.allNames list.
     * @subjectNPC The NPC to assign a name to.
     */
    public static void assignNPCName(NPC subjectNPC)
    {
        int generatedIndex;
        int numNamesLeft;

        numNamesLeft = NPC.allNames.size();
        generatedIndex = NPC.randomGen.nextInt(numNamesLeft); // Generate random index
        subjectNPC.name = NPC.allNames.get(generatedIndex);
        NPC.allNames.remove(subjectNPC.name); // Remove this name from the possible names that other NPCs can be assigned
    }
}