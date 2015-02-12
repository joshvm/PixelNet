package res;

import javax.swing.ImageIcon;

public final class Res {

    private Res(){}

    public static ImageIcon icon(final String name, final int size){
        final String dest = String.format("%s_%d.png", name, size);
        return new ImageIcon(Res.class.getResource(dest));
    }
}
