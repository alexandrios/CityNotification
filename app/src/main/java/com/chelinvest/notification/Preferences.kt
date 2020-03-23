package com.chelinvest.notification

import android.content.Context
import org.joda.time.DateTime

class Preferences private constructor() : BasePreferences() {

    companion object {
        private var INSTANCE: Preferences? = null
        fun getInstance(): Preferences {
            if (INSTANCE == null)
                INSTANCE = Preferences()
            return INSTANCE!!
        }

        private const val CLIENT_HASH = "CLIENT_HASH"

        private const val LAUNCH_COUNT = "LAUNCH_COUNT"

        private const val FCM_TOKEN = "FCM_TOKEN"

        private const val LAST_UPDATE_SESSION_ID = "LAST_UPDATE_SESSION_ID"
        private const val LAST_UPDATE_GROUP_LOGOS = "LAST_UPDATE_GROUP_LOGOS"
        private const val LAST_UPDATE_SERVICE_LOGOS = "LAST_UPDATE_SERVICE_LOGOS"
        private const val LAST_UPDATE_DEFAULT_ADDRESS = "LAST_UPDATE_DEFAULT_ADDRESS"
        private const val LAST_UPDATE_POPULAR_SERVICES = "LAST_UPDATE_POPULAR_SERVICES"
        private const val LAST_UPDATE_GSRV_NAMES = "LAST_UPDATE_GSRV_NAMES"

        private const val SESSION_ID = "SESSION_ID"
        private const val AGENT_ID = "AGENT_ID"
        private const val AGENT_NAME = "AGENT_NAME"
        private const val XGATE_NAME = "XGATE_NAME"
        private const val BRANCH_SHORT = "BRANCH_SHORT"
        private const val TRY_LOGIN = "TRY_LOGIN"
        private const val CURRENT_CARD = "CURRENT_CARD"
        private const val UNS1_ID = "UNS1_ID"
        private const val UNS2_ID = "UNS2_ID"
        private const val IS_PACKS_AUTHORIZED = "IS_PACKS_AUTHORIZED"
        private const val SHOPPING_CART_COUNT = "SHOPPING_CART_COUNT"
        private const val LAST_CARD = "LAST_CARD"
        private const val LAST_PHONE = "LAST_PHONE"
        private const val IS_SHOPPING_CART_INFO_SHOWN = "IS_SHOPPING_CART_INFO_SHOWN"
        private const val VOUCHER_EMAIL = "VOUCHER_EMAIL"
        private const val TO_SEND_EMAIL = "TO_SEND_EMAIL"

        private const val DEFAULT_ADDRESS_REGION_ID = "DEFAULT_ADDRESS_REGION_ID"
        private const val DEFAULT_ADDRESS_REGION_NAME = "DEFAULT_ADDRESS_REGION_NAME"
        private const val DEFAULT_ADDRESS_CITY_ID = "DEFAULT_ADDRESS_CITY_ID"
        private const val DEFAULT_ADDRESS_CITY_NAME = "DEFAULT_ADDRESS_CITY_NAME"

        private const val IS_MY_CARD_TOOLTIP_SHOWN = "IS_MY_CARD_TOOLTIP_SHOWN"
        private const val IS_SHOPPING_CART_TOOLTIP_SHOWN = "IS_SHOPPING_CART_TOOLTIP_SHOWN"

        private const val LAST_SEND_PHONE_PASSWORD_DATE = "LAST_SEND_PHONE_PASSWORD_DATE"

        private const val IS_TEST_SERVER = "IS_TEST_SERVER"
    }

    fun saveClientHash(context: Context, clientHash: String?) = saveString(context, CLIENT_HASH, clientHash)

    fun getClientHash(context: Context): String? = getString(context, CLIENT_HASH)


    fun saveLastUpdateSessionId(context: Context, date: DateTime?) = saveLong(context, LAST_UPDATE_SESSION_ID, date?.millis ?: 0)

    fun getLastUpdateSessionId(context: Context): DateTime? {
        var millis = getLong(context, LAST_UPDATE_SESSION_ID)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }


    fun saveLastUpdateGroupLogos(context: Context, date: DateTime?) = saveLong(context, LAST_UPDATE_GROUP_LOGOS, date?.millis ?: 0)

    fun getLastUpdateGroupLogos(context: Context): DateTime? {
        var millis = getLong(context, LAST_UPDATE_GROUP_LOGOS)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }


    fun saveLastUpdateServiceLogos(context: Context, date: DateTime?) = saveLong(context, LAST_UPDATE_SERVICE_LOGOS, date?.millis ?: 0)

    fun getLastUpdateServiceLogos(context: Context): DateTime? {
        var millis = getLong(context, LAST_UPDATE_SERVICE_LOGOS)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }


    fun saveLastUpdateDefaultAddress(context: Context, date: DateTime?) = saveLong(context, LAST_UPDATE_DEFAULT_ADDRESS, date?.millis ?: 0)

    fun getLastUpdateDefaultAddress(context: Context): DateTime? {
        var millis = getLong(context, LAST_UPDATE_DEFAULT_ADDRESS)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }


    fun saveLastUpdatePopularServices(context: Context, date: DateTime?) = saveLong(context, LAST_UPDATE_POPULAR_SERVICES, date?.millis ?: 0)

    fun getLastUpdatePopularServices(context: Context): DateTime? {
        var millis = getLong(context, LAST_UPDATE_POPULAR_SERVICES)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }


    fun saveLastUpdateGSrvNames(context: Context, date: DateTime?) = saveLong(context, LAST_UPDATE_GSRV_NAMES, date?.millis ?: 0)

    fun getLastUpdateGSrvNames(context: Context): DateTime? {
        var millis = getLong(context, LAST_UPDATE_GSRV_NAMES)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }


    fun saveIsPacksAuthorized(context: Context, isPacksAuthorized: Boolean) = saveBoolean(context, IS_PACKS_AUTHORIZED, isPacksAuthorized)

    fun isPacksAuthorized(context: Context): Boolean = getBoolean(context, IS_PACKS_AUTHORIZED, false)


    fun saveLaunchCount(context: Context, launchCount: Int) = saveInt(context, LAUNCH_COUNT, launchCount)
    fun getLaunchCount(context: Context): Int = getInt(context, LAUNCH_COUNT)

    fun saveFCMToken(context: Context, token: String?) = saveString(context, FCM_TOKEN, token)
    fun getFCMToken(context: Context): String? = getString(context, FCM_TOKEN)

    fun saveSessionId(context: Context, sessionId: String?) = saveString(context, SESSION_ID, sessionId)
    fun getSessionId(context: Context): String? = getString(context, SESSION_ID)

    fun saveAgentId(context: Context, agentId: String?) = saveString(context, AGENT_ID, agentId)
    fun getAgentId(context: Context): String? = getString(context, AGENT_ID)

    fun saveAgentName(context: Context, agentName: String?) = saveString(context, AGENT_NAME, agentName)
    fun getAgentName(context: Context): String? = getString(context, AGENT_NAME)

    fun saveTryLogin(context: Context, value: Boolean) = saveBoolean(context, TRY_LOGIN, value)
    fun getTryLogin(context: Context): Boolean = getBoolean(context, TRY_LOGIN, false)

    fun saveBranchShort(context: Context, branchShort: String?) = saveString(context, BRANCH_SHORT, branchShort)
    fun getBranchShort(context: Context): String? = getString(context, BRANCH_SHORT)

    fun saveXgateType(context: Context, xgateName: String?) = saveString(context, XGATE_NAME, xgateName)
    fun getXgateType(context: Context): String? = getString(context, XGATE_NAME)

    fun saveCurrentCard(context: Context, cardId: String?) = saveString(context, CURRENT_CARD, cardId)

    fun getCurrentCard(context: Context): String? = getString(context, CURRENT_CARD)


    fun saveUns1Id(context: Context, unsId: String?) = saveString(context, UNS1_ID, unsId)

    fun getUns1Id(context: Context): String? = getString(context, UNS1_ID)//"123580777"

    fun saveUns2Id(context: Context, unsId: String?) = saveString(context, UNS2_ID, unsId)

    fun getUns2Id(context: Context): String? = getString(context, UNS2_ID)


    fun saveShoppingCartCount(context: Context, count: Int) = saveInt(context, SHOPPING_CART_COUNT, count)

    fun getShoppingCartCount(context: Context): Int = getInt(context, SHOPPING_CART_COUNT, 0)


    fun saveLastCard(context: Context, card: String?) = saveString(context, LAST_CARD, card)

    fun getLastCard(context: Context): String? = getString(context, LAST_CARD)

    fun saveLastPhone(context: Context, phone: String?) = saveString(context, LAST_PHONE, phone)

    fun getLastPhone(context: Context): String? = getString(context, LAST_PHONE)


    fun saveShoppingCartInfoShown(context: Context, isShown: Boolean) = saveBoolean(context, IS_SHOPPING_CART_INFO_SHOWN, isShown)

    fun isShoppingCartInfoShown(context: Context): Boolean = getBoolean(context, IS_SHOPPING_CART_INFO_SHOWN, false)


    fun saveVoucherEmail(context: Context, sessionId: String?) = saveString(context, VOUCHER_EMAIL, sessionId)

    fun getVoucherEmail(context: Context): String? = getString(context, VOUCHER_EMAIL)

    fun saveToSendEmail(context: Context, toSend: Boolean) = saveBoolean(context, TO_SEND_EMAIL, toSend)

    fun getToSendEmail(context: Context): Boolean = getBoolean(context, TO_SEND_EMAIL, false)


    fun saveDefaultAddressRegionId(context: Context, regionId: String?) = saveString(context, DEFAULT_ADDRESS_REGION_ID, regionId)

    fun getDefaultAddressRegionId(context: Context): String? = getString(context, DEFAULT_ADDRESS_REGION_ID)

    fun saveDefaultAddressRegionName(context: Context, regionName: String?) = saveString(context, DEFAULT_ADDRESS_REGION_NAME, regionName)

    fun getDefaultAddressRegionName(context: Context): String? = getString(context, DEFAULT_ADDRESS_REGION_NAME)

    fun saveDefaultAddressCityId(context: Context, cityId: String?) = saveString(context, DEFAULT_ADDRESS_CITY_ID, cityId)

    fun getDefaultAddressCityId(context: Context): String? = getString(context, DEFAULT_ADDRESS_CITY_ID)

    fun saveDefaultAddressCityName(context: Context, cityName: String?) = saveString(context, DEFAULT_ADDRESS_CITY_NAME, cityName)

    fun getDefaultAddressCityName(context: Context): String? = getString(context, DEFAULT_ADDRESS_CITY_NAME)


    fun saveMyCardTooltipShown(context: Context, isShown: Boolean) = saveBoolean(context, IS_MY_CARD_TOOLTIP_SHOWN, isShown)

    fun isMyCardTooltipShown(context: Context): Boolean = getBoolean(context, IS_MY_CARD_TOOLTIP_SHOWN, false)

    fun saveShoppingCartTooltipShown(context: Context, isShown: Boolean) = saveBoolean(context, IS_SHOPPING_CART_TOOLTIP_SHOWN, isShown)

    fun isShoppingCartTooltipShown(context: Context): Boolean = getBoolean(context, IS_SHOPPING_CART_TOOLTIP_SHOWN, false)


    fun saveLastSendPhonePasswordDate(context: Context, date: DateTime?) = saveLong(context, LAST_SEND_PHONE_PASSWORD_DATE, date?.millis ?: 0)

    fun getLastSendPhonePasswordDate(context: Context): DateTime? {
        var millis = getLong(context, LAST_SEND_PHONE_PASSWORD_DATE)
        if (millis == -1L)
            millis = 0L
        return DateTime(millis)
    }

    // Сейчас работает так: isTestServer == true - используется url для внешних устройств (Interactor.getServerUrl)
    fun saveIsTestServer(context: Context, isTestServer: Boolean) = saveBoolean(context, IS_TEST_SERVER, isTestServer)

    fun isTestServer(context: Context): Boolean = getBoolean(context, IS_TEST_SERVER, false)
}