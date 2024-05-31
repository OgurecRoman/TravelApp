package com.example.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.adapters.DoingAdapter;
import com.example.myapplication.data_base.DBHelper;
import com.example.myapplication.recycle_list.Elem;

import java.util.ArrayList;

public class TODOFragment extends Fragment {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ContentValues contentValues;

    public static TODOFragment newInstance() {
        TODOFragment fragment = new TODOFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_o_d_o, container, false);

        dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        RecyclerView recyclerView = view.findViewById(R.id.list);
        DoingAdapter adapter = new DoingAdapter(getActivity(), database);
        recyclerView.setAdapter(adapter);

        Button but_next = view.findViewById(R.id.back);
        but_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_TODOFragment_to_laguageFragment));

        ImageButton but_del = view.findViewById(R.id.butDelete);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_DOING, null, null);
                DoingAdapter adapter = new DoingAdapter(getActivity(), database);
                recyclerView.setAdapter(adapter);
            }
        });

        ImageButton but_add = view.findViewById(R.id.butAdd);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put(DBHelper.KEY_NAME, "");
                contentValues.put(DBHelper.KEY_SET, 0);
                database.insert(DBHelper.TABLE_DOING, null, contentValues);
                DoingAdapter adapter = new DoingAdapter(getActivity(), database);
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }
}