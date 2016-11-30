package SlickClient;

import org.newdawn.slick.Image;
import jcoinche.protobuf.protos.Packets;

/**
 * Created by marcha_0 on 27/11/16.
 */
public class CardImage {
    private Image image;
    private Packets.Types types;
    private Packets.Values num;

    CardImage(Image image, Packets.Types type, Packets.Values num) {
        this.image = image;
        this.types = type;
        this.num = num;
    }

    public Packets.Types getType()
    {
        return this.types;
    }

    public Packets.Values getValue()
    {
        return this.num;
    }

    public Image getImage() {
        return this.image;
    }
}
