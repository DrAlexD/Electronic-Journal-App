package com.example.electronicdiary.student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Subject;

public class StudentProfileFragment extends Fragment {
    private int studentId;
    private int semesterId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_profile, container, false);

        if (getArguments() != null) {
            studentId = getArguments().getInt("studentId");
            semesterId = getArguments().getInt("semesterId");
        } else {
            studentId = Repository.getInstance().getUser().getId();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            semesterId = Integer.parseInt(sharedPreferences.getString(getString(R.string.current_semester), ""));
        }

        StudentProfileViewModel studentProfileViewModel = new ViewModelProvider(this).get(StudentProfileViewModel.class);
        studentProfileViewModel.downloadStudentById(studentId);
        studentProfileViewModel.downloadSemesterById(semesterId);
        studentProfileViewModel.downloadAvailableStudentSubjects(studentId, semesterId);

        studentProfileViewModel.getSemester().observe(getViewLifecycleOwner(), semester -> {
            if (semester == null) {
                return;
            }

            TextView semesterView = root.findViewById(R.id.semester_text);
            semesterView.setText(semester.toString());
        });

        studentProfileViewModel.getStudent().observe(getViewLifecycleOwner(), student -> {
            if (student == null) {
                return;
            }

            TextView username = root.findViewById(R.id.user_name_text);
            username.setText(student.getFullName());

            studentProfileViewModel.downloadGroupById(student.getGroupId());
        });

        studentProfileViewModel.getGroup().observe(getViewLifecycleOwner(), group -> {
            if (group == null) {
                return;
            }

            TextView userGroup = root.findViewById(R.id.user_group_text);
            userGroup.setVisibility(View.VISIBLE);
            userGroup.setText(group.getTitle());
        });

        final ListView listView = root.findViewById(R.id.studentSubjectsList);
        studentProfileViewModel.getAvailableStudentSubjects().observe(getViewLifecycleOwner(), availableStudentSubjects -> {
            if (availableStudentSubjects == null) {
                return;
            }

            ArrayAdapter<Subject> subjectsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_subject_with_group, R.id.subjectTitle, availableStudentSubjects);
            listView.setAdapter(subjectsAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Bundle bundle = new Bundle();
                bundle.putInt("openPage", 0);
                bundle.putInt("studentId", studentId);
                bundle.putInt("subjectId", availableStudentSubjects.get(position).getId());
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_student_profile_to_student_performance, bundle);
            });
        });
        return root;
    }
}