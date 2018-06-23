package com.example.suketurastogi.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.model.Step;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{

    private ArrayList<Step> steps;
    private Context context;

    private final StepItemClickListener mClickListener;

    public interface StepItemClickListener {
        void onClick(int id);
    }

    public StepsAdapter(StepItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.step_list_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.shortDescription.setText(steps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView shortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);

            shortDescription = (TextView) itemView.findViewById(R.id.short_description_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            int id = steps.get(adapterPosition).getId();
            int id = adapterPosition;
            mClickListener.onClick(id);
        }
    }
}
