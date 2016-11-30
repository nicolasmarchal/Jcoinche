package SlickClient;

import org.newdawn.slick.Image;

/**
 * Created by marcha_0 on 29/11/16.
 */
public class ValuesImage {
    Image image;
    int values;

    public ValuesImage(Image image, int values)
    {
        this.image = image;
        this.values = values;
    }

    public Image getImage() {
        return image;
    }

    public int getValues() {
        return values;
    }
}
