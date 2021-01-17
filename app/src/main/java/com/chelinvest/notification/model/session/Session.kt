package com.chelinvest.notification.model.session

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import com.chelinvest.notification.api.response.MainResponse
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.Serializable
import java.lang.StringBuilder
import java.nio.charset.Charset

class Session (
    var datasource: String? = null,
    var row_num: String? = null,
    var session_id: String? = null,
    var class_name: String? = null,
    var org_id: String? = null,
    var org_name: String? = null,
    var error_note: String? = null
) : Serializable {

    constructor(xml: String) : this() {
        if (xml.isNotEmpty()) {
            parsing(xml)
        }
    }

    /**
     * Инициализация объекта Session из XML
     */
    private fun parsing(xml: String) {

        val inputStream: InputStream = stringToInputStream(xml)
        val parserFactory : XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser: XmlPullParser = parserFactory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)

        var startTag: String? = null
        var tag: String?
        var text: String? = null
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            tag = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    startTag = tag
                    text = null
                }
                XmlPullParser.TEXT -> {
                    text = parser.text
                }
                XmlPullParser.END_TAG -> {
                    if (tag.equals(startTag)) {
                        if (tag.equals("datasource", ignoreCase = true)) {
                            datasource = text
                        } else if (tag.equals("row_num", ignoreCase = true)) {
                            row_num = text
                        } else if (tag.equals("session_id", ignoreCase = true)) {
                            session_id = text
                        } else if (tag.equals("class_name", ignoreCase = true)) {
                            class_name = text
                        } else if (tag.equals("id", ignoreCase = true)) {
                            org_id = text
                        } else if (tag.equals("name", ignoreCase = true)) {
                            org_name = text
                        } else if (tag.equals("error_note", ignoreCase = true)) {
                            error_note = text
                        }
                    }
                }
                else -> {
                }
            }
            eventType = parser.next()
        }
    }

    // Пpeoбpaзyem cтpoкy вo вхoднoй пoтoк
    private fun stringToInputStream(s: String): InputStream {
        return ByteArrayInputStream(s.toByteArray(Charset.forName("windows-1251")))
    }

    /**
     * Инициализация объекта Session из объекта MainResponse
     */
    fun setResponse(response: MainResponse) {
        datasource = response.dataSource
        row_num = response.rowNum.toString()
        session_id = response.sessionId
        class_name = response.class_name
        org_id = response.org_name?.id
        org_name = response.org_name?.name
        error_note = response.errorNote
    }

    override fun toString(): String {
        val str: StringBuilder = StringBuilder()
        str.append("DATASOURCE=$datasource\n")
        str.append("ROW_NUM=$row_num\n")
        str.append("SESSION_ID=$session_id\n")
        str.append("CLASS_NAME=$class_name\n")
        str.append("ORG_ID=$org_id\n")
        str.append("ORG_NAME=$org_name\n")
        str.append("ERROR_NOTE=$error_note")

        return str.toString()
    }

}