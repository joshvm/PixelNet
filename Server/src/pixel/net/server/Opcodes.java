package pixel.net.server;

public interface Opcodes {

    byte Register = 0;
    byte Login = 1;

    byte PopupMessage = 2;
    byte Message = 3;

    byte PlayerJoin = 4;
    byte PlayerLeave = 5;
    byte PlayerUpdate = 6;
}
