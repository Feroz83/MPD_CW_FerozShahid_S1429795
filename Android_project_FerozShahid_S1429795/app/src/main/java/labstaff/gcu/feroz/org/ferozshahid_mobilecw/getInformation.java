//Feroz Shahid - S1429795
package labstaff.gcu.feroz.org.ferozshahid_mobilecw;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class getInformation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    private ProgressDialog progressDialog;
    private String selectedUrl=null;
    private String currentIncidents ="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String plannedRoadWorksUrl="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String result = "";
    private Button backBtn, searchBtn, currentInBtn, plannedRdBtn;
    private ListView listView;
    private Boolean today;
    private TextView noDatatxt, titleName;
    private EditText searchbox;

    private String selectedDate;

    private List<ItemClass> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_information);

        noDatatxt = (TextView) findViewById(R.id.txtNoDataFound);
        titleName = (TextView) findViewById(R.id.titleName);
        searchbox = (EditText) findViewById(R.id.searchbox);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        currentInBtn = (Button) findViewById(R.id.selectDateBtn);

        //textview for message if there is no data found hidden at the start
        //searchbox and search button hidden at start as no date would of been selected
        noDatatxt.setVisibility(View.GONE);
        searchbox.setVisibility(View.GONE);
        searchBtn.setVisibility(View.GONE);
        plannedRdBtn = (Button) findViewById(R.id.selectRss);

        //progress dialog created to show the information is loading
        progressDialog = new ProgressDialog(getInformation.this);
//Feroz Shahid - S1429795

        plannedRdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clears old list after a new date was selected
                if(itemList != null){
                    itemList.clear();
                    result = ""; //clears the result
                    listView.setAdapter(itemAdapter);
                    searchbox.setText(null); //clears the searchbox
                }

                titleName.setText("Current Incidents");
                selectedUrl = currentIncidents; //sets the url to current incidents
                today = true;
                progressDialog.setMessage("Getting Current Incidents ...");
                progressDialog.show(); //starts the progress dialog
                // call method to run thread;
                startThread();


            }
        });

        //button for search
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Searching ...");
                progressDialog.show(); //starts the progress dialog

                //clears the list
                if(itemList != null){
                    itemList.clear();
                    result = ""; //clears the result
                    listView.setAdapter(itemAdapter);
                }


                // call method to run thread;
                startThread();
            }
        });


        //listView with custom adapter
        listView = (ListView) findViewById(R.id.list_view);
            listView.setAdapter(itemAdapter);

        //OnClick listner for item in list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //gets the selected item in the list by getting
                //the item at the position that the user clicked on in the list
               ItemClass itemSelected = itemAdapter.getItem(position);

               //intent to take the user to the selected item activity
                //sends the object to the class using putExtra
                //so it can be retrieved to display on a seperate activity
                //Feroz Shahid - S1429795
                Intent intent = new Intent(getInformation.this, selectedItem.class);
                intent.putExtra("key",itemSelected);
                startActivity(intent);

            }
        });


        //Button to call showDatePicker method
        //so the user can select a specific date
        currentInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          showDatePicker(); // starts the method to show the date picker dialog
          noDatatxt.setVisibility(View.GONE);

            }
        });

        //back button to allow user to go back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    } // End of onCreate

    //method to show the date picker (shows as a dialog style)
    public void showDatePicker(){

        //gets instance of calander
        Calendar calender = Calendar.getInstance();
        int startYear = calender.get(Calendar.YEAR);
        int startMonth = calender.get(Calendar.MONTH);
        int startDay = calender.get(Calendar.DAY_OF_MONTH);

        //creates the date picker and focuses it on the calander instance that was creates
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, getInformation.this, startYear, startMonth, startDay);

        datePickerDialog.show();

    }

    //method to retrieve selected date from the date picker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        //clears old list after a new date was selected
        if(itemList != null){
            itemList.clear();
            result = ""; //clears the result
            listView.setAdapter(itemAdapter);
            searchbox.setText(null); //clears the searchbox
        }

        //creates a new calander instance
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, dayOfMonth);

        //formats the date so that it mathes the format from the XML
        //Feroz Shahid - S1429795
        String dayOfTheWeek = (String) DateFormat.format("EEE", cal);
        String day          = (String) DateFormat.format("dd",   cal);
        String monthString  = (String) DateFormat.format("MMM",  cal);

        //format would be somthing like ( Fri, 02 Mar 2018 00:00:00 GMT )
        selectedDate = dayOfTheWeek+ ", " + day
                + " " + monthString + " " + year;


            titleName.setText("Planned Roadworks"); //sets title to planned roadworks
            selectedUrl = plannedRoadWorksUrl; //sets the url to planned roadworks
            today = false;
            progressDialog.setMessage("Getting Planned Roadworks ...");


        progressDialog.show(); //starts the progress dialog

        // call method to run thread;
        startThread();

    }

    //method that starts the thread
    //checks for internet coonectivity first
    //then runs network access on a separate thread;
    private void startThread(){
        //first checks if there is an internet connection before proceeding
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // internet connection available

            //runs thread
            new Thread(new Task(selectedUrl)).start();

        } else { //no internet connection available

            //creates alert dialog to let user know there is no internet available to retrieve the data
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getInformation.this);
            alertDialog.setMessage("No internet connection was detected!\n\n Check connection and try again.");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)
                {
                }
            });
            progressDialog.hide();
            alertDialog.show();


        }
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";



            try
            {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));


                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //

            getInformation.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    LinkedList<ItemClass> alist = null;
                  //  Log.d("UI thread", "I am the UI thread");
                    //Feroz Shahid - S1429795
                    //Make call to parsing code
                    //stores the parsed data to a list
                    alist = parseData(result);
                }
            });
        }
    }

    private LinkedList<ItemClass> parseData(String dataToParse)
    {
        itemList = new ArrayList<>();
        ItemClass widget = new ItemClass();
        LinkedList <ItemClass> alist  = new LinkedList<ItemClass>();
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","Item Start Tag found");
//Feroz Shahid - S1429795
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag","Title is " + temp);
                        widget.setTitle(temp);
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag","description is " + temp);
                           widget.setDescription(temp);
                        }
                            else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("point"))
                                { //Feroz Shahid - S1429795
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("LOCATION","location is " + temp);
                                    widget.setLocation(temp);
                                }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("pubDate"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag","pubDate is " + temp);
                                widget.setPubDate(temp);
                            }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {

                    //initialises an item adapter
                    itemAdapter = new ItemAdapter(this, 0, itemList);

                    if (xpp.getName().equalsIgnoreCase("item"))
                    {

                        //keeps everything in lowercase so the search
                        //is not case sensitive
                       String searchboxValue = searchbox.getText().toString().toLowerCase();

                        //if date selected was today i.e if current incidents selected get all items in xml
                        if(today) {

                            if (searchboxValue.isEmpty()) {
                                itemList.add(new ItemClass(widget.getTitle(), widget.getDescription(), widget.getLocation(), widget.getPubDate()));
                                listView.setAdapter(itemAdapter);
                            } else if (!searchboxValue.isEmpty() && widget.getTitle().toLowerCase().contains(searchboxValue)) {
                                itemList.add(new ItemClass(widget.getTitle(), widget.getDescription(), widget.getLocation(), widget.getPubDate()));
                                listView.setAdapter(itemAdapter);
                            } //Feroz Shahid - S1429795
                            //if date selected was not today i.e is planned roadworks gets items in list if the date matches the selected date by the user
                        } else if(!today && widget.getPubDate().startsWith(selectedDate)){
                            if (searchboxValue.isEmpty()) {
                                itemList.add(new ItemClass(widget.getTitle(), widget.getDescription(), widget.getLocation(), widget.getPubDate()));
                                listView.setAdapter(itemAdapter);
                            } else if (!searchboxValue.isEmpty() && widget.getTitle().toLowerCase().contains(searchboxValue)) {
                                itemList.add(new ItemClass(widget.getTitle(), widget.getDescription(), widget.getLocation(), widget.getPubDate()));
                                listView.setAdapter(itemAdapter);
                            }
                        }
                        progressDialog.hide(); //hides the progress dialog



                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = alist.size();
                        Log.e("MyTag","channel size is " + size);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            //return alist;
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

//Feroz Shahid - S1429795
        //hides keyboard once the data has been searched
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchbox.getWindowToken(), 0);

        //checks if the adapter is empty
        //this means nothing was found for the date selected
        if(itemAdapter.getCount() == 0){
            //shows the textview that displays
            //text indicating nothing was found for the date selected
            //hides the search bar and button
            noDatatxt.setVisibility(View.VISIBLE);
            searchbox.setVisibility(View.GONE);
            searchBtn.setVisibility(View.GONE);
            progressDialog.hide();
        } else {
            //if there was data found hides the message
            //and shows the searchbar and button to show an additional search option
            searchbox.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.VISIBLE);
            noDatatxt.setVisibility(View.GONE);
        }

        return alist;

    }
//Feroz Shahid - S1429795
}
