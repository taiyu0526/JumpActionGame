package jp.texhacademy.taiyu.kim.jumpactiongame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport

class ResultScrenn (private val mGame: JunpActionGame, private val mScore: Int):ScreenAdapter(){


    companion object{

        internal val GUI_WIDTH = 320f
        internal val GUI_HEIGHT = 480f
    }
    private var mBg: Sprite
    private var mGuiCamera: OrthographicCamera
    private var mGuiViewPort: FitViewport
    private var mFont: BitmapFont

    init {

        if (mGame.mRequestHandler != null){

            mGame.mRequestHandler.showAds(true)
        }

        // 背景の準備
        val bgTexture = Texture("bg2.png")
        mBg = Sprite(TextureRegion(bgTexture, 0, 0, 520, 750))
        mBg.setSize(GUI_WIDTH, GUI_HEIGHT)
        mBg.setPosition(0f, 0f)

        // GUI用のカメラを設定する
        mGuiCamera = OrthographicCamera()
        mGuiCamera.setToOrtho(false, GUI_WIDTH, GUI_HEIGHT)
        mGuiViewPort = FitViewport(GUI_WIDTH, GUI_HEIGHT, mGuiCamera)

        // フォント
        mFont = BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false)
    }

    override fun render(delta: Float) {
        // 描画する
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // カメラの座標をアップデート（計算）し、スプライトの表示に反映させる
        mGuiCamera.update()
        mGame.batch.projectionMatrix = mGuiCamera.combined

        mGame.batch.begin()
        mBg.draw(mGame.batch)
        mFont.draw(mGame.batch, "Score: $mScore", 0f, GUI_HEIGHT / 2 + 200, GUI_WIDTH, Align.center, false)
        mFont.draw(mGame.batch, "Retry?", 0f, GUI_HEIGHT / 2 + 150, GUI_WIDTH, Align.center, false)
        mGame.batch.end()

        if (Gdx.input.justTouched()) {
            if (mGame.mRequestHandler != null){
                mGame.mRequestHandler.showAds(false)
            }
            mGame.screen = GameScreen(mGame)
        }
    }


















}