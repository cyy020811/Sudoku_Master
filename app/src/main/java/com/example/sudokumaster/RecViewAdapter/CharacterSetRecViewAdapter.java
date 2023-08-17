package com.example.sudokumaster.RecViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudokumaster.R;
import com.example.sudokumaster.sudoku.CharacterSet;

import java.util.ArrayList;

public class CharacterSetRecViewAdapter extends RecyclerView.Adapter<CharacterSetRecViewAdapter.ViewHolder> {

    private final ArrayList<CharacterSet> characterSets = new ArrayList<>();
    private Context context;
    private Intent intent;

    public CharacterSetRecViewAdapter(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_set_list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(characterSets.get(position).getName());
        holder.parent.setOnClickListener(v -> {
            intent.putExtra("characterSet", characterSets.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return characterSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivPic;
        private RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivPic = itemView.findViewById(R.id.ivPic);
            parent = itemView.findViewById(R.id.parent);
        }

    }

    public void setCharacterSets() {
        for (CharacterSet characterSet: CharacterSet.values()) {
            characterSets.add(characterSet);
        }
        notifyDataSetChanged();
    }
}
