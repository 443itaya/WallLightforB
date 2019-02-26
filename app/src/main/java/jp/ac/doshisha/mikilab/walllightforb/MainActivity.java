package jp.ac.doshisha.mikilab.walllightforb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import okhttp3.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String url = "http://192.168.1.38/api/p1SV3amWQL4aQF9tPJdJ4vOBqSwzK3iFH1gwENrI/groups/2/action";
    String url_light = "http://192.168.1.38/api/p1SV3amWQL4aQF9tPJdJ4vOBqSwzK3iFH1gwENrI/lights/";
    String json;
    private String res = "";

    int mode = 2;
    int bri = 254;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                            json = "{\"on\":true,\"hue\":42000,\"bri\":"+bri+",\"sat\":254}";
                            postTest(0);
                        }else if(mode == 1){
                            json =  "{\"on\":true,\"hue\":7000,\"bri\":"+bri+",\"sat\":180}";
                            postTest(0);
                        }else if(mode == 2){
                            json = "{\"on\":true,\"bri\":"+bri+",\"ct\":222}";
                            postTest(0);
                        }else if(mode == 3){
                            for(int i = 1; i < 9; i++) {
                                if(i % 4 == 0) json = "{\"on\":true,\"hue\":0,\"bri\":" + bri + ",\"sat\":254}";
                                else if(i % 4 == 1) json = "{\"on\":true,\"hue\":60000,\"bri\":" + bri + ",\"sat\":254}";
                                else if(i % 4 == 2) json = "{\"on\":true,\"hue\":24000,\"bri\":" + bri + ",\"sat\":254}";
                                else json = "{\"on\":true,\"hue\":50000,\"bri\":" + bri + ",\"sat\":254}";
                                postTest(i+15);
                            }
                        }
                    }
                }
        );

        coolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json = "{\"on\":true,\"hue\":42000,\"bri\":"+bri+",\"sat\":254}";
                mode = 0;
                postTest(0);
            }
        });
        warmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json =  "{\"on\":true,\"hue\":7000,\"bri\":"+bri+",\"sat\":180}";
                mode = 1;
                postTest(0);
            }
        });
        nomalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json = "{\"on\":true,\"bri\":"+bri+",\"ct\":222}";
                mode = 2;
                postTest(0);
            }
        });
        partyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 1; i < 9; i++) {
                    if(i % 4 == 0) json = "{\"on\":true,\"hue\":0,\"bri\":" + bri + ",\"sat\":254}";
                    else if(i % 4 == 1) json = "{\"on\":true,\"hue\":60000,\"bri\":" + bri + ",\"sat\":254}";
                    else if(i % 4 == 2) json = "{\"on\":true,\"hue\":24000,\"bri\":" + bri + ",\"sat\":254}";
                    else json = "{\"on\":true,\"hue\":50000,\"bri\":" + bri + ",\"sat\":254}";
                    postTest(i+15);
                }
                mode = 3;
            }
        });
    }


    public void postTest(int num) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request;

        if(num == 0) {
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(url_light+num+"/state")
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
