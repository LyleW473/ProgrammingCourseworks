public class NPC 
{
    private static int numCreated = 0;
    public int id;


    public NPC()
    {
        id = NPC.numCreated;
        NPC.numCreated ++;
    }
}
