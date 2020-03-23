package com.chelinvest.notification.model

import java.io.Serializable

class DeliveAddrBranch : Serializable {

    lateinit var id: String
    lateinit var delive_type: DeliveType
    lateinit var address: String
    lateinit var is_valid: String
    lateinit var is_confirmed: String
    lateinit var start_hour: String
    lateinit var finish_hour: String
    lateinit var timezone: String

}