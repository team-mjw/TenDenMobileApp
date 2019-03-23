package com.teamMJW.tenden;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AddDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        goBackToPreviousPage();
    }

    //For testing purposes......Go back to the Main Page when "Back" button is clicked
    private void goBackToPreviousPage() {
        Button mainPageButton = findViewById(R.id.backMainPage);
        mainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}