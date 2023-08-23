package com.pixelro.nenoonkiosk.data

import com.pixelro.nenoonkiosk.NenoonKioskApplication
import com.pixelro.nenoonkiosk.R
import java.util.Locale

object StringProvider {
    fun getString(
        id: Int
    ): String {

        return NenoonKioskApplication.applicationContext().
            createConfigurationContext(NenoonKioskApplication.applicationContext()
            .resources.configuration).getString(id)
    }
}