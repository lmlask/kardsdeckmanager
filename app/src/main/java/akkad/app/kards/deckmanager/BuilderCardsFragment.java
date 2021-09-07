package akkad.app.kards.deckmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class BuilderCardsFragment extends Fragment {

    private static final String TAG = "BuilderCardsFrag";
    //RV adapter
    RecyclerView recyclerView;
    LinearLayoutManager linear_manager;
    BuilderListCardsAdapter list_adapter;
    BuilderHeadlineCardsAdapter headline_adapter;
    HashMap<Card, Integer> cardsInDeck;
    LinkedHashMap<Card, Integer> allCardsMap, cardsInDeckMap;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public BuilderCardsFragment() {
        // Required empty public constructor
    }

    public static BuilderCardsFragment newInstance() {
        return new BuilderCardsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardsInDeck = new HashMap<>();
        allCardsMap = new LinkedHashMap<>();
        cardsInDeckMap = new LinkedHashMap<>();

        cardsInDeck = ((DeckBuilderActivity) Objects.requireNonNull(getActivity())).getDeck().getCardsInDeck();

        //Sort:
        ArrayList<Card> x;
        x = new ArrayList<>(cardsInDeck.keySet());
        Collections.sort(x);

        LinkedHashMap<Card, Integer> sortedCards = new LinkedHashMap<>();
        for (int i = 0; i < x.size(); i++) {
            sortedCards.put(x.get(i), cardsInDeck.get(x.get(i)));
        }

        list_adapter = new BuilderListCardsAdapter(sortedCards, getActivity());
        headline_adapter = new BuilderHeadlineCardsAdapter(sortedCards, getActivity());
        linear_manager = new LinearLayoutManager(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate RV
        View mView =  inflater.inflate(R.layout.fragment_builder_cards, container, false);
        recyclerView = mView.findViewById(R.id.builder_cards_recyclerview);
        initRecyclerViewList();
        return mView;
    }

    private void initRecyclerViewList(){
        recyclerView.setLayoutManager(linear_manager);
        recyclerView.setAdapter(list_adapter);
    }

    private void initRecyclerViewHeadline(){
        recyclerView.setLayoutManager(linear_manager);
        recyclerView.setAdapter(headline_adapter);
    }

    public void setLayout(String layout) {
        if (layout.equals("list")) {
            initRecyclerViewList();
        }
        else {
            initRecyclerViewHeadline();
        }
    }

    public void updateCardsFragment(HashMap<Card, Integer> cardsInDeck, HashMap<Card, Integer> allCards) {

        //Sort:
        ArrayList<Card> x;
        x = new ArrayList<>(cardsInDeck.keySet());
        Collections.sort(x);

        LinkedHashMap<Card, Integer> sortedCards = new LinkedHashMap<>();
        for (int i = 0; i < x.size(); i++) {
            Log.d(TAG, "updateCardsFragment: sorted_cards put: " + x.get(i).getName() + " " + allCards.get(x.get(i)));
            sortedCards.put(x.get(i), allCards.get(x.get(i)));
            if (allCards.get(x.get(i))==null) {
                sortedCards.put(x.get(i), 0);
            }
        }

        list_adapter = new BuilderListCardsAdapter(sortedCards, getActivity());
        headline_adapter = new BuilderHeadlineCardsAdapter(sortedCards, getActivity());
        linear_manager = new LinearLayoutManager(getContext());

        String layout = ((DeckBuilderActivity) Objects.requireNonNull(getActivity())).getLayout();
        if (layout.equals("list")) {
            initRecyclerViewList();
        }
        else if (layout.equals("headline")) {
            initRecyclerViewHeadline();
        }
    }


}