package jp.texhacademy.taiyu.kim.jumpactiongame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter//スクリーンアダプタクラス
import com.badlogic.gdx.graphics.GL20//指定した色で塗りつぶしを行うクラス
import com.badlogic.gdx.graphics.Texture//テクスチャクラススプライトに貼り付ける画像
import com.badlogic.gdx.graphics.g2d.Sprite//高速に画像を描画するためのクラス
import com.badlogic.gdx.graphics.g2d.TextureRegion//テクスチャを切り取って貼り付けるためのクラス

import com.badlogic.gdx.utils.viewport.FitViewport //ビューポートクラス
import com.badlogic.gdx.graphics.OrthographicCamera //カメラクラス
import com.badlogic.gdx.graphics.TextureArray
//import com.sun.tools.corba.se.idl.constExpr.Or


class GameScreen(private val mGame: JunpActionGame): ScreenAdapter() {

    //カメラのサイズを表す定数を定義
    companion object{
        val CAMERA_WIDTH = 10f
        val CAMERA_HEIGHT = 15f
    }


    private val mBg : Sprite//mBgというプロパティにスプライトクラスを付けている
    private val mCamera : OrthographicCamera //カメラを表す「オートグラフィックカメラ」クラス
    private val mViewport: FitViewport //ビューポートを表す「フィットビューポート」クラス


    init{
        //背景の準備
        val bgTexture = Texture("back.png")//テクスチャクラスで、使用する画像を指定している
        mBg = Sprite(TextureRegion(bgTexture, 0, 0, 540,810))//テクスチャとして用意した画像を切り取って貼り付けるために使用

        mBg.setSize(CAMERA_WIDTH, CAMERA_HEIGHT)

        mBg.setPosition(0f, 0f)//スプライトクラスのsetpositionメソッドで描画する位置を指定している（左下を基準としている）

        //カメラ、ビューポートを生成、設定する
        mCamera = OrthographicCamera()
        mCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT)//縦横比が固定される？
        mViewport = FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT,mCamera)

    }


    //上のコンストラクタで準備したスプライトをレンダーメソッド内で描画する。
    override fun render(delta: Float){
        //画面を描画する準備をする二つのメソッド
        Gdx.gl.glClearColor(0f,0f,0f,1f)//画面をクリアする時の色を赤、緑、青、透過で表す
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)//上のメソッドで指定した色でクリア（つまり塗り潰し）を行う
        //この二つは必ず必要な処理！！


        //カメラの座礁をアップデート（計算）して、スプライトバッチメソッドになったバッチに付けて表示に反映させる
        mCamera.update()
        mGame.batch.projectionMatrix = mCamera.combined
        //スプライトバッチクラスの「セットプロジェクションマトリクスメソッド」をオルグラフィックカメラクラスのコンバインプロパティを引数に与えて呼び出す。

        //流れとしては①アップデートメソッドで、カメラの座標値を計算→　②projectionMatrixメソッド と mcombinedプラパティで座標をスプライトに反映している


        //[！重要！]描画をするさいはスプライトバッチクラスのbeginメソッドとendメソッドの間で行う！
        mGame.batch.begin()//ジャンプアクションゲームクラスを継承したmGameの→スプライトバッチクラスを継承したバッチにbeginをつける


        mBg.setPosition(mCamera.position.x - CAMERA_WIDTH / 2, mCamera.position.y - CAMERA_HEIGHT / 2)
        mBg.draw(mGame.batch)//drawする。つまりmBg描画する(Bgはバックグラウンドの略)




        mGame.batch.end()
        //beginは日本語で「始める」endは「終わる」

        //このままだと偏って表示されるので、カメラとビューポートという仕組みを使う（7/24）
    }

    //resizeメソッドをオーバーライドして、フィットビューポートクラスのアップデートメソッドを呼び出す
    //リサイズメソッドは、物理的な画面のサイズが変更された時に呼び出されるメソッド（物理的な画面のサイズが変更とは？）
    override fun resize(width: Int, height: Int){
        mViewport.update(width, height)
    }


}