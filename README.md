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
