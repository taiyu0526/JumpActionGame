package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.Game//スクリーンアダプタクラスという１画面に相当するクラスを設定して画面遷移を行える機能を持つ
import com.badlogic.gdx.graphics.g2d.SpriteBatch//画僧をGPUで効率的に描画するクラス

class JunpActionGame : Game() {

    lateinit var batch: SpriteBatch//メソッドを定義


    override fun create() {
        batch = SpriteBatch()

        setScreen(GameScreen(this))

    }


}
