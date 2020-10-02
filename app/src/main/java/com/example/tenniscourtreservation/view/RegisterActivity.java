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
import com.example.tenniscourtreservation.databinding.ActivitySignupBinding;
import com.example.tenniscourtreservation.model.Client;
import com.example.tenniscourtreservation.viewModel.ClientViewModel;
import com.example.tenniscourtreservation.viewModel.factory.ClientViewModelFactory;

public class RegisterActivity extends AppCompatActivity {

    TextView loginText;
    Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        ClientViewModel clientViewModel = ViewModelProviders.of(this, new ClientViewModelFactory(this, new Client())).get(ClientViewModel.class);
        binding.setClientModel(clientViewModel);

        loginText =  (TextView) findViewById(R.id.loginText);
        back = (Button) findViewById(R.id.back);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
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