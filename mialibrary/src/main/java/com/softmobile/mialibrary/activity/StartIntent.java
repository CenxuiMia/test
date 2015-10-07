package com.softmobile.mialibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by miahuang on 2015/10/1.
 */
public class StartIntent {
    public static void openActivity(Activity activity,Class<?extends Activity> activityClass) {
        Intent intent = new Intent(activity,activityClass);
        activity.startActivity(intent);
    }

    public static void openActivity(Activity activity,Class<?extends Activity> activityClass,Bundle bundle) {
        Intent intent = new Intent(activity,activityClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static Bundle getActivityIntentBundle(Activity activity) {
        return activity.getIntent().getExtras();
    }

    public static void openWebURI(Context context,String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    public static void openTelephone(Context context,String phone) {
        Intent myIntentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        context.startActivity(myIntentDial);
    }
}
