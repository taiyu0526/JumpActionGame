package jp.texhacademy.taiyu.kim.jumpactiongame

import com.badlogic.gdx.graphics.Texture

class Ufo(texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {


    // UFOはゲームの中で、ゴール地点なので、動かない。大きさを変えることによってゲームの難易度を変えることもできそう。
    companion object {
        // 横幅、高さ
        val UFO_WIDTH = 2.0f
        val UFO_HEIGHT = 1.3f
    }


    //コンストラクタでは、サイズの設定をするだけでOK
    init {
        setSize(UFO_WIDTH, UFO_HEIGHT)
    }

}