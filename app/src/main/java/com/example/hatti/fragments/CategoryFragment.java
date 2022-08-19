package com.example.hatti.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hatti.R;
import com.example.hatti.adapter.CategoryFragmentAdapter;
import com.example.hatti.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<CategoryModel> list;
    private CategoryFragmentAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.rvCategoryFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();


        //List 1
        List<String> nestedList1 = new ArrayList<>();
        nestedList1.add("Alist1");
        nestedList1.add("Alist2");
        nestedList1.add("Alist3");
        nestedList1.add("Alist4");
        nestedList1.add("Alist5");
        nestedList1.add("Alist6");
        //List 2
        List<String> nestedList2 = new ArrayList<>();
        nestedList2.add("ist1");
        nestedList2.add("ist2");
        nestedList2.add("ist3");
        nestedList2.add("ist4");
        nestedList2.add("ist5");
        nestedList2.add("ist6");
        //List 3
        List<String> nestedList3 = new ArrayList<>();
        nestedList3.add("Alist1");
        nestedList3.add("Alist2");
        nestedList3.add("Alist3");
        nestedList3.add("Alist4");
        nestedList3.add("Alist5");
        nestedList3.add("Alist6");
        //List 4
        List<String> nestedList4 = new ArrayList<>();
        nestedList4.add("Alist1");
        nestedList4.add("Alist2");
        nestedList4.add("Alist3");
        nestedList4.add("Alist4");
        nestedList4.add("Alist5");
        nestedList4.add("Alist6");

        list.add(new CategoryModel(nestedList1,"food1"));
        list.add(new CategoryModel(nestedList2,"food2"));
        list.add(new CategoryModel(nestedList3,"food3"));
        list.add(new CategoryModel(nestedList4,"food4"));

        adapter = new CategoryFragmentAdapter(list);
        recyclerView.setAdapter(adapter);

        return view;
    }
}