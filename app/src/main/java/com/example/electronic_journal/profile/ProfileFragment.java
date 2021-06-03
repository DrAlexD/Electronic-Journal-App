package com.example.electronic_journal.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Subject;
import com.example.electronic_journal.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private long semesterId;
    private long professorId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        semesterId = Long.parseLong(sharedPreferences.getString(getString(R.string.current_semester), "-1"));
        professorId = Repository.getInstance().getUser().getId();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.downloadSemesterById(semesterId);

        setPreferences(root, sharedPreferences);

        final ExpandableListView expandableListView = root.findViewById(R.id.subjectsWithGroupsList);

        profileViewModel.downloadAvailableSubjects(professorId, semesterId);
        profileViewModel.downloadAvailableSubjectsWithGroups(professorId, semesterId);
        LiveData<List<Subject>> availableSubjectsLiveData = profileViewModel.getAvailableSubjects();
        LiveData<Map<String, List<SubjectInfo>>> availableSubjectsWithGroupsLiveData =
                Transformations.switchMap(availableSubjectsLiveData, g -> profileViewModel.getAvailableSubjectsWithGroups());

        availableSubjectsWithGroupsLiveData.observe(getViewLifecycleOwner(), availableSubjectsWithGroups -> {
            if (availableSubjectsWithGroups != null) {
                List<Subject> availableSubjects = profileViewModel.getAvailableSubjects().getValue();
                if (availableSubjects != null) {
                    SubjectsWithGroupsAdapter subjectsWithGroupsAdapter = new SubjectsWithGroupsAdapter(getContext(),
                            availableSubjects,
                            availableSubjectsWithGroups);
                    expandableListView.setAdapter(subjectsWithGroupsAdapter);
                    expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong("subjectInfoId", availableSubjectsWithGroups.get(String.valueOf(availableSubjects.get(groupPosition).getId())).get(childPosition).getId());
                        Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
                        return true;
                    });
                }
            }
        });

        return root;
    }

    private void setPreferences(View root, SharedPreferences sharedPreferences) {
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

        profileViewModel.getSemester().observe(getViewLifecycleOwner(), semester -> {
            if (semester != null) {
                TextView semesterView = root.findViewById(R.id.semester_text);
                semesterView.setText(semester.toString());
            }
        });

        TextView userNameView = root.findViewById(R.id.user_name_text);
        userNameView.setText(Repository.getInstance().getUser().getFullName());

        Button addSubjectButton = root.findViewById(R.id.add_subject);
        addSubjectButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addSubjectButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("professorId", professorId);
            bundle.putLong("semesterId", semesterId);
            bundle.putInt("actionCode", 10);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_all_subjects, bundle);
        });

        Button deleteSubjectButton = root.findViewById(R.id.delete_subject);
        deleteSubjectButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        deleteSubjectButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("professorId", professorId);
            bundle.putLong("semesterId", semesterId);
            bundle.putInt("actionCode", 12);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
        });

        Button addGroupButton = root.findViewById(R.id.add_group);
        addGroupButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addGroupButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("professorId", professorId);
            bundle.putLong("semesterId", semesterId);
            bundle.putInt("actionCode", 11);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
        });

        Button deleteGroupButton = root.findViewById(R.id.delete_group);
        deleteGroupButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        deleteGroupButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("professorId", professorId);
            bundle.putLong("semesterId", semesterId);
            bundle.putInt("actionCode", 13);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
        });
    }
}