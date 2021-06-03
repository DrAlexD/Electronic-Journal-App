package com.example.electronic_journal.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronic_journal.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminActionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_actions, container, false);

        int page = getArguments() != null ? getArguments().getInt("openPage") : 0;

        AdminOneTypeActionsPagerAdapter adminOneTypeActionsPagerAdapter = new AdminOneTypeActionsPagerAdapter(this);
        ViewPager2 viewPager = root.findViewById(R.id.admin_one_type_actions_pager);
        viewPager.setAdapter(adminOneTypeActionsPagerAdapter);
        viewPager.setCurrentItem(page, false);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = root.findViewById(R.id.admin_one_type_actions_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0)
                tab.setText("Добавить");
            else if (position == 1)
                tab.setText("Изменить");
            else if (position == 2)
                tab.setText("Удалить");
        }).attach();

        return root;
    }
}