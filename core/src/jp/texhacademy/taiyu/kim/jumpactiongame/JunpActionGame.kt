package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.Game//スクリーンアダプタクラスという１画面に相当するクラスを設定して画面遷移を行える機能を持つ
import com.badlogic.gdx.graphics.g2d.SpriteBatch//画僧をGPUで効率的に描画するクラス



class JunpActionGame : Game() { //継承もとのクラスをGameクラスにする//


    lateinit var batch: SpriteBatch//プロパティ


    override fun create() {
        batch = SpriteBatch()//バッチをパブリックにする
        setScreen(GameScreen(this))//セットスクリーンメソッドでGameScreenクラスをセットし、ゲーム画面を表示するために使う

    }


}
