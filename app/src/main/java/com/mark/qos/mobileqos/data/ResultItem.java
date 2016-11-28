package com.mark.qos.mobileqos.data;

/**
 * Created by tushkevich_m on 22.11.2016.
 */

public class ResultItem {

    int id;
    long id_subscriber;
    String typeConnection;
    long datetime;
    int cid;
    int psd;
    int lac;
    int mcc;
    int mnc;
    int signallevel;
    int asulevel;
    int speed;
    float latitude;
    float longitude;
    int ping;
    float packetlost;
    int download;
    int upload;

    public ResultItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getId_subscriber() {
        return id_subscriber;
    }

    public void setId_subscriber(long id_subscriber) {
        this.id_subscriber = id_subscriber;
    }

    public String getTypeConnection() {
        return typeConnection;
    }

    public void setTypeConnection(String typeConnection) {
        this.typeConnection = typeConnection;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datatime) {
        this.datetime = datatime;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPsd() {
        return psd;
    }

    public void setPsd(int psd) {
        this.psd = psd;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getSignallevel() {
        return signallevel;
    }

    public void setSignallevel(int signallevel) {
        this.signallevel = signallevel;
    }

    public int getAsulevel() {
        return asulevel;
    }

    public void setAsulevel(int asulevel) {
        this.asulevel = asulevel;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public float getPacketlost() {
        return packetlost;
    }

    public void setPacketlost(float packetlost) {
        this.packetlost = packetlost;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }
}

