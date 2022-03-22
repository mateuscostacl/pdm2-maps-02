// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.example.pdm2_maps_02;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnCameraIdleListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final float ZOOM_DELTA = 2.0f;
    private static final float DEFAULT_MIN_ZOOM = 2.0f;
    private static final float DEFAULT_MAX_ZOOM = 22.0f;

    private static final LatLng IFTO_LATLONG = new LatLng(-10.199402, -48.311813);
    private static final LatLng CASA_LATLONG = new LatLng(-10.167728, -48.348274);

    private static final LatLngBounds IFTO = new LatLngBounds(
            new LatLng(-10.200599, -48.314066), new LatLng(-10.198287, -48.309560));
    private static final CameraPosition IFTO_CAMERA = new CameraPosition.Builder()
            .target(IFTO_LATLONG).zoom(16.0f).bearing(0).tilt(0).build();

    private static final LatLngBounds CASA = new LatLngBounds(
            new LatLng(-10.168689, -48.349947), new LatLng(-10.166587, -48.347104));
    private static final CameraPosition CASA_CAMERA = new CameraPosition.Builder()
            .target(CASA_LATLONG).zoom(16.0f).bearing(0).tilt(0).build();

    private GoogleMap mMap;

    /**
     * Internal min zoom level that can be toggled via the demo.
     */
    private float mMinZoom;

    /**
     * Internal max zoom level that can be toggled via the demo.
     */
    private float mMaxZoom;

    private TextView mCameraTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        mMap = null;
        resetMinMaxZoom();

//        mCameraTextView = (TextView) findViewById(R.id.camera_text);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.setOnCameraIdleListener(this);


        LatLng palmas = new LatLng(-10.184446, -48.333762);
        mMap.addMarker(new MarkerOptions().position(palmas).title("Praça dos Girassóis em Palmas Tocantins")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(palmas));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(palmas, 10));
    }

    @Override
    public void onCameraIdle() {
//        mCameraTextView.setText(mMap.getCameraPosition().toString());
    }

    /**
     * Before the map is ready many calls will fail.
     * This should be called on all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "map_not_ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void resetMinMaxZoom() {
        mMinZoom = DEFAULT_MIN_ZOOM;
        mMaxZoom = DEFAULT_MAX_ZOOM;
    }

    /**
     * Click handler for clamping to Adelaide button.
     * @param view
     */
    public void irParaIfto(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.setLatLngBoundsForCameraTarget(IFTO);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(IFTO_CAMERA));
        mMap.addMarker(new MarkerOptions().position(IFTO_LATLONG).title("IFTO")).showInfoWindow();
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ifto));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ifto, 16));
    }

    /**
     * Click handler for clamping to Pacific button.
     * @param view
     */
    public void irParaMinhaCasa(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.setLatLngBoundsForCameraTarget(CASA);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CASA_CAMERA));
        mMap.addMarker(new MarkerOptions().position(CASA_LATLONG).title("Minha casa")).showInfoWindow();
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(casa));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casa, 16));
    }

    public void onLatLngClampReset(View view) {
        if (!checkReady()) {
            return;
        }
        // Setting bounds to null removes any previously set bounds.
        mMap.setLatLngBoundsForCameraTarget(null);
        toast("LatLngBounds clamp reset.");
    }

    public void onSetMinZoomClamp(View view) {
        if (!checkReady()) {
            return;
        }
        mMinZoom += ZOOM_DELTA;
        // Constrains the minimum zoom level.
        mMap.setMinZoomPreference(mMinZoom);
        toast("Min zoom preference set to: " + mMinZoom);
    }

    public void onSetMaxZoomClamp(View view) {
        if (!checkReady()) {
            return;
        }
        mMaxZoom -= ZOOM_DELTA;
        // Constrains the maximum zoom level.
        mMap.setMaxZoomPreference(mMaxZoom);
        toast("Max zoom preference set to: " + mMaxZoom);
    }

    public void onMinMaxZoomClampReset(View view) {
        if (!checkReady()) {
            return;
        }
        resetMinMaxZoom();
        mMap.resetMinMaxZoomPreference();
        toast("Min/Max zoom preferences reset.");
    }
}