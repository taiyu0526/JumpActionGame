package jp.texhacademy.taiyu.kim.jumpactiongame

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class Star(texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : Sprite(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {
        // 横幅、高さ
        val STAR_WIDTH = 0.8f
        val STAR_HEIGHT = 0.8f

        // 状態
        val STAR_EXIST = 0
        val STAR_NONE = 1
    }

    var mState: Int = 0

    //コンストラクタでは、サイズの設定と、状態をスターexist(存在する状態)と初期設定する
    //星は動かないから、アップデートメソッドは書かない。
    init {
        setSize(STAR_WIDTH, STAR_HEIGHT)
        mState = STAR_EXIST
    }


    //プレイヤーが星をゲットしたときに、星がNONE（消える状態）にして、setAlphaメソッドで「透明」にする
    fun get() {
        mState = STAR_NONE
        setAlpha(0f)
    }
}