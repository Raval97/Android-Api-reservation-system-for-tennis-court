package com.example.tenniscourtreservation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.tenniscourtreservation.R;
import com.example.tenniscourtreservation.databinding.ActivityLoginBinding;
import com.example.tenniscourtreservation.model.User;
import com.example.tenniscourtreservation.viewModel.UserViewModel;
import com.example.tenniscourtreservation.viewModel.factory.UserViewModelFactory;

public class LogInActivity extends AppCompatActivity {

    TextView signUpText;
    Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        UserViewModel userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this, new User())).get(UserViewModel.class);
        binding.setUserModel(userViewModel);

        signUpText =  (TextView) findViewById(R.id.signUpText);
        back = (Button) findViewById(R.id.back);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
    }

}