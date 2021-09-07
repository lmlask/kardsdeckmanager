package akkad.app.kards.deckmanager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DeckUtils {

    private static final String TAG = "DeckUtils";
    private final File file;
    HashMap<String, Card> cardMap;
    Context context;
    Boolean emptyFile;

    public DeckUtils(Context context) {
        super();
        this.context = context;
        String root = context.getFilesDir().toString();
        file = new File(root + File.separator + "decks.txt");
        cardMap = getCardMap();
    }

    public ArrayList<Deck> getDecks() {
        ArrayList<Deck> decks = new ArrayList<>();
        FileInputStream in;

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getDecks: File not found. Creating file with sample decks.");
            setSampleDecks();
            e.printStackTrace();
            return getDecks();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        Deck d;
        d = new Deck();

        try {
            String line;

            while ((line = reader.readLine()) != null) {
                d.setDeckName(line);

                d.setCountry((reader.readLine().split(" "))[2].toLowerCase());

                d.setAlly((reader.readLine().split(" "))[1].toLowerCase());

                reader.readLine();
                reader.readLine();
                reader.readLine();

                String currentLine;
                while (!(currentLine = reader.readLine()).equals("")) {
                    int number = (char) currentLine.charAt(0) - '0'; // This weird-ass line just reads the first char in the txt line as the number of cards and converts it to int.
                    String cardname = currentLine.substring(8);
                    for (int i = 0; i < number; i++) {
                        d.addCard(getCardFromMap(cardname));
                    }
                }

                reader.readLine();

                while (!(currentLine = reader.readLine()).equals("")) {
                    int number = (char) currentLine.charAt(0) - '0'; // This weird-ass line just reads the first char in the txt line as the number of cards and converts it to int.
                    String cardname = currentLine.substring(8);
                    for (int i = 0; i < number; i++) {
                        d.addCard(getCardFromMap(cardname));
                    }
                }

                d.setVictories(parseInt((reader.readLine().split(" "))[1]));
                d.setDefeats(parseInt((reader.readLine().split(" "))[1]));
                Log.d(TAG, "getDecks: Deck victories and defeats:" + d.getVictories() + ", " + d.getDefeats());

                reader.readLine();
                reader.readLine();
                reader.readLine();

                if ((reader.readLine()) == null) {
                    decks.add(d);
                    reader.close();
                    Collections.sort(decks);
                    return decks;
                } else {
                    decks.add(d);
                    d = new Deck();
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error in reading the decks database: " + e);
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                Log.d(TAG, "getDecks: Couldn't close file. " + ex);
            }

        }
        Log.d(TAG, "getDecks: Failed to get array of decks.");
        Toast.makeText(context, R.string.add_new_deck, Toast.LENGTH_SHORT).show();
        return new ArrayList<>();
    }

    public void setSampleDecks(){
        InputStream in = context.getResources().openRawResource(R.raw.decks);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine;

        ArrayList<String> lines = new ArrayList<>();
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();

            try (PrintWriter out = new PrintWriter(file)) {
                while ((nextLine = reader.readLine()) != null) {
                    lines.add(nextLine);
                }
                reader.close();
                for (int i = 0; i < lines.size(); i++) {
                    out.println(lines.get(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Card getCardFromMap(String name) {
        if ((cardMap.get(name)) != null) {
            return cardMap.get(name);
        } else {
            return ((Card) cardMap.get("PLACEHOLDER"));
        }
    }

    public HashMap<String, Card> getCardMap() {
        HashMap<String, Card> cards = new HashMap<>();

        //BufferedReader to read a txt file with all the cards information
        InputStream in = context.getResources().openRawResource(R.raw.cards);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine;
        try {
            Card c;
            while ((nextLine = reader.readLine()) != null) { //Reads the rest of the lines and creates cards with them
                String[] row = nextLine.split("\t");
                c = new Card();
                c.setCardId(row[0]);
                c.setName(row[1]);
                c.setKredits(parseInt(row[2]));
                c.setOpCost(parseInt(row[3]));
                c.setCountry(row[4]);
                c.setRarity(row[5]);
                c.setAttack(parseInt(row[6]));
                c.setDefense(parseInt(row[7]));
                c.setType(row[8]);
                c.setSpecial(row[9]);
                if (c.getType() == R.drawable.artillery || c.getType() == R.drawable.fighter || c.getType() == R.drawable.bomber) {
                    c.setAttackImage("long");
                } else {
                    c.setAttackImage("short");
                }
                c.setPassive(parseBoolean(row[10]));
                c.setExpansion(row[11]);
                cards.put(row[1], c);
                Log.d(TAG, "getCardMap: Adding card " + c.getName());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading the file:" + ex);
        }
        return cards;
    }

    private HashMap<String, Card> getIDMap() {
        HashMap<String, Card> cards = new HashMap<>();

        //BufferedReader to read a txt file with all the cards information
        InputStream in = context.getResources().openRawResource(R.raw.cards);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine;
        try {
            Card c;
            while ((nextLine = reader.readLine()) != null) { //Reads the rest of the lines and creates cards with them
                String[] row = nextLine.split("\t");
                c = new Card();
                c.setCardId(row[0]);
                c.setName(row[1]);
                c.setKredits(parseInt(row[2]));
                c.setOpCost(parseInt(row[3]));
                c.setCountry(row[4]);
                c.setRarity(row[5]);
                c.setAttack(parseInt(row[6]));
                c.setDefense(parseInt(row[7]));
                c.setType(row[8]);
                c.setSpecial(row[9]);
                if (c.getType() == R.drawable.artillery || c.getType() == R.drawable.fighter || c.getType() == R.drawable.bomber) {
                    c.setAttackImage("long");
                } else {
                    c.setAttackImage("short");
                }
                c.setPassive(parseBoolean(row[10]));
                cards.put(row[0], c);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading the file:" + ex);
        }
        return cards;
    }

    public void deleteDeckByName (String deckName) {
        Log.d(TAG, "deleteDeckByName: Deleting " + deckName);
        FileInputStream in = null;
        ArrayList<String> lines = new ArrayList<>();

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "deleteDeckByName: File not found, even though it should have been created when opening DeckManager!");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //Copy all decks to memory except for the one to delete
        try {
            String nextLine;
            while (!(nextLine = reader.readLine()).equals(deckName)) {
                lines.add(nextLine);
                Log.d(TAG, "deleteDeckByName: READ: " + nextLine);
            }
            //Skip deck to delete!
            while (!(nextLine = reader.readLine()).equals("IMPORT CODE:")) {
                //Do nothing
                Log.d(TAG, "deleteDeckByName: SKIP: " + nextLine);
            }
            reader.readLine();
            reader.readLine();
            //Continue reading until EOF
            while ((nextLine = reader.readLine()) != null) {
                lines.add(nextLine);
                Log.d(TAG, "deleteDeckByName: READ: " + nextLine);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Now get all those lines and put them in a new file
        try {
            file.createNewFile();

            try (PrintWriter out = new PrintWriter(file)) {
                for (int i = 0; i < (lines.size()-2); i++) {
                    out.println(lines.get(i));
                    Log.d(TAG, "deleteDeckByName: PRINT TO FILE: " + lines.get(i));
                }
                //Strip all (up to 2) blank lines at the end:
                try {
                    for (int i = 2; i > 0; i--) {
                        if (!lines.get(lines.size() - i).equals("")) {
                            out.println(lines.get(lines.size() - i));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    Log.d(TAG, "deleteDeckByName: Last deck deleted!");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateWinRate(String deckName, int victories, int defeats) {
        Log.d(TAG, "updateWinRate: Deleting " + deckName);

        FileInputStream in = null;
        ArrayList<String> lines = new ArrayList<>();

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "updateWinRate: File not found, even though it should have been created when opening DeckManager!");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //Copy all lines to memory
        try {
            String nextLine;
            while (!(nextLine = reader.readLine()).equals(deckName)) {
                lines.add(nextLine);
                Log.d(TAG, "updateWinRate: READ: " + nextLine);
            }
            lines.add(deckName);

            while (!(nextLine = reader.readLine()).split(" ")[0].equals("Victories:")) {
                lines.add(nextLine);
                Log.d(TAG, "updateWinRate: READ: " + nextLine);
            }
            lines.add("Victories: " + victories);
            lines.add("Defeats: " + defeats);
            reader.readLine();

            while ((nextLine = reader.readLine()) != null) {
                lines.add(nextLine);
                Log.d(TAG, "updateWinRate: READ: " + nextLine);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Now get all those lines and put them in a new file
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();

            try (PrintWriter out = new PrintWriter(file)) {
                for (int i = 0; i < (lines.size()-2); i++) {
                    out.println(lines.get(i));
                    Log.d(TAG, "updateWinRate: PRINT TO FILE: " + lines.get(i));
                }
                //Strip all (up to 2) blank lines at the end:
                try {
                    for (int i = 2; i > 0; i--) {
                        if (!lines.get(lines.size() - i).equals("")) {
                            out.println(lines.get(lines.size() - i));
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    Log.d(TAG, "deleteDeckByName: Last deck deleted!");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Deck getDeckByName(String deckName) {
        FileInputStream in;

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getDecks: Decks file not found.");
            e.printStackTrace();
            return new Deck();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        Deck d;
        d = new Deck();

        Log.d(TAG, "getDeckByName: Looking for deck " + deckName + ".");

        try {
            String line;

            try {
                while (!(line = reader.readLine()).equals(deckName)) {
                    Log.d(TAG, "getDeckByName: Skipping line: " + line);
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                return new Deck();
            }

            d.setDeckName(deckName);
            d.setCountry((reader.readLine().split(" "))[2].toLowerCase());
            d.setAlly((reader.readLine().split(" "))[1].toLowerCase());
            reader.readLine();
            reader.readLine();
            reader.readLine();

            //Add main country cards
            while (!(line = reader.readLine()).equals("")) {
                int number = (char) line.charAt(0) - '0'; // This weird-ass line just reads the first char in the txt line as the number of cards and converts it to int.
                String cardname = line.substring(8);
                for (int i = 0; i < number; i++) {
                    d.addCard(getCardFromMap(cardname));
                    Log.d(TAG, "getDecks: " + cardname + ": added " + getCardFromMap(cardname).getName());
                }
            }

            reader.readLine();

            //Add ally country cards
            while (!(line = reader.readLine()).equals("")) {
                int number = (char) line.charAt(0) - '0'; // This weird-ass line just reads the first char in the txt line as the number of cards and converts it to int.
                String cardname = line.substring(8);
                for (int i = 0; i < number; i++) {
                    d.addCard(getCardFromMap(cardname));
                    Log.d(TAG, "getDecks: " + cardname + ": added " + getCardFromMap(cardname).getName());
                }
            }

            d.setVictories(parseInt((reader.readLine().split(" "))[1]));
            d.setDefeats(parseInt((reader.readLine().split(" "))[1]));

            return d;
        }
        catch (IOException e) {
            throw new RuntimeException("Error in reading the decks database: " + e);
        }
        finally {
            try {
                in.close();
            } catch (IOException ex) {
                Log.d(TAG, "getDeckByName: Error closing the file.");
                ex.printStackTrace();
            }
        }
    }

    public void changeDeckName(String oldName, String newName) {
        Log.d(TAG, "changeDeckName: Changing " + oldName + " to " + newName);

        FileInputStream in = null;
        ArrayList<String> lines = new ArrayList<>();

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "changeDeckName: File not found, even though it should have been created when opening DeckManager!");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //Copy all lines to memory, replacing oldName with newName
        try {
            String nextLine;
            while (!(nextLine = reader.readLine()).equals(oldName)) {
                lines.add(nextLine);
                Log.d(TAG, "changeDeckName: READ: " + nextLine);
            }
            lines.add(newName);
            Log.d(TAG, "changeDeckName: ADD: " + newName);
            //Continue reading until EOF
            while ((nextLine = reader.readLine()) != null) {
                lines.add(nextLine);
                Log.d(TAG, "changeDeckName: READ: " + nextLine);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Now get all those lines and put them in a new file
        try {
            file.createNewFile();

            try (PrintWriter out = new PrintWriter(file)) {
                for (int i = 0; i < (lines.size()); i++) {
                    out.println(lines.get(i));
                    Log.d(TAG, "changeDeckName: PRINT TO FILE: " + lines.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void newDeck(String deckName, String mainCountry, String allyCountry, HashMap<Card, Integer> cardsInDeck) {
        emptyFile = isFileEmpty();
        Log.d(TAG, "newDeck: cardsInDeck is " + cardsInDeck);
        FileInputStream in = null;
        ArrayList<String> lines = new ArrayList<>();

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "changeDeckName: File not found, even though it should have been created when opening DeckManager!");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                lines.add(nextLine);
                Log.d(TAG, "newDeck: ADD: " + nextLine);
                //Continue reading until EOF
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            file.createNewFile();


            try (PrintWriter out = new PrintWriter(file)) {

                for (int i = 0; i < (lines.size()); i++) {
                    out.println(lines.get(i));
                    Log.d(TAG, "newDeck: PRINT TO FILE: " + lines.get(i));
                }

                //Now print the new deck:

                HashMap<String, String> countryMap = new HashMap<>();
                countryMap.put("usa", "USA"); countryMap.put("britain", "Britain"); countryMap.put("soviet", "Soviet"); countryMap.put("france", "France");
                countryMap.put("germany", "Germany"); countryMap.put("japan", "Japan"); countryMap.put("italy", "Italy");

                HashMap<String, String> hqMap = new HashMap<>();
                hqMap.put("usa", "CHERBOURG"); hqMap.put("britain", "ALEXANDRIA"); hqMap.put("soviet", "STALINGRAD");
                hqMap.put("germany", "DANZIG"); hqMap.put("japan", "HSINKING");

                if (!emptyFile) {
                    out.println(""); // New line to separate decks
                }
                out.println(deckName);
                out.println("Major power: " + countryMap.get(mainCountry));
                out.println("Ally: " + countryMap.get(allyCountry));
                out.println("HQ: " + hqMap.get(mainCountry));
                out.println("");
                out.println(countryMap.get(mainCountry) + ":");

                for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
                    if (entry.getKey().getCountryName().equals(mainCountry)) {
                        out.println(entry.getValue() + "x (" + entry.getKey().getKreditsNumber() + "K) " + entry.getKey().getName());
                    }
                }

                out.println("");
                out.println(countryMap.get(allyCountry) + ":");
                for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
                    if (entry.getKey().getCountryName().equals(allyCountry)) {
                        out.println(entry.getValue() + "x (" + entry.getKey().getKreditsNumber() + "K) " + entry.getKey().getName());
                    }
                }

                out.println("");
                out.println("Victories: 0");
                out.println("Defeats: 0");
                out.println("");
                out.println("IMPORT CODE:");
                out.println("NOT_IMPLEMENTED");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDeckDescription(String deckName, String mainCountry, String allyCountry, HashMap<Card, Integer> cardsInDeck) {

        String sep = System.lineSeparator();

        HashMap<String, String> countryMap = new HashMap<>();
        countryMap.put("usa", "USA"); countryMap.put("britain", "Britain"); countryMap.put("soviet", "Soviet"); countryMap.put("france", "France");
        countryMap.put("germany", "Germany"); countryMap.put("japan", "Japan"); countryMap.put("italy", "Italy");

        HashMap<String, String> hqMap = new HashMap<>();
        hqMap.put("usa", "CHERBOURG"); hqMap.put("britain", "ALEXANDRIA"); hqMap.put("soviet", "STALINGRAD");
        hqMap.put("germany", "DANZIG"); hqMap.put("japan", "HSINKING");

        StringBuilder builder = new StringBuilder()
                .append(deckName).append(sep)
                .append("Major power: ").append(countryMap.get(mainCountry)).append(sep)
                .append("Ally: ").append(countryMap.get(allyCountry)).append(sep)
                .append("HQ: ").append(hqMap.get(mainCountry)).append(sep)
                .append(sep)
                .append(countryMap.get(mainCountry)).append(":").append(sep);

                for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
                    if (entry.getKey().getCountryName().equals(mainCountry)) {
                        builder.append(entry.getValue()).append("x (").append(entry.getKey().getKreditsNumber()).append("K) ").append(entry.getKey().getName())
                                .append(sep);
                    }
                }

                builder.append(sep);
                builder.append(countryMap.get(allyCountry)).append(":").append(sep);

                for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
                    if (entry.getKey().getCountryName().equals(allyCountry)) {
                        builder.append(entry.getValue()).append("x (").append(entry.getKey().getKreditsNumber()).append("K) ").append(entry.getKey().getName())
                                .append(sep);
                    }
                }

                builder.append(sep);
                builder.append("IMPORT CODE: ").append(sep);
                builder.append(getImportCode(deckName));

                return builder.toString();
            }


    public void setCardsInDeck(String deckName, HashMap<Card, Integer> cardsInDeck) {
        deleteDeckByName(deckName);
        newDeck(deckName, this.getDeckByName(deckName).getCountry(), this.getDeckByName(deckName).getAlly(), cardsInDeck);
    }

    public ArrayList<String> getDeckNameList() {
        ArrayList<String> deckNames = new ArrayList<>();
        FileInputStream in;

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getDecks: File not found. Creating file with sample decks.");
            setSampleDecks();
            e.printStackTrace();
            return getDeckNameList();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            String line;

            while ((line = reader.readLine()) != null) {
                deckNames.add(line);
                Log.d(TAG, "getDeckNameList: ADDING NAME: " + line);
                //noinspection UnusedAssignment
                while (!(line = reader.readLine()).equals("IMPORT CODE:")) {
                    Log.d(TAG, "getDeckNameList: skipping...");
                }
                reader.readLine();
                reader.readLine();
            }
            Log.d(TAG, "getDeckNameList: deckNames: " + deckNames);
            return deckNames;
        }
        catch (IOException e) {
            throw new RuntimeException("Error in reading the decks database: " + e);
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                Log.d(TAG, "getDeckNameList: Couldn't close file. " + ex);
            }

        }
    }

    private boolean isFileEmpty() {
        FileInputStream in = null;
        ArrayList<String> lines = new ArrayList<>();

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "isFileEmpty: File not found, even though it should have been created when opening DeckManager!");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                lines.add(nextLine);
                Log.d(TAG, "newDeck: ADD: " + nextLine);
                //Continue reading until EOF
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (lines.size() == 0) {
            Log.d(TAG, "isFileEmpty: File is empty.");
            return true;
        } else {
            return false;
        }
    }

    public String getImportCode(String deckName) {
        //TODO: Still untested, may be fucky.

        Deck deck = getDeckByName(deckName);

        HashMap<String, Integer> countryMap = new HashMap<>();
        countryMap.put("germany", 1); countryMap.put("britain", 2);
        countryMap.put("japan", 3); countryMap.put("soviet", 4);
        countryMap.put("usa", 5); countryMap.put("france", 6);
        countryMap.put("italy", 7);

        StringBuilder builder = new StringBuilder();
        builder.append("%%")
                .append(countryMap.get(deck.getCountry()))
                .append(countryMap.get(deck.getAlly()))
                .append("|");

        //Cards 1 time
        for (Map.Entry<Card, Integer> entry : deck.getCardsInDeck().entrySet()) {
            if (entry.getValue() == 1) {
                builder.append(entry.getKey().getCardId());
            }
        }
        builder.append(";");

        //Cards 2 times
        for (Map.Entry<Card, Integer> entry : deck.getCardsInDeck().entrySet()) {
            if (entry.getValue() == 2) {
                builder.append(entry.getKey().getCardId());
            }
        }
        builder.append(";");

        //Cards 3 times
        for (Map.Entry<Card, Integer> entry : deck.getCardsInDeck().entrySet()) {
            if (entry.getValue() == 3) {
                builder.append(entry.getKey().getCardId());
            }
        }
        builder.append(";");

        //Cards 4 times
        for (Map.Entry<Card, Integer> entry : deck.getCardsInDeck().entrySet()) {
            if (entry.getValue() == 4) {
                builder.append(entry.getKey().getCardId());
            }
        }
        return builder.toString();
    }

    public Deck getDeckByCode(String importCode) {

        HashMap<Integer, String> countryMap = new HashMap<>();
        countryMap.put(1, "germany"); countryMap.put(2, "britain");
        countryMap.put(3, "japan"); countryMap.put(4, "soviet");
        countryMap.put(5, "usa"); countryMap.put(6, "france");
        countryMap.put(7, "italy");

        HashMap<String, Card> cards = getIDMap();
        Deck deck = new Deck();
        deck.setCardMap(new HashMap<>());

        if (importCode.length() < 10) {
            Toast.makeText(context, R.string.short_code, Toast.LENGTH_LONG).show();
            return null;
        }

        String main = importCode.substring(2, 3);
        String ally = importCode.substring(3, 4);


        try {
            deck.setCountry(countryMap.get(Integer.parseInt(main)));
            deck.setAlly(countryMap.get(Integer.parseInt(ally)));
        } catch (NumberFormatException e) {
            Toast.makeText(context, R.string.error_reading_code, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return new Deck();
        }

        String code = importCode.substring(5);

        String[] codes = {null, null, null, null};

        int counter = 0;
        for (int i = 0; i < 3; i++) {
            StringBuilder builder = new StringBuilder();
            while (code.charAt(counter)!=';') {
                builder.append(code.charAt(counter));
                counter++;
            }
            counter++;
            codes[i] = builder.toString();
            Log.d(TAG, "getDeckByCode: codes[" + i + "] is " + codes[i]);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = counter; i < code.length(); i++){
            builder.append(code.charAt(counter));
            counter++;
        }
        codes[3] = builder.toString();
        Log.d(TAG, "getDeckByCode: codes[" + 3 + "] is " + codes[3]);
        Log.d(TAG, "getDeckByCode: codes are " + codes[0] + " " + codes[1] + " " + codes[2] + " " + codes[3]);


        //Add cards to deck
        for (int i = 0; i < 4; i++) {
            String currentCode = codes[i];

            Iterable<String> codesIter = Splitter.fixedLength(2).split(currentCode);

            ArrayList<String> cardCodes = new ArrayList<>();
            for (String str : codesIter) {
                cardCodes.add(str);
            }

            boolean errors = false;
            Log.d(TAG, "getDeckByCode: cardCodes are" + cardCodes);
            for (int j = 0; j < cardCodes.size(); j++) {
                Log.d(TAG, "getDeckByCode: Reading card with id " + cardCodes.get(j));
                for (int k = 0; k < i+1; k++) {
                    if (cards.get(cardCodes.get(j)) != null) {
                        deck.addCard(cards.get(cardCodes.get(j)));
                    } else if ((cards.get(cardCodes.get(j)) == null) && (!cardCodes.get(0).equals(""))) {
                        errors = true;
                    }
                }
            }
            if (errors) {
                Toast.makeText(context, R.string.card_problems, Toast.LENGTH_LONG).show();
            }
        }

        deck.setVictories(0);
        deck.setDefeats(0);
        return deck;
    }
}