package com.telyo.finemoodnote.entity;

/**
 * Created by Administrator on 2017/7/17.
 */

public class RecyclerDescribe {
    private String describe;
    private boolean isFinished;
    private String planLevel;

    public String getPlanLevel() {
        return planLevel;
    }

    public void setPlanLevel(String planLevel) {
        this.planLevel = planLevel;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
