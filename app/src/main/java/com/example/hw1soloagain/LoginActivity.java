package com.example.hw1soloagain;

import static android.graphics.Color.parseColor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    private  TextView alertBox;
    private EditText username;
    private EditText password;
    private Button btnLogin;
    private List<User> validUsers = new ArrayList<>();
    String correctPassword = BuildConfig.TEST_KEY;
    private static int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alertBox = findViewById(R.id.alertBox);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);


        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<User>> call = jsonPlaceHolderApi.getUser();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()) {
                    alertBox.setText("Code: "+ response.code());
                }
                List<User> users = response.body();
                validUsers.addAll(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                alertBox.setText(t.getMessage());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bgColor = android.R.color.background_light;
                boolean usernameValid = true;
                boolean passwordValid = true;
                String errorMessage = "";
                alertBox.setText(errorMessage);
                alertBox.setBackgroundColor(getResources().getColor(bgColor));
                username.setBackgroundColor(getResources().getColor(bgColor));
                password.setBackgroundColor(getResources().getColor(bgColor));


                if (!usernameCheck(validUsers, username.getText().toString())) {
                    usernameValid = false;
                    errorMessage += "Username is invalid.";
                    alertBox.setBackgroundColor(parseColor("#F7FF282C"));
                    username.setBackgroundColor(parseColor("#F7FF282C"));
                }
                if(!passwordCheck(correctPassword, password.getText().toString())){
                    passwordValid = false;
                    errorMessage += "Password is invalid.";
                    alertBox.setBackgroundColor(parseColor("#F7FF282C"));
                    password.setBackgroundColor(parseColor("#F7FF282C"));
                }
                if(usernameValid && passwordValid){
                    nextActivity(view);
                }
                else{
                    alertBox.setText(errorMessage);
                }
            }
        });




    }

    public static boolean usernameCheck(List<User> validUsers, String inputUsername){
        for (User validUser: validUsers) {
            if (validUser.getUsername().equals(inputUsername)) {
                userId = validUser.getId();
                return true;
            }
        }
        return false;
    }
    public static boolean passwordCheck(String systemPassword, String inputPassword){
        if (systemPassword.equals(inputPassword)) {
            return true;
        }
        return false;
    }

    public void nextActivity(View view) {
        Intent intent = com.example.hw1soloagain.MainActivity.getIntent(getApplicationContext(), userId, username.getText().toString());
        startActivity(intent);
    }
}