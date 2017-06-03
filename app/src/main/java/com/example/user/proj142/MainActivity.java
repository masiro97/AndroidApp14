package com.example.user.proj142;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText et;
    String urlInput = "";
    TextView tv;


    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {

            try {
                URL url = new URL(urlInput);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    final String data = readData(urlConnection.getInputStream());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(data);
                        }
                    });
                }

                urlConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void OnButton(View v) {

        if(v.getId() == R.id.button2){
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent);
        }

        else{
            urlInput = et.getText().toString();
            thread.start();

        }
    }

    String readData(InputStream is) {

        String data = "";
        Scanner s = new Scanner(is);
        while (s.hasNext()) data += s.nextLine() + "\n";
        s.close();
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.editText);
        tv = (TextView) findViewById(R.id.textView);
    }
}
