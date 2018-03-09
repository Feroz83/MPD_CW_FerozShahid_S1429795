//Feroz Shahid - S1429795
package labstaff.gcu.feroz.org.ferozshahid_mobilecw;

/**
 * Created by Feroz on 22/02/2018.
 */


/**
 * Created by Feroz on 22/02/2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ItemAdapter extends ArrayAdapter<ItemClass>{

    List<ItemClass> listItem;

    public ItemAdapter(Context context, int resource, List<ItemClass> objects) {
        super(context, resource, objects);

        listItem = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        ItemClass currentItem = listItem.get(position);

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //sets the textviews of the content from the XML
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentItem.getTitle());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentItem.getPubDate());


        //checks first if there is a <br /> tag as this always has a
        //start date and end date
        if(currentItem.getDescription().contains("<br />")) {

            //splits the decsription to extract the start and end date
            String[] extractDate = currentItem.getDescription().split("<br />");


            String startDate = extractDate[0].replace("Start Date: ", "");
            String enddate = extractDate[1].replace("End Date: ", "");

            //gets days between the start and finish
            long daysBetween = getDaysBetween(startDate, enddate);


            //changes the background colour of the list depending on the
            //length of time a roadwork is in place
            if (daysBetween >= 30) {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.red, null));
            } else if (daysBetween >= 20) { //Feroz Shahid - S1429795
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.orange, null));
            } else if (daysBetween >= 15) {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.amber, null));
            } else if (daysBetween >= 7) {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.yellow, null));
            } else if (daysBetween >= 3) {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.darkGreen, null));
            } else if (daysBetween < 3) {
                listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.green, null));
            } //Feroz Shahid - S1429795

            dateTextView.setText("Duration: " + Long.toString(daysBetween) + " days");

        }

        return listItemView;
    }

    //method to calculate the days between two dates
    private long getDaysBetween(String startDate, String enddate){

        //sets format of date to the same format as the XML
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Date convertedStartDate = new Date();
        Date convertedEndDate = new Date(); //Feroz Shahid - S1429795
        try {
            convertedStartDate = dateFormat.parse(startDate);
            convertedEndDate = dateFormat.parse(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //calculates the difference
        long diff = convertedEndDate.getTime() - convertedStartDate.getTime();
        return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }
} //Feroz Shahid - S1429795

