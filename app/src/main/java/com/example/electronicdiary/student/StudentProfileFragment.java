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

import com.example.electronicdiary.R;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_profile, container, false);

        //TODO поиск всех предметов у студента
        ArrayList<String> subjects = new ArrayList<>();
        subjects.add("Матан");
        subjects.add("Мобилки");
        subjects.add("Алгебра");

        String studentName = getArguments().getString("student");
        String studentGroup = getArguments().getString("group");

        final ListView listView = root.findViewById(R.id.studentSubjectsList);
        ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_subject, R.id.subjectTitle, subjects);
        listView.setAdapter(subjectsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            /*Bundle bundle = new Bundle();
            bundle.putString("subject", subjects.get(position));
            bundle.putString("student", studentName);
            bundle.putString("group", studentGroup);
            Navigation.findNavController(view).navigate(R.id.action_student_profile_to_student_performance, bundle);*/
        });

        TextView userName = root.findViewById(R.id.user_name_text);
        userName.setText(studentName);
        TextView userGroup = root.findViewById(R.id.user_group_text);
        userGroup.setVisibility(View.VISIBLE);
        userGroup.setText(studentGroup);

        return root;
    }
}