package com.arjava.filmjadwal.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 *
 * Created by arjava on 12/26/17.
 */

public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
