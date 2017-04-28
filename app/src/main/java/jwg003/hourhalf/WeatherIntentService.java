package jwg003.hourhalf;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherIntentService extends IntentService {
    private static final  String  TAG =  "WeatherService" ;
    private static final  String  ACTION_GET_TEMP =  "jwg003.hourhalf.GET_TEMP";
    public static final String TEMP_ACTION = "jwg003.hourhalf.action.TEMP_RECEIVED";
    public static final String TEMP_RESPONSE = "wg003.hourhalf.response.TEMP_RESPONSE";
    public static final String TEMP_EXTRA = "wg003.hourhalf.extra.TEMP";
    public static final String HUMIDITY_EXTRA = "wg003.hourhalf.extra.HUMIDITY";


    public WeatherIntentService() {
        super("WeatherIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionGetWeather(Context context) {
        Intent intent = new Intent(context, WeatherIntentService.class);
        intent.setAction(ACTION_GET_TEMP);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG,"onHandleIntent" + action);
            handleAction();
        }
    }

    private void handleAction() {
        String url = "http://api.openweathermap.org/data/2.5/weather?zip=94040,us&units=imperial&appid=4d36b5f1fce463fe1647b8b9711bf707";
        String json = "empty";
        try {
            json = IOUtils.toString(new URL(url), "UTF-8");
            Log.d(TAG, "json = " + json);
            JSONObject jsonWeather = new JSONObject(json);
            JSONObject main = jsonWeather.getJSONObject("main");
            double temp = main.getDouble("temp");
            double humdity = main.getDouble("humidity");
            Log.d(TAG, "temp = " + temp);

            Intent tempResponseIntent = new Intent(TEMP_RESPONSE);
            tempResponseIntent.putExtra(TEMP_EXTRA, temp);
            sendBroadcast(tempResponseIntent);

//            Intent tempHumidityIntent = new Intent(TEMP_RESPONSE);
//            tempHumidityIntent.putExtra(HUMIDITY_EXTRA, humdity);
//            sendBroadcast(tempHumidityIntent);

        } catch (IOException e) {
            Log.e(TAG, "Error getting JSON weather data ", e);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON string " + json, e);
        }
    }
}