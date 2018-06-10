import java.io.Serializable;

/**
 * @author Marcin Rudko
 * @version 31.05.2018
 **/

public class Game implements Serializable {
    static final long serialVersionUID = 17975L;                  //-> Klucz obiektu do weryfikacji zapisu i wczytywania danych
    Object[] game;
    String gameTitle;
    String gameProducer;
    String gameCategory;
    int gamePlayers;
    float gamePrice;

    public Game(String gameTitle,String gameProducer,String gameCategory,int gamePlayers,float gamePrice){

        this.game = new Object[6];
        this.gameTitle = gameTitle;
        this.gameProducer = gameProducer;
        this.gameCategory = gameCategory;
        this.gamePlayers = gamePlayers;
        this.gamePrice = gamePrice;

        this.game[0] = false;
        this.game[1] = gameTitle;
        this.game[2] = gameProducer;
        this.game[3] = gameCategory;
        this.game[4] = gamePlayers;
        this.game[5] = gamePrice;
    }
}
