import java.util.HashSet;
import java.util.ArrayList;

public class NPC 
{
    private static int numCreated = 0;
    private static HashSet<String> allConversations; // Hashset for O(1) removals
    private ArrayList<String> possibleConversations = new ArrayList<String>();
    public int id;

    public NPC()
    {
        id = NPC.numCreated;
        NPC.numCreated ++;
    }

    /**
     * Initialises a static HashSet<String> containing all of the possible conversations that NPCs can use
     */
    public static void createConversationsHashSet(TextPrinter textPrinter)
    {  
        NPC.allConversations = textPrinter.returnContentsHashSet("texts/npc_texts.txt");
        for (String p: NPC.allConversations){
            System.out.println("Text: " + p);
        }
    }
}
