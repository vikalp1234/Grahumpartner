package com.app.graham.partner.pozo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditprofileResponceModel {
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
