//Feroz Shahid - S1429795
package labstaff.gcu.feroz.org.ferozshahid_mobilecw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button roadWorksBtn = (Button) findViewById(R.id.currentIncidentsBtn);
//Feroz Shahid - S1429795
        //Button when user clicks 'start' takes user to the getInformation activity
        roadWorksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Feroz Shahid - S1429795
                Intent intent = new Intent(MainActivity.this, getInformation.class);
                startActivity(intent);
            }                                                    // the button pressed was Road Works
        });
    }
}
