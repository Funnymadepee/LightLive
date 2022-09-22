package com.lzm.lightLive.http.request.hy;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;

public class HuYaDanMuFormatDataTarsBase extends TarsStructBase {

    private HyDanMuFormatData danMuFormatData;

    @Override
    public void writeTo(TarsOutputStream os) {

    }

    @Override
    public void readFrom(TarsInputStream is) {
        is.setServerEncoding("utf-8");
        danMuFormatData = new HyDanMuFormatData();
        danMuFormatData.setFontColor(is.read(0, 0, false));
        danMuFormatData.setFontSize(is.read(0, 1, false));
        danMuFormatData.setTextSpeed(is.read(0, 2, false));
        danMuFormatData.setTransitionType(is.read(0, 3, false));
        danMuFormatData.setPopupStyle(is.read(0, 4, false));
    }

    public HyDanMuFormatData getDanMuFormatData() {
        return danMuFormatData;
    }

    public static class HyDanMuFormatData extends DanMuFormat {
    }
}