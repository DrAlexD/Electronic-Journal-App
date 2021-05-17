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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.Subject;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileFragment extends Fragment {
    private long studentId;
    private long semesterId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_profile, container, false);

        if (getArguments() != null) {
            studentId = getArguments().getLong("studentId");
        } else {
            studentId = Repository.getInstance().getUser().getId();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        semesterId = Long.parseLong(sharedPreferences.getString(getString(R.string.current_semester), "-1"));

        StudentProfileViewModel studentProfileViewModel = new ViewModelProvider(this).get(StudentProfileViewModel.class);
        studentProfileViewModel.downloadStudentById(studentId);
        studentProfileViewModel.downloadSemesterById(semesterId);
        studentProfileViewModel.downloadAvailableStudentSubjects(studentId, semesterId);

        studentProfileViewModel.getSemester().observe(getViewLifecycleOwner(), semester -> {
            if (semester != null) {
                TextView semesterView = root.findViewById(R.id.semester_text);
                semesterView.setText(semester.toString());
            }
        });

        LiveData<Result<Student>> studentLiveData = studentProfileViewModel.getStudent();
        LiveData<Group> groupLiveData =
                Transformations.switchMap(studentLiveData, student -> {
                    if (student instanceof Result.Success) {
                        Student studentData = ((Result.Success<Student>) student).getData();

                        TextView username = root.findViewById(R.id.user_name_text);
                        username.setText(studentData.getFullName());

                        studentProfileViewModel.downloadGroupById(studentData.getGroup().getId());

                        return studentProfileViewModel.getGroup();
                    } else
                        return null;
                });

        groupLiveData.observe(getViewLifecycleOwner(), group -> {
            if (group != null) {
                TextView userGroup = root.findViewById(R.id.user_group_text);
                userGroup.setVisibility(View.VISIBLE);
                userGroup.setText(group.getTitle());
            }
        });

        final ListView listView = root.findViewById(R.id.studentSubjectsList);
        studentProfileViewModel.getAvailableStudentSubjects().observe(getViewLifecycleOwner(), availableStudentSubjects -> {
            if (availableStudentSubjects != null) {
                List<Subject> subjects = new ArrayList<>();
                for (StudentPerformanceInSubject availableStudentSubject : availableStudentSubjects) {
                    subjects.add(availableStudentSubject.getSubjectInfo().getSubject());
                }

                ArrayAdapter<Subject> subjectsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_subject_with_group,
                        R.id.subjectTitle, subjects);
                listView.setAdapter(subjectsAdapter);
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 0);
                    bundle.putLong("studentPerformanceInSubjectId", availableStudentSubjects.get(position).getId());
                    Navigation.findNavController(view).navigate(R.id.action_student_profile_to_student_performance, bundle);
                });
            }
        });
        return root;
    }
}