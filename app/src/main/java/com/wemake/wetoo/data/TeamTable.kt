package com.wemake.wetoo.data

import com.google.firebase.firestore.DocumentReference

data class TeamTable(
    var interest: String? = null,
    var approvals: MutableList<String>? = null,
)