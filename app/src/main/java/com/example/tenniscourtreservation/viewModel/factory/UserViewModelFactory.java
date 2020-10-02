package com.example.tenniscourtreservation.viewModel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenniscourtreservation.view.LogInActivity;
import com.example.tenniscourtreservation.model.User;
import com.example.tenniscourtreservation.viewModel.UserViewModel;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private User user;
    private Context context;

    public UserViewModelFactory(LogInActivity activity, User user) {
        this.context = activity;
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new UserViewModel(context,user);
    }
}
