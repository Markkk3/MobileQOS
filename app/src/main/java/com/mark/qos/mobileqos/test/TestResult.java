package com.mark.qos.mobileqos.test;


public class TestResult {


    int id;
    String Id_subscriber;
    String Type_connection;
    long datetime;
    int cid;
    int psd;
    int lac;
    int mcc;
    int mnc;
    int signal_level;
    int asu_level;
    float latitude;
    float longitude;
    int speed;
    int ping;
    int packet_loss;
    int download;
    int upload;


    public TestResult() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_subscriber() {
        return Id_subscriber;
    }

    public void setId_subscriber(String id_subscriber) {
        this.Id_subscriber = id_subscriber;
    }

    public String getType_connection() {
        return Type_connection;
    }

    public void setType_connection(String type_connection) {
        Type_connection = type_connection;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
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

    public int getSignal_level() {
        return signal_level;
    }

    public void setSignal_level(int signal_level) {
        this.signal_level = signal_level;
    }

    public int getAsu_level() {
        return asu_level;
    }

    public void setAsu_level(int asu_level) {
        this.asu_level = asu_level;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getPacket_loss() {
        return packet_loss;
    }

    public void setPacket_loss(int packet_loss) {
        this.packet_loss = packet_loss;
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
