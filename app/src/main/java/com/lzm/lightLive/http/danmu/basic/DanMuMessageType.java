package com.lzm.lightLive.http.danmu.basic;

import java.util.Objects;

public enum DanMuMessageType {
    /***/
    DAN_MU("danmu", "弹幕", 0),
    OTHER("other", "其他", 1),
    GIFT("gift", "礼物", 2);

    private final String text;
    private final String chineseText;
    private final Integer typeValue;

    DanMuMessageType(String text, String chineseText, Integer typeValue) {
        this.text = text;
        this.chineseText = chineseText;
        this.typeValue = typeValue;
    }

    /**
     * 根据text获取枚举
     * @param text text
     * @return 枚举对象
     */
    public static DanMuMessageType getEnumByValue(String text) {
        if (text != null) {
            for (DanMuMessageType danMuMessageType : DanMuMessageType.values()) {
                if (Objects.equals(danMuMessageType.getText(), text)) {
                    return danMuMessageType;
                }
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public String getChineseText() {
        return chineseText;
    }

    public Integer getTypeValue() {
        return typeValue;
    }
}