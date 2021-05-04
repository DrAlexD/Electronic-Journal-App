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
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.JwtResponse;
import com.example.electronicdiary.MainActivity;
import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;

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
                if (user == null) {
                    return;
                }

                Repository.getInstance().setUser(user);
                openMainActivity();
            });
        } else {
            if (Long.parseLong(sharedPreferences.getString(getString(R.string.current_semester), "-1")) == -1)
                sharedPreferences.edit().putString(getString(R.string.current_semester),
                        String.valueOf(Repository.getInstance().getLastSemesterId())).apply();

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

        loginViewModel.getResponse().observe(this, response -> {
            if (response == null) {
                return;
            }
            //loadingProgressBar.setVisibility(View.GONE);
            if (response instanceof Result.Success) {
                String jwtToken = ((Result.Success<JwtResponse>) response).getData().getToken();
                Long userId = ((Result.Success<JwtResponse>) response).getData().getId();
                Boolean isUserProfessor = ((Result.Success<JwtResponse>) response).getData().isProfessor();
                sharedPreferences.edit().putBoolean(getString(R.string.is_remember_me), isRememberMe.isChecked()).apply();
                sharedPreferences.edit().putString("jwtToken", jwtToken).apply();
                sharedPreferences.edit().putLong("userId", userId).apply();
                sharedPreferences.edit().putBoolean("isUserProfessor", isUserProfessor).apply();

                loginViewModel.getLoggedUser(new JwtResponse(jwtToken, userId, isUserProfessor));
                loginViewModel.getUser().observe(this, user -> {
                    if (user == null) {
                        return;
                    }

                    Repository.getInstance().setUser(user);
                    openMainActivity();
                });
            } else {
                String error = ((Result.Error) response).getError();
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
            //setResult(Activity.RESULT_OK);
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