package com.lzm.lightLive.http.bean.hy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Stream implements Parcelable{
    @SerializedName("flv")
    private Flv flv;

    public Stream(Flv flv) {
        this.flv = flv;
    }

    protected Stream(Parcel in) {
        flv = in.readParcelable(Flv.class.getClassLoader());
    }

    public static final Creator<Stream> CREATOR = new Creator<Stream>() {
        @Override
        public Stream createFromParcel(Parcel in) {
            return new Stream(in);
        }

        @Override
        public Stream[] newArray(int size) {
            return new Stream[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(flv, flags);
    }

    static class Flv implements Parcelable{

        @SerializedName("multiLine")
        List<MultiLine> multiLine;

        @SerializedName("rateArray")
        List<RateArray> rateArray;

        public Flv(List<MultiLine> multiLine, List<RateArray> rateArray) {
            this.multiLine = multiLine;
            this.rateArray = rateArray;
        }

        protected Flv(Parcel in) {
            multiLine = in.createTypedArrayList(MultiLine.CREATOR);
            rateArray = in.createTypedArrayList(RateArray.CREATOR);
        }

        public final Creator<Flv> CREATOR = new Creator<Flv>() {
            @Override
            public Flv createFromParcel(Parcel in) {
                return new Flv(in);
            }

            @Override
            public Flv[] newArray(int size) {
                return new Flv[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(multiLine);
            dest.writeTypedList(rateArray);
        }

        public List<MultiLine> getMultiLine() {
            return multiLine;
        }

        public void setMultiLine(List<MultiLine> multiLine) {
            this.multiLine = multiLine;
        }

        public List<RateArray> getRateArray() {
            return rateArray;
        }

        public void setRateArray(List<RateArray> rateArray) {
            this.rateArray = rateArray;
        }
    }

    static class MultiLine implements Parcelable{
        @SerializedName("url")
        String url;
        @SerializedName("cdnType")
        String cdnType;
        @SerializedName("webPriorityRate")
        int webPriorityRate;
        @SerializedName("lineIndex")
        int lineIndex;

        public MultiLine(String url, String cdnType,
                         int webPriorityRate, int lineIndex) {
            this.url = url;
            this.cdnType = cdnType;
            this.webPriorityRate = webPriorityRate;
            this.lineIndex = lineIndex;
        }

        protected MultiLine(Parcel in) {
            url = in.readString();
            cdnType = in.readString();
            webPriorityRate = in.readInt();
            lineIndex = in.readInt();
        }

        public static final Creator<MultiLine> CREATOR = new Creator<MultiLine>() {
            @Override
            public MultiLine createFromParcel(Parcel in) {
                return new MultiLine(in);
            }

            @Override
            public MultiLine[] newArray(int size) {
                return new MultiLine[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
            dest.writeString(cdnType);
            dest.writeInt(webPriorityRate);
            dest.writeInt(lineIndex);
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCdnType() {
            return cdnType;
        }

        public void setCdnType(String cdnType) {
            this.cdnType = cdnType;
        }

        public int getWebPriorityRate() {
            return webPriorityRate;
        }

        public void setWebPriorityRate(int webPriorityRate) {
            this.webPriorityRate = webPriorityRate;
        }

        public int getLineIndex() {
            return lineIndex;
        }

        public void setLineIndex(int lineIndex) {
            this.lineIndex = lineIndex;
        }
    }

    static class RateArray implements Parcelable {
        @SerializedName("sDisplayName")
        String sDisplayName;
        @SerializedName("iBitRate")
        int iBitRate;
        @SerializedName("iCodecType")
        int iCodecType;
        @SerializedName("iCompatibleFlag")
        int iCompatibleFlag;
        @SerializedName("iHEVCBitRate")
        int iHEVCBitRate;

        public RateArray(String sDisplayName, int iBitRate,
                         int iCodecType, int iCompatibleFlag,
                         int iHEVCBitRate) {
            this.sDisplayName = sDisplayName;
            this.iBitRate = iBitRate;
            this.iCodecType = iCodecType;
            this.iCompatibleFlag = iCompatibleFlag;
            this.iHEVCBitRate = iHEVCBitRate;
        }

        protected RateArray(Parcel in) {
            sDisplayName = in.readString();
            iBitRate = in.readInt();
            iCodecType = in.readInt();
            iCompatibleFlag = in.readInt();
            iHEVCBitRate = in.readInt();
        }

        public static final Creator<RateArray> CREATOR = new Creator<RateArray>() {
            @Override
            public RateArray createFromParcel(Parcel in) {
                return new RateArray(in);
            }

            @Override
            public RateArray[] newArray(int size) {
                return new RateArray[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(sDisplayName);
            dest.writeInt(iBitRate);
            dest.writeInt(iCodecType);
            dest.writeInt(iCompatibleFlag);
            dest.writeInt(iHEVCBitRate);
        }

        public String getsDisplayName() {
            return sDisplayName;
        }

        public void setsDisplayName(String sDisplayName) {
            this.sDisplayName = sDisplayName;
        }

        public int getiBitRate() {
            return iBitRate;
        }

        public void setiBitRate(int iBitRate) {
            this.iBitRate = iBitRate;
        }

    }


    public Flv getFlv() {
        return flv;
    }

    public void setFlv(Flv flv) {
        this.flv = flv;
    }
}
