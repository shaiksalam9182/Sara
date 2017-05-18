package com.example.raj.sara;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Gestures extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener{

    GestureLibrary mlibrary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);

        mlibrary = GestureLibraries.fromRawResource(this,R.raw.gesture);
        if (!mlibrary.load()){
            finish();
        }

        GestureOverlayView gestures = (GestureOverlayView)findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);
    }


    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = mlibrary.recognize(gesture);
        if (predictions.size()>0 && predictions.get(0).score>1.0){
            for (int i=0;i<predictions.size();i++){
                String result = predictions.get(i).name;
                Toast.makeText(Gestures.this,result,Toast.LENGTH_LONG).show();
            }

        }
    }
}
