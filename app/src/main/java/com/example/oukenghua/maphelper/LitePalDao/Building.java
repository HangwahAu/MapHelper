package com.example.oukenghua.maphelper.LitePalDao;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oukenghua on 2018/4/11.
 */

public class Building extends DataSupport implements Parcelable{

    private String address;
    private String security;
    private String policemen;
    private String serial;
    private String parent;

    protected Building(Parcel parcel){
        address = parcel.readString();
        security = parcel.readString();
        policemen = parcel.readString();
        serial = parcel.readString();
        parent = parcel.readString();
    }

    public static final Parcelable.Creator<Building> CREATOR = new Parcelable.Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel parcel) {
            return new Building(parcel);
        }

        @Override
        public Building[] newArray(int i) {
            return new Building[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(security);
        parcel.writeString(policemen);
        parcel.writeString(serial);
        parcel.writeString(parent);
    }

    private Community community;

    private UnitRoom unitRoom;
    private List<UnitRoom> unitRooms = new ArrayList<>();

    public Building(String address, String security, String policemen, String serial, String parent) {
        this.address = address;
        this.security = security;
        this.policemen = policemen;
        this.serial = serial;
        this.parent = parent;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial_number) {
        this.serial = serial_number;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent_id) {
        this.parent = parent_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getPolicemen() {
        return policemen;
    }

    public void setPolicemen(String policemen) {
        this.policemen = policemen;
    }

}
