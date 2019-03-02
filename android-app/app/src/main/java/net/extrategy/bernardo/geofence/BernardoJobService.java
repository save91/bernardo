package net.extrategy.bernardo.geofence;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import net.extrategy.bernardo.utilities.InjectorUtils;

public class BernardoJobService extends JobService {
    private static final String TAG = BernardoJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        Log.i(TAG, "onStartJob");
        BernardoGeofenceService bernardoGeofenceService = InjectorUtils.provideGeofenceService(getApplicationContext());
        bernardoGeofenceService.registerGeofencing();

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i(TAG, "onStopJob");

        return false; // Answers the question: "Should this job be retried?"
    }
}
