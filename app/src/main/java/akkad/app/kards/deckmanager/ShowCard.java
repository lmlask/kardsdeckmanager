package akkad.app.kards.deckmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import static android.view.animation.AnimationUtils.loadAnimation;

public class ShowCard extends AppCompatActivity {
    private static final String TAG = "ShowCard";
    CardView cardView;
    Animation slideIn, slideOut, fadeIn, fadeOut;
    ImageView bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.card_show);
        slideIn = loadAnimation(getApplicationContext(), R.anim.slide_in);
        slideOut = loadAnimation(getApplicationContext(), R.anim.slide_out);
        fadeIn = loadAnimation(getApplicationContext(), R.anim.my_fade_in);
        fadeOut = loadAnimation(getApplicationContext(), R.anim.my_fade_out);
        Log.d(TAG, "onCreate: started.");
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("card_image")){
            Log.d(TAG, "getIncomingIntent: intent has extras.");

            int card_image = getIntent().getIntExtra("card_image", R.mipmap.ic_launcher);
            setImage(card_image);
        }
    }

    //Working.
    private void setImage(int image) {
        Log.d(TAG, "setImage: Setting card image.");
        ImageView cardImage = findViewById(R.id.card_image_show);
        cardView = findViewById(R.id.card_container);
        bg = findViewById(R.id.black_bg);

        cardImage.setImageResource(image);

        cardView.startAnimation(slideIn);
        bg.startAnimation(fadeIn);

    }

    @Override
    public void onBackPressed() {
        cardView.startAnimation(slideOut);
        bg.startAnimation(fadeOut);
        super.onBackPressed();
        overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out);

    }

    public void cardHide(View view) {
        cardView.startAnimation(slideOut);
        bg.startAnimation(fadeOut);
        this.finish();
        overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out);

    }
}
