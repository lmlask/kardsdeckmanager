package akkad.app.kards.deckmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.animation.AnimationUtils.loadAnimation;

public class ShowChart extends AppCompatActivity {
    private static final String TAG = "ShowChart";
    CardView cardView;
    Animation slideInSlow;
    Animation slideOut;
    Animation fadeIn;
    Animation fadeOut;
    ImageView bg;

    ArrayList<Float> graphAll;
    ArrayList<Float> graphUnits;
    ArrayList<Integer> countsAll;
    ArrayList<Integer> images;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.chart_show);
        slideInSlow = loadAnimation(getApplicationContext(), R.anim.slide_in_slow);
        slideOut = loadAnimation(getApplicationContext(), R.anim.slide_out);
        fadeIn = loadAnimation(getApplicationContext(), R.anim.my_fade_in_fast);
        fadeOut = loadAnimation(getApplicationContext(), R.anim.my_fade_out);
        cardView = findViewById(R.id.chart_container);
        bg = findViewById(R.id.black_bg_chart);

        cardView.startAnimation(fadeIn);
        bg.startAnimation(fadeIn);

        graphAll = new ArrayList<>();
        graphUnits = new ArrayList<>();
        countsAll = new ArrayList<>();
        images = new ArrayList<>();

        getIncomingIntents();
        inflateGraph();
    }

    private void getIncomingIntents() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("title")){
            Log.d(TAG, "getIncomingIntent: intent has extra Title.");
            title = (String) getIntent().getStringExtra("title");
        }
        if(getIntent().hasExtra("graph_all")){
            Log.d(TAG, "getIncomingIntent: intent has extra graph_all.");
            graphAll = (ArrayList<Float>) getIntent().getSerializableExtra("graph_all");
        }
        if(getIntent().hasExtra("graph_units")){
            Log.d(TAG, "getIncomingIntent: intent has extra graph_units.");
            graphUnits = (ArrayList<Float>) getIntent().getSerializableExtra("graph_units");
        }
        else {
            graphUnits = new ArrayList<>(Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f));
        }
        if(getIntent().hasExtra("counts_all")){
            Log.d(TAG, "getIncomingIntent: intent has extra counts_all.");
            countsAll = (ArrayList<Integer>) getIntent().getSerializableExtra("counts_all");
        }
        if(getIntent().hasExtra("images")){
            Log.d(TAG, "getIncomingIntent: intent has extra images.");
            images = (ArrayList<Integer>) getIntent().getSerializableExtra("images");
        }
    }

    @Override
    public void onBackPressed() {
        cardView.startAnimation(fadeOut);
        bg.startAnimation(fadeOut);
        super.onBackPressed();
        overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out);

    }

    public void chartHide(View view) {
        cardView.startAnimation(fadeOut);
        bg.startAnimation(fadeOut);
        this.finish();
        overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out);

    }

    private void inflateGraph() {
        ArrayList<Integer> imageViews = new ArrayList<>(Arrays.asList(R.id.image_1, R.id.image_2,
                R.id.image_3, R.id.image_4, R.id.image_5, R.id.image_6, R.id.image_7));

        //First inflate the images of each bar.
        for (int i = 0; i < 7; i++) {
            ImageView image = (ImageView) findViewById(imageViews.get(i));
            image.setImageResource(images.get(i));
            // Fix type images if TYPES.
            if (title.equals(getString(R.string.types))) {
                image.setBackgroundColor(Color.parseColor("#BDB594"));
                image.setColorFilter(Color.parseColor("#000000"));
            }
            // Fix attack or defense images.
            else if (title.equals(getString(R.string.attack)) || title.equals(getString(R.string.defense))) {
                image.setColorFilter(Color.parseColor("#000000"));
            }
        }
        //Now inflate the long bars.
        ArrayList<Integer> bar_views = new ArrayList<>(Arrays.asList(R.id.bar_1, R.id.bar_2, R.id.bar_3, R.id.bar_4,
                R.id.bar_5, R.id.bar_6, R.id.bar_7));
        for (int i = 0; i < 7; i++) {
            ImageView bar = (ImageView) findViewById(bar_views.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            if (graphAll.get(i) == 0.99f) {
                params.width = MATCH_PARENT;
            } else {
                params.matchConstraintPercentWidth = graphAll.get(i);
            }
            bar.setLayoutParams(params);
            bar.startAnimation(slideInSlow);

            if (title.equals(getString(R.string.cost))) {
                bar.setBackgroundColor(Color.parseColor("#D09600"));
            }
            else {
                bar.setBackgroundColor(Color.parseColor("#000000"));
            }
        }

        //Hide key and mode/avg if unnecessary
        if (!title.equals(getString(R.string.cost))) {
            hideKey();
        }
        if (title.equals(getString(R.string.types))) {
            hideKey();
            hideMode();
        }

        //Make numbers visible on Attack and Defense screens
        if (title.equals(getString(R.string.attack)) || title.equals(getString(R.string.defense))) {
            ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(R.id.number_1, R.id.number_2, R.id.number_3, R.id.number_4, R.id.number_5, R.id.number_6, R.id.number_7));
            for (int i = 0; i < 7; i++) {
                TextView number = findViewById(numbers.get(i));
                number.setVisibility(View.VISIBLE);
            }
        }

        //Now inflate the short bars.
        ArrayList<Integer> bar_views_short = new ArrayList<>(Arrays.asList(R.id.bar_1_units, R.id.bar_2_units, R.id.bar_3_units, R.id.bar_4_units,
                R.id.bar_5_units, R.id.bar_6_units, R.id.bar_7_units));
        for (int i = 0; i < 7; i++) {
            ImageView bar = (ImageView) findViewById(bar_views_short.get(i));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            if (graphUnits.get(i) == 0.99f) {
                params.width = MATCH_PARENT;
            } else {
                params.matchConstraintPercentWidth = graphUnits.get(i);
            }
            bar.setLayoutParams(params);
            Log.d(TAG, "inflateGraph: Starting bar animation.");
            bar.startAnimation(slideInSlow);
        }
        // Now inflate the card counters.
        ArrayList<Integer> counters = new ArrayList<>(Arrays.asList(R.id.count_1, R.id.count_2, R.id.count_3, R.id.count_4,
                R.id.count_5, R.id.count_6, R.id.count_7));
        for (int i = 0; i < 7; i++) {
            TextView counter = findViewById(counters.get(i));
            counter.setText(Integer.toString(countsAll.get(i)));
        }
        //Now inflate title, mode and average.
        TextView titleView = findViewById(R.id.chart_title);
        titleView.setText(title);

        TextView modeCount = findViewById(R.id.mode_count);
        int mode = graphAll.indexOf(0.99f) + 1;
        modeCount.setText(Integer.toString(mode));

        TextView avgCount = findViewById(R.id.avg_count);

        double avg;
        int sum = 0;

        for (int i = 0; i < 7; i++) {
            sum = sum + ((countsAll.get(i))*(i+1));
            Log.d(TAG, "inflateGraph: Summing: + " + countsAll.get(i) + " * " + (i+1) + " = " + ((countsAll.get(i))*(i+1)));
        }
        avg = (double) sum / 39;

        Log.d(TAG, "inflateGraph: SUM: " + sum);
        Log.d(TAG, "inflateGraph: AVG: " + avg);

        avgCount.setText(String.format("%.1f", avg));
    }

    private void hideKey() {
        ImageView square1 = findViewById(R.id.square_1);
        ImageView square2 = findViewById(R.id.square_2);
        TextView text1 = findViewById(R.id.text3);
        TextView text2 = findViewById(R.id.text4);

        square1.setVisibility(View.GONE);
        square2.setVisibility(View.GONE);
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);

        TextView mode = findViewById(R.id.text1);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mode.getLayoutParams();
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        mode.setLayoutParams(params);
    }

    private void hideMode() {
        ArrayList<Integer> views = new ArrayList<>(Arrays.asList(R.id.text1, R.id.text2, R.id.avg_count, R.id.mode_count));
        for (int i = 0; i < 4; i++) {
            TextView textView = findViewById(views.get(i));
            textView.setVisibility(View.GONE);
        }
        ImageView kcp = findViewById(R.id.kcp);
        kcp.setVisibility(View.VISIBLE);
    }
}

