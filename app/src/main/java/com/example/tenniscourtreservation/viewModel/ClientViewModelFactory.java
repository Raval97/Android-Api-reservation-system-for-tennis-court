package com.example.tenniscourtreservation.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenniscourtreservation.LogInActivity;
import com.example.tenniscourtreservation.RegisterActivity;
import com.example.tenniscourtreservation.model.Client;

public class ClientViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Client client;
    private Context context;

    public ClientViewModelFactory(RegisterActivity activity, Client client) {
        this.context = activity;
        this.client = client;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new ClientViewModel(context, client);
    }
}
