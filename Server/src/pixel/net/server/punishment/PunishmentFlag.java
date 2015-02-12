package pixel.net.server.punishment;

public enum PunishmentFlag {

    Mute("Mute", 0x2),
    Ban("Ban", 0x4),
    IpMute("IP-Mute", 0x8),
    IpBan("IP-Ban", 0x10);

    public final String name;
    public final int flag;

    private PunishmentFlag(final String name, final int flag){
        this.name = name;
        this.flag = flag;
    }

    public String toString(){
        return name;
    }

    public static PunishmentFlag byFlag(final int flag){
        for(final PunishmentFlag p : values())
            if(p.flag == flag)
                return p;
        return null;
    }
}
