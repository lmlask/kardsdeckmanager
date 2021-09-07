package akkad.app.kards.deckmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DeckManagerActivity extends AppCompatActivity {
    private static final String TAG = "DeckManagerActivity";

    private static final int EDIT_DECK_CODE = 1;
    private static final int NEW_DECK_CODE = 2;
    private static final int VIEW_DECK_CODE = 3;
    private static final int EDIT_FROM_MANAGER_CODE = 4;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COLUMNS_NUMBER = "columnsNumber";

    DrawerLayout dl;
    RecyclerView recyclerView;
    DeckManagerAdapter adapter;
    StaggeredGridLayoutManager manager;
    ArrayList<Deck> decks;
    HashMap<String, Card> cardMap;
    ToggleButton usaFilter, britainFilter, sovietFilter, germanyFilter, japanFilter;
    HashMap<ToggleButton, String> countryMap;
    DeckUtils utils;
    AlertDialog newDeckDialog, importDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_manager);

        initWindow();
        initFilters();

        utils = new DeckUtils(this);
        cardMap = utils.getCardMap();
        decks = utils.getDecks();

        //Load sharedprefs
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int spanCountInt = sharedPreferences.getInt(COLUMNS_NUMBER, 2);

        //Init recyclerview
        recyclerView = findViewById(R.id.manager_recyclerview);
        adapter = new DeckManagerAdapter(decks, this);
        manager = new StaggeredGridLayoutManager(spanCountInt, LinearLayoutManager.VERTICAL);

        initRecyclerView();
        setUpImportDialog();
        resetFilterButtons();
    }

    private void initWindow() {
        Toolbar collectionToolbar = findViewById(R.id.toolbar_settings);
        super.setSupportActionBar(collectionToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.deck_manager_title);

        dl = findViewById(R.id.manager_drawer_layout);
        dl.setScrimColor(Color.parseColor("#80000000"));

        Window window = this.getWindow();
        window.setStatusBarColor (Color.parseColor("#181818"));
    }

    private void initFilters() {
        usaFilter = findViewById(R.id.filter_usa);
        britainFilter = findViewById(R.id.filter_britain);
        sovietFilter = findViewById(R.id.filter_soviet);
        germanyFilter = findViewById(R.id.filter_germany);
        japanFilter = findViewById(R.id.filter_japan);

        countryMap = new HashMap<>();
        countryMap.put(usaFilter, "usa");
        countryMap.put(britainFilter, "britain");
        countryMap.put(sovietFilter, "soviet");
        countryMap.put(germanyFilter, "germany");
        countryMap.put(japanFilter, "japan");
    }

    private void resetFilterButtons() {
        usaFilter.setChecked(false);
        britainFilter.setChecked(false);
        sovietFilter.setChecked(false);
        germanyFilter.setChecked(false);
        japanFilter.setChecked(false);
    }



    @Override
    public void onResume() {
        super.onResume();
        resetFilterButtons();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();

        // Filter button
        if (id == R.id.action_collection_filter) {
            if (!dl.isDrawerOpen(GravityCompat.END)) dl.openDrawer(GravityCompat.END);
            else dl.closeDrawer(GravityCompat.END);
            return true;
        }
        
        else if (id == R.id.action_new_deck) {
            showNewDeckDialog();
            return true;
        }

        // Back button (action bar)
        else if (id == android.R.id.home) {
            if (dl.isDrawerOpen(GravityCompat.END)) {
                dl.closeDrawer(GravityCompat.END);
            } else if (!selectionFinder().equals("NO_COUNTRY")) {
                for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
                    entry.getKey().setChecked(false);
                }
                adapter.swapDataSet(decks);
            } else {
                super.onBackPressed();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.END)) {
            dl.closeDrawer(GravityCompat.END);
        } else if (!selectionFinder().equals("NO_COUNTRY")) {
            for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
                entry.getKey().setChecked(false);
            }
            adapter.swapDataSet(decks);
        } else {
            super.onBackPressed();
        }
    }

    private String selectionFinder() {
        //Returns a ArrayList of 3 strings reflecting the current collection_menu.
        String selection = "";

        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            if (entry.getKey().isChecked()) {
                selection = entry.getValue();
                break;
            }
            selection = "NO_COUNTRY";
        }
        return selection;
    }

    private void deckFilter(ArrayList<Deck> mDecks) {
        ArrayList<Deck> decksFiltered = new ArrayList<>(mDecks);
        Deck d;
        Iterator<Deck> i = decksFiltered.iterator();
        while (i.hasNext()) {
            d = i.next();
            if (!(d.getCountry().equals(selectionFinder())
                    || (selectionFinder().equals("NO_COUNTRY")))) {
                i.remove();
            }
        }
        adapter.swapDataSet(decksFiltered);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void filterCountry(View v) {
        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            if (entry.getKey() != v) {
                entry.getKey().setChecked(false);
            }
        }
        deckFilter(decks);
    }

    private void setUpImportDialog() {
        AlertDialog.Builder imp = new AlertDialog.Builder(DeckManagerActivity.this, R.style.AlertDialogTheme);
        LayoutInflater inflater = DeckManagerActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_import_deck, null);
        imp.setTitle(getString(R.string.import_deck_title));
        imp.setView(dialogView);
        imp.setIcon(R.drawable.ic_import);
        imp.setMessage(getString(R.string.import_deck_message));

        EditText importEditName = dialogView.findViewById(R.id.edit_text_deckname);
        importEditName.setHint(getString(R.string.import_name_hint));

        EditText importEdit = dialogView.findViewById(R.id.edit_text_importcode);
        importEdit.setHint(getString(R.string.import_hint));

        imp.setNegativeButton(R.string.cancel, null);
        imp.setPositiveButton(R.string.import_deck, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Get last line of import text:
                String importCodeText = importEdit.getText().toString();
                //Split lines and only get the last one after trimming:
                importCodeText = importCodeText.trim();
                String[] importCodeTextArray = importCodeText.split("[\\r\\n]+");
                String importCode = importCodeTextArray[importCodeTextArray.length - 1];
                importCode = importCode.trim();
                Log.d(TAG, "onClick: ImportCode is " + importCode);


                String deckName = (importEditName.getText().toString());
                if (deckName.equals("")) {
                    for (int i = 0; i < decks.size(); i++) {
                        if (decks.get(i).getDeckName().equals("Imported Deck")) {
                            Toast.makeText(DeckManagerActivity.this, "Please rename your other imported deck before importing this one.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    deckName = "Imported Deck";
                }

                //Check for repeated name:
                ArrayList<String> nameArray = utils.getDeckNameList();
                boolean repeatedThis = false;
                for (int i = 0; i < nameArray.size(); i++) {
                    Log.d(TAG, "onClick: Testing " + nameArray.get(i) + " against " + importEditName.getText().toString());
                    if (nameArray.get(i).equals(importEditName.getText().toString())) {
                        Log.d(TAG, "onClick: Name is repeated.");
                        repeatedThis = true;
                        break;
                    }
                }

                if (!repeatedThis) {
                    Log.d(TAG, "onClick: Getting deck by code " + importCode);
                    Deck deck = utils.getDeckByCode(importCode);
                    if (deck == null) {
                        return;
                    }
                    Log.d(TAG, "onClick: CALLED as if name is not repeated.");
                    utils.newDeck(deckName, deck.getCountry(), deck.getAlly(), deck.getCardsInDeck());
                    decks = utils.getDecks();
                    adapter.swapDataSet(decks);
                } else {
                    Toast.makeText(DeckManagerActivity.this, getString(R.string.repeated_name), Toast.LENGTH_SHORT).show();
                }
            }
        });
        importDialog = imp.create();
    }

    public Card getCardFromMap(String name) {
        if ((cardMap.get(name)) != null) {
            return cardMap.get(name);
        } else {
            return cardMap.get("PLACEHOLDER");
        }
    }

    private void showNewDeckDialog() {
        ArrayList<String> nameArray = utils.getDeckNameList();

        AlertDialog.Builder a = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_deck, null);
        a.setView(dialogView);
        a.setTitle(getString(R.string.create_new_deck));
        a.setIcon(R.drawable.add_circle_icon);
        a.setCancelable(true);

        EditText edit = dialogView.findViewById(R.id.edit_title_new);
        ToggleButton main_usa = dialogView.findViewById(R.id.nation_usa);
        ToggleButton main_britain = dialogView.findViewById(R.id.nation_britain);
        ToggleButton main_soviet = dialogView.findViewById(R.id.nation_soviet);
        ToggleButton main_germany = dialogView.findViewById(R.id.nation_germany);
        ToggleButton main_japan = dialogView.findViewById(R.id.nation_japan);

        ToggleButton ally_usa = dialogView.findViewById(R.id.ally_usa);
        ToggleButton ally_britain = dialogView.findViewById(R.id.ally_britain);
        ToggleButton ally_soviet = dialogView.findViewById(R.id.ally_soviet);
        ToggleButton ally_germany = dialogView.findViewById(R.id.ally_germany);
        ToggleButton ally_japan = dialogView.findViewById(R.id.ally_japan);
        ToggleButton ally_france = dialogView.findViewById(R.id.ally_france);
        ToggleButton ally_italy = dialogView.findViewById(R.id.ally_italy);

        HashMap<ToggleButton, String> mainMap = new HashMap<>();
        mainMap.put(main_usa, "usa"); mainMap.put(main_britain, "britain"); mainMap.put(main_soviet, "soviet");
        mainMap.put(main_germany, "germany"); mainMap.put(main_japan, "japan");

        HashMap<ToggleButton, String> allyMap = new HashMap<>();
        allyMap.put(ally_usa, "usa"); allyMap.put(ally_britain, "britain"); allyMap.put(ally_soviet, "soviet");
        allyMap.put(ally_germany, "germany"); allyMap.put(ally_japan, "japan"); allyMap.put(ally_france, "france"); allyMap.put(ally_italy, "italy");

        a.setNegativeButton(R.string.cancel, null);


        //Set OnClickListener for main nation buttons
        for (ToggleButton i : mainMap.keySet()) {
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Decheck all others
                    for (ToggleButton button : mainMap.keySet()) {
                        if (!(button == i)) {
                            button.setChecked(false);
                        }
                    }
                }
            });
        }

        //Set onClickListener for ally buttons
        for (ToggleButton i : allyMap.keySet()) {
            i.setOnClickListener(v -> {
                //Decheck all others
                for (ToggleButton button : allyMap.keySet()) {
                    if (!(button == i)) {
                        button.setChecked(false);
                    }
                }
            });
        }

        a.setNeutralButton(R.string.import_deck, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        importDialog.show();
                    }
                });

        a.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean repeatedName = false;
                boolean hasMain = false;
                boolean hasAlly = false;
                String main = "";
                String ally = "";
                String name = null;


                for (ToggleButton i : mainMap.keySet()) {
                    if (i.isChecked()) {
                        main = mainMap.get(i);
                        hasMain = true;
                    }
                }

                for (ToggleButton i : allyMap.keySet()) {
                    if (i.isChecked()) {
                        ally = allyMap.get(i);
                        hasAlly = true;
                    }
                }

                for (int i= 0; i < nameArray.size(); i++) {
                    if (nameArray.get(i).equals(edit.getText().toString())) {
                        repeatedName = true;
                        break;
                    }
                }

                if (edit.getText().toString().equals("")) {
                    for (int i= 0; i < nameArray.size(); i++) {
                        if (nameArray.get(i).equals("Deck " + (decks.size()+1))) {
                            name = "Deck " + (decks.size()+1) + " (new)";
                            break;
                        }
                    }
                    if (name == null) {
                        name = "Deck " + (decks.size()+1);
                    }

                } else {
                    name = edit.getText().toString();
                }

                if (!hasMain) {
                    Toast.makeText(DeckManagerActivity.this, R.string.no_main_country, Toast.LENGTH_SHORT).show();
                }
                else if (!hasAlly) {
                    Toast.makeText(DeckManagerActivity.this, R.string.no_ally, Toast.LENGTH_SHORT).show();
                }
                else if (main.equals(ally)) {
                    Toast.makeText(DeckManagerActivity.this, R.string.main_ally_same, Toast.LENGTH_SHORT).show();
                }
                else if (repeatedName) {
                    Toast.makeText(DeckManagerActivity.this, R.string.repeated_name, Toast.LENGTH_SHORT).show();
                }

                if ((hasMain) && (hasAlly) && (!repeatedName)) {
                    if (!main.equals(ally)) {
                        Intent editDeckIntent = new Intent(DeckManagerActivity.this, DeckBuilderActivity.class);
                        editDeckIntent.putExtra("deckName", name);
                        editDeckIntent.putExtra("mainCountry", main);
                        editDeckIntent.putExtra("allyCountry", ally);
                        editDeckIntent.putExtra("newDeck", true);
                        editDeckIntent.putExtra("requestCode", NEW_DECK_CODE);
                        startActivityForResult(editDeckIntent, NEW_DECK_CODE);
                    }
                }
            }
        });
        newDeckDialog = a.create();
        newDeckDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_DECK_CODE) {
            if (resultCode == RESULT_OK) {

                // Get Deck data from Intent
                Deck returnDeck = (Deck) data.getSerializableExtra("deck");
                String returnDeckName = data.getStringExtra("deckName");

                //Change cards file to match
                utils.newDeck(returnDeckName, returnDeck.getCountry(), returnDeck.getAlly(), returnDeck.getCardsInDeck());

                //Show deck in decks
                decks.add(returnDeck);
                adapter.swapDataSet(decks);
            }
        }

        if (requestCode == VIEW_DECK_CODE) {
            if (resultCode == RESULT_OK) {
                adapter.swapDataSet(utils.getDecks());
            }
        }

        if (requestCode == EDIT_FROM_MANAGER_CODE) {
            if (resultCode == RESULT_OK) {
                adapter.swapDataSet(utils.getDecks());
            }
        }
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }
}




