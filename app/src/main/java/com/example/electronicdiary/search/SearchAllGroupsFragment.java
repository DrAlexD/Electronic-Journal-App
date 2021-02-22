package com.example.electronicdiary.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import java.util.ArrayList;

public class SearchAllGroupsFragment extends Fragment {
    private GroupsAdapter groupsAdapter;
    private ArrayList<String> groups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_all_groups, container, false);

        downloadData();

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            /*int position = viewHolder.getAdapterPosition();

            Bundle bundle = new Bundle();
            bundle.putString("student", getArguments().getString("student"));
            bundle.putString("group", groups.get(position));*/

            //TODO добавление студента в базу

            Navigation.findNavController(view).navigate(R.id.action_search_all_groups_to_admin_actions);
        };
        groupsAdapter = new GroupsAdapter(getContext(), groups, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAllGroupsList);
        recyclerView.setAdapter(groupsAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.allGroupsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        //TODO поиск всех студентов
        groups = new ArrayList<>();
        groups.add("1ИУ9-11");
        groups.add("2ИУ9-21");
        groups.add("3ИУ9-31");
        groups.add("4ИУ9-41");
        groups.add("5ИУ9-51");
        groups.add("6ИУ9-61");
        groups.add("7ИУ9-71");
        groups.add("8ИУ9-81");
        groups.add("9ИУ9-91");
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