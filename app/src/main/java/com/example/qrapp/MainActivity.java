package com.example.qrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    CodeScanner mCodeScanner;
    public String dni;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CodeScannerView scannerView = findViewById(R.id.id_qrCodeScannerView);
        mCodeScanner = new CodeScanner(this,scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                MainActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Code"+ result.getText(), Toast.LENGTH_SHORT).show();
                        dni = result.getText();

                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.100.119:3001")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                retrofitInterface = retrofit.create(RetrofitInterface.class);


                Call<userData> call = retrofitInterface.post(dni);

                //calling  api

                call.enqueue(new Callback<userData>() {
                    @Override
                    public void onResponse(Call<userData> call, Response<userData> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Post Updated Title: "+" PostId: ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<userData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        //Gson gson = new GsonBuilder().serializeNulls().create();



    }

    @Override
    protected void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }




}
