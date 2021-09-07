package akkad.app.kards.deckmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mopub.mobileads.MoPubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static android.view.animation.AnimationUtils.loadAnimation;

public class BuilderCollectionFragment extends Fragment {

    //RV adapter
    RecyclerView recyclerView;
    LinearLayoutManager linear_manager;
    BuilderListCollectionAdapter list_adapter;
    BuilderHeadlineCollectionAdapter headline_adapter;
    HashMap<Card, Integer> allCards, cardsInDeck;
    LinkedHashMap<Card, Integer> sortedCards;
    String layout, mainCountry, allyCountry;
    Animation fadeIn;

    public BuilderCollectionFragment() {
        // Required empty public constructor
    }

    public static BuilderCollectionFragment newInstance() {
        return new BuilderCollectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCards = new HashMap<>();
        cardsInDeck = new HashMap<>();

        fadeIn = loadAnimation(getActivity(), R.anim.my_fade_in);

        mainCountry = ((DeckBuilderActivity) Objects.requireNonNull(getActivity())).getDeck().getCountry();
        allyCountry = ((DeckBuilderActivity)getActivity()).getDeck().getAlly();

        allCards = ((DeckBuilderActivity)getActivity()).getAllCards();
        cardsInDeck = ((DeckBuilderActivity)getActivity()).getDeck().getCardsInDeck();

        //Sort:
        ArrayList<Card> cardList;
        cardList = new ArrayList<>(allCards.keySet());
        Collections.sort(cardList);

        sortedCards = new LinkedHashMap<>();

        for (int i = 0; i < cardList.size(); i++) {
            if ((cardList.get(i).getCountryName().equals(mainCountry)) || (cardList.get(i).getCountryName().equals(allyCountry))) {
                sortedCards.put(cardList.get(i), getCardAmount(cardList.get(i).getName()));
            }
        }

        list_adapter = new BuilderListCollectionAdapter(sortedCards, getActivity(), BuilderCollectionFragment.this);
        headline_adapter = new BuilderHeadlineCollectionAdapter(sortedCards, getActivity());
        linear_manager = new LinearLayoutManager(getContext());

        layout = "list";

    }

    private int getCardAmount(String cardName) {
        cardsInDeck = ((DeckBuilderActivity) Objects.requireNonNull(getActivity())).getDeck().getCardsInDeck();
        for (Map.Entry<Card, Integer> entry : cardsInDeck.entrySet()) {
            if (cardName.equals(entry.getKey().getName())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate RV
        View mView =  inflater.inflate(R.layout.fragment_builder_collection, container, false);
        recyclerView = mView.findViewById(R.id.builder_collection_recyclerview);
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

    public void updateCollectionFragment(HashMap<Card, Integer> allCards) {

        //Sort:
        ArrayList<Card> cardList;
        cardList = new ArrayList<>(allCards.keySet());
        Collections.sort(cardList);

        sortedCards = new LinkedHashMap<>();

        for (int i = 0; i < cardList.size(); i++) {
            if ((cardList.get(i).getCountryName().equals(mainCountry)) || (cardList.get(i).getCountryName().equals(allyCountry))) {
                sortedCards.put(cardList.get(i), getCardAmount(cardList.get(i).getName()));
            }
        }

        list_adapter = new BuilderListCollectionAdapter(sortedCards, getActivity(), BuilderCollectionFragment.this);
        headline_adapter = new BuilderHeadlineCollectionAdapter(sortedCards, getActivity());
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