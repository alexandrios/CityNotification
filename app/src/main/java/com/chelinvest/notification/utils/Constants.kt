package com.chelinvest.notification.utils

object Constants {

    // --- XGate ---
    const val URL_XGATE_MOBILE_STUB = "url-xgate-mobile-stub"
    const val URL_XGATE_MOBILE_INNER_JEV =
        "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"
    const val URL_XGATE_MOBILE_INNER_DVV = "https://xgate-xml-mob.chelinvest.ru:12345/request/xml"
    //const val URL_XGATE_MOBILE_INNER_DVV = "https://xgate-xml-mob.chelinvest.ru:7843/request/xml" // ссылка на рабочий шлюз

    const val URL_XGATE_MOBILE_INNER =
        //"https://xgate-xml-mob.chelinvest.ru:12345/request/xml"  // Vasily Dobry. The latest url
        "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"
    //  "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob/process-post"

    //const val URL_XGATE_MOBILE_INNER = "http://grepka:12345"        // Vasily Dobry
    //const val URL_XGATE_MOBILE_INNER = "http://192.168.10.169:12345"  // Vasily Dobry
    //https://xgate-xml-mob.chelinvest.ru:12345/request/xml  // Vasily Dobry. The latest url

    const val URL_XGATE_MOBILE_OUTER =
        "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"
    const val RELEASE_SERVER_URL = "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob/process-post"
    const val TEST_SERVER_URL =
        "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"


    const val SUBSCRIPTION_ID = "SUBSCRIPTION_ID"
    const val SUBSCRIPTION_NAME = "SUBSCRIPTION_NAME"
    const val BRANCH_ID = "BRANCH_ID"
    const val BRANCH_NAME = "BRANCH_NAME"
    const val EDIT_ADDRESS = "EDIT_ADDRESS"
    const val ADD_ADDRESS = "ADD_ADDRESS"
    const val SUBSCRIPTION = "SUBSCRIPTION"
    const val DELIVERY_TYPE = "DELIVERY_TYPE"
    const val DELIVE_NAME = "DELIVE_NAME"
    const val ADDRESS_MODEL = "ADDRESS_MODEL"
    const val SUBSCR_INFO = "SUBSCR_INFO"

    const val EMAIL_ID = "2"
    const val SMS_ID = "3"
    const val APP_PUSH_ID = "4"

    const val EMAIL = "EMAIL"
    const val SMS = "SMS"
    const val APP_PUSH = "APP_PUSH"

    const val FRAGMENT_TAG = "FRAGMENT_TAG"
    const val ADDRESS_DATA = "ADDRESS_DATA"
    const val ADDRESS_FCM_TOKEN = "ADDRESS_FCM_TOKEN"

    const val LIMIT_VALUE = "LIMIT_VALUE"

    // parameter requestCode in the startActivityForResult(intent, requestCode)
    const val EDITSUBSCRACTIVITY_CODE = 2
    const val ADDRESSACTIVITY_CODE = 3
    const val EDITADDRESSACTIVITY_CODE = 4

    const val GROUP_LOGOS_DIRECTORY_NAME = "GroupLogos"
    const val SERVICE_LOGOS_DIRECTORY_NAME = "ServiceLogos"

    //--------------------------------------------------------------------------------------------

    const val LOG_TAG = "myLogs"

    const val ENCODING = "windows-1251"
    const val REQUEST_BODY = "request_xml=%s"
    const val BASE_URL = "https://xgate-xml-mob.chelinvest.ru:12345/request/xml/"

    const val AGENT = "AGENT"

    //preferences
    const val COMMON_PREFERENCES = "COMMON_PREFERENCES"
    const val SESSION_ID = "SESSION_ID"
    const val LAUNCH_COUNT = "LAUNCH_COUNT"
    const val BRANCH_SHORT = "BRANCH_SHORT"
    const val FCM_TOKEN = "FCM_TOKEN"
    const val CHANGE_SUBSCR_LIST = "CHANGE_SUBSCR_LIST"
    const val CHANGE_ADDRESS = "CHANGE_ADDRESS"
    const val SELECTED_ITEM = "SELECTED_ITEM"
    const val PREFER_TIME_ZONE = "PREFER_TIME_ZONE"

    const val DEFAULT_START_HOUR = "9"
    const val DEFAULT_FINISH_HOUR = "22"
    const val DEFAULT_TIME_ZONE = "5"
    const val DEFAULT_TIME_ZONE_INT = 5
    const val DEFAULT_SMS = "+7"
    const val DEFAULT_EMAIL = ""

    const val ADDRESS_TEXT = "ADDRESS_TEXT"
    const val HOUR_START = "HOUR_START"
    const val HOUR_FINISH = "HOUR_FINISH"
    const val EDIT_TEXT = "EDIT_TEXT"

}