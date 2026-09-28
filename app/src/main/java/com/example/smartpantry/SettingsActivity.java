package com.example.smartpantry;
import android.content.*;import android.os.*;import android.widget.*;import androidx.appcompat.app.AppCompatActivity;
public class SettingsActivity extends AppCompatActivity{
 protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_settings);android.content.SharedPreferences p=getSharedPreferences("settings",0);Switch s=findViewById(R.id.switchExpiry);s.setChecked(p.getBoolean("expiry_alerts",true));s.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("expiry_alerts",c).apply());findViewById(R.id.btnBackSettings).setOnClickListener(v->finish());}
}