package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.GameType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Mines AI Assistant", appName)
    }

    @Test
    fun `verify game detection from target url`() {
        val url = "https://1win.ng/casino/play/v_1wingames:double?p=x8kz"
        val detected = GameType.detectFromUrl(url)
        assertEquals(GameType.DOUBLE_DRUM, detected)
    }
}
