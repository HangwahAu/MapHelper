package com.example.oukenghua.maphelper.LitePalDao;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oukenghua on 2018/4/11.
 */

public class Community extends DataSupport implements Parcelable {

    private String address;
    private String security;
    private String policemen;
    private String serial;
    private String parent;

    protected Community(Parcel parcel){
        address = parcel.readString();
        security = parcel.readString();
        policemen = parcel.readString();
        serial = parcel.readString();
        parent = parcel.readString();
    }

    public static final Creator<Community> CREATOR = new Creator<Community>() {
        @Override
        public Community createFromParcel(Parcel parcel) {
            return new Community(parcel);
        }

        @Override
        public Community[] newArray(int i) {
            return new Community[i];
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

    private Building building;
    private List<Building> buildings = new ArrayList<Building>();

    public Community(String address, String security, String policemen, String serial_number, String parent_id) {
        this.address = address;
        this.security = security;
        this.policemen = policemen;
        this.serial = serial_number;
        this.parent = parent_id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
