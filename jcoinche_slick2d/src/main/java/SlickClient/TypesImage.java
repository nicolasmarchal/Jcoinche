package SlickClient;

import org.newdawn.slick.Image;
import jcoinche.protobuf.protos.Packets;

/**
 * Created by marcha_0 on 29/11/16.
 */
public class TypesImage {

    Image image;
    Packets.Types type;

    public TypesImage(Image image, Packets.Types type)
    {
        this.image = image;
        this.type = type;
    }

    public Image getImage()
    {
        return image;
    }

    public Packets.Types getType()
    {
        return type;
    }
}
