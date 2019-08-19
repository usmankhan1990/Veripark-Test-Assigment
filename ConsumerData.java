package com.config;

public class ConsumerData  {

    int _id;
    String service_number;
    String meter_reading;
    String cost;
    String date_time;

    public ConsumerData(){   }

    public ConsumerData(String service_number, String meter_reading, String cost, String date_time){
        this.service_number = service_number;
        this.meter_reading = meter_reading;
        this.cost = cost;
        this.date_time = date_time;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getServiceNumber(){
        return this.service_number;
    }

    public void setServiceNumber(String name){
        this.service_number = name;
    }

    public String getMeterReading(){
        return this.meter_reading;
    }

    public void setMeterReading(String meter_reading){
        this.meter_reading = meter_reading;
    }

    public String getDateTime(){
        return this.date_time;
    }

    public void setDateTime(String date_time){
        this.date_time = date_time;
    }

    public String getCost(){
        return this.cost;
    }

    public void setCost(String cost){
        this.cost = cost;
    }
}
