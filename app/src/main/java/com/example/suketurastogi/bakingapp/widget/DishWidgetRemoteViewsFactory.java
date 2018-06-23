package com.example.suketurastogi.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.suketurastogi.bakingapp.R;
import com.example.suketurastogi.bakingapp.model.Dish;
import com.example.suketurastogi.bakingapp.model.Ingredient;
import com.example.suketurastogi.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.suketurastogi.bakingapp.ui.MainActivity.dishes;

public class DishWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;

    public DishWidgetRemoteViewsFactory(Context mContext) {
        context = mContext;
    }

    @Override
    public void onCreate() {
        loadDishes();
    }

    @Override
    public void onDataSetChanged() {
        loadDishes();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(dishes == null) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.empty_widget);
            return remoteViews;
        }

        position = context.getSharedPreferences("dish_details", MODE_PRIVATE).getInt("dish_id", 0);
        Dish dish = dishes.get(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dish_widget_list_item);
        remoteViews.setTextViewText(R.id.dish_name_text_view, dish.getName());

        String servingsString = "Servings : " + dishes.get(position).getServings();
        remoteViews.setTextViewText(R.id.servings_count_text_view, servingsString);

        for (int i = 0; i < dish.getIngredients().size(); i++) {
            RemoteViews ingItem = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_item);
            ingItem.setTextViewText(R.id.ingredient_name_text_view, dish.getIngredients().get(i).getIngredient());
            ingItem.setTextViewText(R.id.quantity_text_view,
                    dish.getIngredients().get(i).getQuantity() + " " + dish.getIngredients().get(i).getMeasure());

            remoteViews.addView(R.id.ingredients_items_list, ingItem);
        }

        Intent intent = new Intent();
        intent.putExtra("item", position);
        remoteViews.setOnClickFillInIntent(R.id.ingredients_items_list, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void loadDishes() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                context.getString(R.string.json_url),
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
                        } catch (JSONException e) {
                            // timber log statement
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(arrayRequest);
    }
}
