package com.example.tenniscourtreservation.viewModel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenniscourtreservation.view.RegisterActivity;
import com.example.tenniscourtreservation.model.Client;
import com.example.tenniscourtreservation.viewModel.ClientViewModel;

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
