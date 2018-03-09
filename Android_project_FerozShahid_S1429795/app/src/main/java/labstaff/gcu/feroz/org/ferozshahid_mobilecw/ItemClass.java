//Feroz Shahid - S1429795
//object for item in XML
package labstaff.gcu.feroz.org.ferozshahid_mobilecw;

import java.io.Serializable;

public class ItemClass implements Serializable
{
    private String title;
    private String description;
    private  String location;
    private String pubDate;

    public ItemClass()
    {
        title = "";
        description = "";
        location = "";
        pubDate = "";
    }

    public ItemClass(String atitle, String adescription, String alocation, String apubDate)
    {
        title = atitle;
        description = adescription;
        location = alocation;
        pubDate = apubDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String atitle)
    {
        title = atitle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String adescription)
    {
        description = adescription;
    }

    public String getLocation()
    {
        return location;
    } //Feroz Shahid - S1429795
    //Feroz Shahid - S1429795
    public void setLocation(String alocation)
    {
        location = alocation;
    }

    public String getPubDate()
    {
        return pubDate;
    }

    public void setPubDate(String apubDate)
    {
        pubDate = apubDate;
    }

    public String toString()
    {

        String temp = title + "," + description + "," + location + "," + pubDate ;

        return temp;
    }

} // End of class
//Feroz Shahid - S1429795