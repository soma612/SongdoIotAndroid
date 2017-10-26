package net.iot.helloworld;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("Hi, Hong gil dong");

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //전화걸기 기능
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                //전화걸기권한 요청에 대해서 사용자에게 설명해야함
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
        int permissionLocationCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionLocationCheck !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },2);
        }
    }


    public void startSubactivity(View view) {
        EditText editText = (EditText) findViewById(R.id.edittext);
        Intent intent =
                new Intent(MainActivity.this, SubActivity.class);
        intent.putExtra("edittext", editText.getText().toString());
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    public void start4thActivity(View view){
        Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
        startActivity(intent);
    }

    public void startScrollViewActivity(View view){
        Intent intent = new Intent(MainActivity.this, ScrollViewActivity.class);
        startActivity(intent);
    }

    public void startNaverOpenAPIActivity(View view){
        Intent intent = new Intent(MainActivity.this, NaverOpenAPIActivity.class);
        startActivity(intent);
    }

    public void startDaumOpenAPIActivity(View view){
        Intent intent = new Intent(MainActivity.this, DaumOpenAPIActivity.class);
        startActivity(intent);
    }
    public void startGoogleMapActivity(View view){
        Intent intent = new Intent(MainActivity.this, GoogleMapActivity.class);
        startActivity(intent);
    }
    public void startDatabaseActivity(View view){
        Intent intent = new Intent(MainActivity.this, SQLiteDatabaseActivity.class);
        startActivity(intent);
    }
    public void startSensorActivity(View view){
        Intent intent = new Intent(MainActivity.this, SensorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("edittext");
                    EditText editText = (EditText)findViewById(R.id.edittext);
                    editText.setText(result);
                }
                break;
        }
    }
}
