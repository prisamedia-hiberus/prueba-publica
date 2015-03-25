package com.prisadigital.realmedia.adlib.data;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;

public class AdFormat implements Cloneable {
    private static final int  MAX_PIXELS                 = 50;

    public static final int   TYPE_IMAGE                 = 0;
    public static final int   TYPE_GIF                   = 1;
    public static final int   TYPE_VIDEO                 = 2;
    public static final int   TYPE_HTML5                 = 3;
    public static final int   TYPE_PREROLL               = 4;
    public static final int   TYPE_XML                   = 5;

    public static final int   EFFECT_NONE                = 10;
    public static final int   EFFECT_EXPAND_CLICK        = 20;
    public static final int   EFFECT_EXPAND_DRAG_UP      = 30;
    public static final int   EFFECT_EXPAND_DRAG_DOWN    = 31;
    public static final int   EFFECT_FULLSCREEN          = 40;
    public static final int   EFFECT_FULLSCREEN_FROM_TOP = 50;

    private String            medio;
    private int               effect;
    private int               type;
    private String            url;
    private String            clickUrl;
    private String            pixels[];
    private int               duration;
    private int               width;
    private int               height;
    private boolean           automute;
    private boolean           autoplay;
    private boolean           openInNavigator;
    private Bitmap            image;
    private AnimationDrawable gifAnimation;

    public AdFormat() {
        pixels = new String[10];
        width = -1;
        height = -1;
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getPixel(int position) {
        if (position < 0 || position >= pixels.length)
            return "";

        return pixels[position];
    }

    public void addPixel(String pixel) {
        // Search an empty slot and store the pixel
        for (int i = 0; i < MAX_PIXELS; i++) {
            if (pixels[i] == null) {
                pixels[i] = pixel;
                break;
            }
        }
    }
    
    public void setPixelAt(String pixel, int position) {
        pixels[position] = pixel;
    }
    
    public String[] getPixels() {
        return pixels;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public AnimationDrawable getMovie() {
        return gifAnimation;
    }

    public void setAnimationDrawable(AnimationDrawable gifAnimation) {
        this.gifAnimation = gifAnimation;
    }

    public boolean isAnimationDrawable() {
        return gifAnimation != null && image == null;
    }

    public boolean isAutomute() {
        return automute;
    }

    public void setAutomute(boolean automute) {
        this.automute = automute;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }
    public boolean isOpenInNavigator() {
        return openInNavigator;
    }
    public void setOpenInNavigator(boolean openInNavigator) {
        this.openInNavigator = openInNavigator;
    }
    
    @Override
    public AdFormat clone() {
        AdFormat obj = null;
        try{
            obj=(AdFormat) super.clone();
            
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
}
