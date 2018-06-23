package com.example.suketurastogi.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredients;
    private Context context;

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.ingredientName.setText(ingredients.get(position).getIngredient());
        holder.quantity.setText(ingredients.get(position).getQuantity() + " " + ingredients.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName, quantity;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name_text_view);
            quantity = (TextView) itemView.findViewById(R.id.quantity_text_view);
        }
    }
}
