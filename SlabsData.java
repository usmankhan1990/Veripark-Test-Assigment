package com.config;

public class SlabsData  {

    int _id;
    int slabs_1_100;
    int slabs_100_500;
    int slabs_501;

    public SlabsData(){   }

    public SlabsData(int slabs_1_100, int slabs_100_500, int slabs_501){
        this.slabs_1_100 = slabs_1_100;
        this.slabs_100_500 = slabs_100_500;
        this.slabs_501 = slabs_501;
    }

    public void setID(int id){
        this._id = id;
    }

    public int getSlabs_1_100(){
        return this.slabs_1_100;
    }

    public void setSlabs_1_100(int name){
        this.slabs_1_100 = name;
    }

    public int getSlabs_100_500(){
        return this.slabs_100_500;
    }

    public void setSlabs_100_500(int name){
        this.slabs_100_500 = name;
    }

    public int getSlabs_501(){
        return this.slabs_501;
    }

    public void setSlabs_501(int name){
        this.slabs_501 = name;
    }
}
