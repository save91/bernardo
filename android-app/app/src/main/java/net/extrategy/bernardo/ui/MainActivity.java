package net.extrategy.bernardo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.extrategy.bernardo.R;
import net.extrategy.bernardo.network.BernardoNetworkService;
import net.extrategy.bernardo.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mButtonDoor;
    private Button mButtonGate;

    private BernardoNetworkService mBernardoNetworkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBernardoNetworkService = InjectorUtils.provideNetworService(getApplicationContext());

        mButtonDoor = findViewById(R.id.button_door);
        mButtonGate = findViewById(R.id.button_gate);

        mButtonDoor.setOnClickListener((View v) -> {
            mBernardoNetworkService.startOpenDoorService();
            Toast.makeText(this, R.string.btn_door, Toast.LENGTH_LONG).show();
        });

        mButtonGate.setOnClickListener((View v) -> {
            Toast.makeText(this, R.string.btn_gate, Toast.LENGTH_LONG).show();
        });

    }
}
