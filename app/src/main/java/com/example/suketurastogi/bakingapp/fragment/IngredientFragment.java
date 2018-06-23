package com.example.suketurastogi.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.adapter.IngredientsAdapter;
import com.example.suketurastogi.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {

    private ArrayList<Ingredient> ingredients;

    public IngredientFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);

        RecyclerView ingredientsList = (RecyclerView) rootView.findViewById(R.id.ingredients_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientsList.setLayoutManager(layoutManager);

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.setIngredients(ingredients);
        ingredientsList.setAdapter(ingredientsAdapter);

        return rootView;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
