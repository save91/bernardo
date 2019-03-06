package net.extrategy.bernardo.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import net.extrategy.bernardo.R;
import net.extrategy.bernardo.geofence.BernardoGeofenceService;
import net.extrategy.bernardo.network.BernardoNetworkService;
import net.extrategy.bernardo.utilities.InjectorUtils;
import net.extrategy.bernardo.utilities.SchedulerUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    private MainActivityViewModel mViewModel;
    private String mOldMessage;

    private Button mButtonDoor;
    private Button mButtonGate;
    private Switch mSwitchCloser;

    private AlertDialog mAlertDoor;
    private AlertDialog mAlertGate;

    private boolean mIsUserCloserToExtrategy = false;

    private BernardoNetworkService mBernardoNetworkService;
    private BernardoGeofenceService mBernardoGeofenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBernardoNetworkService = InjectorUtils.provideNetworService(getApplicationContext());
        mBernardoGeofenceService = InjectorUtils.provideGeofenceService(getApplicationContext());

        mButtonDoor = findViewById(R.id.button_door);
        mButtonGate = findViewById(R.id.button_gate);
        mSwitchCloser = findViewById(R.id.switch_closer);

        mAlertDoor = buildAlertDoor();
        mAlertGate = buildAlertGate();

        mSwitchCloser.setOnCheckedChangeListener((CompoundButton button, boolean isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "checked");
            } else {
                Log.i(TAG, "unchecked");
            }
        });

        mButtonDoor.setOnClickListener((View v) -> {
            if (mIsUserCloserToExtrategy) {
                mBernardoNetworkService.startOpenDoorService();
            } else {
                mAlertDoor.show();
            }
        });

        mButtonGate.setOnClickListener((View v) -> {
            if (mIsUserCloserToExtrategy) {
                mBernardoNetworkService.startOpenGateService();
            } else {
                mAlertGate.show();
            }
        });

        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.isCloserToExtrategy().observe(this, isCloser -> {
            if (isCloser == null) {
                mIsUserCloserToExtrategy = false;
            } else {
                mIsUserCloserToExtrategy = isCloser;
            }
        });

        mViewModel.isOpeningTheDoor().observe(this, loading -> {
            if (loading != null) {
                mButtonDoor.setEnabled(!loading);
            }
        });

        mViewModel.isOpeningTheGate().observe(this, loading -> {
            if (loading != null) {
                mButtonGate.setEnabled(!loading);
            }
        });

        mViewModel.onMessage().observe(this, message -> {
            mOldMessage = message;
            if (message != null) {
                showToast(message);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }

        SchedulerUtils.scheduleGeofencing(this);
        mBernardoGeofenceService.registerGeofencing();
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SchedulerUtils.scheduleGeofencing(this);
                    mBernardoGeofenceService.registerGeofencing();
                }
            }
        }
    }
}
