package com.example.tenniscourtreservation.viewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tenniscourtreservation.model.Client;
import com.example.tenniscourtreservation.model.User;

public class ClientViewModel extends ViewModel {

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> surname = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();

    public Client client;
    private Context context;

    public ClientViewModel(Context context, Client client) {
        this.client = client;
        this.context = context;
    }

    public void onSignUpClick() {
        client.setName(name.getValue());
        client.setSurname(surname.getValue());
        client.setEmailAddress(emailAddress.getValue());
        try {
            client.setPhoneNumber(Long.valueOf(phoneNumber.getValue()));
        } catch (NumberFormatException e) {
            System.out.println("error");
        }
        switch (client.isValid()) {
            case "Incorrect Name":
                Toast.makeText(context, "please enter valid name", Toast.LENGTH_LONG).show();
                break;
            case "Incorrect Surname":
                Toast.makeText(context, "please enter valid surname", Toast.LENGTH_LONG).show();
                break;
            case "Incorrect Phone Number":
                Toast.makeText(context, "please enter valid phone number", Toast.LENGTH_LONG).show();
                break;
            case "Incorrect Email Address":
                Toast.makeText(context, "please enter valid email address", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "Successful create account for:"+client.getName(), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
