package com.chelinvest.notification.additional

// --- XGate ---
const val URL_XGATE_MOBILE_STUB = "url-xgate-mobile-stub"
const val URL_XGATE_MOBILE_INNER_JEV = "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"
//const val URL_XGATE_MOBILE_INNER_DVV = "https://xgate-xml-mob.chelinvest.ru:12345/request/xml"
const val URL_XGATE_MOBILE_INNER_DVV = "https://xgate-xml-mob.chelinvest.ru:7843/request/xml" // ссылка на рабочий шлюз

const val URL_XGATE_MOBILE_INNER =
    //"https://xgate-xml-mob.chelinvest.ru:12345/request/xml"  // Vasily Dobry. The latest url
    "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"
    //  "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob/process-post"

//const val URL_XGATE_MOBILE_INNER = "http://grepka:12345"        // Vasily Dobry
//const val URL_XGATE_MOBILE_INNER = "http://192.168.10.169:12345"  // Vasily Dobry
//https://xgate-xml-mob.chelinvest.ru:12345/request/xml  // Vasily Dobry. The latest url

const val URL_XGATE_MOBILE_OUTER = "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"
const val RELEASE_SERVER_URL =     "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob/process-post"
const val TEST_SERVER_URL =        "https://xgate-xml-mob.chelinvest.ru:2843/xgate-mob-test/process-post"

const val REQUEST_BODY = "request_xml=%s"

const val ESIA_URL = "https://ivpay.chelinvest.ru/apiesiasend"
const val ESIA_REDIRECT_URL = "https://ivpay.chelinvest.ru/apiesiaok"
const val REALM_SCHEMA_VERSION = 21L

const val SUBSCRIPTION_ID = "SUBSCRIPTION_ID"
const val SUBSCRIPTION_NAME = "SUBSCRIPTION_NAME"
const val BRANCH_ID = "BRANCH_ID"
const val BRANCH_NAME = "BRANCH_NAME"
const val EDIT_ADDRESS = "EDIT_ADDRESS"
const val ADD_ADDRESS = "ADD_ADDRESS"
const val SUBSCRIPTION = "SUBSCRIPTION"
const val DELIVERY_TYPE = "DELIVERY_TYPE"
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

const val LIMIT_VALUE = "LIMIT_VALUE"

// parameter requestCode in the startActivityForResult(intent, requestCode)
const val EDITSUBSCRACTIVITY_CODE = 2
const val ADDRESSACTIVITY_CODE = 3
const val EDITADDRESSACTIVITY_CODE = 4


const val EDIT_REQUEST_CODE = 5347
const val ESIA_LOGIN_REQUEST_CODE = 5349
const val ESIA_LOGIN2_REQUEST_CODE = 5350
const val ESIA_DATA_KEY = "ESIA_DATA_KEY"

const val GROUP_LOGOS_DIRECTORY_NAME = "GroupLogos"
const val SERVICE_LOGOS_DIRECTORY_NAME = "ServiceLogos"



