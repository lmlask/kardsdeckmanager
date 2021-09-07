package akkad.app.kards.deckmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mopub.mobileads.MoPubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


import static java.lang.Integer.parseInt;


public class CollectionActivity extends AppCompatActivity {


    private static final String TAG = "Tag";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COLUMNS_NUMBER = "columnsNumber";
    private DrawerLayout dl;

    //Filter button maps
    HashMap<ToggleButton, String> countryMap;
    HashMap<ToggleButton, String> typeMap;
    HashMap<ToggleButton, String> kreditMap;

    //Filter buttons
    ToggleButton usaFilter, britainFilter, sovietFilter, franceFilter, germanyFilter, japanFilter, italyFilter;
    ToggleButton infantryFilter, tankFilter, artilleryFilter, fighterFilter, bomberFilter, orderFilter, cmFilter;
    ToggleButton k1Filter, k2Filter, k3Filter, k4Filter, k5Filter, k6Filter, k7Filter;

    //RecyclerView adapter
    RecyclerView recyclerView;
    String layout;

    LinearLayoutManager linear_manager;
    StaggeredGridLayoutManager grid_manager;

    GridCollectionAdapter grid_adapter;
    ListCollectionAdapter list_adapter;
    HeadlineCollectionAdapter headline_adapter;

    ArrayList<Card> cardsUnfiltered;

    Thread cardLoadingThread;
    Handler mainUIHandler;
    ProgressBar progBar;

    Animation slideIn;
    Animation slideOut;
    Animation fadeIn;
    Animation fadeOut;
    DeckUtils utils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        countryMap = new HashMap<>();
        typeMap = new HashMap<>();
        kreditMap = new HashMap<>();

        Toolbar collectionToolbar = findViewById(R.id.toolbar_collection);
        super.setSupportActionBar(collectionToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.card_collection_title);

        Window window = this.getWindow();
        window.setStatusBarColor (Color.parseColor("#181818"));

        dl = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
        dl.setScrimColor(Color.parseColor("#80000000"));

        utils = new DeckUtils(this);



        mainUIHandler = new Handler();
        progBar = findViewById(R.id.progress_bar_collection);

        MoPubView moPubView = (MoPubView) findViewById(R.id.adview);

        moPubView.setAdUnitId("f0666ab1f43d43bbae6a6c3471cb62e1");
        moPubView.loadAd();

        backgroundThread();
    }

    private void initRV() {
        //Initialize RecyclerView
        layout = "list";
        recyclerView = findViewById(R.id.collection_recyclerview);
        list_adapter = new ListCollectionAdapter(cardsUnfiltered, this);
        headline_adapter = new HeadlineCollectionAdapter(cardsUnfiltered, this);
        grid_adapter = new GridCollectionAdapter(cardsUnfiltered, this);

        //Load sharedprefs
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int spanCountInt = sharedPreferences.getInt(COLUMNS_NUMBER, 2);

        linear_manager = new LinearLayoutManager(this);
        grid_manager = new StaggeredGridLayoutManager(spanCountInt, LinearLayoutManager.VERTICAL);
        initRecyclerViewList();
    }







    private void backgroundThread() {

        cardLoadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Init buttons
                usaFilter = findViewById(R.id.filter_usa); britainFilter = findViewById(R.id.filter_britain); sovietFilter = findViewById(R.id.filter_soviet);
                franceFilter = findViewById(R.id.filter_france); germanyFilter = findViewById(R.id.filter_germany); japanFilter = findViewById(R.id.filter_japan);
                italyFilter = findViewById(R.id.filter_italy);

                infantryFilter = findViewById(R.id.filter_infantry); tankFilter = findViewById(R.id.filter_tank); artilleryFilter = findViewById(R.id.filter_artillery);
                fighterFilter = findViewById(R.id.filter_fighter); bomberFilter = findViewById(R.id.filter_bomber); orderFilter = findViewById(R.id.filter_order);
                cmFilter = findViewById(R.id.filter_cm);

                k1Filter = findViewById(R.id.filter_k1); k2Filter = findViewById(R.id.filter_k2); k3Filter = findViewById(R.id.filter_k3);
                k4Filter = findViewById(R.id.filter_k4); k5Filter = findViewById(R.id.filter_k5); k6Filter = findViewById(R.id.filter_k6);
                k7Filter = findViewById(R.id.filter_k7);

                //Add buttons to their HashMaps:
                countryMap.put(usaFilter, "usa"); countryMap.put(britainFilter, "britain"); countryMap.put(sovietFilter, "soviet");
                countryMap.put(franceFilter, "france"); countryMap.put(germanyFilter, "germany"); countryMap.put(japanFilter, "japan");
                countryMap.put(italyFilter, "italy");

                typeMap.put(infantryFilter, "infantry"); typeMap.put(tankFilter, "tank"); typeMap.put(artilleryFilter, "artillery");
                typeMap.put(fighterFilter, "fighter"); typeMap.put(bomberFilter, "bomber"); typeMap.put(orderFilter, "order");
                typeMap.put(cmFilter, "countermeasure");

                kreditMap.put(k1Filter, "1"); kreditMap.put(k2Filter, "2"); kreditMap.put(k3Filter, "3");
                kreditMap.put(k4Filter, "4"); kreditMap.put(k5Filter, "5"); kreditMap.put(k6Filter, "6");
                kreditMap.put(k7Filter, "7");

                //Init ads
                //TODO


                //Get cards
                HashMap<String, Card> cards = utils.getCardMap();
                cardsUnfiltered = new ArrayList<>();
                ArrayList<Card> tempList = new ArrayList<>(cards.values());
                for (int i = 0; i < tempList.size(); i++) {
                    if (!tempList.get(i).getTypeName().equals("hq")) {
                        cardsUnfiltered.add(tempList.get(i));
                    }
                }
                Collections.sort(cardsUnfiltered);

                mainUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progBar.setVisibility(View.INVISIBLE);
                        initRV();
                    }
                });
            }
        });

        cardLoadingThread.start();
    }

    //Filter icon in the top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.collection_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (layout) {
                    case "grid":
                        grid_adapter.getFilter().filter(newText);
                        break;
                    case "list":
                        list_adapter.getFilter().filter(newText);
                        break;
                    case "headline":
                        headline_adapter.getFilter().filter(newText);
                        break;
                }
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();

        // Filter button
        if (id == R.id.action_collection_filter) {
            if(!dl.isDrawerOpen(GravityCompat.END)) dl.openDrawer(GravityCompat.END);
            else dl.closeDrawer(GravityCompat.END);
            return true;
        }

        if (id == R.id.action_change_layout) {
            switch (layout) {
                case "grid":
                    item.setIcon(R.drawable.headline_icon);
                    layout = "list";
                    initRecyclerViewList();
                    cardFilter(cardsUnfiltered);
                    return true;
                case "list":
                    item.setIcon(R.drawable.grid_icon);
                    layout = "headline";
                    initRecyclerViewHeadline();
                    cardFilter(cardsUnfiltered);
                    return true;
                case "headline":
                    item.setIcon(R.drawable.list_icon);
                    layout = "grid";
                    initRecyclerViewGrid();
                    cardFilter(cardsUnfiltered);
                    return true;
            }
        }

        // Back button (action bar)
        else if (id == android.R.id.home) {
            if (dl.isDrawerOpen(GravityCompat.END)) {
                dl.closeDrawer(GravityCompat.END);
            }
            else if ((!selectionFinder().get(0).equals("NO_COUNTRY")) || (!selectionFinder().get(1).equals("NO_TYPE")) || (!selectionFinder().get(2).equals("NO_KREDIT"))) {
                resetFilters();
                swapData(cardsUnfiltered);
            }
            else {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //Open and close drawer
    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.END)) {
            dl.closeDrawer(GravityCompat.END);
        }
        else if ((!selectionFinder().get(0).equals("NO_COUNTRY")) || (!selectionFinder().get(1).equals("NO_TYPE")) || (!selectionFinder().get(2).equals("NO_KREDIT"))) {
            resetFilters();
            swapData(cardsUnfiltered);
        }
        else {
            super.onBackPressed();
        }
    }


    public void resetFilters() {
        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            entry.getKey().setChecked(false);
        }
        for (Map.Entry<ToggleButton, String> entry : typeMap.entrySet()) {
            entry.getKey().setChecked(false);
        }
        for (Map.Entry<ToggleButton, String> entry : kreditMap.entrySet()) {
            entry.getKey().setChecked(false);
        }
        cardFilter(cardsUnfiltered);
    }

    public void resetFilterButtons() {
        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            entry.getKey().setChecked(false);
        }
        for (Map.Entry<ToggleButton, String> entry : typeMap.entrySet()) {
            entry.getKey().setChecked(false);
        }
        for (Map.Entry<ToggleButton, String> entry : kreditMap.entrySet()) {
            entry.getKey().setChecked(false);
        }
    }


    public void filterCountry(View v) {
        for (Map.Entry<ToggleButton, String> entry : countryMap.entrySet()) {
            if (entry.getKey() != (ToggleButton)v) {
                entry.getKey().setChecked(false);
            }
        }
        cardFilter(cardsUnfiltered);
    }


    public void filterType(View v) {
        for (Map.Entry<ToggleButton, String> entry : typeMap.entrySet()) {
            if (entry.getKey() != (ToggleButton)v) {
                entry.getKey().setChecked(false);
            }
        }
        cardFilter(cardsUnfiltered);
    }


    public void filterKredits(View v) {
        for (Map.Entry<ToggleButton, String> entry : kreditMap.entrySet()) {
            if (entry.getKey() != (ToggleButton)v) {
                entry.getKey().setChecked(false);
            }
        }
        cardFilter(cardsUnfiltered);
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

    private void cardFilter(ArrayList<Card> mCards) {
        ArrayList<Card> cardsFiltered = new ArrayList<>(mCards);
        Card crd;
        Iterator<Card> i = cardsFiltered.iterator();
        while (i.hasNext()) {
            crd = (Card) i.next();
            if (!(crd.getCountryName().equals(selectionFinder().get(0))
                    || (selectionFinder().get(0).equals("NO_COUNTRY")))) {
                i.remove();
            }
        }

        Iterator<Card> j = cardsFiltered.iterator();
        while (j.hasNext()) {
            crd = (Card) j.next();
            if (!(crd.getTypeName().equals(selectionFinder().get(1))
                    || (selectionFinder().get(1).equals("NO_TYPE")))) {
                j.remove();
            }
        }

        //If you for some reason are seeing this, I am so sorry for the ugly.
        Iterator<Card> k = cardsFiltered.iterator();
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
        swapData(cardsFiltered);
    }


    private void initRecyclerViewList(){
        recyclerView.setLayoutManager(linear_manager);
        recyclerView.setAdapter(list_adapter);
    }

    private void initRecyclerViewGrid(){
        recyclerView.setLayoutManager(grid_manager);
        recyclerView.setAdapter(grid_adapter);
    }

    private void initRecyclerViewHeadline(){
        recyclerView.setLayoutManager(linear_manager);
        recyclerView.setAdapter(headline_adapter);
    }

    private void swapData(ArrayList<Card> newList) {
        switch (layout) {
            case "grid":
                grid_adapter.swapDataSet(newList);
                break;
            case "list":
                list_adapter.swapDataSet(newList);
                break;
            case "headline":
                headline_adapter.swapDataSet(newList);
                break;
        }
    }

}