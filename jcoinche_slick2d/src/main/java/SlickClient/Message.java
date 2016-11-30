package SlickClient;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * Created by marcha_0 on 30/11/16.
 */
public class Message {

    private TrueTypeFont _ttf;
    private String _errorMsg;
    private String _turnWin;

    private String _gamePoints1;
    private String _gamePoints2;

    public Message()
    {
        _ttf = new TrueTypeFont(Fonts.font, true);
        _errorMsg = null;
        _turnWin = null;
        _gamePoints1 = null;
        _gamePoints2 = null;
    }

    public void draw() {
        if (_errorMsg != null) {
            _ttf.drawString(10, 750, _errorMsg, Color.white);
        }
        if (_turnWin != null) {
            _ttf.drawString(10, 800, _turnWin, Color.white);
        }
        if (_gamePoints1 != null && _gamePoints2 != null)
        {
            _ttf.drawString(1200, 20, _gamePoints1, Color.white);
            _ttf.drawString(1200, 50, _gamePoints2, Color.white);

        }
    }

    public String get_errorMsg() {
        return _errorMsg;
    }

    public void set_errorMsg(String _errorMsg) {
        this._errorMsg = _errorMsg;
    }

    public String get_turnWin() {
        return _turnWin;
    }

    public void set_turnWin(String _turnWin) {
        this._turnWin = _turnWin;
    }

    public void setGamePoint(String teamName1, String teamName2, int pointsTeam1, int pointsTeam2) {
        _gamePoints1 = teamName1 + ": " + pointsTeam1;
        _gamePoints2 = teamName2 + ": " + pointsTeam2;
    }
}
