package com.example.hw1soloagain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hw1soloagain.JsonPlaceHolderApi;
import com.example.hw1soloagain.Post;
import com.example.hw1soloagain.R;

import java.util.List;

import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String ACTIVITY_LABEL = "MAIN_ACTIVITY";
    private TextView welcomeBanner;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int userId = getIntent().getIntExtra(ACTIVITY_LABEL+"USERID", -1);
        String username = getIntent().getStringExtra(ACTIVITY_LABEL+"USERNAME");

        welcomeBanner = findViewById(R.id.welcome_banner);
        textViewResult = findViewById(R.id.text_view_result);

        welcomeBanner.setText("Welcome " + username + "! You are user #" + userId);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()) {
                    textViewResult.setText("Code: "+ response.code());
                }
                List<Post> posts = response.body();
                for (Post post: posts) {
                    if(post.getUserId() == userId) {
                        String content = "";
                        content += "Username: " + username + "\t\t\t\t\t\t\t\t\t\t\t\tUser ID: " + post.getUserId() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n\n";

                        textViewResult.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    public static Intent getIntent(Context context, int userId, String username){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ACTIVITY_LABEL+"USERID", userId);
        intent.putExtra(MainActivity.ACTIVITY_LABEL+"USERNAME", username);
        return intent;
    }
}