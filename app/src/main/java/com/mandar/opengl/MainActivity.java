package com.mandar.opengl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView m_Surface,mGLView;
    public MainActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_Surface = new GLSurfaceView(this);



        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;



        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            m_Surface.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
            m_Surface.setRenderer(new GLRenderer(this));
        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }



//        try
//        {
//            ImageView iv = (ImageView)findViewById(R.id.imageView);
//
//            // get input stream
//            InputStream ims = getAssets().open("opengl.png");
//            // load image as Drawable
//            Drawable d = Drawable.createFromStream(ims, null);
//            // set image to ImageView
//            iv.setImageDrawable(d);
//            ims .close();
//        }
//        catch(IOException ex)
//        {
//            return;
//        }


        setContentView(m_Surface);

        //setContentView(R.layout.activity_main);

//        float mmInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 20,
//                getResources().getDisplayMetrics());
//        ViewGroup.LayoutParams layoutParams=m_Surface.getLayoutParams();
//        layoutParams.width=(int)mmInPx;
//        layoutParams.height=(int)mmInPx;
//        m_Surface.setLayoutParams(layoutParams);
    }


    @Override
    protected void onPause() {
        super.onPause();
        m_Surface.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_Surface.onResume();
    }
}