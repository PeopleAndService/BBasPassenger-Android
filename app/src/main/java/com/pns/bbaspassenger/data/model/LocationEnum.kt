package com.pns.bbaspassenger.data.model

import com.pns.bbaspassenger.R

enum class LocationEnum(val id: Int, val code: Int, val cityName: String) {
    NONE(R.id.chip0, 0, "없음"),
    CHANGWON(R.id.chip1, 38010, "창원시"),
    JINJU(R.id.chip2, 38030, "진주시"),
    TONGYEONG(R.id.chip3, 38050, "통영시"),
    GIMHAE(R.id.chip4, 38070, "김해시"),
    MIRYANG(R.id.chip5, 38080, "밀양시"),
    GEOJE(R.id.chip6, 38090, "거제시"),
    YANGSAN(R.id.chip7, 38100, "양산시"),
    UIRYEONG(R.id.chip8, 38310, "의령군"),
    HAMAN(R.id.chip9, 38320, "함안군"),
    CHANGNYEONG(R.id.chip10, 38330, "창녕군"),
    GOSUNG(R.id.chip11, 38340, "고성군"),
    NAMHAE(R.id.chip12, 38350, "남해군"),
    HADONG(R.id.chip13, 38360, "하동군"),
    SANCHUNG(R.id.chip14, 38370, "산청군"),
    HAMYANG(R.id.chip15, 38380, "함양군"),
    GEOCHANG(R.id.chip16, 38390, "거창군"),
    HAPCHEON(R.id.chip17, 38400, "합천군");

    companion object {
        fun positionFind(pos: Int) = values().find { it.ordinal == pos } ?: NONE

        fun idFind(id: Int) = values().find { it.id == id } ?: NONE

        fun codeFind(code: Int) = values().find { it.code == code } ?: NONE
    }
}