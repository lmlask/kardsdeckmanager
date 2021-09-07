package akkad.app.kards.deckmanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ViewerCardsFragment extends Fragment {

    //RV adapter
    RecyclerView recyclerView;
    LinearLayoutManager linear_manager;
    FragmentListCollectionAdapter list_adapter;
    FragmentHeadlineCollectionAdapter headline_adapter;
    HashMap<Card, Integer> cardMap;
    String layout;
    View rootView;
    int cardCount, mainCount, allyCount, mainCountryImage, allyCountryImage;
    FloatingActionMenu fam;
    FloatingActionButton deleteFab, editFab, drawFab, winFab;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    public ViewerCardsFragment() {
        // Required empty public constructor
    }

    public static ViewerCardsFragment newInstance(HashMap<Card, Integer> cards, int cardCount, int mainCount, int allyCount, int mainCountryImage, int allyCountryImage) {
        ViewerCardsFragment fragment = new ViewerCardsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, cards);
        args.putInt(ARG_PARAM2, cardCount);
        args.putInt(ARG_PARAM3, mainCount);
        args.putInt(ARG_PARAM4, allyCount);
        args.putInt(ARG_PARAM5, mainCountryImage);
        args.putInt(ARG_PARAM6, allyCountryImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<Card, Integer> tempMap;
        tempMap = new HashMap<>();
        Bundle b = this.getArguments();
        cardMap = new HashMap<>();

        assert b != null;
        if (b.getSerializable(ARG_PARAM1) != null) {
            tempMap = (HashMap<Card, Integer>)b.getSerializable(ARG_PARAM1);
        }

        for (Map.Entry<Card, Integer> entry : tempMap.entrySet()) {
            cardMap.put(entry.getKey(), entry.getValue());
        }

        cardCount = b.getInt(ARG_PARAM2);
        mainCount = b.getInt(ARG_PARAM3);
        allyCount = b.getInt(ARG_PARAM4);
        mainCountryImage = b.getInt(ARG_PARAM5);
        allyCountryImage = b.getInt(ARG_PARAM6);

        ArrayList<Card> x;
        x = new ArrayList<>(cardMap.keySet());

        Collections.sort(x);

        //I'm using a linkedHashMap to keep the order constant.
        LinkedHashMap<Card, Integer> sortedCards = new LinkedHashMap<>();
        for (int i = 0; i < x.size(); i++) {
            sortedCards.put(x.get(i), cardMap.get(x.get(i)));
        }


        list_adapter = new FragmentListCollectionAdapter(sortedCards, getActivity());
        headline_adapter = new FragmentHeadlineCollectionAdapter(sortedCards, getActivity());
        linear_manager = new LinearLayoutManager(getContext());

        layout = "list";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate RV
        View mView =  inflater.inflate(R.layout.fragment_viewer_cards, container, false);
        rootView = mView;
        recyclerView = (RecyclerView)mView.findViewById(R.id.viewer_cards_recyclerview);
        initRecyclerViewList();

        ImageView mainImage = mView.findViewById(R.id.main_image);
        ImageView allyImage = mView.findViewById(R.id.ally_image);
        TextView mainCountText = mView.findViewById(R.id.main_count);
        TextView allyCountText = mView.findViewById(R.id.ally_count);
        TextView totalCount = mView.findViewById(R.id.total_count);

        mainImage.setImageResource(mainCountryImage);
        allyImage.setImageResource(allyCountryImage);
        mainCountText.setText(Integer.toString(mainCount));
        allyCountText.setText(Integer.toString(allyCount));

        String cardCountString = ((cardCount + 1) + ("/40"));
        totalCount.setText(cardCountString);
        if (cardCount > 39) {
            totalCount.setTextColor(Color.parseColor("#FF0000"));
        } else {
            totalCount.setTextColor(Color.parseColor("#D3D1C3"));
        }

        if (allyCount > 12) {
            allyCountText.setTextColor(Color.parseColor("#FF0000"));
        } else {
            allyCountText.setTextColor(Color.parseColor("#D3D1C3"));
        }

        initButtons(mView);

        return mView;
    }

    public void initButtons(View mView) {
        fam = mView.findViewById(R.id.floating_action_menu);
        deleteFab = mView.findViewById(R.id.delete_fab);
        editFab = mView.findViewById(R.id.edit_fab);
        drawFab = mView.findViewById(R.id.draw_fab);
        winFab = mView.findViewById(R.id.win_fab);

        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeckViewerActivity)getActivity()).deleteDeckHandler(v);
            }
        });

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeckViewerActivity)getActivity()).editDeckHandler(v);
            }
        });

        drawFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeckViewerActivity)getActivity()).simulateDrawHandler(v);
            }
        });

        winFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DeckViewerActivity)getActivity()).updateWinRateHandler(v);
            }
        });


    }

    public void initRecyclerViewList(){
        recyclerView.setLayoutManager(linear_manager);
        recyclerView.setAdapter(list_adapter);
    }

    public void initRecyclerViewHeadline(){
        recyclerView.setLayoutManager(linear_manager);
        recyclerView.setAdapter(headline_adapter);
    }

    public void changeLayout(String layout) {
        if (layout.equals("list")) {
            initRecyclerViewList();
        }
        else if (layout.equals("headline")){
            initRecyclerViewHeadline();
        }
    }

    public void updateCards(){
        cardMap = ((DeckViewerActivity)getActivity()).getDeck().getCardsInDeck();

        ArrayList<Card> x;
        x = new ArrayList<>(cardMap.keySet());

        Collections.sort(x);

        //I'm using a linkedHashMap to keep the order constant.
        LinkedHashMap<Card, Integer> sortedCards = new LinkedHashMap<>();
        for (int i = 0; i < x.size(); i++) {
            sortedCards.put(x.get(i), cardMap.get(x.get(i)));
        }
        list_adapter = new FragmentListCollectionAdapter(sortedCards, getActivity());
        headline_adapter = new FragmentHeadlineCollectionAdapter(sortedCards, getActivity());

        layout = ((DeckViewerActivity)getActivity()).getLayout();
        if (layout.equals("list")) {
            initRecyclerViewList();
        } else if (layout.equals("headline")) {
            initRecyclerViewHeadline();
        }
    }

    public void updateBottomBar(Deck deck) {
        ImageView mainImage = rootView.findViewById(R.id.main_image);
        ImageView allyImage = rootView.findViewById(R.id.ally_image);
        TextView mainCountText = rootView.findViewById(R.id.main_count);
        TextView allyCountText = rootView.findViewById(R.id.ally_count);
        TextView totalCount = rootView.findViewById(R.id.total_count);

        mainImage.setImageResource(deck.getMainImage());
        allyImage.setImageResource(deck.getAllyImage());
        mainCountText.setText(Integer.toString(deck.getMainCount()));
        allyCountText.setText(Integer.toString(deck.getAllyCount()));

        String cardCountString = ((deck.getCardCount() + 1) + ("/40"));
        totalCount.setText(cardCountString);

        if (deck.getCardCount() > 39) {
            totalCount.setTextColor(Color.parseColor("#FF0000"));
        } else {
            totalCount.setTextColor(Color.parseColor("#D3D1C3"));
        }

        if (deck.getAllyCount() > 12) {
            allyCountText.setTextColor(Color.parseColor("#FF0000"));
        } else {
            allyCountText.setTextColor(Color.parseColor("#D3D1C3"));
        }
    }

}