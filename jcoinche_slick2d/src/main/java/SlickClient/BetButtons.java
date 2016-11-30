package SlickClient;

import client.PacketSender;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcha_0 on 28/11/16.
 */
public class BetButtons {

    private List<Button> buttonsTypes;
    private List<Button> buttonsValues;

    private Button surCoinche;
    private Button coinche;
    private Button pass;

    private int selectedType;
    private int selectedValue;

    private boolean _turn;

    public BetButtons() throws SlickException {
        buttonsTypes = new ArrayList<Button>();
        buttonsValues = new ArrayList<Button>();

        selectedType = -1;
        selectedValue = -1;
        _turn = false;

        surCoinche = new Button(Ressources.surCoinche);
        coinche = new Button(Ressources.coinche);
        pass = new Button(Ressources.pass);

        buttonsTypes.add(new Button(Ressources.typesImages.get(0).getImage(), Ressources.typesImages.get(0).getType()));
        buttonsTypes.add(new Button(Ressources.typesImages.get(1).getImage(), Ressources.typesImages.get(1).getType()));
        buttonsTypes.add(new Button(Ressources.typesImages.get(2).getImage(), Ressources.typesImages.get(2).getType()));
        buttonsTypes.add(new Button(Ressources.typesImages.get(3).getImage(), Ressources.typesImages.get(3).getType()));
        buttonsTypes.add(new Button(Ressources.typesImages.get(4).getImage(), Ressources.typesImages.get(4).getType()));
        buttonsTypes.add(new Button(Ressources.typesImages.get(5).getImage(), Ressources.typesImages.get(5).getType()));

        buttonsValues.add(new Button(Ressources.valuesImages.get(0).getImage(), Ressources.valuesImages.get(0).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(1).getImage(), Ressources.valuesImages.get(1).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(2).getImage(), Ressources.valuesImages.get(2).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(3).getImage(), Ressources.valuesImages.get(3).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(4).getImage(), Ressources.valuesImages.get(4).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(5).getImage(), Ressources.valuesImages.get(5).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(6).getImage(), Ressources.valuesImages.get(6).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(7).getImage(), Ressources.valuesImages.get(7).getValues()));
        buttonsValues.add(new Button(Ressources.valuesImages.get(8).getImage(), Ressources.valuesImages.get(8).getValues()));


        int x; int y;
        x = 500;
        y = 550;
        for (Button button : buttonsTypes)
        {
            button.setX(x);
            button.setY(y);
            x += button.getImage().getWidth() + 10;
        }
        x = 480;
        y = 560;
        for (Button button : buttonsValues)
        {
            button.setX(x);
            button.setY(y);
            x += button.getImage().getWidth() + 5;
        }
        pass.setX(520);
        pass.setY(500);
        coinche.setX(520 + coinche.getImage().getWidth() + 5);
        coinche.setY(500);
        surCoinche.setX(520 + coinche.getImage().getWidth() * 2 + 10 );
        surCoinche.setY(500);
        //setPosition
    }

    public void checkMouse(int posX, int posY)
    {
        for (Button button : buttonsTypes)
            button.onMouse(posX, posY);

        for (Button button : buttonsValues)
            button.onMouse(posX, posY);

        coinche.onMouse(posX, posY);
        surCoinche.onMouse(posX, posY);
        pass.onMouse(posX, posY);
    }

    public void onClick(int posX, int posY)
    {
       if (!_turn)
            return;
        int i = 0;
        if (selectedType == -1) {
            for (Button button : buttonsTypes) {
                button.setOnClick(posX, posY);
                if (button.isClicked())
                    selectedType = i;
                ++i;
            }
            pass.setOnClick(posX, posY);
            coinche.setOnClick(posX, posY);
            surCoinche.setOnClick(posX, posY);
            if (pass.isClicked())
            {
                PacketSender.getDefaultInstance().sendBetPass();
                init_bet();
                return;
            }
            if (coinche.isClicked())
            {
                PacketSender.getDefaultInstance().sendBetCoinche();

                init_bet();
                return;
            }
            if (surCoinche.isClicked())
            {
                PacketSender.getDefaultInstance().sendBetSurCoinche();
                init_bet();
                return;
            }

            return;
        }
        i = 0;
        if (selectedValue == -1) {
            for (Button button : buttonsValues) {
                button.setOnClick(posX, posY);
                if (button.isClicked()){
                    selectedValue = i;
                    PacketSender.getDefaultInstance().sendGraphBet(buttonsTypes.get(selectedType).getType(), button.getValues());
                    // SEND BET
                    init_bet();

                }

                ++i;
            }
        }
    }

    private void init_bet() {
        selectedType = -1;
        selectedValue = -1;
        _turn = false;

        pass.setClick(false);
        coinche.setClick(false);
        surCoinche.setClick(false);
        for (Button button : buttonsValues) {
            button.setClick(false);
        }
        for (Button button : buttonsTypes) {
            button.setClick(false);
        }
    }

    public void setTurn(boolean turn)
    {
        _turn = turn;
    }

    public void drawButtons() {
        if (!_turn)
            return;
        if (selectedType == -1) {
            for (Button button : buttonsTypes) {
                button.draw();
            }
            pass.draw();
            coinche.draw();
            surCoinche.draw();
        }
        if (selectedType != -1 && selectedValue == -1)
        {
            for (Button button : buttonsValues)
                button.draw();
        }
    }

}
