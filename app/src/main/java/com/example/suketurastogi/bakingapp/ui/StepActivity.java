package com.example.suketurastogi.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.fragment.StepDetailFragment;
import com.example.suketurastogi.bakingapp.model.Dish;
import com.example.suketurastogi.bakingapp.model.Step;

public class StepActivity extends AppCompatActivity {

    public static final String TAG = StepActivity.class.getSimpleName();

    public static Dish dish;
    public static Step step;
    public static int STEP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            dish = DishActivity.dish;
            STEP_ID = DishActivity.STEP_ID;
        } else {
            dish = savedInstanceState.getParcelable("dish");
            STEP_ID = savedInstanceState.getInt("id");
        }

        step = dish.getSteps().get(STEP_ID);

        setContentView(R.layout.activity_step);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setData(dish, STEP_ID, false);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("dish", dish);
        outState.putInt("id", STEP_ID);
    }

    public void previousStep(View view) {
        STEP_ID = STEP_ID - 1;
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setData(dish, STEP_ID, false);
        stepDetailFragment.playerPosition = 0;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    public void nextStep(View view) {
        STEP_ID = STEP_ID + 1;
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setData(dish, STEP_ID, false);
        stepDetailFragment.playerPosition = 0;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
