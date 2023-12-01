package dependencies.entities;

public class Player 
{
    private Room currentRoom;

    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    public void setCurrentRoom(Room selectedRoom)
    {
        currentRoom = selectedRoom;
    }

}
