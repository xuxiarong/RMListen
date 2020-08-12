package com.rm.component_comm

import lm.ComponentManager
import lm.IMusicPlayer
import lm.MusicPlayerImpl
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testPlay(){

        var musicPlayerImpl = MusicPlayerImpl()

        ComponentManager.instance.injectMusic(musicPlayerImpl)

        val IMusicPlayer = ComponentManager.instance.getImplWithClass(IMusicPlayer::class.java) as IMusicPlayer
        IMusicPlayer.play("haha")
    }
}