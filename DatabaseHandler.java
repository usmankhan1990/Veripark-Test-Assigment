package com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.config.ConsumerData;
import com.config.SlabsData;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "consumerBilling";
    private static final String TABLE_CONSUMERS = "consumers";
    private static final String KEY_ID = "id";
    private static final String KEY_SERVICE_NUMBER = "service_number";
    private static final String KEY_METER_READING = "meter_reading";
    private static final String KEY_COST = "cost";
    private static final String KEY_DATE_INQUIRY = "date_inquiry";

    private static final String TABLE_SLABS = "slabs";
    private static final String KEY_SLABS_1_100 = "slabs_one";
    private static final String KEY_SLABS_100_500 = "slabs_two";
    private static final String KEY_501 = "slabs_three";

    String CREATE_CONSUMER_TABLE = "CREATE TABLE " + TABLE_CONSUMERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SERVICE_NUMBER + " TEXT,"
            + KEY_METER_READING + " TEXT," + KEY_COST + " TEXT," + KEY_DATE_INQUIRY + " TEXT"+ ")";

    String CREATE_BILL_SLABS = "CREATE TABLE " + TABLE_SLABS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SLABS_1_100 + " INTEGER,"
            + KEY_SLABS_100_500 + " INTEGER," + KEY_501 + " INTEGER," + KEY_DATE_INQUIRY + " INTEGER"+ ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * <p>This function is to upgrade create DB and tables</p>
     * @param db - SQLiteDatabase
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONSUMER_TABLE);
        db.execSQL(CREATE_BILL_SLABS);
    }

    /**
     * <p>This function is to upgrade DB</p>
     * @param db - SQLiteDatabase
     * @param oldVersion - Old version of DB
     * @param newVersion - New version of DB
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLABS);
        // Create tables again
        onCreate(db);
    }

    /**
     * <p>This function is to add the new consumer in DB</p>
     * @param consumerData - Consumer Data object
     */

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

    /**
     * <p>This function is to get selected consumer in a list from DB</p>
     * @param serviceNumber - Service number of a consumer
     */

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

    /**
     * <p>This function add slab data in DB</p>
     * @param slabsData - Slabs Data object
     */
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

    /**
     * <p>This function get the slab data from DB and returns List of Slab Data</p>
     */
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
}

