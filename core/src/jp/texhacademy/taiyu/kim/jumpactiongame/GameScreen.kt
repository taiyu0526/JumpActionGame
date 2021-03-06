package jp.texhacademy.taiyu.kim.jumpactiongame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter//スクリーンアダプタクラス
import com.badlogic.gdx.graphics.GL20//指定した色で塗りつぶしを行うクラス
import com.badlogic.gdx.graphics.Texture//テクスチャクラススプライトに貼り付ける画像
import com.badlogic.gdx.Preferences//スコアを永続化するための「環境設定」クラス
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.BitmapFont//フォントを表示させるクラス
import com.badlogic.gdx.graphics.g2d.Sprite//高速に画像を描画するためのクラス
import com.badlogic.gdx.graphics.g2d.TextureRegion//テクスチャを切り取って貼り付けるためのクラス
import com.badlogic.gdx.utils.viewport.FitViewport //ビューポートクラス
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.OrthographicCamera //カメラクラス


import java.util.*
import kotlin.collections.ArrayList


class GameScreen(private val mGame: JunpActionGame): ScreenAdapter() {
    //定数だと思ってください
    companion object{
        //カメラのサイズを表す定数を定義
        val CAMERA_WIDTH = 10f
        val CAMERA_HEIGHT = 15f


        val WORLD_WIDTH = 10f
        val WORLD_HEIGHT = 15 * 20 //２０画面分登ると終了


                //GUI用のカメラ
        val GUI_WIDTH = 320f
        val GUI_HEIGHT = 480f




        val GAME_STATE_READY = 0
        val GAME_STATE_PLAYING = 1
        val GAME_STATE_GAMEOVER = 2

        val GRAVITY = -12
    }


    private val mBg : Sprite//mBgというプロパティにスプライトクラスを付けている
    private val mCamera : OrthographicCamera //カメラを表す「オートグラフィックカメラ」クラス
    private val mViewport: FitViewport //ビューポートを表す「フィットビューポート」クラス

    private val mGuiCamera : OrthographicCamera //
    private val mGuiViewport: FitViewport

    private var mRandom: Random

    private var mSteps: ArrayList<Step>
    private var mStars: ArrayList<Star>
    private lateinit var mUfo: Ufo
    private lateinit var mPlayer: Player
    private lateinit var mEnemy: ArrayList<Enemy>

    private var mGameState: Int
    private var mHeightSoFar: Float = 0f
    private var mTouchPoint: Vector3

    private var mFont:BitmapFont //ビットマップフォントプロパティ
    private var mScore: Int//スコアを保持するプロパティ
    private var mHighScore : Int//ハイスコアを保持するプロパティ

    private var mPrefs: Preferences//環境設定を保持するプロパティ

    var sound : Sound

    var sound2 :Sound

    var sound3:Sound







    init{
        //背景の準備
        val bgTexture = Texture("bg1.png")//テクスチャクラスで、使用する画像を指定している
        mBg = Sprite(TextureRegion(bgTexture, 0, 0, 520,750))//テクスチャとして用意した画像を切り取って貼り付けるために使用

        mBg.setSize(CAMERA_WIDTH, CAMERA_HEIGHT)

        mBg.setPosition(0f, 0f)//スプライトクラスのsetpositionメソッドで描画する位置を指定している（左下を基準としている）

        //カメラ、ビューポートを生成、設定する
        mCamera = OrthographicCamera()
        mCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT)//縦横比が固定される？
        mViewport = FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT,mCamera)

        // GUI用のカメラを設定する
        mGuiCamera = OrthographicCamera()   // ←追加する
        mGuiCamera.setToOrtho(false, GUI_WIDTH, GUI_HEIGHT) // ←追加する
        mGuiViewport = FitViewport(GUI_WIDTH, GUI_HEIGHT, mGuiCamera)   // ←追加する


        mRandom = Random()
        mSteps = ArrayList<Step>()
        mStars = ArrayList<Star>()
        mEnemy = ArrayList<Enemy>()
        mGameState = GAME_STATE_READY
        mTouchPoint = Vector3()


        mFont = BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"),false)
        mFont.data.setScale(0.8f)//フォントのサイズを０.８倍に小さく
        mScore = 0
        mHighScore = 0

        sound = Gdx.audio.newSound(Gdx.files.internal("data/coinget.mp3"))

        sound2 = Gdx.audio.newSound(Gdx.files.internal("data/sound2.mp3"))

        sound3 = Gdx.audio.newSound(Gdx.files.internal("data/himei.mp3"))





        // ハイスコアをPreferencesから取得する
        mPrefs = Gdx.app.getPreferences("taiyuJumpActionGame") // ←追加する
        mHighScore = mPrefs.getInteger("HIGHSCORE", 0) // ←追加する


        createStage()

    }




    //上のコンストラクタで準備したスプライトをレンダーメソッド内で描画する。
    override fun render(delta: Float){


        //それぞれの状態をアップデートする
        update(delta)



        //画面を描画する準備をする二つのメソッド
        Gdx.gl.glClearColor(0f,0f,0f,1f)//画面をクリアする時の色を赤、緑、青、透過で表す
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)//上のメソッドで指定した色でクリア（つまり塗り潰し）を行う
        //この二つは必ず必要な処理！！


        //カメラの座礁をアップデート（計算）して、スプライトバッチメソッドになったバッチに付けて表示に反映させる
        mCamera.update()
        mGame.batch.projectionMatrix = mCamera.combined
        //スプライトバッチクラスの「セットプロジェクションマトリクスメソッド」をオルグラフィックカメラクラスのコンバインプロパティを引数に与えて呼び出す。
        //流れとしては①アップデートメソッドで、カメラの座標値を計算→　②projectionMatrixメソッド と mcombinedプラパティで座標をスプライトに反映している


        //カメラの中心を超えたらカメラをプレイヤーのy上に移動させる　
        if (mPlayer.y > mCamera.position.y){

            mCamera.position.y = mPlayer.y
        }//これでプレイヤーに合わせてカメラが動くようになるが、途中からタッチしても反応しなくなる。カメラの座標がどんどん上に上がって、タッチする領域が画面の外に出てしますから。
        //これを解決するために、GUI用のカメラを別途用意する



        //[！重要！]描画をするさいはスプライトバッチクラスのbeginメソッドとendメソッドの間で行う！
        mGame.batch.begin()//ジャンプアクションゲームクラスを継承したmGameの→スプライトバッチクラスを継承したバッチにbeginをつける


        mBg.setPosition(mCamera.position.x - CAMERA_WIDTH / 2, mCamera.position.y - CAMERA_HEIGHT / 2)
        mBg.draw(mGame.batch)//drawする。つまりmBg描画する(Bgはバックグラウンドの略)

        //bgm



        // Step
        for (i in 0 until mSteps.size) {
            mSteps[i].draw(mGame.batch)
        }

        // Star
        for (i in 0 until mStars.size) {
            mStars[i].draw(mGame.batch)
        }

        //UFO
        mUfo.draw(mGame.batch)

        //Player
        mPlayer.draw(mGame.batch)

        //enemy
        for (i in 0 until mEnemy.size) {
            mEnemy[i].draw(mGame.batch)
        }


        mGame.batch.end()
        //beginは日本語で「始める」endは「終わる」



        //このままだと偏って表示されるので、カメラとビューポートという仕組みを使う（7/24）

        //スコアの表示
        mGuiCamera.update()
        mGame.batch.projectionMatrix = mGuiCamera.combined

        mGame.batch.begin()

        mFont.draw(mGame.batch, "HighScore: $mHighScore", 16f, GUI_HEIGHT -15)
        mFont.draw(mGame.batch, "Score: $mScore", 16f, GUI_HEIGHT -35)

        mGame.batch.end()


    }

    //resizeメソッドをオーバーライドして、フィットビューポートクラスのアップデートメソッドを呼び出す
    //リサイズメソッドは、物理的な画面のサイズが変更された時に呼び出されるメソッド（物理的な画面のサイズが変更とは？）
    override fun resize(width: Int, height: Int){
        mViewport.update(width, height)
        mGuiViewport.update(width, height)
    }



    // ステージを作成する
    private fun createStage() {


        // テクスチャの準備
        val stepTexture =Texture("kumo.png")
        val starTexture = Texture("coin.png")
        val playerTexture = Texture("uma.png")
        val ufoTexture = Texture("ufo.png")
        val enemyTexture = Texture("enemy.png")

        // StepとStarをゴールの高さまで配置していく
        var y = 0f

        val maxJumpHeight = Player.PLAYER_JUMP_VELOCITY * Player.PLAYER_JUMP_VELOCITY / (2 * -GRAVITY)

        while (y < WORLD_HEIGHT - 5) {

            val type = if(mRandom.nextFloat() > 0.9f) Step.STEP_TYPE_MOVING else Step.STEP_TYPE_STATIC
            //ランダムでステップが止まっているものか、動いているものになるこの場合は、８０％の確率で止まっているもの


            val x = mRandom.nextFloat() * (WORLD_WIDTH - Step.STEP_WIDTH)

            val step = Step(type, stepTexture, 0, 0, 256, 126)
            step.setPosition(x, y)
            mSteps.add(step)

            if (mRandom.nextFloat() > 0.6f) {
                val star = Star(starTexture, 0, 0, 128 , 128)
                star.setPosition(step.x + mRandom.nextFloat(), step.y + Star.STAR_HEIGHT + mRandom.nextFloat() * 3)
                mStars.add(star)
            }


            //ランダムで敵が出てくる確率。例えば0.5だと５０％
            if (mRandom.nextFloat() > 0.8f){

                val enemy = Enemy(enemyTexture,0,0,120,120)
                enemy.setPosition(step.x - mRandom.nextFloat(), step.y  - mRandom.nextFloat() * 3)
                mEnemy.add(enemy)
            }

            y += (maxJumpHeight - 0.5f)

            y -= mRandom.nextFloat() * (maxJumpHeight / 3)
        }




        // Playerを配置
        mPlayer = Player(playerTexture, 0, 0, 126, 126)
        mPlayer.setPosition(WORLD_WIDTH / 2 - mPlayer.width / 2 , Step.STEP_HEIGHT)

        // ゴールのUFOを配置
        mUfo = Ufo(ufoTexture, 0, 0, 120, 74)
        mUfo.setPosition(WORLD_WIDTH / 2 - Ufo.UFO_WIDTH / 2, y)
    }






    // それぞれのオブジェクトの状態をアップデートする
    private fun update(delta: Float) {
        when (mGameState) {
            GAME_STATE_READY ->
                updateReady()

            GAME_STATE_PLAYING ->

                updatePlaying(delta)

            GAME_STATE_GAMEOVER ->
                updateGameOver()
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched()) {
            mGameState = GAME_STATE_PLAYING
        }
    }

    private fun updatePlaying(delta: Float) {
        var accel = 0f

        if (Gdx.input.isTouched) {
            mGuiViewport.unproject(mTouchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            val left = Rectangle(0f, 0f, GUI_WIDTH / 2, GUI_HEIGHT)
            val right = Rectangle(GUI_WIDTH / 2, 0f, GUI_WIDTH / 2, GUI_HEIGHT)
            if (left.contains(mTouchPoint.x, mTouchPoint.y)) {
                accel = 5.0f
            }
            if (right.contains(mTouchPoint.x, mTouchPoint.y)) {
                accel = -5.0f
            }
        }

        // Step
        for (i in 0 until mSteps.size) {
            mSteps[i].update(delta)
        }

        // Player
        if (mPlayer.y <= 0.5f) {
            mPlayer.hitStep()
        }
        mPlayer.update(delta, accel)
        mHeightSoFar = Math.max(mPlayer.y, mHeightSoFar)

        checkCollision()


        checkGameOver()
    }

    //UFOとの当たり判定ボーディングリアクティングルプロパティとは日本語で「境界矩形」つまり長方形の境界を作る
    private fun checkCollision(){
        //mPlayerの矩形の境界が、重なったら→どこに？→mUfoの矩形の境界と→ゲームのステータスを「ゲームオーバー」状態にする
        if (mPlayer.boundingRectangle.overlaps(mUfo.boundingRectangle)){


            sound2.play(0.5f)
            mGameState = GAME_STATE_GAMEOVER
            return
        }

        //エネミーとの当たり判定、当たったらゲームオーバー
        for (i in 0 until mEnemy.size){

            val enemy = mEnemy[i]

            if (mPlayer.boundingRectangle.overlaps(enemy.boundingRectangle)){

                sound3.play()
                mGameState = GAME_STATE_GAMEOVER
            }


            }





                //スターとの当たり判定

        for (i in 0 until mStars.size){

            val star = mStars[i]

            if (star.mState == Star.STAR_NONE){

                continue
            }

            if (mPlayer.boundingRectangle.overlaps(star.boundingRectangle)){

                star.get()
                sound.play(0.5f)

                //スターを取った後、スコアに反映させる
                mScore++
                if (mScore > mHighScore){

                    mHighScore = mScore

                    mPrefs.putInteger("HIGHSCORE", mHighScore) //第１引数にキー、第２引数に値を指定する
                    mPrefs.flush() //flushメソッドは値を永続化する。呼び忘れに注意！
                }


                break

            }



        }


        //ステップとの当たり判定
        //上昇中はステップとの当たり判定を確認しない

        if (mPlayer.velocity.y > 0){

            return
        }

        for (i in 0 until mSteps.size){

            val step = mSteps[i]

            if (step.mState == Step.STEP_STATE_VANISH){

                continue
            }

            if (mPlayer.y > step.y){
                if (mPlayer.boundingRectangle.overlaps(step.boundingRectangle)){

                    mPlayer.hitStep()

                    if (mRandom.nextFloat() > 0.5f){

                        step.vanish()
                    }

                    break
                }
            }
        }


    }


    private fun checkGameOver(){



        if (mHeightSoFar - CAMERA_HEIGHT / 2 > mPlayer.y) {
            Gdx.app.log("JampActionGame", "GAMEOVER")
            sound3.play()
            mGameState = GAME_STATE_GAMEOVER
        }
    }




    private fun updateGameOver() {

        if (Gdx.input.justTouched()){
            mGame.screen = ResultScrenn(mGame,mScore)
        }

    }






}