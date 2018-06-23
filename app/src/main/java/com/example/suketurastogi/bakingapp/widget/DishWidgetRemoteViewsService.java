package com.example.suketurastogi.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class DishWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DishWidgetRemoteViewsFactory(this.getApplicationContext());
    }
}
