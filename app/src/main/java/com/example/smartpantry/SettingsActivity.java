package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity
        extends AppCompatActivity {

 @Override
 protected void onCreate(Bundle b) {

  super.onCreate(b);

  setContentView(
          R.layout.activity_settings
  );

  SharedPreferences preferences =
          getSharedPreferences(
                  "settings",
                  MODE_PRIVATE
          );

  Switch expirySwitch =
          findViewById(
                  R.id.switchExpiry
          );

  boolean expiryAlertsEnabled =
          preferences.getBoolean(
                  "expiry_alerts",
                  true
          );

  expirySwitch.setChecked(
          expiryAlertsEnabled
  );

  expirySwitch.setOnCheckedChangeListener(
          (buttonView, isChecked) -> {

           preferences
                   .edit()
                   .putBoolean(
                           "expiry_alerts",
                           isChecked
                   )
                   .apply();

           String message;

           if (isChecked) {

            message =
                    "Expiry alerts enabled";

           } else {

            message =
                    "Expiry alerts disabled";
           }

           Toast.makeText(
                   SettingsActivity.this,
                   message,
                   Toast.LENGTH_SHORT
           ).show();
          }
  );

  findViewById(
          R.id.btnBackSettings
  ).setOnClickListener(
          v -> finish()
  );
 }
}