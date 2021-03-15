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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        setPreferences(root);

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.downloadAvailableSubjectsWithGroups();

        final ExpandableListView expandableListView = root.findViewById(R.id.subjectsWithGroupsList);
        profileViewModel.getAvailableSubjects().observe(getViewLifecycleOwner(), availableSubjects -> {
            if (availableSubjects == null) {
                return;
            }

            SubjectsWithGroupsAdapter subjectsWithGroupsAdapter = new SubjectsWithGroupsAdapter(getContext(), availableSubjects, profileViewModel.getAvailableSubjectsWithGroups().getValue());
            expandableListView.setAdapter(subjectsWithGroupsAdapter);
            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putString("subject", availableSubjects.get(groupPosition));
                bundle.putString("group", profileViewModel.getAvailableSubjectsWithGroups().getValue().get(availableSubjects.get(groupPosition)).get(childPosition));
                Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
                return true;
            });
        });

        profileViewModel.getAvailableSubjectsWithGroups().observe(getViewLifecycleOwner(), availableSubjectsWithGroups -> {
            if (availableSubjectsWithGroups == null) {
                return;
            }

            SubjectsWithGroupsAdapter subjectsWithGroupsAdapter = new SubjectsWithGroupsAdapter(getContext(), profileViewModel.getAvailableSubjects().getValue(), availableSubjectsWithGroups);
            expandableListView.setAdapter(subjectsWithGroupsAdapter);
            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putString("subject", profileViewModel.getAvailableSubjects().getValue().get(groupPosition));
                bundle.putString("group", availableSubjectsWithGroups.get(profileViewModel.getAvailableSubjects().getValue().get(groupPosition)).get(childPosition));
                Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
                return true;
            });
        });

        return root;
    }

    private void setPreferences(View root) {
        //TODO добавить отображение выбранного семестра в профиле преподавателя
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

        TextView userNameView = root.findViewById(R.id.user_name_text);
        userNameView.setText(Repository.getInstance().getUser().getFullName());

        Button addSubjectButton = root.findViewById(R.id.add_subject);
        addSubjectButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addSubjectButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("actionCode", 10);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_all_subjects, bundle);
        });

        Button addGroupButton = root.findViewById(R.id.add_group);
        addGroupButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addGroupButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("actionCode", 11);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
        });

        Button deleteSubjectButton = root.findViewById(R.id.delete_subject);
        deleteSubjectButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        deleteSubjectButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("actionCode", 12);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
        });

        Button deleteGroupButton = root.findViewById(R.id.delete_group);
        deleteGroupButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        deleteGroupButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("actionCode", 13);
            Navigation.findNavController(root).navigate(R.id.action_profile_to_search_available_subjects, bundle);
        });
    }
}