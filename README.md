# Veripark-Test-Assignment
Veripark assignment for Bill Calculation

# Veripark E-Bill Calculator
- This application is to calculate Bill as per Units. It also saves the history of recent Inquiries of bill as per User Service Number. You can also dynamically change the **Slabs** for the Unit, it will change the calculation accordingly

## Requirement for the assignment

The requirements are as follows:
1)	An input screen contains:
a.	Current meter reading.
b.	Service Number of the customer (10 digits alpha numeric).
2)	The application should do the following
a.	Record current date and reading values on the device
b.	İf previous reading was recorded for the same customer before get and display the last reading and calculate only the difference ( means we need to store history of previous bills )
c.	Calculate the cost
3)	You should develop your own algorithm for the cost calculation
4)	The slabs should be configurable 
5)	There should be a user interface for the application to enter the required values and get the result.

## Configuration and code snippets

```Java
    public void addSLABS(SlabsData slabsData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SLABS, null,null);
        ContentValues values = new ContentValues();
        values.put(KEY_SLABS_1_100, slabsData.getSlabs_1_100());
        values.put(KEY_SLABS_100_500, slabsData.getSlabs_100_500());
        values.put(KEY_501, slabsData.getSlabs_501());
        db.insert(TABLE_SLABS, null, values);
        db.close();
    }
    
    public List<SlabsData> getSelectedSlabData() {
        List<SlabsData> slabDataList = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT * FROM " + TABLE_SLABS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SlabsData consumerData = new SlabsData();
                consumerData.setID(Integer.parseInt(cursor.getString(0)));
                consumerData.setSlabs_1_100(cursor.getInt(1));
                consumerData.setSlabs_100_500(cursor.getInt(2));
                consumerData.setSlabs_501(cursor.getInt(3));
                slabDataList.add(consumerData);
            } while (cursor.moveToNext());
        }
        return slabDataList;
    }
```

In **DataBaseHandler** class, I am using following code to save Consumer Data History in Data base.

```Java
public void addConsumer(ConsumerData consumerData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVICE_NUMBER, consumerData.getServiceNumber()); // Service Number
        values.put(KEY_METER_READING, consumerData.getMeterReading()); // Meter Reading
        values.put(KEY_COST, consumerData.getCost()); // Meter Reading
        values.put(KEY_DATE_INQUIRY, consumerData.getDateTime()); // Date Time For Inquiry
        db.insert(TABLE_CONSUMERS, null, values);
        db.close(); // Closing database connection
    }

    public List<ConsumerData> getSelectedConsumerData(String serviceNumber) {

        List<ConsumerData> consumerDataList = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT * FROM " + TABLE_CONSUMERS + " WHERE service_number == "+ serviceNumber+" ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ConsumerData consumerData = new ConsumerData();
                consumerData.setID(Integer.parseInt(cursor.getString(0)));
                consumerData.setServiceNumber(cursor.getString(1));
                consumerData.setMeterReading(cursor.getString(2));
                consumerData.setCost(cursor.getString(3));
                consumerData.setDateTime(cursor.getString(4));
                consumerDataList.add(consumerData);
            } while (cursor.moveToNext());
        }
        // return consumer data list
        return consumerDataList;
    }
```

In **HelperMethods** class, I am using following code to **Calculate Bill** as per **Slabs** from Date base.
```Java
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

            if(first100>500){
                int lastSlab = first100 - 400;

                billAmount = (lastSlab * slabsDataList.get(0).slabs_501)+
                                400*slabsDataList.get(0).slabs_100_500+
                        firstSlabCalculation;
            }
        }
        }catch (Exception exp){
        Log.e("Bill Calc Exception",exp.getMessage());
        }
        return billAmount;
    }
```

In **HelperMethods** class, I am using following code to **Show Unit Division** as per **Slabs** from Date base for the bill.

```Java
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

                if (first100 > 500) {
                    int lastSlab = first100 - 400;

                    billDivision = "1-100 units @ Rs. " + slabsDataList.get(0).slabs_1_100 + "x 100" + " \n\n101 – 500 units @ Rs. " +
                            slabsDataList.get(0).slabs_100_500 + " x 400" + "\n\n > 500 units @ Rs. " + slabsDataList.get(0).slabs_501 + " x " + lastSlab;
                }
            }
        }catch (Exception exp){
            Log.e("Unit Division Exception",exp.getMessage());
        }
        return billDivision;
    }
```

In **SlabsActivity** I am saving Slab 1, Slab 2 & Slab 3 input as per Unit formula. Following code I am using for Submit button listener:

```Java
private inner class submitButtonListener : View.OnClickListener {
        override fun onClick(view: View) {

            if (edt_first_slab.text.toString().isEmpty() || edt_second_slab.text.toString().isEmpty() || edt_third_slab.toString().isEmpty()) {
                helperMethods.messagePopUp(getString(R.string.please_input_all_values),"error",this@SlabsActivity)
                return
            }
            helperMethods.saveSlabsInDataBase(this@SlabsActivity , edt_first_slab.text.toString().toInt(), edt_second_slab.text.toString().toInt(), edt_third_slab.text.toString().toInt())

            val intent = MainActivity.newIntent(this@SlabsActivity)
            startActivity(intent)
        }
    }
```

In **MainActivity** I am using following code in Calucate & History button listeners. From these listeners I am calling database functions to save and get information.

```Java

private inner class CalculateBtnListener : View.OnClickListener {
        override fun onClick(view: View) {

            if (edt_units.text.toString().isEmpty() || edt_service_num.text.toString().isEmpty()) {
                helperMethods.messagePopUp(getString(R.string.please_extract_valid_input),"error",this@MainActivity)
                return
            }

            var units = Integer.parseInt(edt_units.text.toString())
            val serviceNumber = edt_service_num.text.toString()

            val billAmount = BillCalculator.calculateBill(units, this@MainActivity)
            val unitDivision = BillCalculator.getUnitDivision(units, this@MainActivity)

            txtCalculatedAmount.text = "Total Cost: Rs. "+java.lang.Double.toString(billAmount)
            txtBillDivision.text = unitDivision
            helperMethods.saveConsumerDataInDataBase(this@MainActivity, serviceNumber, units.toString(), billAmount.toString())
        }
    }

    /**
     * <p>This is click listener of history button</p>
     */
    private inner class HistoryBtnListener : View.OnClickListener {
        override fun onClick(view: View) {

            val serviceNumber = edt_service_num.text.toString()

            if (serviceNumber.isEmpty()) {
                helperMethods.messagePopUp(getString(R.string.please_input_service_number),"error",this@MainActivity)
                return
            }

            val intent = HistoryActivity.newIntent(this@MainActivity, serviceNumber)
            startActivity(intent)
        }
    }
    
```    

