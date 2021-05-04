package com.example.electronicdiary.profile;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;

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
        profileViewModel.downloadAvailableSubjectsWithGroups(professorId, semesterId);

        setPreferences(root, sharedPreferences);

        final ExpandableListView expandableListView = root.findViewById(R.id.subjectsWithGroupsList);
        profileViewModel.getAvailableSubjectsWithGroups().observe(getViewLifecycleOwner(), availableSubjectsWithGroups -> {
            if (availableSubjectsWithGroups == null) {
                return;
            }

            SubjectsWithGroupsAdapter subjectsWithGroupsAdapter = new SubjectsWithGroupsAdapter(getContext(), profileViewModel.getAvailableSubjects().getValue(), availableSubjectsWithGroups);
            expandableListView.setAdapter(subjectsWithGroupsAdapter);
            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putLong("groupId", availableSubjectsWithGroups.get(profileViewModel.getAvailableSubjects().getValue().get(groupPosition)).get(childPosition).getGroup().getId());
                bundle.putLong("subjectId", profileViewModel.getAvailableSubjects().getValue().get(groupPosition).getId());
                bundle.putLong("lecturerId", availableSubjectsWithGroups.get(profileViewModel.getAvailableSubjects().getValue().get(groupPosition)).get(childPosition).getLecturerId());
                bundle.putLong("seminarianId", availableSubjectsWithGroups.get(profileViewModel.getAvailableSubjects().getValue().get(groupPosition)).get(childPosition).getSeminarianId());
                bundle.putLong("semesterId", semesterId);
                Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
                return true;
            });
        });

        return root;
    }

    private void setPreferences(View root, SharedPreferences sharedPreferences) {
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

        profileViewModel.getSemester().observe(getViewLifecycleOwner(), semester -> {
            if (semester == null) {
                return;
            }

            TextView semesterView = root.findViewById(R.id.semester_text);
            semesterView.setText(semester.toString());
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

        Button addGroupButton = root.findViewById(R.id.add_group);
        addGroupButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addGroupButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("professorId", professorId);
            bundle.putLong("semesterId", semesterId);
            bundle.putInt("actionCode", 11);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
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