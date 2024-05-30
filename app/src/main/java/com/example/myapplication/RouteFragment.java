package com.example.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.adapters.CityAdapter;
import com.example.myapplication.adapters.ItemAdapter;
import com.example.myapplication.data_base.DBHelper;

import java.util.Calendar;

public class RouteFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String date = "";
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ContentValues contentValues;
    private String[] list_month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля",
            "августа", "сентября", "октября", "ноября", "декабря"};

    public static RouteFragment newInstance(String param1) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        RecyclerView recyclerView = view.findViewById(R.id.list);
        CityAdapter adapter = new CityAdapter(getActivity(), database);
        recyclerView.setAdapter(adapter);

        ImageButton but_del = view.findViewById(R.id.but_delete);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_CITIES, null, null);
                CityAdapter adapter = new CityAdapter(getActivity(), database);
                recyclerView.setAdapter(adapter);
            }
        });

        ImageButton but_add = view.findViewById(R.id.but_add);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues.put(DBHelper.KEY_CITY, "");
                contentValues.put(DBHelper.KEY_DAY, Calendar.getInstance().getTime().getDate());
                contentValues.put(DBHelper.KEY_MONTH, list_month[Calendar.getInstance().getTime().getMonth()]);
                database.insert(DBHelper.TABLE_CITIES, null, contentValues);
                CityAdapter adapter = new CityAdapter(getActivity(), database);
                recyclerView.setAdapter(adapter);
            }
        });

        Button btn_next = view.findViewById(R.id.next);
        CalendarView calendar = view.findViewById(R.id.calendar);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //bundle.putString("city", editText.getText().toString());
                bundle.putString("date", date);
                Navigation.findNavController(view).navigate(R.id.action_routeFragment_to_weatherFragment, bundle);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String elm = "", eld = "";
                if (month < 10) elm = "0";
                if (dayOfMonth < 10) eld = "0";
                String selectedDate = new StringBuilder().append(year).append("-")
                        .append(elm).append(month + 1).append("-").append(eld).append(dayOfMonth).toString();
                date = selectedDate;
            }
        });

        return view;
    }
}