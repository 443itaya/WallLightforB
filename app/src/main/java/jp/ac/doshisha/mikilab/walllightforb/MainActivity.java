package jp.ac.doshisha.mikilab.walllightforb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import okhttp3.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String url = "http://192.168.1.38/api/p1SV3amWQL4aQF9tPJdJ4vOBqSwzK3iFH1gwENrI/groups/2/action";
    String json;
    private String res = "";

    int mode = 2;
    int bri = 254;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        Button coolButton = (Button)findViewById(R.id.button_cool);
        Button warmButton = (Button)findViewById(R.id.button_warm);
        Button nomalButton = (Button)findViewById(R.id.button_normal);
        Button partyButton = (Button)findViewById(R.id.button_party);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        bri = seekBar.getProgress();
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // ツマミを離したときに呼ばれる
                        if(mode == 0){
                            json = "{\"on\":true,\"hue\":24000,\"bri\":"+bri+",\"sat\":254}";
                            postTest(0);
                        }else if(mode == 1){
                            json =  "{\"on\":true,\"hue\":60000,\"bri\":"+bri+",\"sat\":254}";
                            postTest(0);
                        }else if(mode == 2){
                            json = "{\"on\":true,\"hue\":46014,\"bri\":"+bri+",\"sat\":254}";
                            postTest(0);
                        }else if(mode == 3){
                            json = "{\"on\":true,\"hue\":0,\"bri\":"+bri+",\"sat\":254}";
                            postTest(1);
                        }
                    }
                }
        );

        coolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json = "{\"on\":true,\"hue\":24000,\"bri\":"+bri+",\"sat\":254}";
                mode = 0;
                postTest(0);
            }
        });
        warmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json =  "{\"on\":true,\"hue\":60000,\"bri\":"+bri+",\"sat\":254}";
                mode = 1;
                postTest(0);
            }
        });
        nomalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json = "{\"on\":true,\"hue\":46014,\"bri\":"+bri+",\"sat\":254}";
                mode = 2;
                postTest(0);
            }
        });
        partyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json = "{\"on\":true,\"hue\":0,\"bri\":"+bri+",\"sat\":254}";
                mode = 3;
                postTest(1);
            }
        });
    }


    public void postTest(int num) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request;

        if(num == 1) {
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
        }

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.w("onResponse", res);
                    }
                });
            }
        });
    }
    private void failMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.w("failMessage", "fail");
            }
        });
    }
}
