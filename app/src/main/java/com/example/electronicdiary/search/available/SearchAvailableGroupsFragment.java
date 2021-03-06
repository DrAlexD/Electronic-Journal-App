package com.example.electronicdiary.search.available;

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
import com.example.electronicdiary.search.GroupsAdapter;

import java.util.ArrayList;

public class SearchAvailableGroupsFragment extends Fragment {
    private GroupsAdapter groupsAdapter;
    private ArrayList<String> groups;
    private int actionCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_available_groups, container, false);

        actionCode = getArguments().getInt("actionCode");

        downloadData();

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            if (actionCode == 13) {
                //TODO удалить группу из базы
                /*getArguments().getString("subjectTitle")*/
                Navigation.findNavController(view).navigate(R.id.action_search_available_groups_to_profile);
            }
        };

        groupsAdapter = new GroupsAdapter(getContext(), groups, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAvailableGroupsList);
        recyclerView.setAdapter(groupsAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.availableGroupsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        if (actionCode == 13) {
            //TODO поиск доступных групп у определенного предмета
            groups = new ArrayList<>();
            groups.add("1ИУ9-11");
            groups.add("2ИУ9-21");
            /*getArguments().getString("subjectTitle");*/
        } else {
            //TODO поиск доступных групп
            groups = new ArrayList<>();
            groups.add("1ИУ9-11");
            groups.add("2ИУ9-21");
            groups.add("3ИУ9-31");
            groups.add("4ИУ9-41");
        }
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