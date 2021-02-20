package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment {
    private String studentName;
    private String group;

    private ArrayList<String> subjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_profile, container, false);

        studentName = getArguments().getString("student");
        group = getArguments().getString("group");

        downloadData();

        setPreferences(root);

        StudentProfileViewModel studentProfileViewModel = new ViewModelProvider(this).get(StudentProfileViewModel.class);
        studentProfileViewModel.setStudentName(studentName);
        studentProfileViewModel.setGroup(group);
        studentProfileViewModel.setSubjects(subjects);

        ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_subject, R.id.subjectTitle, subjects);
        final ListView listView = root.findViewById(R.id.studentSubjectsList);
        listView.setAdapter(subjectsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("subject", subjects.get(position));
            bundle.putString("student", studentName);
            bundle.putString("group", group);
            Navigation.findNavController(view).navigate(R.id.action_student_profile_to_student_performance, bundle);
        });

        return root;
    }

    private void downloadData() {
        //TODO поиск всех предметов у студента
        subjects = new ArrayList<>();
        subjects.add("Матан");
        subjects.add("Мобилки");
        subjects.add("Алгебра");
    }

    private void setPreferences(View root) {
        TextView username = root.findViewById(R.id.user_name_text);
        username.setText(studentName);

        TextView userGroup = root.findViewById(R.id.user_group_text);
        userGroup.setVisibility(View.VISIBLE);
        userGroup.setText(group);
    }
}