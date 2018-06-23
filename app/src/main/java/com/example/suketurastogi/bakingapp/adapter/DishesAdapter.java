package com.example.suketurastogi.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.model.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {

    private ArrayList<Dish> dishes;
    private Context context;

    private final DishItemClickListener mClickListener;

    public interface DishItemClickListener {
        void onClick(int id);
    }

    public DishesAdapter(DishItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.dish_list_item, parent, false);

        return new DishViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        holder.dishName.setText(dishes.get(position).getName());

        String servingsString = "Servings: " + Integer.toString(dishes.get(position).getServings());
        holder.servingsCount.setText(servingsString);

        if (!TextUtils.isEmpty(dishes.get(position).getImage())) {
            Picasso.with(context)
                    .load(dishes.get(position).getImage())
                    .placeholder(R.drawable.serve)
                    .into(holder.dishImage);
        }
    }

    @Override
    public int getItemCount() {
        if (dishes == null) return 0;
        return dishes.size();
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    class DishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView dishName, servingsCount;
        ImageView dishImage;

        public DishViewHolder(View itemView) {
            super(itemView);
            dishName = (TextView) itemView.findViewById(R.id.dish_name_text_view);
            servingsCount = (TextView) itemView.findViewById(R.id.servings_count_text_view);
            dishImage = (ImageView) itemView.findViewById(R.id.dish_image_view);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int id = dishes.get(adapterPosition).getId();
            mClickListener.onClick(id);
        }
    }
}
