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
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GroupPerformanceViewModel groupPerformanceViewModel;
    private ModuleViewModel moduleViewModel;

    public static ModuleFragment newInstance(int index, GroupPerformanceViewModel groupPerformanceViewModel) {
        ModuleFragment fragment = new ModuleFragment();
        fragment.setGroupPerformanceViewModel(groupPerformanceViewModel);
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    static String getTitle(int position) {
        return "Модуль " + position;
    }

    private void setGroupPerformanceViewModel(GroupPerformanceViewModel groupPerformanceViewModel) {
        this.groupPerformanceViewModel = groupPerformanceViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduleViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        moduleViewModel.setGroupPerformanceViewModel(groupPerformanceViewModel);
        int index = getArguments().getInt(ARG_SECTION_NUMBER);
        moduleViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

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