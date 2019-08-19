package com.helper;

import android.content.Context;

import com.config.ConsumerData;
import com.config.SlabsData;
import com.db.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.sspp.library.SweetAlertDialog;

public class HelperMethods {

    private static final HelperMethods ourInstance = new HelperMethods();

    public static HelperMethods getInstance() {
        return ourInstance;
    }

    /**
     * <p>This function get the current date and return it as a String.</p>
     */
    public String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date date = new Date();
        String currentDateandTime =  sdf.format(date);
        return currentDateandTime;
    }

    /**
     * <p>This function saves the data for slabs in DB.</p>
     *
     * @param serviceNumber - service number of consumer
     * @param units - units from bill
     * @param cost - calculated cost as per units
     * @param context - context of Activity or Fragment
     *
     */
    public void saveConsumerDataInDataBase(Context context, String serviceNumber, String units, String cost){
        DatabaseHandler db = new DatabaseHandler(context);
        db.addConsumer(new ConsumerData(serviceNumber, units, cost, getCurrentDate()));
    }

    /**
     * <p>This function saves the data for slabs in DB.</p>
     *
     * @param slabOne - slab from 1 to 100
     * @param slabTwo - slab from 100 to 500
     * @param slabThree - slab from 500 to onwards
     * @param context - context of Activity or Fragment
     *
     */
    public void saveSlabsInDataBase(Context context, int slabOne, int slabTwo, int slabThree){
        DatabaseHandler db = new DatabaseHandler(context);
        db.addSLABS(new SlabsData(slabOne, slabTwo , slabThree));
    }

    /**
     * <p>This function get the data for slabs from DB and returns a list of slabs.</p>
     *
     * @param context - context of Activity or Fragment
     *
     */
    public List<SlabsData> getSlabsData(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        List<SlabsData> slabsDataList =  db.getSelectedSlabData();
        return slabsDataList;
    }

    /**
     * <p>This function display Pop up message.</p>
     *
     * @param message - required message to display
     * @param alertType - success or error type
     * @param context - context of Activity or Fragment
     *
     */
    public void messagePopUp(String message, String alertType, Context context){

        if(alertType.equalsIgnoreCase("success")){
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Dear User!")
                    .setContentText(message)
                    .show();
        }else{
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Dear User!")
                    .setContentText(message)
                    .show();
        }
    }

}