package com.example.projectthree.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectthree.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class DynamicUrlActivity extends AppCompatActivity {
    public static final String imageUrl = "https://homepages.cae.wisc.edu/~ece533/images/boat.png";
    private TextView result;
    private Button getResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_url);
        initView();

    }

    private void initView() {
        result = findViewById(R.id.result);
        getResult = findViewById(R.id.getResult);

        //listeners
        getResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://base.url/").build();

                CallPic callPic = retrofit.create(CallPic.class);
                callPic.loadPic(imageUrl).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            System.out.println("is Sucessfull..."+response.body().toString());
                            System.out.println("Response boydy Content Type"+response.body().contentType());

                            result.setText(response.body().contentType().toString());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }


    public interface CallPic {
        @GET
        Call<ResponseBody> loadPic(@Url String url);
    }
}