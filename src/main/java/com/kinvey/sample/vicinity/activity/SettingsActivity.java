package com.kinvey.sample.vicinity.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.util.SetTypeface;
import com.kinvey.sample.vicinity.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.vicinity_radius)
    TextView vicinityRadius;

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar)
    TextView textView_toolbar;

    MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        init();
        clickListener();
        initializeTypeface();
    }

    public void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView_toolbar.setText(getResources().getString(R.string.action_settings));
    }

    public void initializeTypeface() {
        SetTypeface typeface = new SetTypeface(this);
        Typeface tf = typeface.getFont("regular");
        textView_toolbar.setTypeface(tf);
        vicinityRadius.setTypeface(tf);
    }

    public void clickListener() {
        vicinityRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeRadiusInput();
            }
        });
    }

    public void takeRadiusInput() {
        CharSequence inputPrefill = String.valueOf(Utils.getSharedPreferenceInteger(SettingsActivity.this, "radius"));
        CharSequence inputHintRadius = "Radius";

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.enter_radius)
                .content(R.string.unit_type)
                .typeface("Montserrat-SemiBold.otf", "Montserrat-Regular.otf")
                .theme(Theme.LIGHT)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(inputHintRadius, inputPrefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        Utils.setSharedPreferenceInteger(SettingsActivity.this, "radius",
                                Integer.parseInt(charSequence.toString()));
                    }
                }).show();
    }

}
