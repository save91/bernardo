package net.extrategy.bernardo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.extrategy.bernardo.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mButtonDoor;
    private Button mButtonGate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonDoor = findViewById(R.id.button_door);
        mButtonGate = findViewById(R.id.button_gate);

        mButtonDoor.setOnClickListener((View v) -> {
            Toast.makeText(this, R.string.btn_door, Toast.LENGTH_LONG).show();
        });

        mButtonGate.setOnClickListener((View v) -> {
            Toast.makeText(this, R.string.btn_gate, Toast.LENGTH_LONG).show();
        });

    }
}
