package jp.texhacademy.taiyu.kim.jumpactiongame

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import jp.texhacademy.taiyu.kim.jumpactiongame.JunpActionGame

class AndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(JunpActionGame(), config)
    }
}
