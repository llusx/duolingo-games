package cth.webapp.duogames.duogames.control;

import cth.webapp.duogames.duogames.model.MultiplayerGame;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "multi")
@ApplicationScoped
public class MultiplayerBean {

    private Map<String, MultiplayerGame> games;

    public MultiplayerBean() {
        games = new HashMap<>();
    }

    public boolean tryGame(String gameid, String uid, int nrWords, String language) {

        Boolean canJoin = joinGame(gameid, uid, nrWords, language);
        Boolean canCreate = createGame(gameid, uid, nrWords, language);
        Boolean canStart = canJoin | canCreate;

        return canStart;

    }

    private boolean createGame(String gameid, String uid, int nrWords, String language) {
        if (games.containsKey(gameid)) {
            return false;
        }

        games.put(gameid, new MultiplayerGame(nrWords, language, uid));
        return true;
    }

    private boolean joinGame(String gameid, String uid, int nrWords, String language) {
        if (!games.containsKey(gameid)) {
            return false;
        }

        MultiplayerGame game = games.get(gameid);

        return game.addParticipant(uid, nrWords, language);
    }

    public String getGameIdByUser(String uid) {
        return " ";
    }

    public int getRanking(String gameid, String userid, Integer score) {
        MultiplayerGame game = games.get(gameid);

        game.addScore(userid, score);

        return game.getRanking(userid);

    }

    private void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String generateGameID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
