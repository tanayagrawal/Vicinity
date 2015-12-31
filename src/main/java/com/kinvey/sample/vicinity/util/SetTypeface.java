package com.kinvey.sample.vicinity.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Tanay Agrawal on 10/10/2015.
 */
public class SetTypeface {

    String lightPath = "fonts/Montserrat-Light.otf";
    String boldPath = "fonts/Montserrat-Bold.otf";
    String regularPath = "fonts/Montserrat-Regular.otf";
    String semiBoldPath = "fonts/Montserrat-SemiBold.otf";
    String blackPath = "fonts/Montserrat-Black.otf";

    Context context;
    public SetTypeface(Context context){
        this.context = context;
    }

    public Typeface getFont(String type) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(), regularPath);

        switch (type) {
            case "light":
                tf = Typeface.createFromAsset(context.getAssets(), lightPath);
                break;
            case "bold":
                tf = Typeface.createFromAsset(context.getAssets(), boldPath);
                break;
            case "regular":
                tf = Typeface.createFromAsset(context.getAssets(), regularPath);
                break;
            case "semiBold":
                tf = Typeface.createFromAsset(context.getAssets(), semiBoldPath);
                break;
            case "black":
                tf = Typeface.createFromAsset(context.getAssets(), blackPath);
                break;
        }
       return tf;
    }


}
