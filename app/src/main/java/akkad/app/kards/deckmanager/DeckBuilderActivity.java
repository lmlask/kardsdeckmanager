package akkad.app.kards.deckmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class DeckBuilderActivity extends AppCompatActivity {

    private static final String TAG = "DeckBuilder";
    private static final int EDIT_DECK_CODE = 1;
    private static final int NEW_DECK_CODE = 2;
    private static final int EDIT_FROM_MANAGER_CODE = 4;
    private BuilderCollectionFragment collectionFragment;
    private BuilderCardsFragment cardsFragment;
    private String layout, deckName;
    private DrawerLayout dl;
    HashMap<Card, Integer> allCards, cardsInDeck;
    HashMap<String, Card> stringMap;
    private Deck deck;
    private AlertDialog nameDialog, exitDialog;
    private boolean deckChanged, newDeck, nameChanged;
    DeckUtils utils;
    private int requestCode;
    ImageView mainImage, allyImage;
    TextView mainCountText, allyCountText, totalCount;
    FloatingActionButton button;

    //Initializing filter vars
    ToggleButton country1btn, country2btn, infBtn, artBtn, tankBtn, orderBtn, cmBtn, fighterBtn, bomberBtn, k1btn, k2btn, k3btn, k4btn, k5btn, k6btn, k7btn;
    HashMap<ToggleButton, String> countryMap, typeMap, kreditMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);

        layout = "list";
        deckChanged = false;
        nameChanged = false;
        utils = new DeckUtils(this);
        button = findViewById(R.id.fab_save);

        getIncomingIntents();
        setUpToolbar();
        populateAllCards();

        if (newDeck) {
            deck.setCardMap(new HashMap<>());
            if(getIntent().hasExtra("deckName")){
                Log.d(TAG, "getIncomingIntent: intent has extra deckName.");
                deck.setDeckName(getIntent().getStringExtra("deckName"));
            }
        }

        setUpDialogs();
        initBottomBar();
        updateBottomBar();
        initFilters();
        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        Toast.makeText(this, "Tap to add or remove cards. Tap and hold to see the card image.", Toast.LENGTH_LONG).show();
    }



    private void setUpToolbar() {
        //Toolbar
        Toolbar toolbar = findViewById(R.id.deckbuilder_toolbar);
        super.setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(deckName);

        //Status bar
        Window window = this.getWindow();
        window.setStatusBarColor (Color.parseColor("#181818"));

        //View pager
        ViewPager viewPager = findViewById(R.id.builder_view_pager);
        TabLayout tabLayout = findViewById(R.id.builder_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        DeckBuilderActivity.ViewPagerAdapter vpAdapter = new DeckBuilderActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);

        //Fragments
        collectionFragment = BuilderCollectionFragment.newInstance();
        cardsFragment = BuilderCardsFragment.newInstance();

        //View pager
        vpAdapter.addFragment(collectionFragment, getString(R.string.collection));
        vpAdapter.addFragment(cardsFragment, getString(R.string.deck));
        //vpAdapter.addFragment(statsFragment, getString(R.string.stats));
        viewPager.setAdapter(vpAdapter);

        //Drawer layout
        dl = (DrawerLayout) findViewById(R.id.builder_drawer_layout);
        dl.setScrimColor(Color.parseColor("#80000000"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.builder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();

        if (id == R.id.action_change_name) {
            nameDialog.show();
            return true;
        }

        if (id == R.id.action_collection_filter) {
            if(!dl.isDrawerOpen(GravityCompat.END)) {
                dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
                dl.openDrawer(GravityCompat.END);
                dl.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        button.hide(true);
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        button.show(true);
                        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) { }
                });
                button.hide(true);
            }
            else {
                dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
                dl.closeDrawer(GravityCompat.END);
                dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
                button.show(true);
            }
            return true;
        }

        if (id == R.id.action_change_layout) {
            if (layout.equals("list")) {
                ((BuilderCardsFragment)cardsFragment).setLayout("headline");
                ((BuilderCollectionFragment)collectionFragment).setLayout("headline");
                item.setIcon(R.drawable.list_icon);
                layout = "headline";
            }
            else if (layout.equals("headline")) {
                ((BuilderCardsFragment)cardsFragment).setLayout("list");
                ((BuilderCollectionFragment)collectionFragment).setLayout("list");
                item.setIcon(R.drawable.headline_icon);
                layout = "list";
            }
            return true;
        }

        // Top left btn
        if (id == android.R.id.home) {
            if (dl.isDrawerOpen(GravityCompat.END)) {
                dl.closeDrawer(GravityCompat.END);
                button.show(true);
            }
            else if (deckChanged) {
                exitDialog.show();
            }
            else if (newDeck) {
                exitDialog.show();
            }
            else {
                finishHandler(null);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: Called.");
        if (dl.isDrawerOpen(GravityCompat.END)) {
            dl.closeDrawer(GravityCompat.END);
            dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
            button.show(true);
        }
        else if (deckChanged) {
            exitDialog.show();
        }
        else if (newDeck) {
            exitDialog.show();
        }
        else {
            finishHandler(null);
        }
    }

    private void getIncomingIntents() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("deckName")){
            Log.d(TAG, "getIncomingIntent: intent has extra deckName.");
            deckName = getIntent().getStringExtra("deckName");
        }
        if(getIntent().hasExtra("requestCode")){
            Log.d(TAG, "getIncomingIntent: intent has extra requestCode.");
            requestCode = getIntent().getIntExtra("requestCode", EDIT_DECK_CODE);
        }
        deck = utils.getDeckByName(deckName);
        Log.d(TAG, "onCreate: Looking for deck " + deckName);
        if(getIntent().hasExtra("newDeck")){
            Log.d(TAG, "getIncomingIntent: intent has extra newDeck.");

            newDeck = getIntent().getBooleanExtra("newDeck", false);
            deckChanged = true;
            if(getIntent().hasExtra("mainCountry")){
                Log.d(TAG, "getIncomingIntent: intent has extra mainCountry.");
                deck.setCountry(getIntent().getStringExtra("mainCountry"));
            }
            if(getIntent().hasExtra("allyCountry")){
                Log.d(TAG, "getIncomingIntent: intent has extra allyCountry.");
                deck.setAlly(getIntent().getStringExtra("allyCountry"));
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            invalidateOptionsMenu();
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void setUpDialogs() {
        final AlertDialog.Builder h = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        h.setTitle(R.string.exit_without_saving);
        h.setMessage(R.string.exit_without_saving_message);
        h.setIcon(android.R.drawable.ic_dialog_alert);
        h.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        h.setNegativeButton(R.string.cancel, null);


        final AlertDialog.Builder c = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_title, null);
        c.setView(dialogView);
        c.setTitle(getString(R.string.namechange_title));
        c.setIcon(R.drawable.edit_icon);
        c.setCancelable(true);
        EditText edit = (EditText) dialogView.findViewById(R.id.edit_title);
        edit.setHint(deck.getDeckName());

        c.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean repeatedName = false;
                ArrayList<String> nameList = utils.getDeckNameList();
                String newName = edit.getText().toString().trim();
                if (nameList.contains(newName)) {
                    repeatedName = true;
                }
                if (!newName.equals("") && !repeatedName) {
                    newName = newName.trim();
                    getSupportActionBar().setTitle(newName);
                    deck.setDeckName(newName);
                    nameChanged = true;
                } else if (repeatedName) {
                    Toast.makeText(DeckBuilderActivity.this, getString(R.string.repeated_name), Toast.LENGTH_SHORT).show();
                }
                edit.setText("");
            }});
        c.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit.setText("");
            }
        });
        nameDialog = c.create();
        exitDialog = h.create();
    }

    public Deck getDeck() {
        return deck;
    }

    public void finishHandler(View v) {
        if (deck.getCardCount() != 39 || deck.getAllyCount() > 12) {
            //TODO: Invalid deck warning
        }
        if (requestCode == EDIT_FROM_MANAGER_CODE) {
            Intent result = new Intent();
            result.putExtra("deck", deck);
            result.putExtra("deckName", deck.getDeckName());
            result.putExtra("oldName", deckName);
            result.putExtra("deckChanged", deckChanged);
            result.putExtra("nameChanged", nameChanged);
            setResult(RESULT_OK, result);

            if (nameChanged) {
                utils.changeDeckName(deckName, deck.getDeckName());
            }

            if (deckChanged) {
                utils.deleteDeckByName(deck.getDeckName());
                utils.newDeck(deck.getDeckName(), deck.getCountry(), deck.getAlly(), deck.getCardsInDeck());
                Toast.makeText(this, getString(R.string.cards_in_deck_changed), Toast.LENGTH_SHORT).show();
            }

            finish();
        }
        else if (requestCode == NEW_DECK_CODE) {
            Intent result = new Intent();
            result.putExtra("deck", deck);
            result.putExtra("deckName", deck.getDeckName());
            setResult(RESULT_OK, result);

            finish();
        }
        else {
            //Code is EDIT_DECK_CODE
            Intent result = new Intent();
            result.putExtra("deck", deck);
            result.putExtra("deckName", deck.getDeckName());
            result.putExtra("oldName", deckName);
            result.putExtra("deckChanged", deckChanged);
            result.putExtra("nameChanged", nameChanged);
            setResult(RESULT_OK, result);

            finish();
        }
    }

    public void initBottomBar() {
        mainImage = findViewById(R.id.main_image);
        allyImage = findViewById(R.id.ally_image);
        mainCountText = findViewById(R.id.main_count);
        allyCountText = findViewById(R.id.ally_count);
        totalCount = findViewById(R.id.total_count);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishHandler(v);
            }
        });
    }

    public void updateBottomBar() {
        Integer cardCount = deck.getCardCount()+1; // +1 to account for HQ card.
        String cardCountDraw = (cardCount.toString() + "/40");

        mainImage.setImageResource(deck.getMainImage());
        allyImage.setImageResource(deck.getAllyImage());
        Log.d(TAG, "updateBottomBar: mainCountry is" + deck.getCountry());
        mainCountText.setText(deck.getMainCount().toString());
        allyCountText.setText(deck.getAllyCount().toString());
        totalCount.setText(cardCountDraw);

        if (deck.getAllyCount() > 12) {
            allyCountText.setTextColor(Color.parseColor("#FF0000"));
        } else {
            allyCountText.setTextColor(Color.parseColor("#D3D1C3"));
        }

        if (deck.getCardCount() > 39) {
            totalCount.setTextColor(Color.parseColor("#FF0000"));
        } else {
            totalCount.setTextColor(Color.parseColor("#D3D1C3"));
        }
    }

    public HashMap<Card, Integer> getAllCards() {
        return allCards;
    }

    @Override
    protected void onDestroy() {
        try {
            nameDialog.dismiss();
            exitDialog.dismiss();
        } catch (NullPointerException ex) {
            Log.d(TAG, "onDestroy: Null caught.");
        }
        super.onDestroy();
    }

    private void populateAllCards() {
        cardsInDeck = new HashMap<>();
        allCards = new HashMap<>();

        stringMap = utils.getCardMap();

        for (Map.Entry<String, Card> entry : stringMap.entrySet()) {
            //Find card key in deck.
            //TODO: This is the bug. If a card has an OnlySpawnable version it goes missing.
            if (!entry.getValue().getExpansion().equals("OnlySpawnable")) {
                allCards.put(entry.getValue(), 0);
                for (Map.Entry<Card, Integer> entry_deck : deck.getCardsInDeck().entrySet()) {
                    if (entry_deck.getKey().getName().equals(entry.getKey())) {
                        allCards.put(entry.getValue(), entry_deck.getValue());
                        cardsInDeck.put(entry.getValue(), entry_deck.getValue());
                        break;
                    }
                }
            }
        }
        deck.setCardMap(cardsInDeck);
    }

    public String getLayout() {
        return layout;
    }

    private void initFilters() {
        HashMap<String, Integer> countrySelectorMap = new HashMap<>();
        countrySelectorMap.put("usa", R.drawable.usa_toggle_button_layout); countrySelectorMap.put("britain", R.drawable.britain_toggle_button_layout);
        countrySelectorMap.put("soviet", R.drawable.soviet_toggle_button_layout); countrySelectorMap.put("france", R.drawable.france_toggle_button_layout);
        countrySelectorMap.put("germany", R.drawable.germany_toggle_button_layout); countrySelectorMap.put("italy", R.drawable.italy_toggle_button_layout);
        countrySelectorMap.put("japan", R.drawable.japan_toggle_button_layout);

        country1btn = findViewById(R.id.filter_country1);
        country2btn = findViewById(R.id.filter_country2);

        Log.d(TAG, "initFilters: Trying to set country buttons.");
        country1btn.setBackgroundResource(countrySelectorMap.get(deck.getCountry()));
        country2btn.setBackgroundResource(countrySelectorMap.get(deck.getAlly()));

        infBtn = findViewById(R.id.filter_infantry);
        artBtn = findViewById(R.id.filter_artillery);
        tankBtn = findViewById(R.id.filter_tank);
        fighterBtn = findViewById(R.id.filter_fighter);
        bomberBtn = findViewById(R.id.filter_bomber);
        orderBtn = findViewById(R.id.filter_order);
        cmBtn = findViewById(R.id.filter_cm);

        k1btn = findViewById(R.id.filter_k1);
        k2btn = findViewById(R.id.filter_k2);
        k3btn = findViewById(R.id.filter_k3);
        k4btn = findViewById(R.id.filter_k4);
        k5btn = findViewById(R.id.filter_k5);
        k6btn = findViewById(R.id.filter_k6);
        k7btn = findViewById(R.id.filter_k7);

        countryMap = new HashMap<>(); typeMap = new HashMap<>(); kreditMap = new HashMap<>();

        countryMap.put(country1btn, deck.getCountry()); countryMap.put(country2btn, deck.getAlly());

        typeMap.put(infBtn, "infantry"); typeMap.put(artBtn, "artillery"); typeMap.put(tankBtn, "tank"); typeMap.put(fighterBtn, "fighter");
        typeMap.put(bomberBtn, "bomber"); typeMap.put(orderBtn, "order"); typeMap.put(cmBtn, "countermeasure");

        kreditMap.put(k1btn, "1"); kreditMap.put(k2btn, "2"); kreditMap.put(k3btn, "3"); kreditMap.put(k4btn, "4");
        kreditMap.put(k5btn, "5"); kreditMap.put(k6btn, "6"); kreditMap.put(k7btn, "7");

    }

    public void filterCountry(View v) {
        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            if (entry.getKey() != (ToggleButton)v) {
                entry.getKey().setChecked(false);
            }
        }
        filterCards(2);
    }

    public void filterType(View v) {
        for (Map.Entry<ToggleButton, String> entry : typeMap.entrySet()) {
            if (entry.getKey() != (ToggleButton)v) {
                entry.getKey().setChecked(false);
            }
        }
        filterCards(2);
    }

    public void filterKredits(View v) {
        for (Map.Entry<ToggleButton, String> entry : kreditMap.entrySet()) {
            if (entry.getKey() != (ToggleButton)v) {
                entry.getKey().setChecked(false);
            }
        }
        filterCards(2);
    }

    private ArrayList<String> selectionFinder() {
        //Returns an ArrayList of 3 strings reflecting the current collection_menu.
        ArrayList<String> selection = new ArrayList<>();

        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            if (entry.getKey().isChecked()) {
                selection.add(0, entry.getValue());
                break;
            }
            selection.add(0, "NO_COUNTRY");
        }

        for (Map.Entry<ToggleButton, String> entry : typeMap.entrySet()) {
            if (entry.getKey().isChecked()) {
                selection.add(1, entry.getValue());
                break;
            }
            selection.add(1, "NO_TYPE");
        }

        for (Map.Entry<ToggleButton, String> entry : kreditMap.entrySet()) {
            if (entry.getKey().isChecked()) {
                selection.add(2, entry.getValue());
                break;
            }
            selection.add(2, "NO_KREDIT");
        }
        return selection;
    }

    private void filterCards(int mode) {
        //Filter both collection and cards at the same time.

        ArrayList<Card> cardsFilteredCollection = new ArrayList<>(allCards.keySet());
        Card crd;

        Iterator<Card> i = cardsFilteredCollection.iterator();
        while (i.hasNext()) {
            crd = (Card) i.next();
            if (!(crd.getCountryName().equals(selectionFinder().get(0))
                    || (selectionFinder().get(0).equals("NO_COUNTRY")))) {
                i.remove();
            }
        }

        Iterator<Card> j = cardsFilteredCollection.iterator();
        while (j.hasNext()) {
            crd = (Card) j.next();
            if (!(crd.getTypeName().equals(selectionFinder().get(1))
                    || (selectionFinder().get(1).equals("NO_TYPE")))) {
                j.remove();
            }
        }

        Iterator<Card> k = cardsFilteredCollection.iterator();
        while (k.hasNext()) {
            crd = (Card) k.next();
            if (!(selectionFinder().get(2).equals("NO_KREDIT"))) {
                if (!((crd.getKreditsNumber() == (parseInt(selectionFinder().get(2)))))) {
                    if ((!((crd.getKreditsNumber() <= 1) && parseInt(selectionFinder().get(2)) == 1))) {
                        if ((!((crd.getKreditsNumber() >= 7) && parseInt(selectionFinder().get(2)) == 7))) {
                            k.remove();
                        }
                    }
                }
            }
        }

        ArrayList<Card> cardsFilteredInDeck = new ArrayList<>(deck.getCardsInDeck().keySet());
        Card crd2;

        Iterator<Card> l = cardsFilteredInDeck.iterator();
        while (l.hasNext()) {
            crd2 = (Card) l.next();
            if (!(crd2.getCountryName().equals(selectionFinder().get(0))
                    || (selectionFinder().get(0).equals("NO_COUNTRY")))) {
                l.remove();
            }
        }

        Iterator<Card> m = cardsFilteredInDeck.iterator();
        while (m.hasNext()) {
            crd2 = (Card) m.next();
            if (!(crd2.getTypeName().equals(selectionFinder().get(1))
                    || (selectionFinder().get(1).equals("NO_TYPE")))) {
                m.remove();
            }
        }

        Iterator<Card> n = cardsFilteredInDeck.iterator();
        while (n.hasNext()) {
            crd2 = (Card) n.next();
            if (!(selectionFinder().get(2).equals("NO_KREDIT"))) {
                if (!((crd2.getKreditsNumber() == (parseInt(selectionFinder().get(2)))))) {
                    if ((!((crd2.getKreditsNumber() <= 1) && parseInt(selectionFinder().get(2)) == 1))) {
                        if ((!((crd2.getKreditsNumber() >= 7) && parseInt(selectionFinder().get(2)) == 7))) {
                            n.remove();
                        }
                    }
                }
            }
        }

        HashMap<Card, Integer> collectionMap = new HashMap<>();
        for (int o = 0; o < cardsFilteredCollection.size(); o++) {
            collectionMap.put(cardsFilteredCollection.get(o), allCards.get(cardsFilteredCollection.get(o)));
        }

        HashMap<Card, Integer> cardsInDeckMap = new HashMap<>();
        for (int p = 0; p < cardsFilteredInDeck.size(); p++) {
            cardsInDeckMap.put(cardsFilteredInDeck.get(p), allCards.get(cardsFilteredInDeck.get(p)));
            Log.d(TAG, "filterCards: PUTTING: " + cardsFilteredInDeck.get(p) + " " + allCards.get(cardsFilteredInDeck.get(p)));
        }

        //TODO: This could be fucky. Careful.
        if (mode == 0) { //Update cards (from collection)
            cardsFragment.updateCardsFragment(cardsInDeckMap, collectionMap);
        } else if (mode == 1) { //Update collection (from cards)
            collectionFragment.updateCollectionFragment(collectionMap);
        } else if (mode == 2) { //Update all
            cardsFragment.updateCardsFragment(cardsInDeckMap, collectionMap);
            collectionFragment.updateCollectionFragment(collectionMap);
        }

    }

    public boolean addCardToDeck(String cardName) {
        Card c = new Card();
        boolean cardIsInDeck = false;
        
        //Check if card is in deck:
        for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
            if (entry.getKey().getName().equals(cardName)) {
                c = entry.getKey();
                cardIsInDeck = true;
                break;
            }
        }
        
        //If it isn't, get a generic key:
        if (!cardIsInDeck) {
            for (Map.Entry<Card, Integer> entry : allCards.entrySet()) {
                if (entry.getKey().getName().equals(cardName)) {
                    c = entry.getKey();
                    break;
                }
            }
        }
        
        if (cardIsInDeck && cardsInDeck.get(c) == c.getRarityLimit()) {
            Toast.makeText(this, R.string.card_limit_reached_2, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (c.getCountryName().equals(deck.getAlly()) && c.getRarity().equals("elite")) {
            Toast.makeText(this, R.string.ally_elite, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (cardIsInDeck && cardsInDeck.get(c) < c.getRarityLimit()) {
            deck.addCard(c);
            cardsInDeck = deck.getCardsInDeck();
            allCards.put(c, cardsInDeck.get(c));
            updateBottomBar();
            filterCards(0);
            deckChanged = true;
            return true;
        } else if (!cardIsInDeck) {
            deck.addCard(c);
            cardsInDeck = deck.getCardsInDeck();
            allCards.put(c, cardsInDeck.get(c));
            updateBottomBar();
            filterCards(0);
            deckChanged = true;
            return true;
        }
        Log.d(TAG, "addCardToDeck: Something went wrong.");
        return false;
    }

    public boolean removeCardFromDeck(String cardName) {
        Log.d(TAG, "removeCardFromDeck: Called.");
        cardsInDeck = deck.getCardsInDeck();
        Card c = new Card();
        for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
            if (entry.getKey().getName().equals(cardName)) {
                c = entry.getKey();
                Log.d(TAG, "removeCardFromDeck: Found key.");
                break;
            }
        }

        if (c != null) {
            Log.d(TAG, "removeCardFromDeck: cardsInDeck contains this key.");
            deck.removeCard(c);
            cardsInDeck = deck.getCardsInDeck();
            allCards.put(c, cardsInDeck.get(c));
            updateBottomBar();
            filterCards(1);
            deckChanged = true;
            return true;
        } else {
            return false;
        }
    }
}