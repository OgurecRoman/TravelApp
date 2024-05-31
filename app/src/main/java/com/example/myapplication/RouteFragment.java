package com.example.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;

import com.example.myapplication.adapters.CityAdapter;
import com.example.myapplication.data_base.DBHelper;

import java.util.Calendar;

public class RouteFragment extends Fragment {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ContentValues contentValues;
    private String[] list_month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля",
            "августа", "сентября", "октября", "ноября", "декабря"};

    public static RouteFragment newInstance() {
        RouteFragment fragment = new RouteFragment();
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
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        CalendarView calendar = view.findViewById(R.id.calendar);

        dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        RecyclerView recyclerView = view.findViewById(R.id.list);
        CityAdapter adapter = new CityAdapter(getActivity(), database, calendar);
        recyclerView.setAdapter(adapter);

        ImageButton but_del = view.findViewById(R.id.but_delete);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_CITIES, null, null);
                CityAdapter adapter = new CityAdapter(getActivity(), database, calendar);
                recyclerView.setAdapter(adapter);
            }
        });

        ImageButton but_add = view.findViewById(R.id.but_add);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put(DBHelper.KEY_CITY, "");
                contentValues.put(DBHelper.KEY_SET, 0);
                contentValues.put(DBHelper.KEY_DAY, Calendar.getInstance().getTime().getDate());
                contentValues.put(DBHelper.KEY_MONTH, list_month[Calendar.getInstance().getTime().getMonth()]);
                database.insert(DBHelper.TABLE_CITIES, null, contentValues);
                CityAdapter adapter = new CityAdapter(getActivity(), database, calendar);
                recyclerView.setAdapter(adapter);
            }
        });

        Button btn_next = view.findViewById(R.id.next);
        btn_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_routeFragment_to_weatherFragment));

        return view;
    }
}