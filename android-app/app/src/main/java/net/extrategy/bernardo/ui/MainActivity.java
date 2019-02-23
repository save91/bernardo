package net.extrategy.bernardo.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

    private AlertDialog mAlertDoor;
    private AlertDialog mAlertGate;

    private BernardoNetworkService mBernardoNetworkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBernardoNetworkService = InjectorUtils.provideNetworService(getApplicationContext());

        mButtonDoor = findViewById(R.id.button_door);
        mButtonGate = findViewById(R.id.button_gate);

        mAlertDoor = buildAlertDoor();
        mAlertGate = buildAlertGate();

        mButtonDoor.setOnClickListener((View v) -> mAlertDoor.show());

        mButtonGate.setOnClickListener((View v) -> mAlertGate.show());

    }

    private AlertDialog buildAlertDoor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.alert_door_message)
                .setTitle(R.string.alert_door_title);

        builder.setPositiveButton(R.string.ok_door, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mBernardoNetworkService.startOpenDoorService();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        return builder.create();
    }

    private AlertDialog buildAlertGate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.alert_gate_message)
                .setTitle(R.string.alert_gate_title);

        builder.setPositiveButton(R.string.ok_gate, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mBernardoNetworkService.startOpenGateService();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        return builder.create();
    }
}
