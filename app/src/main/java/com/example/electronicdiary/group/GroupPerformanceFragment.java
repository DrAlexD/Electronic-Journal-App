package com.example.electronicdiary.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.R;

public class GroupPerformanceFragment extends Fragment {
    private GroupPerformanceViewModel groupPerformanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);
        groupPerformanceViewModel = new ViewModelProvider(this,
                new GroupPerformanceViewModelFactory(getArguments().getString("group"),
                        getArguments().getString("subject")))
                .get(GroupPerformanceViewModel.class);
        final TextView textView = root.findViewById(R.id.group_performance_text);
        groupPerformanceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}