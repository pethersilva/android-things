package com.pethersilva.sample_switch_led;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BUTTON_PIN_NAME = "BCM6";
    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManagerService service = new PeripheralManagerService();
        Log.d(TAG, "Available GPIO: " + service.getGpioList());

        try {
            mLedGpio = service.openGpio(BUTTON_PIN_NAME);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio.setEdgeTriggerType(Gpio.EDGE_NONE);
            mLedGpio.setActiveType(Gpio.ACTIVE_HIGH);
            mLedGpio.setValue(false);

            ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
            toggle.setChecked(false);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {
                            mLedGpio.setValue(true);
                        } else {
                            mLedGpio.setValue(false);
                        }

                    } catch (IOException gpioExcetpion){

                    }
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mLedGpio.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mLedGpio = null;
        }
    }
}