package com.alexwglenn.whatcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexwglenn.whatcard.model.AuthorizeResponse;
import com.alexwglenn.whatcard.model.LoginCredentials;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import retrofit2.Response;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Created by aglenn on 7/21/16.
 */
public class LoginActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";

    @Inject
    ThisCardService thisCardService;

    @InjectView(R.id.sign_in_button)
    Button signInButton;

    @InjectView(R.id.email)
    EditText emailField;
    @InjectView(R.id.password)
    EditText passwordField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        ((WhatCard)getApplication()).getComponent().inject(this);

        AuthorizeResponse credentials = Realm.getDefaultInstance().where(AuthorizeResponse.class).findFirst();

        if (credentials != null) {
            goToMain();
            return;
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisCardService.logIn(new LoginCredentials(emailField.getText().toString(), passwordField.getText().toString()))
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Response<AuthorizeResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error logging in: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final Response<AuthorizeResponse> authorizeResponseResponse) {

                        if (authorizeResponseResponse.code() == 200) {
                            Realm realm = Realm.getDefaultInstance();

                            Realm.Transaction transaction = new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(authorizeResponseResponse.body());
                                }
                            };

                            realm.executeTransaction(transaction);
                            goToMain();

                        } else {

                        }
                    }
                });
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
