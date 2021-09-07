package akkad.app.kards.deckmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DeckViewerActivity extends AppCompatActivity {

    private static final String TAG = "DeckViewer";
    private static final int EDIT_DECK_CODE = 1;
    private HashMap<Card, Integer> cards;
    private int cardCount, victories, defeats, mainCount, allyCount, mainCountryImage, allyCountryImage;
    private String layout, deckName;
    private Deck deck;
    private ViewerCardsFragment cardsFragment;
    private ViewerStatsFragment statsFragment;
    private AlertDialog deleteDialog, winrateDialog, resetDialog;
    private DeckUtils utils;
    Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_viewer);

        mainHandler = new Handler();

        initWindow();
        utils = new DeckUtils(this);
        getIncomingIntents();
        getDeckFromUtils();
    }

    private void initWindow() {
        Toolbar toolbar = findViewById(R.id.deckviewer2_toolbar);
        super.setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Loading...");

        Window window = this.getWindow();
        window.setStatusBarColor (Color.parseColor("#181818"));
    }

    private void getIncomingIntents() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("deckName")){
            Log.d(TAG, "getIncomingIntent: intent has extra deckName.");
            deckName = (String) getIntent().getStringExtra("deckName");
        } else {
            Log.d(TAG, "getIncomingIntents: ERROR. No deckName intent.");
        }
    }

    private void getDeckFromUtils() {
        Thread deckThread = new Thread(new Runnable() {
            @Override
            public void run() {
                deck = utils.getDeckByName(deckName);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initViewPager();
                        setUpDialogs();
                    }
                });
            }
        });
        deckThread.start();
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        //gets the deck title, card map and card count

        getSupportActionBar().setTitle(deckName);
        tabLayout.setupWithViewPager(viewPager);

        cardsFragment = ViewerCardsFragment.newInstance(deck.getCardsInDeck(), deck.getCardCount(), deck.getMainCount(), deck.getAllyCount(),
                deck.getMainImage(), deck.getAllyImage());
        statsFragment = ViewerStatsFragment.newInstance(deck.getCardsInDeck(), deck.getVictories(), deck.getDefeats());

        victories = deck.getVictories();
        defeats = deck.getDefeats();

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        vpAdapter.addFragment(cardsFragment, getString(R.string.tab_text_1));
        vpAdapter.addFragment(statsFragment, getString(R.string.tab_text_2));
        viewPager.setAdapter(vpAdapter);

        layout = "list";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_viewer_cards_menu, menu);
        if (layout.equals("list")) {
            menu.getItem(0).setIcon(R.drawable.headline_icon);
        } else {
            menu.getItem(0).setIcon(R.drawable.list_icon);
        }
        return true;
    }


    public void setLayout(String layout) {
        this.layout = layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();

        if (id == R.id.action_layout_deck) {
            if (layout.equals("list")) {
                item.setIcon(R.drawable.list_icon);
                layout = "headline";
                cardsFragment.changeLayout("headline");
            }
            else if (layout.equals("headline")) {
                item.setIcon(R.drawable.headline_icon);
                layout = "list";
                cardsFragment.changeLayout("list");
            }
            return true;
        }

        // Back button (action bar)
        else if (id == android.R.id.home) {
            Intent result = new Intent();
            setResult(RESULT_OK, result);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
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

    public void openCostDetails(View view) {
        ArrayList<Integer> imageList = new ArrayList<>(Arrays.asList(R.drawable.k1, R.drawable.k2, R.drawable.k3, R.drawable.k4, R.drawable.k5, R.drawable.k6, R.drawable.k7));
        Intent costChartIntent = new Intent(this.getApplicationContext(), ShowChart.class);
        costChartIntent.putExtra("graph_all", statsFragment.getCostGraph());
        costChartIntent.putExtra("graph_units", statsFragment.getUnitCostGraph());
        costChartIntent.putExtra("counts_all", statsFragment.getCostCounts());
        costChartIntent.putExtra("images", imageList);
        costChartIntent.putExtra("title", getString(R.string.cost));
        startActivity(costChartIntent);
    }

    public void openTypeDetails(View view) {
        ArrayList<Integer> imageList = new ArrayList<>(Arrays.asList(R.drawable.infantry_square, R.drawable.tank_square, R.drawable.artillery_square, R.drawable.fighter_square, R.drawable.bomber_square, R.drawable.order_square, R.drawable.cm_square));
        Intent typeChartIntent = new Intent(this.getApplicationContext(), ShowChart.class);
        typeChartIntent.putExtra("graph_all", statsFragment.getTypeGraph());
        typeChartIntent.putExtra("counts_all", statsFragment.getTypeCounts());
        typeChartIntent.putExtra("images", imageList);
        typeChartIntent.putExtra("title", getString(R.string.types));
        startActivity(typeChartIntent);
    }

    public void openAttackDetails(View view) {
        ArrayList<Integer> imageList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            imageList.add(R.drawable.attack_chart);
        }
        Intent attackChartIntent = new Intent(this.getApplicationContext(), ShowChart.class);
        attackChartIntent.putExtra("graph_all", statsFragment.getAttackGraph());
        attackChartIntent.putExtra("counts_all", statsFragment.getAttackCounts());
        attackChartIntent.putExtra("images", imageList);
        attackChartIntent.putExtra("title", getString(R.string.attack));
        startActivity(attackChartIntent);
    }

    public void openDefenseDetails(View view) {
        ArrayList<Integer> imageList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            imageList.add(R.drawable.defense_chart);
        }
        Intent defenseChartIntent = new Intent(this.getApplicationContext(), ShowChart.class);
        defenseChartIntent.putExtra("graph_all", statsFragment.getDefenseGraph());
        defenseChartIntent.putExtra("counts_all", statsFragment.getDefenseCounts());
        defenseChartIntent.putExtra("images", imageList);
        defenseChartIntent.putExtra("title", getString(R.string.defense));
        startActivity(defenseChartIntent);
    }
    
    public void editDeckHandler(View view) {
        FloatingActionMenu menu = findViewById(R.id.floating_action_menu);
        menu.close(true);
        Log.d(TAG, "editDeck: Clicked EDIT.");
        Intent editDeckIntent = new Intent(this.getApplicationContext(), DeckBuilderActivity.class);
        editDeckIntent.putExtra("deckName", deckName);
        startActivityForResult(editDeckIntent, EDIT_DECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == EDIT_DECK_CODE) {
            if (resultCode == RESULT_OK) {

                // Get Deck data from Intent
                Deck returnDeck = (Deck) data.getSerializableExtra("deck");
                String returnDeckName = data.getStringExtra("deckName");
                String oldName = data.getStringExtra("oldName");
                boolean deckChanged = data.getBooleanExtra("deckChanged", false);
                boolean nameChanged = data.getBooleanExtra("nameChanged", false);

                //Change cards file to match
                //Change deck in memory
                assert returnDeck != null;
                Log.d(TAG, "onActivityResult: returnDeck is " + returnDeck);
                Log.d(TAG, "onActivityResult: returnDeck vic/def are" + returnDeck.getVictories() + " " + returnDeck.getDefeats());
                deckName = returnDeck.getDeckName();

                deck = returnDeck;

                if (nameChanged) {
                    utils.changeDeckName(oldName, returnDeckName);
                }

                if (deckChanged) {
                    Log.d(TAG, "onActivityResult: deck changed.");
                    utils.deleteDeckByName(returnDeck.getDeckName());
                    assert returnDeck != null;
                    utils.newDeck(returnDeck.getDeckName(), returnDeck.getCountry(), returnDeck.getAlly(), returnDeck.getCardsInDeck());
                    deck = utils.getDeckByName(returnDeckName);
                    Toast.makeText(this, getString(R.string.cards_in_deck_changed), Toast.LENGTH_SHORT).show();
                }

                // Set title to deck name
                Objects.requireNonNull(getSupportActionBar()).setTitle(deckName);

                cardsFragment.updateCards();
                cardsFragment.updateBottomBar(deck);
                statsFragment.updateStatsFragment();
            }
        }
    }
    
    public void updateWinRateHandler(View view) {
        victories = deck.getVictories();
        defeats = deck.getDefeats();
        Log.d(TAG, "updateWinRatioHandler: Victories and defeats are " + victories + " " + defeats);

        FloatingActionMenu menu = findViewById(R.id.floating_action_menu);
        menu.close(true);
        //Winrate updater dialog
        final AlertDialog.Builder w = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_winrate_picker, null);
        w.setTitle(getString(R.string.win_rate_smol));
        w.setMessage(getString(R.string.update_winrate_message));
        w.setView(dialogView);
        w.setIcon(R.drawable.star_icon);

        final NumberPicker victoryNumberPicker = (NumberPicker) dialogView.findViewById(R.id.np1);
        victoryNumberPicker.setMaxValue(99);
        victoryNumberPicker.setMinValue(0);
        victoryNumberPicker.setWrapSelectorWheel(false);
        victoryNumberPicker.setValue(victories);
        victoryNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> { });

        final NumberPicker defeatNumberPicker = (NumberPicker) dialogView.findViewById(R.id.np2);
        defeatNumberPicker.setMaxValue(99);
        defeatNumberPicker.setMinValue(0);
        defeatNumberPicker.setWrapSelectorWheel(false);
        defeatNumberPicker.setValue(defeats);
        defeatNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> { });

        w.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                victories = victoryNumberPicker.getValue();
                defeats = defeatNumberPicker.getValue();
                utils.updateWinRate(deck.getDeckName(), victories, defeats);
                deck.setVictories(victories);
                deck.setDefeats(defeats);
                statsFragment.updateStatsFragment();
                Toast.makeText(DeckViewerActivity.this, getString(R.string.win_rate_set), Toast.LENGTH_SHORT).show();
            }
        });

        w.setNeutralButton(getString(R.string.reset), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetDialog.show();
            }
        });
        Button addVictoryButton = dialogView.findViewById(R.id.button_add_victory);
        Button addDefeatButton = dialogView.findViewById(R.id.button_add_defeat);

        addVictoryButton.setOnClickListener(v -> {
            victories = victories + 1;
            victoryNumberPicker.setValue(victories);
            utils.updateWinRate(deck.getDeckName(), victories, defeats);
            deck.setVictories(victories);
            deck.setDefeats(defeats);
            statsFragment.updateStatsFragment();
            Toast.makeText(DeckViewerActivity.this, getString(R.string.added_victory), Toast.LENGTH_SHORT).show();
            winrateDialog.hide();
        });

        addDefeatButton.setOnClickListener(v -> {
            defeats = defeats + 1;
            defeatNumberPicker.setValue(defeats);
            utils.updateWinRate(deck.getDeckName(), victories, defeats);
            deck.setVictories(victories);
            deck.setDefeats(defeats);
            statsFragment.updateStatsFragment();
            Toast.makeText(DeckViewerActivity.this, R.string.added_defeat, Toast.LENGTH_SHORT).show();
            winrateDialog.hide();
        });

        // Reset winrate confirmation dialog
        final AlertDialog.Builder r = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.reset_title))
                .setMessage(getString(R.string.reset_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.reset), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        victoryNumberPicker.setValue(0);
                        defeatNumberPicker.setValue(0);
                        victories = 0;
                        defeats = 0;
                        utils.updateWinRate(deckName, victories, defeats);
                        deck.setVictories(0);
                        deck.setDefeats(0);
                        statsFragment.updateStatsFragment();
                        Toast.makeText(DeckViewerActivity.this, getString(R.string.win_rate_reset), Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(getString(R.string.cancel), null);
        winrateDialog = w.create();
        resetDialog = r.create();
        winrateDialog.show();
    }

    public void simulateDrawHandler(View view) {
        FloatingActionMenu menu = findViewById(R.id.floating_action_menu);
        menu.close(true);
        Log.d(TAG, "simulateDraw: Clicked SIMULATE DRAW.");
        if (deck.getCardCount() != 39) {
            Toast.makeText(this, "You cannot draw from an invalid deck.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent draw_intent = new Intent(this.getApplicationContext(), DrawActivity.class);
        draw_intent.putExtra("cards", deck.getCardsInDeck());
        draw_intent.putExtra("country", deck.getCountry());
        startActivity(draw_intent);
    }
    
    public void deleteDeckHandler(View view) {
        FloatingActionMenu menu = findViewById(R.id.floating_action_menu);
        menu.close(true);
        Log.d(TAG, "deleteDeck: Clicked DELETE DECK.");
        deleteDialog.show();
    }

    private void setUpDialogs() {
        // Delete deck confirmation dialog
        final AlertDialog.Builder d = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.delete_title))
                .setMessage(getString(R.string.delete_confirmation) + " " + deckName + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        utils.deleteDeckByName(deckName);
                        Intent result = new Intent();
                        setResult(RESULT_OK, result);
                        finish();
                    }})
                .setNegativeButton(getString(R.string.cancel), null);

        deleteDialog = d.create();
    }

    @Override
    protected void onDestroy() {
        try {
            deleteDialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } try {
            winrateDialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } try {
            resetDialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public Deck getDeck() {
        return deck;
    }

    public String getLayout() {
        return layout;
    }
}