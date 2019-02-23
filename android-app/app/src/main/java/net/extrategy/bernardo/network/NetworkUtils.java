package net.extrategy.bernardo.network;

import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String SERVER = "http://10.0.2.2:8080/api/v1.0";
    private static final String DOOR_PATH = "/door";
    private static final String GATE_PATH = "/gate";

    /**
     * Builds the URL used to talk to the proxy server
     *
     * @return The URL to use to open the door.
     */
    public static URL buildDoorUrl() {
        Uri builtUri = Uri.parse(SERVER + DOOR_PATH).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the proxy server
     *
     * @return The URL to use to open the gate.
     */
    public static URL buildGateUrl() {
        Uri builtUri = Uri.parse(SERVER + GATE_PATH).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
