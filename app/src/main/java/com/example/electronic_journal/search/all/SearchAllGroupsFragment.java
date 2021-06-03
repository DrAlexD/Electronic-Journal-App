package com.example.electronic_journal.search.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.SubjectInfo;
import com.example.electronic_journal.search.GroupsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchAllGroupsFragment extends Fragment {
    private GroupsAdapter groupsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_all_groups, container, false);

        SearchAllGroupsViewModel searchAllGroupsViewModel = new ViewModelProvider(this).get(SearchAllGroupsViewModel.class);
        searchAllGroupsViewModel.downloadAllGroups();

        int actionCode = getArguments().getInt("actionCode");

        if (actionCode == 3) {
            root.findViewById(R.id.fromGroupTextView).setVisibility(View.VISIBLE);
        } else if (actionCode == 33) {
            root.findViewById(R.id.toGroupTextView).setVisibility(View.VISIBLE);
        }

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAllGroupsList);
        searchAllGroupsViewModel.getAllGroups().observe(getViewLifecycleOwner(), allGroups -> {
            if (allGroups != null) {
                List<Group> allGroups2 = new ArrayList<>();

                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    if (actionCode == 0) {
                        String studentName = getArguments().getString("studentName");
                        String studentSecondName = getArguments().getString("studentSecondName");
                        String studentLogin = getArguments().getString("studentLogin");
                        String studentPassword = getArguments().getString("studentPassword");

                        searchAllGroupsViewModel.downloadGroupById(groupsAdapter.getGroups().get(position).getId());
                        LiveData<Group> groupLiveData = searchAllGroupsViewModel.getGroup();
                        LiveData<Result<Boolean>> answerLiveData = Transformations.switchMap(groupLiveData, group -> {
                            searchAllGroupsViewModel.addStudent(studentName, studentSecondName, group, studentLogin, studentPassword);
                            return searchAllGroupsViewModel.getAnswerWithSuccess();
                        });

                        answerLiveData.observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                if (answer instanceof Result.Success) {
                                    Toast.makeText(getContext(), "Успешное добавление студента", Toast.LENGTH_SHORT).show();

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("openPage", actionCode);
                                    Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                                } else {
                                    String error = ((Result.Error) answer).getError();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else if (actionCode == 2) {
                        searchAllGroupsViewModel.deleteGroup(groupsAdapter.getGroups().get(position).getId());

                        searchAllGroupsViewModel.getAnswer().observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Toast.makeText(getContext(), "Успешное удаление группы", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", actionCode);
                                Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions, bundle);
                            }
                        });
                    } else if (actionCode == 3) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("actionCode", 33);
                        bundle.putLong("fromGroupId", groupsAdapter.getGroups().get(position).getId());
                        Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_search_all_groups, bundle);
                    } else if (actionCode == 33) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("actionCode", 3);
                        bundle.putLong("fromGroupId", getArguments().getLong("fromGroupId"));
                        bundle.putLong("toGroupId", groupsAdapter.getGroups().get(position).getId());
                        Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_search_check_students_in_group, bundle);
                    } else if (actionCode == 10 || actionCode == 11) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("professorId", getArguments().getLong("professorId"));
                        bundle.putLong("groupId", groupsAdapter.getGroups().get(position).getId());
                        bundle.putLong("subjectId", getArguments().getLong("subjectId"));
                        bundle.putLong("semesterId", getArguments().getLong("semesterId"));

                        Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_dialog_subject_info_adding, bundle);
                    }
                };

                if (actionCode == 10 || actionCode == 11) {
                    searchAllGroupsViewModel.downloadAvailableGroupsInSubject(getArguments().getLong("professorId"),
                            getArguments().getLong("subjectId"), getArguments().getLong("semesterId"));

                    searchAllGroupsViewModel.getAvailableGroupsInSubject().observe(getViewLifecycleOwner(), availableGroupsInSubject -> {
                        if (availableGroupsInSubject == null) {
                            allGroups2.addAll(allGroups);
                        } else {
                            List<Group> groups = new ArrayList<>();
                            for (SubjectInfo subjectInfo : availableGroupsInSubject) {
                                groups.add(subjectInfo.getGroup());
                            }

                            for (Group group1 : allGroups) {
                                boolean isOk = true;
                                for (Group group2 : groups) {
                                    if (group1.getId() == group2.getId()) {
                                        isOk = false;
                                        break;
                                    }
                                }
                                if (isOk)
                                    allGroups2.add(group1);
                            }
                        }

                        groupsAdapter = new GroupsAdapter(getContext(), allGroups2, onItemClickListener);
                        recyclerView.setAdapter(groupsAdapter);
                        recyclerView.setHasFixedSize(false);
                    });
                } else if (actionCode == 33) {
                    for (Group group : allGroups) {
                        if (group.getId() != getArguments().getLong("fromGroupId")) {
                            allGroups2.add(group);
                        }
                    }
                    groupsAdapter = new GroupsAdapter(getContext(), allGroups2, onItemClickListener);
                    recyclerView.setAdapter(groupsAdapter);
                    recyclerView.setHasFixedSize(false);
                } else {
                    allGroups2.addAll(allGroups);
                    groupsAdapter = new GroupsAdapter(getContext(), allGroups2, onItemClickListener);
                    recyclerView.setAdapter(groupsAdapter);
                    recyclerView.setHasFixedSize(false);
                }
            }
        });

        final SearchView searchView = root.findViewById(R.id.allGroupsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());

        return root;
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                groupsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}