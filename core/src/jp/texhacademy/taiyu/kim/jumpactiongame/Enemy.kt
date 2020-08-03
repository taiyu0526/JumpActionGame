package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class Enemy(texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : Sprite(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {
        // 横幅、高さ
        val ENEMY_WIDTH = 1.0f
        val ENEMY_HEIGHT = 1.0f

        // 状態
        val ENEMY_EXIST = 0
        val ENEMY_SMILE = 1
    }

    var mState: Int = 0

    //コンストラクタでは、サイズの設定と、状態をスターexist(存在する状態)と初期設定する
    //星は動かないから、アップデートメソッドは書かない。
    init {
        setSize(ENEMY_WIDTH, ENEMY_HEIGHT)
        mState = ENEMY_EXIST
    }



}