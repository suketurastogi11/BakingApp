package com.example.suketurastogi.bakingapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.adapter.DishesAdapter;
import com.example.suketurastogi.bakingapp.model.Dish;
import com.example.suketurastogi.bakingapp.model.Ingredient;
import com.example.suketurastogi.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DishesAdapter.DishItemClickListener {

    private ProgressBar progressBar;

    private RecyclerView dishesList;
    private DishesAdapter dishesAdapter;
    private GridLayoutManager layoutManager;

    public static ArrayList<Dish> dishes;
    public static int DISH_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        dishesList = (RecyclerView) findViewById(R.id.dishes_list);

        layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.dish_columns));
        dishesList.setLayoutManager(layoutManager);

        dishesAdapter = new DishesAdapter(this);
        dishesList.setAdapter(dishesAdapter);

        if (dishes != null) {
            updateDishes();
        } else if (savedInstanceState == null) {
            if(isOnline()) {
                loadDishes();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error")
                        .setMessage("Please check your internet connnection.")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isOnline()) {
                                    loadDishes();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        } else {
            dishes = savedInstanceState.getParcelableArrayList("dishes");
            updateDishes();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(int id) {

        DISH_ID = id-1;

        Intent intent = new Intent(this, DishActivity.class);
        startActivity(intent);
    }

    public void loadDishes() {

        showProgressbar();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                getString(R.string.json_url),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            int dishCount = response.length();
                            dishes = new ArrayList<>(dishCount);

                            for (int i = 0; i < dishCount; i++) {

                                JSONObject dishObject = response.getJSONObject(i);
                                int id = dishObject.getInt("id");
                                String name = dishObject.getString("name");
                                int servings = dishObject.getInt("servings");
                                String image = dishObject.getString("image");

                                JSONArray ingredientsArray = dishObject.getJSONArray("ingredients");
                                int ingCount = ingredientsArray.length();
                                ArrayList<Ingredient> ingredients = new ArrayList<>(ingCount);

                                for (int j = 0; j < ingCount; j++) {
                                    JSONObject ingObject = ingredientsArray.getJSONObject(j);

                                    double quantity = ingObject.getDouble("quantity");
                                    String measure = ingObject.getString("measure");
                                    String ingredient = ingObject.getString("ingredient");

                                    ingredients.add(new Ingredient(quantity, measure, ingredient));
                                }

                                JSONArray stepsArray = dishObject.getJSONArray("steps");
                                int stepCount = stepsArray.length();
                                ArrayList<Step> steps = new ArrayList<>(stepCount);

                                for (int j = 0; j < stepCount; j++) {
                                    JSONObject stepObject = stepsArray.getJSONObject(j);

                                    int stepId = stepObject.getInt("id");
                                    String shortDescription = stepObject.getString("shortDescription");
                                    String description = stepObject.getString("description");
                                    String videoURL = stepObject.getString("videoURL");
                                    String thumbnailURL = stepObject.getString("thumbnailURL");

                                    steps.add(new Step(stepId, shortDescription, description, videoURL, thumbnailURL));
                                }

                                dishes.add(new Dish(id, name, servings, ingredients, steps, image));
                            }

                            updateDishes();
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(arrayRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("dishes", dishes);
    }

    public void updateDishes() {
        dishesAdapter.setDishes(dishes);
        dishesAdapter.notifyDataSetChanged();

        showDishes();
    }

    public void showDishes() {
        dishesList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
        dishesList.setVisibility(View.INVISIBLE);
    }
}
