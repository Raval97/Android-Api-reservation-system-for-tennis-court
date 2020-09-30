package com.example.tenniscourtreservation.viewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tenniscourtreservation.model.User;

public class UserViewModel extends ViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    public User user;
    private Context context;

    public UserViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
    }

    public void onSubmitClick() {
        user.setUsername(username.getValue());
        user.setPassword(password.getValue());
        if (user.isValid().equals("Incorrect Username")) {
            Toast.makeText(context, "please enter valid username", Toast.LENGTH_LONG).show();
        } else if (user.isValid().equals("Incorrect Password")) {
            Toast.makeText(context, "please enter valid password", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, user.getUsername(), Toast.LENGTH_LONG).show();
        }
    }
}
