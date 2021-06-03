package com.example.electronic_journal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.electronic_journal.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String role = sharedPreferences.getString("userRole", "");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (role.equals("ROLE_ADMIN")) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_profile, R.id.nav_search_available_students, R.id.nav_admin_actions, R.id.nav_settings, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
        } else if (role.equals("ROLE_PROFESSOR")) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_profile, R.id.nav_search_available_students, R.id.nav_settings, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_student_profile, R.id.nav_settings, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavGraph navGraph = navController.getGraph();
        navGraph.setStartDestination(role.equals("ROLE_STUDENT") ? R.id.nav_student_profile : R.id.nav_profile);
        navGraph.findNode(R.id.nav_student_profile).setLabel(getString(role.equals("ROLE_STUDENT") ? R.string.menu_profile : R.string.menu_student_profile));
        navController.setGraph(navGraph);

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();
        if (role.equals("ROLE_ADMIN")) {
            navMenu.removeItem(R.id.nav_student_profile);
            navMenu.findItem(R.id.nav_admin_actions).setVisible(
                    sharedPreferences.getBoolean(getString(R.string.is_admin_rules), false));
        } else if (role.equals("ROLE_PROFESSOR")) {
            navMenu.removeItem(R.id.nav_student_profile);
            navMenu.removeItem(R.id.nav_admin_actions);
            sharedPreferences.edit().putBoolean(getString(R.string.is_admin_rules), false).apply();
        } else {
            navMenu.removeItem(R.id.nav_profile);
            navMenu.removeItem(R.id.nav_search_available_students);
            navMenu.removeItem(R.id.nav_admin_actions);
            sharedPreferences.edit().putBoolean(getString(R.string.is_admin_rules), false).apply();
            sharedPreferences.edit().putBoolean(getString(R.string.is_professor_rules), false).apply();
        }

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                sharedPreferences.edit().putBoolean(getString(R.string.is_remember_me), false).apply();
                Repository.getInstance().logout();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.close();
                }
                return handled;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name_text);
        userName.setText(Repository.getInstance().getUser().getFullName());
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (getString(R.string.is_admin_rules).equals(s)) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu navMenu = navigationView.getMenu();
            navMenu.findItem(R.id.nav_admin_actions).setVisible(
                    sharedPreferences.getBoolean(getString(R.string.is_admin_rules), false));
        }
    }
}