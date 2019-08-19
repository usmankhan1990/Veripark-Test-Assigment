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
