package com.alexwglenn.whatcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alexwglenn.whatcard.model.APIError;
import com.alexwglenn.whatcard.model.AuthorizeResponse;
import com.alexwglenn.whatcard.model.LoginCredentials;
import com.alexwglenn.whatcard.util.ErrorUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import retrofit2.Response;
import rx.Observable;
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
    @InjectView(R.id.login_progress)
    ProgressBar progress;

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

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && passwordField.getText().length() > 0) {
                    signInButton.setEnabled(true);
                } else {
                    signInButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && emailField.getText().length() > 0) {
                    signInButton.setEnabled(true);
                } else {
                    signInButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInButton.animate().setDuration(300).alpha(0.0f);
                progress.animate().setDuration(300).alpha(1.0f);
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

                        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle(getString(R.string.login_error));
                        builder.setMessage(e.getMessage());
                        builder.setNegativeButton("Ok", null);
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                signInButton.animate().setDuration(300).alpha(1.0f);
                                progress.animate().setDuration(300).alpha(0.0f);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });

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
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(getString(R.string.login_error));
                            APIError error = ErrorUtils.parseError(authorizeResponseResponse);
                            builder.setMessage(error.message);
                            builder.setNegativeButton("Ok", null);
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    signInButton.animate().setDuration(300).alpha(1.0f);
                                    progress.animate().setDuration(300).alpha(0.0f);

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
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
