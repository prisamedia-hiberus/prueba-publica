package com.prisadigital.realmedia.adlib.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AdUnit implements Parcelable, Cloneable {
    public static final int AD_FORMAT_NONE     = -1;
    public static final int AD_FORMAT_BASIC    = 0;
    public static final int AD_FORMAT_EXPANDED = 1;

    // Formats Arrays
    private AdFormat        formats[];

    public AdUnit() {
        // Initialize Unit
        initialize();
    }

    private void initialize() {
        // Prepare Array
        formats = new AdFormat[2];
    }

    public AdFormat getAdFormat(int type) {
        return formats[type];
    }

    public void setAdFormat(AdFormat adFormat, int type) {
        // Save Format
        formats[type] = adFormat;
    }

    public boolean isAdFormat(int type) {
        return formats[type] != null;
    }

    public AdFormat[] getAdFormats() {
        return formats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for(AdFormat af : formats) {
            if (af == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeString(af.getMedio() != null ? af.getMedio() : "");
                dest.writeInt(af.getEffect());
                dest.writeInt(af.getType());
                dest.writeString(af.getUrl() != null ? af.getUrl() : "");
                dest.writeString(af.getClickUrl() != null ? af.getClickUrl() : "");
                dest.writeInt(af.getPixels().length);
                for(String p : af.getPixels()) {
                    dest.writeString(p != null ? p : "");
                }
                dest.writeInt(af.getDuration());
                dest.writeInt(af.getWidth());
                dest.writeInt(af.getHeight());
                dest.writeInt(af.isAutomute() ? 1 : 0);
                dest.writeInt(af.isAutoplay() ? 1 : 0);
            }
        }
    }
    
    public static final Parcelable.Creator<AdUnit> CREATOR = new Parcelable.Creator<AdUnit>() {

        @Override
        public AdUnit createFromParcel(Parcel source) {
            AdUnit adUnit = new AdUnit();

            for(int i = 0; i < 2; i++) {
                // Read isValid
                boolean isValid = source.readInt() == 1;
                
                if (isValid) {
                    // Create AdFormat
                    AdFormat af = new AdFormat();
                    
                    String medio = source.readString();
                    int effect = source.readInt();
                    int type = source.readInt();
                    String url = source.readString();
                    String clickUrl = source.readString();
                    int numPixels = source.readInt();

                    for(int j = 0; j < numPixels; j++) {
                        // Save pUrl
                        String pUrl = source.readString();
                        
                        if (!pUrl.equals("")) {
                            af.setPixelAt(pUrl, j);
                        }
                    }
                    
                    int duration = source.readInt();
                    int width = source.readInt();
                    int height = source.readInt();
                    boolean automute = source.readInt() == 1;
                    boolean autoplay = source.readInt() == 1;
                    
                    // Set Params
                    af.setMedio(medio.equals("") ? null : medio);
                    af.setEffect(effect);
                    af.setType(type);
                    af.setUrl(url.equals("") ? null : url);
                    af.setClickUrl(clickUrl.equals("") ? null : clickUrl);
                    af.setDuration(duration);
                    af.setWidth(width);
                    af.setHeight(height);
                    af.setAutomute(automute);
                    af.setAutoplay(autoplay);

                    // Save AdFormat
                    adUnit.setAdFormat(af, i);
                }
            }
            
            return adUnit;
        }

        @Override
        public AdUnit[] newArray(int size) {
            throw new UnsupportedOperationException();
        }
    };
    
    @Override
    public AdUnit clone() {
        AdUnit obj = null;
        try{
            obj=(AdUnit) super.clone();
            obj.formats = this.formats.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
}
