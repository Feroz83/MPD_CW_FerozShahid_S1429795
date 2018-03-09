//Feroz Shahid - S1429795
package labstaff.gcu.feroz.org.ferozshahid_mobilecw;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class selectedItem extends FragmentActivity implements OnMapReadyCallback {

    private TextView titleTxt, dateTxt, descTxt;
    private Double latitude, longitude;
    private Button backBtn;
    private GoogleMap mMap;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.selectedMap); //Feroz Shahid - S1429795
        mapFragment.getMapAsync(this);


        titleTxt = (TextView) findViewById(R.id.selectedItemTitle);
        dateTxt = (TextView) findViewById(R.id.selectedItemDate);
        descTxt = (TextView) findViewById(R.id.selectedItemDesc);
        backBtn = (Button) findViewById(R.id.selectedBackBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        //gets the object extra intent from the other class
        Intent i = getIntent();
        ItemClass item = (ItemClass) i.getSerializableExtra("key");


        //gets information from the object
        title = item.getTitle(); //Feroz Shahid - S1429795
        String description =item.getDescription();
        String location = item.getLocation();
        String date = item.getPubDate();


        //splits the location as the lat and long is in one long file
        //the space is a regex, this is extracted
        String[] getLatLon = location.split(" ");

        //sets the lat and long from the extracted string
        latitude = Double.parseDouble(getLatLon[0]); //Feroz Shahid - S1429795
        longitude = Double.parseDouble(getLatLon[1]);
        //Feroz Shahid - S1429795
        //replaces the tag <br /> with a new line
        description = description.replace("<br />","\n\n");

        //sets the text views in the selectedItem view
        titleTxt.setText(title);
        dateTxt.setText(date);
        descTxt.setText(description);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in the location that the selected item is in
        //moves the camera and zooms in close to that part of the map
        LatLng markerLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(markerLocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 16));
    }

} //Feroz Shahid - S1429795
