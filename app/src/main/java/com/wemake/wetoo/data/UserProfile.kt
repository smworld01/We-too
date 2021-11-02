package com.wemake.wetoo.data

import com.google.firebase.firestore.DocumentSnapshot


// 사용 법
//val userProfile = UserProfile(
//    "강릉원주대학교",
//    4,
//    "smworld02@gmail.com",
//    listOf("게임", "안드로이드"),
//    "https://123.com",
//    "안녕하세요."
//)


data class UserProfile(
    var university: String? = null,
    var name: String? = null,
    var grade: String? = null,
    var email: String? = null,
    var ktid: String? = null,
    var ktoid: String? = null,
    var tel: String? = null,
    var interest: String? = null,
    val Interests: List<String>? = null,
    val image: String? = null,
    val introduction: String? = null,
    val visibility: String = "Private", // 공개 여부. 혹시나 해서 만듬. Public or Private
    val matchRef: DocumentSnapshot? = null, // 팀에 대한 경로 없으면 null
) {}
