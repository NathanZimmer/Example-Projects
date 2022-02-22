package com.example.led;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    private String style; //style of the lights; currently color and rainbow
    private int[] RGB; //rgb values from 0 to 255
    private double brightness; //brightness from 0.0 to 1.0
    private int[] custom_RGB = {0, 0, 0}; //int array for user set color values
    private final int[][] COLORS = {{255, 0, 0},    //red
                                    {0, 255, 0},    //green
                                    {0, 0, 255},    //blue
                                    {200, 0, 255},  //purple
                                    {255, 0, 25},   //pink
                                    {0, 255, 255},  //cyan
                                    {255, 100, 0},  //orange
                                    {255, 255, 0},  //yellow
                                    {255, 255, 255}}; //white

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Nathan's LED controller");

        //sets color_spinner
        Spinner spinner = findViewById(R.id.color_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.LED_colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //assigns values based on sharedPrefs
        sharedPrefs = getSharedPreferences("Buttons and sliders", Context.MODE_PRIVATE);
        style = sharedPrefs.getString("style", "color");
        try {
            RGB = COLORS[sharedPrefs.getInt("color_position", 0)];
        }catch(Exception e) {
            RGB = custom_RGB;
        }
        brightness = (double) sharedPrefs.getInt("brightness", 100) / 100;
        custom_RGB[0] = sharedPrefs.getInt("custom_r", 0);
        custom_RGB[1] = sharedPrefs.getInt("custom_g", 0);
        custom_RGB[2] = sharedPrefs.getInt("custom_b", 0);

        //creates sharedPrefs editor
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //- - - - - - listeners - - - - - -

        SeekBar brightness_bar = findViewById(R.id.brightness_bar);
        brightness_bar.setProgress(sharedPrefs.getInt("brightness", 100));
        brightness_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = (double) progress / 100;
                editor.putInt("brightness", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new SSH().execute("python3 RGB_controller/set_options.py " + " " + style + " " + brightness);
            }
        });

        ToggleButton on_off = findViewById(R.id.on_off);
        on_off.setChecked(sharedPrefs.getBoolean("on_off", false));
        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new SSH().execute("sudo pkill python; sudo python3 RGB_controller/main_file_v2.py");
                    editor.putBoolean("on_off", true);
                }
                else {
                    new SSH().execute("python3 RGB_controller/set_options.py off");
                    editor.putBoolean("on_off", false);
                }
                editor.apply();
            }
        });

        spinner.setSelection(sharedPrefs.getInt("color_position", 0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idd) {
                try {
                    RGB = COLORS[position];
                }catch(Exception e) {
                    RGB = custom_RGB;
                }
                new SSH().execute("python3 RGB_controller/set_options.py " + style + " " + brightness + " " + RGB[0] + " " + RGB[1] + " " + RGB[2]);
                editor.putInt("color_position", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadioGroup lightSelections = findViewById(R.id.light_configs);
        if (sharedPrefs.getString("style", "color").equals("color")) {
            lightSelections.check(R.id.solid_color);
        }
        else {
            lightSelections.check(R.id.rainbow);
        }
        lightSelections.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        System.out.println("what");
                        break;
                    case R.id.solid_color:
                        style = "color";
                        break;
                    case R.id.rainbow:
                        style = "rainbow";
                        break;
                }
                new SSH().execute("python3 RGB_controller/set_options.py " + style);
                editor.putString("style", style);
                editor.apply();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //for button clicks
        if (id == R.id.custom_color) {
            colorPopupWindow();
        }
        if (id == R.id.other_options) {
            settingsPopupWindow();
        }

        return super.onOptionsItemSelected(item);
    }

    //popup window for custom color entry
    public void colorPopupWindow() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View Popup = getLayoutInflater().inflate(R.layout.color_popup, null);

        EditText r, g, b;
        Button submit_color, cancel_color;

        r = Popup.findViewById(R.id.red_text);
        g = Popup.findViewById(R.id.green_text);
        b = Popup.findViewById(R.id.blue_text);

        submit_color = Popup.findViewById(R.id.submit_color);
        cancel_color = Popup.findViewById(R.id.cancel_color);

        dialogBuilder.setView(Popup);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        //listener for submit button
        submit_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                custom_RGB[0] = Math.min(255, Integer.parseInt(r.getText().toString()));
                custom_RGB[1] = Math.min(255, Integer.parseInt(g.getText().toString()));
                custom_RGB[2] = Math.min(255, Integer.parseInt(b.getText().toString()));
                new SSH().execute("python3 RGB_controller/set_options.py " + style + " " + brightness + " " + custom_RGB[0] + " " + custom_RGB[1] + " " + custom_RGB[2]);

                //saves current custom colors to shared preferences
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("custom_r", Integer.parseInt(r.getText().toString()));
                editor.putInt("custom_g", Integer.parseInt(g.getText().toString()));
                editor.putInt("custom_b", Integer.parseInt(b.getText().toString()));
                editor.apply();

                dialog.dismiss();
            }
        });

        //listener for cancel button
        cancel_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //settings popup window
    public void settingsPopupWindow() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View Popup = getLayoutInflater().inflate(R.layout.settings_popup, null);

        dialogBuilder.setView(Popup);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        //listeners and other stuff goes here
    }
}

/*
to-do:
 - add settings for rainbow speed and colorWipe speed
 - add brightness for rainbow
 - add more style options
    - add relevant settings for said additional styles
 - make numbers for current custom settings appear in the r g b text fields
 - make color change when custom settings are updated
 */