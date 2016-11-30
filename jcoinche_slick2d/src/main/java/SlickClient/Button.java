package SlickClient;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by marcha_0 on 28/11/16.
 */

import jcoinche.protobuf.protos.Packets;

public class Button {

    private Image image;
    private boolean isClicked;
    private boolean onMouse;
    private Packets.Types type;
    private int values;


    private int x, y;

    public Button(Image image, Packets.Types type) throws SlickException {
        this.image = image;
        this.x = 0;
        this.y = 0;
        this.type = type;
    }

    public Button(Image image, int values) throws SlickException {
        this.image = image;
        this.x = 0;
        this.y = 0;
        this.values = values;
    }

    public Button(Image image) {
        this.image = image;
        this.x = 0;
        this.y = 0;
    }

    public boolean onMouse(int posX, int posY)
    {
        onMouse = posX >= x && posX <= x + image.getWidth() && posY >= y && posY <= y + image.getHeight();
        return onMouse;
    }

    public void setOnClick(int posX, int posY){
        if (onMouse)
            isClicked = !isClicked;
    }

    public boolean isOnMouse()
    {
        return onMouse;
    }

    public boolean isClicked()
    {
        return isClicked;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Image getImage()
    {
        return image;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void draw()
    {
        if (onMouse)
            image.draw(x, y, 1.0f, new Color(220, 220, 220, 255));
        else
            image.draw(x, y, 1.0f);
    }

    public Packets.Types getType() {
        return type;
    }

    public int getValues() {
        return values;
    }

    public void setClick(boolean click) {
        this.onMouse = false;
        this.isClicked = false;
    }
}
