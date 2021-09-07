package akkad.app.kards.deckmanager;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Deck implements Comparable<Deck>, Serializable {

    private static final String TAG = "Deck.java";
    String deckName, country, ally;
    HashMap<Card, Integer> cardMap;
    int victories, defeats, id;

    //Setters
    public void setDeckName(String deckName) {
        if (this.cardMap == null) {
            this.cardMap = new HashMap<>();
        }
        this.deckName = deckName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCardMap(HashMap<Card, Integer> cardMap) {
        this.cardMap = new HashMap<>();
        this.cardMap = cardMap;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAlly(String ally) {
        this.ally = ally;
    }

    //Getters

    public int getId() {
        return id;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getCountry() {
        return country;
    }

    public String getAlly() {
        return ally;
    }

    public Integer getCardCount() {
        int count = 0;
        for (Map.Entry<Card, Integer> entry : cardMap.entrySet()) {
            count = count + entry.getValue();
        }
        return count;
    }

    public int getBackImage() {
        HashMap<String, Integer> backMap = new HashMap<>();
        backMap.put("usa", R.drawable.usa_back);
        backMap.put("britain", R.drawable.britain_back);
        backMap.put("soviet", R.drawable.soviet_back);
        backMap.put("germany", R.drawable.germany_back);
        backMap.put("japan", R.drawable.japan_back);
        try {
            return backMap.get(country);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "getBackImage: " + ex);
            return R.mipmap.ic_launcher;
        }
    }

    public int getMainImage() {
        HashMap<String, Integer> mainMap = new HashMap<>();
        mainMap.put("usa", R.drawable.usa_unselected);
        mainMap.put("britain", R.drawable.britain_unselected);
        mainMap.put("soviet", R.drawable.soviet_unselected);
        mainMap.put("germany", R.drawable.germany_unselected);
        mainMap.put("japan", R.drawable.japan_unselected);
        mainMap.put("france", R.drawable.france_unselected);
        mainMap.put("italy", R.drawable.italy_unselected);
        try {
            return mainMap.get(country);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "getMainImage: " + ex);
            return R.mipmap.ic_launcher;
        }

    }


    public int getAllyImage() {
        HashMap<String, Integer> allyMap = new HashMap<>();
        allyMap.put("usa", R.drawable.usa_unselected);
        allyMap.put("britain", R.drawable.britain_unselected);
        allyMap.put("soviet", R.drawable.soviet_unselected);
        allyMap.put("germany", R.drawable.germany_unselected);
        allyMap.put("japan", R.drawable.japan_unselected);
        allyMap.put("france", R.drawable.france_unselected);
        allyMap.put("italy", R.drawable.italy_unselected);
        try {
            return allyMap.get(ally);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "getAllyImage: " + ex);
            return R.mipmap.ic_launcher;
        }

    }

    public Integer getMainCount() {
        int mainCount = 0;
        for (Map.Entry<Card, Integer> entry : cardMap.entrySet()) {
            if (entry.getKey().getCountryName().equals(country)) {
                mainCount = mainCount + entry.getValue();
            }
        }
        return mainCount;
    }

    public Integer getAllyCount() {
        return (getCardCount() - getMainCount());
    }

    public HashMap<Card, Integer> getCardsInDeck() {
        if (cardMap==null) {
            return new HashMap<>();
        } else {
            return cardMap;
        }
    }

    public void addCard(Card card) {
        int count = 0;
        for (Map.Entry<Card, Integer> entry : cardMap.entrySet()) {
            if (entry.getKey().getName().equals(card.getName())) {
                count = count + entry.getValue();
            }
        }
        cardMap.put(card, (1 + count));
        Log.d(TAG, "addCard: Added " + card.getName() + " " + (1+count) + " times");
    }

    public void removeCard(Card card) {
        if (cardMap.get(card) == 1) {
            try {
                cardMap.remove(card);
            }
            catch (NullPointerException ex) {
                Log.d(TAG, "removeCard: Null pointer on card removal, it is not on the deck.");
            }
        }
        else if (!cardMap.containsKey(card)) {
            Log.d(TAG, "removeCard: Deck does not contain that card...");
        }
        else if (cardMap.get(card) > 1) {
            cardMap.put(card, cardMap.get(card)-1);
        }
    }

    public boolean isValid() {
        return (getCardCount() == 39) && (getAllyCount() <= 12);
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getVictories() {
        return victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public int getOrderCount() {
        int orderCount = 0;
        for (Map.Entry<Card, Integer> entry : cardMap.entrySet()) {
            if ((entry.getKey().getTypeName().equals("order")) || (entry.getKey().getTypeName().equals("cm"))) {
                orderCount = orderCount + entry.getValue();
            }
        }
        return orderCount;
    }

    public int compareTo(Deck d) {
        return deckName.compareToIgnoreCase(d.getDeckName());
    }
}