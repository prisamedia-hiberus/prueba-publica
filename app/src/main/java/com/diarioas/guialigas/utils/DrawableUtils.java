package com.diarioas.guialigas.utils;

import android.content.Context;

import com.diarioas.guialigas.BuildConfig;

public class DrawableUtils {

    private static final String SHIELD_PREV = "escudo_";

    public static int getDrawableId(Context ctx, String url,
                                    int numCharacterExtension) {
        int idLocal = 0;
        if (url != null && ctx != null)
            idLocal = ctx.getResources().getIdentifier(
                    url.substring(0, url.length() - numCharacterExtension),
                    "drawable", ctx.getPackageName());
        return idLocal;
    }

    public static int getDrawableShieldId(Context ctx, String url,
                                          int numCharacterExtension) {
        int idLocal = 0;
        String shieldPath = SHIELD_PREV + url.split("_")[1];
        if (shieldPath != null && ctx != null) {
            idLocal = ctx.getResources().getIdentifier(
                    shieldPath.substring(0, shieldPath.length() - numCharacterExtension),
                    "drawable", ctx.getPackageName());
        }
        return idLocal;
    }
}
