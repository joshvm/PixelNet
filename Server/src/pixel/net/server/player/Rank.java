package pixel.net.server.player;

public enum Rank {

    Player("Player", 0, 0),
    Helper("Helper", 1, 0x00879C),
    Moderator("Moderator", 2, 0x00C903),
    Administrator("Administrator", 3, 0xC900AB),
    Owner("Owner", 4, 0xFF0000);

    public final String name;
    public final byte id;
    public final int color;

    private Rank(final String name, final int id, final int color){
        this.name = name;
        this.id = (byte)id;
        this.color = color;
    }

    public String toString(){
        return name;
    }

    public static Rank byId(final byte id){
        for(final Rank r : values())
            if(r.id == id)
                return r;
        return null;
    }
}
