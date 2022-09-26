package com.lzm.lightLive.http.danmu.basic

open class DanMuUserInfo {
    var level = 0
    var uid: String? = null
    var badge: String? = null
    var avatar: String? = null
    var nickName: String? = null
    var badgeLevel: String? = null
    var isVip = false
    var isBlackList = false

    constructor() {}
    constructor(nickName: String?) {
        this.nickName = nickName
    }

    constructor(
        level: Int,
        uid: String?,
        badge: String?,
        avatar: String?,
        nickName: String?,
        badgeLevel: String?
    ) {
        this.level = level
        this.uid = uid
        this.badge = badge
        this.avatar = avatar
        this.nickName = nickName
        this.badgeLevel = badgeLevel
    }

    override fun toString(): String {
        return "DanMuUserInfo{" +
                "level=" + level +
                ", uid='" + uid + '\'' +
                ", badge='" + badge + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isVip=" + isVip +
                ", nickName='" + nickName + '\'' +
                ", badgeLevel='" + badgeLevel + '\'' +
                ", isBlackList=" + isBlackList +
                '}'
    }
}