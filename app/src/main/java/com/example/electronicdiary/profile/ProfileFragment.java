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

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private HashMap<String, ArrayList<String>> subjectsWithGroups;
    private ArrayList<String> subjects;
    private ArrayList<ArrayList<String>> groups;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        downloadData();

        setPreferences(root);

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.setSubjectsWithGroups(subjectsWithGroups);
        profileViewModel.setSubjects(subjects);
        profileViewModel.setGroups(groups);

        SubjectsWithGroupsAdapter subjectsWithGroupsAdapter = new SubjectsWithGroupsAdapter(getContext(), subjects, subjectsWithGroups);
        final ExpandableListView expandableListView = root.findViewById(R.id.subjectsWithGroupsList);
        expandableListView.setAdapter(subjectsWithGroupsAdapter);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("subject", subjects.get(groupPosition));
            bundle.putString("group", subjectsWithGroups.get(subjects.get(groupPosition)).get(childPosition));
            Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
            return true;
        });

        return root;
    }

    private void setPreferences(View root) {
        //TODO добавить отображение выбранного семестра в профиле преподавателя
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = sharedPreferences.getString("username", "");
        boolean isAdminRules = sharedPreferences.getBoolean(getString(R.string.is_admin_rules), false);

        TextView usernameView = root.findViewById(R.id.user_name_text);
        usernameView.setText(username);

        Button addSubjectButton = root.findViewById(R.id.add_subject);
        addSubjectButton.setVisibility(isAdminRules ? View.VISIBLE : View.GONE);
        addSubjectButton.setOnClickListener(view -> {

        });

        Button addGroupButton = root.findViewById(R.id.add_group);
        addGroupButton.setVisibility(isAdminRules ? View.VISIBLE : View.GONE);
        addGroupButton.setOnClickListener(view -> {

        });
    }

    private void downloadData() {
        //TODO поиск предметов и групп у преподавателя
        subjectsWithGroups = new HashMap<>();

        subjects = new ArrayList<>();
        subjects.add("Матан");
        subjects.add("Мобилки");
        subjects.add("Алгебра");

        groups = new ArrayList<>();
        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("ИУ9-11");
        groups1.add("ИУ9-21");
        groups1.add("ИУ9-31");
        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("ИУ9-41");
        groups2.add("ИУ9-51");
        groups2.add("ИУ9-61");
        ArrayList<String> groups3 = new ArrayList<>();
        groups3.add("ИУ9-11");
        groups3.add("ИУ9-31");
        groups3.add("ИУ9-51");
        groups.add(groups1);
        groups.add(groups2);
        groups.add(groups3);

        for (int i = 0; i < subjects.size(); i++) {
            subjectsWithGroups.put(subjects.get(i), groups.get(i));
        }
    }
}