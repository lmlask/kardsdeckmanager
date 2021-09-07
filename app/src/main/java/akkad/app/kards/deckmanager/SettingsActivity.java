package akkad.app.kards.deckmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SettingsActivity";

    //Constants
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COLUMNS_NUMBER = "columnsNumber";
    public static final String BLITZ_COST = "blitzCost";
    public static final String EXPORT_CODE_ONLY = "exportCodeOnly";
    public static final String BACKGROUND = "backgroundImage";

    Spinner columns;
    Switch blitz, export;
    CardView bg1, bg2, bg3, bg4;
    ImageView thumb;
    int columnsNumber, background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initVars();
        initSelectors();

        Toolbar collectionToolbar = findViewById(R.id.toolbar_settings);
        super.setSupportActionBar(collectionToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.settings));

        Window window = this.getWindow();
        window.setStatusBarColor (Color.parseColor("#181818"));

        loadData();
    }

    private void initVars() {
        columns = findViewById(R.id.settings_spinner_columns);
        blitz = findViewById(R.id.settings_switch_blitz);
        export = findViewById(R.id.settings_switch_export);
        bg1 = findViewById(R.id.bgcard1);
        bg2 = findViewById(R.id.bgcard2);
        bg3 = findViewById(R.id.bgcard3);
        bg4 = findViewById(R.id.bgcard4);
        thumb = findViewById(R.id.bg_thumb);
        background = 0;
        columnsNumber = 2;
    }

    private void initSelectors() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.adapter_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        columns.setAdapter(adapter);
        columns.setOnItemSelectedListener(this);
        ArrayList<CardView> cards = new ArrayList<>(Arrays.asList(bg1, bg2, bg3, bg4));
        HashMap<View, Integer> bgs = new HashMap<>();
        bgs.put(bg1, 0); bgs.put(bg2, 1); bgs.put(bg3, 2); bgs.put(bg4, 3);
        ArrayList<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4));

        for (int i = 0; i < 4; i++) {
            cards.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Selected background is " + bgs.get(v));
                    thumb.setImageResource(images.get(bgs.get(v)));
                    background = bgs.get(v);
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: Selected number of columns is " + parent.getItemAtPosition(position));
        columnsNumber = Integer.parseInt(columns.getSelectedItem().toString());
        Log.d(TAG, "onItemSelected: columnsNumber is " + columnsNumber);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu clicks
        int id = item.getItemId();

        // Back button (action bar)
        if (id == android.R.id.home) {
            saveData();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
        super.onBackPressed();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(COLUMNS_NUMBER, columnsNumber);
        Log.d(TAG, "saveData: ColumnsNumber is " + columnsNumber);
        editor.putBoolean(BLITZ_COST, blitz.isChecked());
        editor.putBoolean(EXPORT_CODE_ONLY, export.isChecked());
        editor.putInt(BACKGROUND, background);
        Log.d(TAG, "saveData: Background is " + background);

        editor.apply();

        Toast.makeText(this, R.string.saved_data, Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        columnsNumber = sharedPreferences.getInt(COLUMNS_NUMBER, 2);
        columns.setSelection(columnsNumber-1);

        blitz.setChecked(sharedPreferences.getBoolean(BLITZ_COST, false));
        export.setChecked(sharedPreferences.getBoolean(EXPORT_CODE_ONLY, false));

        background = sharedPreferences.getInt(BACKGROUND, 0);
        ArrayList<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4));
        thumb.setImageResource(images.get(background));
    }
}