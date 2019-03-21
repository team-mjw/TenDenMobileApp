package com.teamMJW.tenden;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edit();
        cancel();
    }

    private void cancel() {
        Button mainPageButton = (Button) findViewById(R.id.cancelButton);
        mainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Canceled", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //populating the fields with the current mode data
    private void edit() {
        if (getIntent().hasExtra("modeName")) {
            String name = getIntent().getExtras().getString("modeName");
            EditText editName = (EditText) findViewById(R.id.modeName);
            editName.setText(name);
        }
    }
}
