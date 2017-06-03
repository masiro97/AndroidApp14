package com.example.user.proj142;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Main3Activity extends AppCompatActivity {


    String userid = "";
    String password = "";
    TextView msg;
    EditText id, pass;
    Boolean isFirst = false;
    Handler handler = new Handler();
    class mThread extends Thread{
        @Override
        public void run() {
            try {
                URL url = new URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                String postData = "userid=" + URLEncoder.encode(userid) +
                        "&password=" + URLEncoder.encode(password);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream;

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();
                final String result = loginResult(inputStream);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("FAIL")) {
                            msg.setText("로그인에 실패했습니다");
                        } else {
                            msg.setText(result + "님 로그인 성공");
                        }
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String loginResult(InputStream is) {

        String data = "";
        Scanner s = new Scanner(is);
        while (s.hasNext()) data += s.nextLine();
        s.close();
        return data;
    }

    public void OnButton(View v) {
        userid = id.getText().toString();
        password = pass.getText().toString();

        if (userid.equals("") || password.equals(""))
            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요",
                    Toast.LENGTH_SHORT).show();

        else {
            mThread thread = new mThread();
            thread.start();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        msg = (TextView) findViewById(R.id.textView);
        id = (EditText) findViewById(R.id.userid);
        pass = (EditText) findViewById(R.id.password);
        pass.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
