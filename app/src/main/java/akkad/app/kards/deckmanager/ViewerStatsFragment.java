package akkad.app.kards.deckmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewerStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewerStatsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "cards";
    private static final String ARG_PARAM2 = "victories";
    private static final String ARG_PARAM3 = "defeats";
    private static final String TAG = "ViewerStatsFragment";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BLITZ_COST = "blitzCost";

    private HashMap<Card, Integer> cards;

    private int victories;
    private int defeats;

    public ViewerStatsFragment() {
        // Required empty public constructor
    }

    //A whole lotta math on this one.

    private ArrayList<Float> costGraph;
    private ArrayList<Float> unitCostGraph;

    private ArrayList<Integer> costCounts;

    private ArrayList<Float> typeGraph;
    private ArrayList<Integer> typeCounts;

    private ArrayList<Float> attackGraph;
    private ArrayList<Integer> attackCounts;

    private ArrayList<Float> defenseGraph;
    private ArrayList<Integer> defenseCounts;

    private ArrayList<Float> unitRatioGraph;
    private ArrayList<Integer> unitRatioCounts;

    private ArrayList<Float> winRatioGraph;
    private ArrayList<Integer> winRatioCounts;

    private View rootView;

    //Getters
    public ArrayList<Float> getCostGraph() { return costGraph; }
    public ArrayList<Float> getUnitCostGraph() { return unitCostGraph; }
    public ArrayList<Integer> getCostCounts() { return costCounts; }

    public ArrayList<Float> getTypeGraph() { return typeGraph; }
    public ArrayList<Integer> getTypeCounts() { return typeCounts; }

    public ArrayList<Float> getAttackGraph() { return attackGraph; }
    public ArrayList<Integer> getAttackCounts() { return attackCounts; }

    public ArrayList<Float> getDefenseGraph() { return defenseGraph; }
    public ArrayList<Integer> getDefenseCounts() { return defenseCounts; }

    public ArrayList<Float> getUnitRatioGraph() { return unitRatioGraph; }
    public ArrayList<Integer> getUnitRatioCounts() { return unitRatioCounts; }

    public ArrayList<Float> getWinRatioGraph() { return winRatioGraph; }
    public ArrayList<Integer> getWinRatioCounts() { return winRatioCounts; }

    public static ViewerStatsFragment newInstance(HashMap<Card, Integer> cards, int victories, int defeats) {
        ViewerStatsFragment fragment = new ViewerStatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, cards);
        args.putInt(ARG_PARAM2, victories);
        args.putInt(ARG_PARAM3, defeats);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cards = (HashMap) getArguments().getSerializable(ARG_PARAM1);
            victories = getArguments().getInt(ARG_PARAM2);
            defeats = getArguments().getInt(ARG_PARAM3);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.action_layout_deck);
        if (item!=null){
            item.setVisible(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_viewer_stats, container, false);
        rootView = mView;
        updateStatsFragment();
        return mView;
    }

    private void calculateCostStats(HashMap<Card, Integer> cards) {
        // This map maps cost values to the number of cards with that value.
        HashMap<Integer, Integer> costMap = new HashMap<>();
        costMap.put(1, 0); costMap.put(2, 0); costMap.put(3, 0); costMap.put(4, 0);
        costMap.put(5, 0); costMap.put(6, 0); costMap.put(7, 0);

        HashMap<Integer, Integer> orderCostMap = new HashMap<>();
        orderCostMap.put(1, 0); orderCostMap.put(2, 0); orderCostMap.put(3, 0); orderCostMap.put(4, 0);
        orderCostMap.put(5, 0); orderCostMap.put(6, 0); orderCostMap.put(7, 0);

        costCounts = new ArrayList<>();

        int cost_bucket;
        int old_value;
        int new_value;
        SharedPreferences sharedPreferences = ((DeckViewerActivity) Objects.requireNonNull(getActivity())).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean blitz;
        blitz = sharedPreferences.getBoolean(BLITZ_COST, false);

        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            int cost;

            if ((blitz) && (entry.getKey().getSpecial().contains("Blitz"))) {
                Log.d(TAG, "calculateCostStats: Blitz ACTIVATED");
                cost = entry.getKey().getKreditsNumber() + entry.getKey().getOpCost();
                Log.d(TAG, "calculateCostStats: Card is " + entry.getKey().getName() + ", cost is " + cost);
            } else {
                cost = entry.getKey().getKreditsNumber();
            }

            if (cost < 1) {
                cost_bucket = 1;
            }
            else cost_bucket = Math.min(cost, 7);
            old_value = costMap.get(cost_bucket);
            new_value = old_value + entry.getValue();
            costMap.put(cost_bucket, new_value);

            if ((entry.getKey().getTypeName().equals("order")) || (entry.getKey().getTypeName().equals("countermeasure"))) {
                old_value = orderCostMap.get(cost_bucket);
                new_value = old_value + entry.getValue();
                orderCostMap.put(cost_bucket, new_value);
            }
        }


        //Find maximum value and create costCounts
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : costMap.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
            costCounts.add(entry.getValue());
        }

        assert maxEntry != null;
        int max = maxEntry.getValue();
        // Initialize list of float values (1 is 100%, refers to the graph bar size)
        costGraph = new ArrayList<>(Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f));
        // Set the highest value as a 100% sized bar, for some reason 0.99f works but 1f bugs
        costGraph.add(maxEntry.getKey()-1, 0.99f);
        // Add the rest of the values, calculating their size relative to the maximum value
        for (int i = 0; i<7; i++) {
            float barSize = (float) costMap.get(i+1)/max;
            if (barSize == 1f) {
                costGraph.add(i, 0.99f);
            }
            else {
                costGraph.add(i, barSize);
            }
        }

        unitCostGraph = new ArrayList<>();
        //Time to create unitCostGraph
        for (int i = 0; i<7; i++) {
            float barSizeOrder = (float) orderCostMap.get(i+1)/max;
            float barSize = costGraph.get(i) - barSizeOrder;
            if (barSize == 1f) {
                unitCostGraph.add(i, 0.99f);
            }
            else {
                unitCostGraph.add(i, barSize);
            }
        }
    }

    private void calculateTypeStats(HashMap<Card, Integer> cards) {
        // This is messy as fuck.

        // This map maps card types to the number of cards with that value.
        HashMap<String, Integer> typeMap = new HashMap<>();
        typeMap.put("infantry", 0); typeMap.put("tank", 0); typeMap.put("artillery", 0); typeMap.put("fighter", 0);
        typeMap.put("bomber", 0); typeMap.put("order", 0); typeMap.put("countermeasure", 0);

        //This works fine, but the order is fucked up, hence it is inflating it wrong.
        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            String type_bucket = entry.getKey().getTypeName();
            int old_value = typeMap.get(type_bucket);
            int new_value = old_value + entry.getValue();
            typeMap.put(type_bucket, new_value);
        }

        //Find maximum value
        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : typeMap.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        int max = maxEntry.getValue();
        // Initialize list of float values (1 is 100%, refers to the graph bar size)
        typeGraph = new ArrayList<>(Arrays.asList(0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f));
        typeCounts = new ArrayList<>();

        // Set the highest value as a 100% sized bar, for some reason 0.99f works but 1.0f bugs
        ArrayList<String> types = new ArrayList<>(Arrays.asList("infantry", "tank", "artillery", "fighter", "bomber", "order", "countermeasure"));

        typeGraph.add(types.indexOf(maxEntry.getKey()), 0.99f);

        for (int i = 0; i < 7; i++) {
            float barSize = (float) typeMap.get(types.get(i))/max;
            if (barSize == 1f) {
                typeGraph.add(i, 0.99f);
            }
            else {
                typeGraph.add(i, barSize);
            }
            typeCounts.add(typeMap.get(types.get(i)));
        }
    }

    private void calculateAttackStats(HashMap<Card, Integer> cards) {

        // This map maps cost values to the number of cards with that value.
        HashMap<Integer, Integer> attackMap = new HashMap<>();
        attackMap.put(1, 0); attackMap.put(2, 0); attackMap.put(3, 0); attackMap.put(4, 0);
        attackMap.put(5, 0); attackMap.put(6, 0); attackMap.put(7, 0);

        int attack_bucket;
        int old_value;
        int new_value;
        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            int attack = entry.getKey().getAttack();
            if (attack < 1) {
                continue;
            }
            else attack_bucket = Math.min(attack, 7);
            old_value = attackMap.get(attack_bucket);
            new_value = old_value + entry.getValue();
            attackMap.put(attack_bucket, new_value);
        }
        //Find maximum value
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : attackMap.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        int max = maxEntry.getValue();
        // Initialize list of float values (1 is 100%, refers to the graph bar size)
        attackGraph = new ArrayList<>(Arrays.asList(0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f));
        attackCounts = new ArrayList<>();
        // Set the highest value as a 100% sized bar, for some reason 0.99f works but 1f bugs
        attackGraph.add(maxEntry.getKey()-1, 0.99f);
        // Add the rest of the values, calculating their size relative to the maximum value
        for (int i = 0; i<7; i++) {
            float barSize = (float) attackMap.get(i+1)/max;
            if (barSize == 1f) {
                attackGraph.add(i, 0.99f);
            }
            else {
                attackGraph.add(i, barSize);
            }
            attackCounts.add(attackMap.get(i+1));
        }
    }

    private void calculateDefenseStats(HashMap<Card, Integer> cards) {
        // I'm quite proud of this method if I do say so myself

        // This map maps cost values to the number of cards with that value.
        HashMap<Integer, Integer> defenseMap = new HashMap<>();
        defenseMap.put(1, 0); defenseMap.put(2, 0); defenseMap.put(3, 0); defenseMap.put(4, 0);
        defenseMap.put(5, 0); defenseMap.put(6, 0); defenseMap.put(7, 0);

        int defense_bucket;
        int old_value;
        int new_value;
        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            int defense = entry.getKey().getDefense();
            if (defense < 1) {
                continue;
            }
            else defense_bucket = Math.min(defense, 7);
            old_value = defenseMap.get(defense_bucket);
            new_value = old_value + entry.getValue();
            defenseMap.put(defense_bucket, new_value);
        }
        //Find maximum value
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : defenseMap.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        int max = maxEntry.getValue();
        // Initialize list of float values (1 is 100%, refers to the graph bar size)
        defenseGraph = new ArrayList<>(Arrays.asList(0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f));
        defenseCounts = new ArrayList<>();
        // Set the highest value as a 100% sized bar, for some reason 0.99f works but 1f bugs
        defenseGraph.add(maxEntry.getKey()-1, 0.99f);
        // Add the rest of the values, calculating their size relative to the maximum value
        for (int i = 0; i<7; i++) {
            float barSize = (float) defenseMap.get(i+1)/max;
            if (barSize == 1f) {
                defenseGraph.add(i, 0.99f);
            }
            else {
                defenseGraph.add(i, barSize);
            }
            defenseCounts.add(defenseMap.get(i+1));
        }
    }

    private void calculateUnitRatio(HashMap<Card, Integer> cards) {

        unitRatioGraph = new ArrayList<>();
        unitRatioCounts = new ArrayList<>();

        int unitCount = 0;
        int orderCount = 0;
        int cardCount;

        //Counts units and orders/Countermeasures
        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            if ((entry.getKey().getTypeName().equals("order")) || (entry.getKey().getTypeName().equals("countermeasure"))) {
                orderCount = orderCount + entry.getValue();
            }
            else {
                unitCount = unitCount + entry.getValue();
            }
        }

        cardCount = unitCount + orderCount;

        float unitBar = (float) unitCount /cardCount;
        float orderBar = (float) orderCount /cardCount;

        unitRatioGraph.add(unitBar);
        unitRatioGraph.add(orderBar);

        unitRatioCounts.add(unitCount);
        unitRatioCounts.add(orderCount);


    }

    private void calculateWinRatio(int victories, int defeats) {
        winRatioGraph = new ArrayList<>();
        winRatioCounts = new ArrayList<>();

        int games = victories + defeats;
        float victoryBar = (float) victories/games;
        float defeatBar = (float) defeats/games;

        //Handle I N F I N I T E B A R S
        if (victories == games) {
            victoryBar = 0.99f;
            defeatBar = 0f;
        }
        if (defeats == games) {
            defeatBar = 0.99f;
            victoryBar = 0f;
        }

        winRatioGraph.add(victoryBar);
        winRatioGraph.add(defeatBar);

        winRatioCounts.add(victories);
        winRatioCounts.add(defeats);
    }

    public void updateStatsFragment() {
        Deck deck = ((DeckViewerActivity)getActivity()).getDeck();

        cards = deck.getCardsInDeck();
        victories = deck.getVictories();
        defeats = deck.getDefeats();

        calculateCostStats(cards);
        calculateTypeStats(cards);
        calculateAttackStats(cards);
        calculateDefenseStats(cards);
        calculateUnitRatio(cards);
        calculateWinRatio(victories, defeats);
        // Set size of Cost graph bars according to the already calculated Stats
        for (int i = 0; i<7; i++) {
            ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.k1_bar, R.id.k2_bar, R.id.k3_bar, R.id.k4_bar, R.id.k5_bar, R.id.k6_bar, R.id.k7_bar));
            ImageView bar = (ImageView) rootView.findViewById(bar_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            params.matchConstraintPercentWidth = costGraph.get(i);
            bar.setLayoutParams(params);
        }
        // Set size of Type graph bars according to the already calculated Stats
        for (int i = 0; i<7; i++) {
            ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.inf_bar, R.id.tank_bar, R.id.art_bar, R.id.fighter_bar, R.id.bomber_bar, R.id.order_bar, R.id.cm_bar));
            ImageView bar = (ImageView) rootView.findViewById(bar_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            params.matchConstraintPercentWidth = typeGraph.get(i);
            bar.setLayoutParams(params);
        }
        // Set size of Attack graph bars according to the already calculated Stats
        for (int i = 0; i<7; i++) {
            ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.a1_bar, R.id.a2_bar, R.id.a3_bar, R.id.a4_bar, R.id.a5_bar, R.id.a6_bar, R.id.a7_bar));
            ArrayList<Integer> image_views = new ArrayList<>(Arrays.asList(R.id.a1_image, R.id.a2_image, R.id.a3_image, R.id.a4_image, R.id.a5_image, R.id.a6_image, R.id.a7_image));
            ImageView bar = (ImageView) rootView.findViewById(bar_views.get(i));
            ImageView image = (ImageView) rootView.findViewById(image_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            params.matchConstraintPercentWidth = attackGraph.get(i);
            bar.setLayoutParams(params);
            if (attackGraph.get(i) == 0f) {
                image.setImageResource(R.drawable.attack);
            }
            else {
                image.setImageResource(R.drawable.attack_chart);
            }
        }
        // Set size of Defense bars according to the already calculated Stats
        for (int i = 0; i<7; i++) {
            ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.d1_bar, R.id.d2_bar, R.id.d3_bar, R.id.d4_bar, R.id.d5_bar, R.id.d6_bar, R.id.d7_bar));
            ArrayList<Integer> image_views = new ArrayList<>(Arrays.asList(R.id.d1_image, R.id.d2_image, R.id.d3_image, R.id.d4_image, R.id.d5_image, R.id.d6_image, R.id.d7_image));
            ImageView bar = (ImageView) rootView.findViewById(bar_views.get(i));
            ImageView image = (ImageView) rootView.findViewById(image_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            params.matchConstraintPercentWidth = defenseGraph.get(i);
            bar.setLayoutParams(params);
            if (defenseGraph.get(i) == 0f) {
                image.setImageResource(R.drawable.defense);
            }
            else {
                image.setImageResource(R.drawable.defense_chart);
            }
        }
        // Set size of Unit Rate graph bars according to the already calculated Stats
        for (int i = 0; i<2; i++) {
            ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.units_bar, R.id.order_bar_2));
            ImageView bar = (ImageView) rootView.findViewById(bar_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            Log.d(TAG, "onCreateView: unitRatioGraph[0]: " + unitRatioGraph.get(0));
            if (unitRatioGraph.get(i) == 0.99f) {
                params.width = MATCH_PARENT;
            }
            else {
                params.width = 0;
                params.matchConstraintPercentWidth = unitRatioGraph.get(i);
            }
            bar.setLayoutParams(params);
        }
        TextView unitCount = rootView.findViewById(R.id.units_count_text);
        unitCount.setText(String.format("%02d", unitRatioCounts.get(0)));
        TextView orderCount = rootView.findViewById(R.id.orders_count_text);
        orderCount.setText(String.format("%02d", unitRatioCounts.get(1)));

        // Set size of win rate bars
        Log.d(TAG, "updateStatsFragment: WinRate graphs are " + winRatioGraph);
        Log.d(TAG, "updateStatsFragment: WinRate counts are " + winRatioCounts);
        for (int i = 0; i<2; i++) {
            ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.win_bar, R.id.lose_bar));
            ImageView bar = (ImageView) rootView.findViewById(bar_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            if (winRatioGraph.get(i) == 0.99f) {
                Log.d(TAG, "updateStatsFragment: 0.99f called!");
                params.width = MATCH_PARENT;
            }
            else {
                params.width = 0;
                params.matchConstraintPercentWidth = winRatioGraph.get(i);
            }

            if (winRatioCounts.get(i) == 0) {
                bar.setVisibility(View.INVISIBLE);
            } else {
                bar.setVisibility(View.VISIBLE);
            }

            bar.setLayoutParams(params);
        }
        TextView winCount = rootView.findViewById(R.id.win_count_text);
        winCount.setText(String.format("%02d", winRatioCounts.get(0)));
        TextView loseCount = rootView.findViewById(R.id.lose_count_text);
        loseCount.setText(String.format("%02d", winRatioCounts.get(1)));
    }
}