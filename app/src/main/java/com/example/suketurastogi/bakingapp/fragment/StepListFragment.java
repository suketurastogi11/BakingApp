package com.example.suketurastogi.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.adapter.StepsAdapter;
import com.example.suketurastogi.bakingapp.model.Dish;
import com.example.suketurastogi.bakingapp.ui.DishActivity;

public class StepListFragment extends Fragment implements StepsAdapter.StepItemClickListener{

    private Dish dish;

    private OnIngredientClickListener mIngredientCallback;

    @Override
    public void onClick(int id) {
        mStepCallback.onStepSelected(id);
    }

    public interface OnIngredientClickListener {
        void onIngredientsSelected();
    }

    OnStepClickListener mStepCallback;

    public interface OnStepClickListener {
        void onStepSelected(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mIngredientCallback = (OnIngredientClickListener) context;
            mStepCallback = (OnStepClickListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement click listeners");
        }
    }

    public StepListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        dish = DishActivity.dish;

        CardView ingredientsCardView = (CardView) rootView.findViewById(R.id.ingredients_cardview);
        ingredientsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIngredientCallback.onIngredientsSelected();
            }
        });

        RecyclerView stepsList = (RecyclerView) rootView.findViewById(R.id.steps_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        stepsList.setLayoutManager(layoutManager);

        StepsAdapter stepsAdapter = new StepsAdapter(this);
        stepsAdapter.setSteps(dish.getSteps());
        stepsList.setAdapter(stepsAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
