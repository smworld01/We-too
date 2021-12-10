package com.wemake.wetoo.data

import com.google.firebase.firestore.DocumentReference

data class MatchTable(
    var interest: String? = null,
    var users: MutableList<DocumentReference>? = null,
    var approvals: MutableList<String>? = null,
)