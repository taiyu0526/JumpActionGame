package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.Game//スクリーンアダプタクラスという１画面に相当するクラスを設定して画面遷移を行える機能を持つ
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.g2d.SpriteBatch//画僧をGPUで効率的に描画するクラス
import com.badlogic.gdx.Gdx



class JunpActionGame(val mRequestHandler: ActivityRequestHandler) : Game() { //継承もとのクラスをGameクラスにする//


    lateinit var batch: SpriteBatch//プロパティ
    lateinit var music : Music




    override fun create() {
        batch = SpriteBatch()//バッチをパブリックにする

        music = Gdx.audio.newMusic(Gdx.files.internal("data/bgm.mp3"))

        music.setLooping(true);


        music.play()



        setScreen(GameScreen(this))//セットスクリーンメソッドでGameScreenクラスをセットし、ゲーム画面を表示するために使う



    }





}
