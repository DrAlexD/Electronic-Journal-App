package com.example.electronicdiary.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.JwtResponse;
import com.example.electronicdiary.MainActivity;
import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final CheckBox isRememberMe = findViewById(R.id.is_remember_me);
        isRememberMe.setChecked(sharedPreferences.getBoolean(getString(R.string.is_remember_me), false));

        if (isRememberMe.isChecked()) {
            loginViewModel.getLoggedUser(new JwtResponse(sharedPreferences.getString("jwtToken", ""),
                    sharedPreferences.getLong("userId", -1), sharedPreferences.getBoolean("isUserProfessor", true)));
            loginViewModel.getUser().observe(this, user -> {
                if (user != null) {
                    if (user instanceof Result.Success) {
                        Repository.getInstance().setUser(((Result.Success<User>) user).getData());
                        openMainActivity();
                    } else {
                        initLoginListeners(isRememberMe, sharedPreferences);
                    }
                }
            });
        } else {
            initLoginListeners(isRememberMe, sharedPreferences);
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void initLoginListeners(CheckBox isRememberMe, SharedPreferences sharedPreferences) {
        final EditText usernameEditText = findViewById(R.id.user_name_edit);
        final EditText passwordEditText = findViewById(R.id.password_edit);
        final Button loginButton = findViewById(R.id.login_button);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }

            usernameEditText.setError(loginFormState.getUsernameError() != null ? getString(loginFormState.getUsernameError()) : null);
            passwordEditText.setError(loginFormState.getPasswordError() != null ? getString(loginFormState.getPasswordError()) : null);

            loginButton.setEnabled(loginFormState.isDataValid());
        });

        LiveData<Result<JwtResponse>> responseLiveData = loginViewModel.getResponse();
        LiveData<Result<User>> userLiveData = Transformations.switchMap(responseLiveData, response -> {
            if (response instanceof Result.Success) {
                String jwtToken = ((Result.Success<JwtResponse>) response).getData().getToken();
                Long userId = ((Result.Success<JwtResponse>) response).getData().getId();
                Boolean isUserProfessor = ((Result.Success<JwtResponse>) response).getData().isProfessor();
                sharedPreferences.edit().putBoolean(getString(R.string.is_remember_me), isRememberMe.isChecked()).apply();
                sharedPreferences.edit().putString("jwtToken", jwtToken).apply();
                sharedPreferences.edit().putLong("userId", userId).apply();
                sharedPreferences.edit().putBoolean("isUserProfessor", isUserProfessor).apply();

                loginViewModel.getLoggedUser(new JwtResponse(jwtToken, userId, isUserProfessor));

                return loginViewModel.getUser();
            } else {
                String error = ((Result.Error) response).getError();
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                return null;
            }
        });

        LiveData<List<Semester>> semestersLiveData = Transformations.switchMap(userLiveData, user -> {
            if (user instanceof Result.Success) {
                Repository.getInstance().setUser(((Result.Success<User>) user).getData());
                if (Long.parseLong(sharedPreferences.getString(getString(R.string.current_semester), "-1")) == -1) {
                    loginViewModel.downloadSemesters();

                    return loginViewModel.getSemesters();
                } else {
                    openMainActivity();
                    return null;
                }
            } else {
                return null;
            }
        });

        semestersLiveData.observe(this, semesters -> {
            if (semesters != null) {
                sharedPreferences.edit().putString(getString(R.string.current_semester),
                        String.valueOf(semesters.get(semesters.size() - 1).getId())).apply();
                openMainActivity();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            //loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
    }
}