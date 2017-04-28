package jwg003.hourhalf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class WeatherActivity extends AppCompatActivity {


    private TextView m_currentTempText;
    private TextView m_currentHumidity;
    private Button m_refreshButton;
    private String TAG = "WeatherActivity";
    private static final String DEGREE = " \u00b0 ";
    private BroadcastReceiver m_receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        m_currentTempText = (TextView) findViewById(R.id.current_temp);
        m_currentHumidity = (TextView) findViewById(R.id.humidity);
        m_refreshButton = (Button) findViewById(R.id.refresh_button);

        m_refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double tempExtra = intent.getDoubleExtra(WeatherIntentService.TEMP_EXTRA, 0);
                Log.d(TAG, "onReceive: " + tempExtra);
                m_currentTempText.setText(tempExtra + DEGREE + "F");

//                double humidity = intent.getDoubleExtra(WeatherIntentService.HUMIDITY_EXTRA, 0);
//                m_currentHumidity.setText("H " + humidity);
            }
        };
        registerReceiver(m_receiver, new IntentFilter(WeatherIntentService.TEMP_RESPONSE));
}


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(m_receiver);
    }

    private void refresh(){
        WeatherIntentService.startActionGetWeather(this);
    }

}