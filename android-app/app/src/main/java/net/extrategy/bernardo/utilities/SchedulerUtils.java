package net.extrategy.bernardo.utilities;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import net.extrategy.bernardo.geofence.BernardoJobService;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {
    private static final int INTERVAL_HOURS = 12;
    private static final int INTERVAL_SECONDS = (int) (TimeUnit.HOURS.toSeconds(INTERVAL_HOURS));
    private static final int SYNC_FLEXTIME_MINUTES = 15;
    private static final int SYNC_FLEXTIME_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(SYNC_FLEXTIME_MINUTES));

    private static final String GEOFENCING_JOB_TAG = "geofencing-job";

    private static boolean sInitialized;

    synchronized public static void scheduleGeofencing(@NonNull final Context context) {
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(BernardoJobService.class)
                .setTag(GEOFENCING_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        INTERVAL_SECONDS,
                        INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);

        sInitialized = true;
    }

    public static void cancelAll(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        dispatcher.cancelAll();
    }

}