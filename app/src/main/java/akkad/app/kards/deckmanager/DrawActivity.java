package akkad.app.kards.deckmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.view.animation.AnimationUtils.loadAnimation;

public class DrawActivity extends AppCompatActivity {

    private static final String TAG = "DrawActivity";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BACKGROUND = "backgroundImage";

    public static final String STATE_DRAWN_4 = "drawn4";
    public static final String STATE_DRAWN_5 = "drawn5";
    public static final String STATE_NOT_DRAWN = "notDrawn";

    String state;
    CardView cardView1, cardView2, cardView3, cardView4, cardView5;
    ImageView cardImg1, cardImg2, cardImg3, cardImg4, cardImg5;
    ArrayList<ImageView> images;
    ToggleButton selector1, selector2, selector3, selector4, selector5;
    Button draw4, draw5, mulligan;
    Animation flipIn, flipOut;
    ArrayList<Card> cardList;
    HashMap<ToggleButton, CardView> selectorMap;
    HashMap<Card, Integer> cards;
    HashMap<CardView, ImageView> imagesMap;
    Handler mainHandler;
    String country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        initVars();
        getIncomingIntent();
        initCardList();
        setBackImages();
        setBackground();
        initSelectors();
    }

    private void initVars() {
        flipIn = loadAnimation(DrawActivity.this, R.anim.my_fade_in);
        flipOut = loadAnimation(DrawActivity.this, R.anim.my_fade_out);

        draw4 = findViewById(R.id.button_draw_4);
        draw5 = findViewById(R.id.button_draw_5);
        mulligan = findViewById(R.id.button_mulligan);

        draw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawFour(v);
            }
        });

        draw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawFive(v);
            }
        });

        mulligan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawMulligan(v);
            }
        });

        cards = new HashMap<>();

        cardView1 = findViewById(R.id.draw_card1);
        cardView2 = findViewById(R.id.draw_card2);
        cardView3 = findViewById(R.id.draw_card3);
        cardView4 = findViewById(R.id.draw_card4);
        cardView5 = findViewById(R.id.draw_card5);

        cardImg1 = findViewById(R.id.draw_card1_image);
        cardImg2 = findViewById(R.id.draw_card2_image);
        cardImg3 = findViewById(R.id.draw_card3_image);
        cardImg4 = findViewById(R.id.draw_card4_image);
        cardImg5 = findViewById(R.id.draw_card5_image);

        selector1 = findViewById(R.id.selector_1);
        selector2 = findViewById(R.id.selector_2);
        selector3 = findViewById(R.id.selector_3);
        selector4 = findViewById(R.id.selector_4);
        selector5 = findViewById(R.id.selector_5);

        selectorMap = new HashMap<>();
        selectorMap.put(selector1, cardView1);
        selectorMap.put(selector2, cardView2);
        selectorMap.put(selector3, cardView3);
        selectorMap.put(selector4, cardView4);
        selectorMap.put(selector5, cardView5);

        imagesMap = new HashMap<>();
        imagesMap.put(cardView1, cardImg1);
        imagesMap.put(cardView2, cardImg2);
        imagesMap.put(cardView3, cardImg3);
        imagesMap.put(cardView4, cardImg4);
        imagesMap.put(cardView5, cardImg5);

        images = new ArrayList<>(Arrays.asList(cardImg1, cardImg2, cardImg3, cardImg4, cardImg5));

        mainHandler = new Handler();

        state = STATE_NOT_DRAWN;
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("cards")) {
            Log.d(TAG, "getIncomingIntent: intent has extra cards.");
            cards = (HashMap<Card, Integer>) getIntent().getSerializableExtra("cards");
        }
        if (getIntent().hasExtra("country")) {
            Log.d(TAG, "getIncomingIntent: intent has extra country.");
            country = (String) getIntent().getStringExtra("country");
        }
    }

    private void initCardList() {
        cardList = new ArrayList<>();

        for (Map.Entry<Card, Integer> entry : cards.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                cardList.add(entry.getKey());
            }
        }
    }

    private void setBackImages() {
        HashMap<String, Integer> backImages = new HashMap<>();
        backImages.put("germany", R.drawable.german_cardback);
        backImages.put("britain", R.drawable.british_cardback);
        backImages.put("japan", R.drawable.japanese_cardback);
        backImages.put("usa", R.drawable.usa_cardback);
        backImages.put("soviet", R.drawable.soviet_cardback);

        int cardBack = backImages.get(country);

        for (int i = 0; i < images.size(); i++) {
            images.get(i).setImageResource(cardBack);
        }
    }

    private void initSelectors() {
        for (Map.Entry<ToggleButton, CardView> entry : selectorMap.entrySet()) {
            ToggleButton button = entry.getKey();
            button.setChecked(false);
            button.setEnabled(false);
        }
    }

    public void drawFour(View v) {
        flipFour();
    }

    public void drawFive(View v) {
        flipFive();
    }

    public void drawMulligan(View v) {
        for (Map.Entry<ToggleButton, CardView> entry : selectorMap.entrySet()) {
            ToggleButton button = entry.getKey();
            if (button.isChecked()) {
                CardView card = entry.getValue();
                ImageView image = imagesMap.get(card);
                button.setChecked(false);
                Collections.shuffle(cardList);
                image.setImageResource(cardList.get(0).getImage());
                cardList.remove(0);
            }
            button.setEnabled(false);
        }
        state = STATE_NOT_DRAWN;
    }

    private void setBackground() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int backgroundInt = sharedPreferences.getInt(BACKGROUND, 0);
        Log.d(TAG, "setBackground: backgroundInt is " + backgroundInt);
        ArrayList<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4));

        ImageView backgroundImage = findViewById(R.id.bg_draw);
        try {
            backgroundImage.setImageResource(images.get(backgroundInt));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

    }

    private void flipFour() {
        setBackImages();
        initCardList();
        for (Map.Entry<ToggleButton, CardView> entry : selectorMap.entrySet()) {
            CardView card = entry.getValue();
            if (!(card == cardView1)) {
                ImageView image = imagesMap.get(card);
                ToggleButton button = entry.getKey();
                button.setChecked(false);
                button.setEnabled(false);
                Collections.shuffle(cardList);
                try {
                    image.setImageResource(cardList.get(0).getImage());
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Your device ran out of memory! We're sorry for the inconvenience.", Toast.LENGTH_LONG).show();
                }
                cardList.remove(0);
                button.setEnabled(true);
            }

            selector1.setEnabled(false);
            selector1.setChecked(false);
            state = STATE_DRAWN_4;
        }
    }

    private void flipFive() {
        initCardList();
        for (Map.Entry<ToggleButton, CardView> entry : selectorMap.entrySet()) {
            CardView card = entry.getValue();
            ImageView image = imagesMap.get(card);
            ToggleButton button = entry.getKey();
            button.setChecked(false);
            button.setEnabled(false);
            Collections.shuffle(cardList);
            try {
                image.setImageResource(cardList.get(0).getImage());
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast.makeText(this, "Your device ran out of memory! We're sorry for the inconvenience.", Toast.LENGTH_LONG).show();
            }
            cardList.remove(0);
            button.setEnabled(true);
        }
        state = STATE_DRAWN_5;
    }
}
