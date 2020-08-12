package lm

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MusicPlayerImpl : IMusicPlayer{
    override fun play(path: String) {
        print( "MusicPlayerImpl play: $path")
    }

}