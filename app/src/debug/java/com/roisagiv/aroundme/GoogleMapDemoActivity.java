package com.roisagiv.aroundme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.SupportMapFragment;

public class GoogleMapDemoActivity extends AppCompatActivity {

  private SupportMapFragment mapFragment;

  public SupportMapFragment getMapFragment() {
    return mapFragment;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_google_map_demo);
    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
  }
}
