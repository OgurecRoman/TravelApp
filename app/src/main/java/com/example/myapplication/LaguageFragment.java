package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.recycle_list.Elem;

import java.util.ArrayList;

public class LaguageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public LaguageFragment() {
    }

    public static LaguageFragment newInstance(String param1, String param2) {
        LaguageFragment fragment = new LaguageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laguage, container, false);

        ArrayList<Elem> items = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.list);
        ItemAdapter adapter = new ItemAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        Button but_next = view.findViewById(R.id.next);
        but_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_laguageFragment_to_TODOFragment));

        ImageButton but_del = view.findViewById(R.id.butDelete);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                ItemAdapter adapter = new ItemAdapter(getActivity(), items);
                recyclerView.setAdapter(adapter);
            }
        });

        ImageButton but_add = view.findViewById(R.id.butAdd);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Elem());
                ItemAdapter adapter = new ItemAdapter(getActivity(), items);
                recyclerView.setAdapter(adapter);
            }
        });
        return view;
    }
}