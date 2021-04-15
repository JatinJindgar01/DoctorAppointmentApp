package com.example.android.doctorsystem;

public class Word {
    private String mName;
    private String mFee;
    //private String mSpeciality;
    //private String mType;
    private String mAddress;
    private double mDistance;
    public Word(String name, String fee, String address, double distance)
    {
        mName=name;
        mFee=fee;
        //mSpeciality=speciality;
        //mType=type;
        mAddress = address;
        mDistance=distance;
    }
    public String getmName() {
        return mName;
    }
//    public String getmSpeciality() {
//        return mSpeciality;
//    }
//    public String getmType() {
//        return mType;
//    }
    public String getmAddress() {
        return mAddress;
    }
    public String getmFee() {
        return mFee;
    }
    public double getmDistance() {
        return mDistance;
    }

}
