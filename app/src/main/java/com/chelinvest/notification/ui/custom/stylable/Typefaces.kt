package com.chelinvest.notification.ui.custom.stylable

import android.content.Context
import android.graphics.Typeface

class Typefaces {

    companion object {

        private var montserratRegular: Typeface? = null
        private var montserratBold: Typeface? = null
        private var montserratLight: Typeface? = null
        private var bebasNeueRegular: Typeface? = null

        fun getDefaultRegular(context: Context): Typeface {
            val typeface = montserratRegular ?: Typeface.createFromAsset(context.assets, "fonts/montserrat_regular.otf")
            if (montserratRegular == null)
                montserratRegular = typeface
            return typeface
        }

        fun getDefaultBold(context: Context): Typeface {
            val typeface = montserratBold ?: Typeface.createFromAsset(context.assets, "fonts/montserrat_bold.otf")
            if (montserratBold == null)
                montserratBold = typeface
            return typeface
        }

        fun getDefaultLight(context: Context): Typeface {
            val typeface = montserratLight ?: Typeface.createFromAsset(context.assets, "fonts/montserrat_light.otf")
            if (montserratLight == null)
                montserratLight = typeface
            return typeface
        }

        fun getBebasNeueRegular(context: Context): Typeface {
            val typeface = bebasNeueRegular ?: Typeface.createFromAsset(context.assets, "fonts/bebas_neue_regular.otf")
            if (bebasNeueRegular == null)
                bebasNeueRegular = typeface
            return typeface
        }

    }

}