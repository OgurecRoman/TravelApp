package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.recycle_list.Elem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Elem> states;

    ItemAdapter(Context context, List<Elem> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        Elem state = states.get(position);
        holder.but_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                states.remove(position);
                ItemAdapter.this.notifyDataSetChanged();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.getOn()) {
                    state.setOn(false);
                    holder.editText.setPaintFlags(holder.editText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                else {
                    state.setOn(true);
                    holder.editText.setPaintFlags(holder.editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                state.setName(holder.editText.getText().toString());
            }
        });

        holder.editText.setText(state.getName());
        holder.checkBox.setChecked(state.getOn());
        if (state.getOn())
            holder.editText.setPaintFlags(holder.editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.editText.setPaintFlags(holder.editText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EditText editText;
        final CheckBox checkBox;
        final ImageButton but_remove;
        ViewHolder(View view){
            super(view);
            editText = view.findViewById(R.id.edittext);
            checkBox = view.findViewById(R.id.checkBox);
            but_remove = view.findViewById(R.id.but_remove);
        }
    }
}
