package com.lzm.lightLive.http.danmu.basic

enum class DanMuMessageType(val text: String, val chineseText: String, val typeValue: Int) {
    /** */
    DAN_MU("danmu", "弹幕", 0),
    OTHER("other", "其他", 1),
    GIFT("gift", "礼物", 2);

    companion object {
        /**
         * 根据text获取枚举
         * @param text text
         * @return 枚举对象
         */
        fun getEnumByValue(text: String?): DanMuMessageType? {
            if (text != null) {
                for (danMuMessageType in values()) {
                    if (danMuMessageType.text == text) {
                        return danMuMessageType
                    }
                }
            }
            return null
        }
    }
}