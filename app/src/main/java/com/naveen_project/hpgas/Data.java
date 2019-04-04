package com.naveen_project.hpgas;

/**
 * Created by SANKAR on 3/30/2018.
 */

public class Data {
    private String gasId, status;

    public Data(String gasId, String status) {
        this.setGasId(gasId);
        this.setStatus(status);
    }

    public String getGasId() {return gasId;}

    public void setGasId(String gasId) {this.gasId = gasId;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}




}
