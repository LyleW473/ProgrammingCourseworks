package dependencies.entities;

import java.util.ArrayList;
import java.util.Random;

import core.TextPrinter;

public class NPC 
{
    private static ArrayList<String> allConversations;
    private static ArrayList<String> allNames;

    public static final int NUM_CONVOS_PER_NPC = 2;
    public static Random randomGen = new Random();
    
    public String name;
    private ArrayList<String> possibleConversations = new ArrayList<String>();

    public NPC()
    {  
        // Assign conversations that this NPC can use
        NPC.assignNPCConversations(this);

        // Assign name
        NPC.assignNPCName(this);
    }

    /**
     * Initialises a static ArrayList<String> containing all of the possible conversations that NPCs can use
     */
    public static void createConversationsList(TextPrinter textPrinter)
    {  
        NPC.allConversations = textPrinter.returnContentsList("dependencies/texts/npc_texts.txt");
        for (String conversation: NPC.allConversations){
            System.out.println("Text: " + conversation);
        }
    }

    /**
     * Initialises a static ArrayList<String> containing all of the possible names that NPCs can be assigned
     */
    public static void createNamesList(TextPrinter textPrinter)
    {  
        NPC.allNames = textPrinter.returnContentsList("dependencies/texts/npc_names.txt");
        for (String name: NPC.allNames){
            System.out.println("Name: " + name);
        }
    }

    /**
     * Assigns conversations that this specific NPC can use, using the stored conversations in the static NPC.allConversations list
     */
    public static void assignNPCConversations(NPC subjectNPC)
    {
        int generatedIndex;
        int numConvosLeft;   
        String conversation;

        // Repeat "NPC.NUM_CONVOS_PER_NPC" times:
        for (int i = 0; i < NPC.NUM_CONVOS_PER_NPC; i++)
        {   
            numConvosLeft = NPC.allConversations.size();
            // No more conversations left to assign
            if (numConvosLeft == 0)
            {
                break;
            }
        
            // Add one randomly selected conversation to this NPC's list of possible conversations
            generatedIndex = NPC.randomGen.nextInt(numConvosLeft);
            conversation = NPC.allConversations.get(generatedIndex);
            subjectNPC.possibleConversations.add(conversation);

            // Remove the conversations assigned to this NPC from the allConversations list (to ensure there are no duplicates)
            ArrayList<String> remainingConversations = new ArrayList<String>();
            for (int j = 0; j < NPC.allConversations.size(); j++)
            {
                if (j != generatedIndex)
                {
                    remainingConversations.add(NPC.allConversations.get(j));
                }
            }
            NPC.allConversations = remainingConversations;
        }
    }
    
    /**
     * @return a random conversation out of the possible conversations this NPC can use.
     */
    public String getRandomConversation()
    {
        return possibleConversations.get(NPC.randomGen.nextInt(possibleConversations.size()));
    }

    /**
     * @return all the possible conversations that this NPC can use.
     */
    public ArrayList<String> getPossibleConversations()
    {
        return possibleConversations;
    }
    
    /**
     * Assigns a name to this NPC, using the stored names in the static NPC.allNames list.
     */
    public static void assignNPCName(NPC subjectNPC)
    {
        int generatedIndex;
        int numNamesLeft;

        numNamesLeft = NPC.allNames.size();
        generatedIndex = NPC.randomGen.nextInt(numNamesLeft);
        subjectNPC.name = NPC.allNames.get(generatedIndex);
        NPC.allNames.remove(subjectNPC.name);
    }
    
    /**
     * @return the assigned name of this NPC
     */
    public String getName()
    {
        return name;
    }

}
