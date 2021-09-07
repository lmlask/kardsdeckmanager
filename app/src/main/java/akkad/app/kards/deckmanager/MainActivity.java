package akkad.app.kards.deckmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.privacy.ConsentDialogListener;
import com.mopub.common.privacy.PersonalInfoManager;
import com.mopub.mobileads.MoPubErrorCode;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.mopub.common.logging.MoPubLog.LogLevel.INFO;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    int bg_current;
    ArrayList<Card> cardList;
    HashMap<String, Card> cardMap;
    DeckUtils utils;
    AlertDialog bugReport, changelogDialog;
    Button deckManagerButton, collectionButton, playkardsButton;
    final SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder("f0666ab1f43d43bbae6a6c3471cb62e1");

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BACKGROUND = "backgroundImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar collectionToolbar = findViewById(R.id.toolbar_main);
        super.setSupportActionBar(collectionToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = this.getWindow();
        window.setStatusBarColor(Color.parseColor("#181818"));

        // Set top left icon
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.settings_icon);

        initButtons();

        utils = new DeckUtils(this);
        cardMap = utils.getCardMap();
        getCardList();
        setBackground();
        configBuilder.withLogLevel(INFO);
        MoPub.initializeSdk(this, configBuilder.build(), initSdkListener());

        // Get a PersonalInformationManager instance, used to query the consent dialog
        PersonalInfoManager mPersonalInfoManager = MoPub.getPersonalInformationManager();

        // Check if you must show the consent dialog
        assert mPersonalInfoManager != null;
        mPersonalInfoManager.shouldShowConsentDialog();

        // Start loading the consent dialog. This call fails if the user has opted out of ad personalization.
        mPersonalInfoManager.loadConsentDialog(new ConsentDialogListener() {
            @Override
            public void onConsentDialogLoaded() {
                // If you choose to show the consent dialog in the future, check if it is ready using isConsentDialogReady() before showing it
                mPersonalInfoManager.showConsentDialog();
            }
            @Override
            public void onConsentDialogLoadFailed(@NonNull MoPubErrorCode moPubErrorCode) {
                MoPubLog.i("Consent dialog failed to load.");
            }
        });
    }


    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                // SDK initialization complete. You may now request ads.
            }
        };
    }







    //Info menu in top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act_menu, menu);
        bg_current = 1;
        return true;
    }

    private void initButtons() {
        deckManagerButton = findViewById(R.id.deck_manager_button);
        collectionButton = findViewById(R.id.card_collection_button);
        playkardsButton = findViewById(R.id.playkards_button);

        deckManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeckManager(v);
            }
        });

        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardCollection(v);
            }
        });

        playkardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayKards(v);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();

        if (id == R.id.action_changelog) {
            AlertDialog.Builder cl = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            cl.setTitle(R.string.changelog);
            cl.setMessage(getString(R.string.changelog_message));
            cl.setIcon(R.drawable.info_icon);

            cl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            cl.setNegativeButton(R.string.cancel, null);
            changelogDialog = cl.create();
            changelogDialog.show();
            return true;
        }

        // Top right btn
        if (id == R.id.action_info_main) {
            Intent infoIntent = new Intent(MainActivity.this, InformationActivity.class);
            startActivity(infoIntent);
            return true;
        }

        if (id == R.id.action_bug_main) {
            AlertDialog.Builder bug = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_bug_report, null);

            EditText email = (EditText) dialogView.findViewById(R.id.edit_text_email);
            EditText message = (EditText) dialogView.findViewById(R.id.edit_text_message);

            email.setHint(R.string.your_email_address);
            message.setHint(R.string.detailed_bug_description);

            bug.setTitle(R.string.report_bug);
            bug.setMessage(getString(R.string.bug_report));
            bug.setView(dialogView);
            bug.setIcon(R.drawable.ic_bug);

            bug.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] recipientMail = {"akkad.dev.bug@gmail.com"};
                    String subjectText = "BUG REPORT";
                    String messageText = message.getText().toString();

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, recipientMail);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, messageText);

                    sendIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.choose_email_client)));
                }
            });
            bug.setNegativeButton(R.string.cancel, null);
            bugReport = bug.create();
            bugReport.show();
            return true;
        }

        // Top left btn
        if (id == android.R.id.home) {
            Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDeckManager(View v) {
        //Launch deck manager activity.
        Intent intent_manager = new Intent(this, DeckManagerActivity.class);
        startActivity(intent_manager);
    }

    public void openCardCollection(View v) {
        //Launch card collection activity.
        Intent intent_collection = new Intent(this, CollectionActivity.class);
        startActivity(intent_collection);
    }

    public void openPlayKards(View v) {
        //Launch playkards
        Intent pkIntent = new Intent();
        pkIntent.setAction(Intent.ACTION_VIEW);
        pkIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        pkIntent.setData(Uri.parse("https://playkards.com"));
        startActivity(pkIntent);
    }

    public void openConfigs(View v) {
        v.setEnabled(false);
    }

    public void openDetails(View v) {
        v.setEnabled(false);
    }

    private void getCardList() {
        cardList = new ArrayList<>();
        cardList.addAll(cardMap.values());
        Collections.sort(cardList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bugReport != null) {
            bugReport.dismiss();
        }
    }

    private void setBackground() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int backgroundInt = sharedPreferences.getInt(BACKGROUND, 0);
        Log.d(TAG, "setBackground: backgroundInt is " + backgroundInt);
        ArrayList<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4));

        ImageView backgroundImage = findViewById(R.id.bg_main);
        backgroundImage.setImageResource(images.get(backgroundInt));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }
}