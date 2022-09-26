package com.lzm.lightLive.http.bean.hy

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Stream protected constructor(parcel: Parcel) : Parcelable {
    @SerializedName("flv")
    var flv: Flv?

    init {
        flv = parcel.readParcelable(Flv::class.java.classLoader)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(flv, flags)
    }

    open class Flv protected constructor(parcel: Parcel) : Parcelable {
        @SerializedName("multiLine")
        var multiLine: List<MultiLine?>?

        @SerializedName("rateArray")
        var rateArray: List<RateArray?>?

        init {
            multiLine = parcel.createTypedArrayList(MultiLine.CREATOR)
            rateArray = parcel.createTypedArrayList(RateArray.CREATOR)
        }

        companion object CREATOR : Parcelable.Creator<Flv?> {
            override fun createFromParcel(parcel: Parcel): Flv {
                return Flv(parcel)
            }

            override fun newArray(size: Int): Array<Flv?> {
                return arrayOfNulls(size)
            }
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeTypedList(multiLine)
            dest.writeTypedList(rateArray)
        }
    }

    open class MultiLine protected constructor(parcel: Parcel) : Parcelable {
        @SerializedName("url")
        var url: String?

        @SerializedName("cdnType")
        var cdnType: String?

        @SerializedName("webPriorityRate")
        var webPriorityRate: Int

        @SerializedName("lineIndex")
        var lineIndex: Int

        init {
            url = parcel.readString()
            cdnType = parcel.readString()
            webPriorityRate = parcel.readInt()
            lineIndex = parcel.readInt()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(url)
            dest.writeString(cdnType)
            dest.writeInt(webPriorityRate)
            dest.writeInt(lineIndex)
        }

        companion object CREATOR : Parcelable.Creator<MultiLine?> {
                override fun createFromParcel(parcel: Parcel): MultiLine {
                    return MultiLine(parcel)
                }

                override fun newArray(size: Int): Array<MultiLine?> {
                    return arrayOfNulls(size)
                }
            }
    }

    open class RateArray protected constructor(parcel: Parcel) : Parcelable {
        @SerializedName("sDisplayName")
        var sDisplayName: String?

        @SerializedName("iBitRate")
        var iBitRate: Int

        @SerializedName("iCodecType")
        var iCodecType: Int

        @SerializedName("iCompatibleFlag")
        var iCompatibleFlag: Int

        @SerializedName("iHEVCBitRate")
        var iHEVCBitRate: Int

        init {
            sDisplayName = parcel.readString()
            iBitRate = parcel.readInt()
            iCodecType = parcel.readInt()
            iCompatibleFlag = parcel.readInt()
            iHEVCBitRate = parcel.readInt()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(sDisplayName)
            dest.writeInt(iBitRate)
            dest.writeInt(iCodecType)
            dest.writeInt(iCompatibleFlag)
            dest.writeInt(iHEVCBitRate)
        }

        fun getsDisplayName(): String? {
            return sDisplayName
        }

        companion object CREATOR: Parcelable.Creator<RateArray?> {
                override fun createFromParcel(parcel: Parcel): RateArray {
                    return RateArray(parcel)
                }

                override fun newArray(size: Int): Array<RateArray?> {
                    return arrayOfNulls(size)
                }
            }
    }

    companion object CREATOR : Parcelable.Creator<Stream?> {
            override fun createFromParcel(parcel: Parcel): Stream {
                return Stream(parcel)
            }

            override fun newArray(size: Int): Array<Stream?> {
                return arrayOfNulls(size)
            }
    }
}