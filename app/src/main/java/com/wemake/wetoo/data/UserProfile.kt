package com.wemake.wetoo.data

import com.google.firebase.firestore.DocumentReference

data class UserProfile(
    var university: String? = null,
    var name: String? = null,
    var grade: String? = null,
    var email: String? = null,
    var ktid: String? = null,
    var ktoid: String? = null,
    var tel: String? = null,
    var interest: String? = null, // 관심분야
    var introduction: String? = null, // 자기소개
    val visibility: String = "Private", // 공개 여부. 혹시나 해서 만듬. Public or Private
    val matchRef: DocumentReference? = null, // 팀에 대한 경로 없으면 null
    var imageUrl : String? = "",
    val fcm_token: String
)