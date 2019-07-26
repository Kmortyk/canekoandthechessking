package com.kmortyk.canekoandthechessking.scores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.database.Scores;

import java.util.List;

// TODO remove class =333
public class ScoreViewAdapter extends RecyclerView.Adapter<ScoreViewAdapter.ViewHolder> {

    private List<Scores.ScoreEntry> data;
    private LayoutInflater inflater;

    public ScoreViewAdapter(Context context, List<Scores.ScoreEntry> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.score_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Scores.ScoreEntry entry = data.get(i);
        viewHolder.mapName.setText("Map: \"" + entry.mapName + "\"");
        viewHolder.time.setText("Time: " + (entry.time / 1000.0f) + " sec.");
        viewHolder.steps.setText("Steps: " + entry.steps);
    }

    @Override
    public int getItemCount() { return data.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mapName, time, steps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mapName = itemView.findViewById(R.id.mapName);
            time = itemView.findViewById(R.id.timeView);
            steps = itemView.findViewById(R.id.steps);
        }

    }

}
