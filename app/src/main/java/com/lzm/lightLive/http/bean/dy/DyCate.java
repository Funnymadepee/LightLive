package com.lzm.lightLive.http.bean.dy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DyCate {

    @SerializedName("error")
    private int error;

    @SerializedName("msg")
    private String message;

    @SerializedName("data")
    private List<DyCateData> data;

    public static class DyCateData {
        @SerializedName("cate2Id")
        private int cateId;

        @SerializedName("cate2Name")
        private String cateName;

        @SerializedName("cate2Icon")
        private String cateIcon;

        @SerializedName("cate2Url")
        private String cateUrl;

        @SerializedName("hot")
        private long cateHeat;

        @Override
        public String toString() {
            return "DyCate{" +
                    "cateId=" + cateId +
                    ", cateName='" + cateName + '\'' +
                    ", cateIcon='" + cateIcon + '\'' +
                    ", cateUrl='" + cateUrl + '\'' +
                    ", cateHeat=" + cateHeat +
                    '}';
        }

        public int getCateId() {
            return cateId;
        }

        public void setCateId(int cateId) {
            this.cateId = cateId;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getCateIcon() {
            return cateIcon;
        }

        public void setCateIcon(String cateIcon) {
            this.cateIcon = cateIcon;
        }

        public String getCateUrl() {
            return cateUrl;
        }

        public void setCateUrl(String cateUrl) {
            this.cateUrl = cateUrl;
        }

        public long getCateHeat() {
            return cateHeat;
        }

        public void setCateHeat(long cateHeat) {
            this.cateHeat = cateHeat;
        }
    }

    @Override
    public String toString() {
        return "DyCate{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DyCateData> getData() {
        return data;
    }

    public void setData(List<DyCateData> data) {
        this.data = data;
    }
}
