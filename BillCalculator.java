package com.config;

import android.content.Context;
import android.util.Log;

import com.helper.HelperMethods;

import java.util.List;

public class BillCalculator {

    static HelperMethods helperMethods = HelperMethods.getInstance();


    /**
     * <p>This function is to calculate bills</p>
     * @param unit - Units from a Bill
     * @param context - Context from Activity or Fragment
     */

    public static double calculateBill(int unit, Context context){

        List<SlabsData> slabsDataList =  helperMethods.getSlabsData(context);

        if(slabsDataList==null || slabsDataList.size()==0){
            return 0.0;
        }

        double billAmount = 0;

        try{

            if(unit>0 && unit <=100){
                billAmount = unit * slabsDataList.get(0).slabs_1_100;

            }

            else if(unit>100 && unit<=500){

                int first100 = unit - 100;

                billAmount = (100* slabsDataList.get(0).slabs_1_100)+ first100*slabsDataList.get(0).slabs_100_500;

            }

            else if(unit>500){

                int first100 = unit-100;

                int firstSlabCalculation = 100 * slabsDataList.get(0).slabs_1_100;

                int lastSlab = first100 - 400;

                billAmount = (lastSlab * slabsDataList.get(0).slabs_501)+
                        400*slabsDataList.get(0).slabs_100_500+
                        firstSlabCalculation;
            }
        }catch (Exception exp){
            Log.e("Bill Calc Exception",exp.getMessage());
        }
        return billAmount;
    }

    /**
     * <p>This function is to get bill division per unit</p>
     * @param unit - Units from a Bill
     * @param context - Context from Activity or Fragment
     */

    public static String getUnitDivision(int unit, Context context){

        String billDivision = "";
        List<SlabsData> slabsDataList =  helperMethods.getSlabsData(context);

        if(slabsDataList==null || slabsDataList.size()==0){
            return "";
        }

        try {
            if (unit > 0 && unit <= 100) {
                billDivision = "1-100 units at @ Rs. " + slabsDataList.get(0).slabs_1_100 + " x " + unit;
            } else if (unit > 100 && unit <= 500) {

                int first100 = unit - 100;

                billDivision = "1-100 units @ Rs. " + slabsDataList.get(0).slabs_1_100 + " x 100" + "\n\n101 – 500 units @ Rs. " + slabsDataList.get(0).slabs_100_500 + " x " + first100;

            } else if (unit > 500) {

                int first100 = unit - 100;

                int lastSlab = first100 - 400;

                billDivision = "1-100 units @ Rs. " + slabsDataList.get(0).slabs_1_100 + "x 100" + " \n\n101 – 500 units @ Rs. " +
                        slabsDataList.get(0).slabs_100_500 + " x 400" + "\n\n > 500 units @ Rs. " + slabsDataList.get(0).slabs_501 + " x " + lastSlab;

            }
        }catch (Exception exp){
            Log.e("Unit Division Exception",exp.getMessage());
        }
        return billDivision;
    }
}
