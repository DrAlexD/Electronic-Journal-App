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

public class ModuleFragment extends Fragment {
    private final GroupPerformanceViewModel groupPerformanceViewModel;
    private ModuleViewModel moduleViewModel;
    private final int position;

    ModuleFragment(int position, GroupPerformanceViewModel groupPerformanceViewModel) {
        this.groupPerformanceViewModel = groupPerformanceViewModel;
        this.position = position;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

        moduleViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        moduleViewModel.setGroupPerformanceViewModel(groupPerformanceViewModel);
        moduleViewModel.setPosition(position);

        final TextView textView = root.findViewById(R.id.module_text);
        moduleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}