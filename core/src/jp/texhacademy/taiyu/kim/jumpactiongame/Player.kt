package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.graphics.Texture

class Player (texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {

        //プレイヤーの横幅と高さを設定
        val PLAYER_WIDTH = 1.0f
        val PLAYER_HEIGHT = 1.0f

        //状態→　０の時はジャンプ、１のときは落ちている最中
        val PLAYER_STATE_JUMP = 0
        val PLAYER_STATE_FALL = 1

        //速度
        val PLAYER_JUMP_VELOCITY = 11.0f
        val PLAYER_MOVE_VELOCITY = 20.0f
    }

    private var mState: Int //状態を保持するmStateを定義

    //コンストラクタでセットサイズメソッドによるサイズ指定と、mStateに落ちている最中を設定　これは初期化？　
    init {
        setSize(PLAYER_WIDTH, PLAYER_HEIGHT)
        mState = PLAYER_STATE_FALL
    }

    //スクリーンのrenderメソッドから呼ばれることを想定しているアップデートメソッド
    //ここでは表示する位置の決定と、状態が変わるかどうかの確認を行う
    fun update(delta: Float, accelX: Float) {

        velocity.add(0f, GameScreen.GRAVITY * delta)
        velocity.x = -accelX / 10 * PLAYER_MOVE_VELOCITY
        setPosition(x + velocity.x * delta, y + velocity.y * delta)

        // y方向の速度が正（＝上方向）のときにSTATEがPLAYER_STATE_JUMPでなければPLAYER_STATE_JUMPにする
        if (velocity.y > 0) {
            if (mState != PLAYER_STATE_JUMP) {
                mState = PLAYER_STATE_JUMP
            }
        }

        // y方向の速度が負（＝下方向）のときにSTATEがPLAYER_STATE_FALLでなければPLAYER_STATE_FALLにする
        if (velocity.y < 0) {
            if (mState != PLAYER_STATE_FALL) {
                mState = PLAYER_STATE_FALL
            }
        }

        // 画面の端まで来たら反対側に移動させる
        if (x + PLAYER_WIDTH / 2 < 0) {
            x = GameScreen.WORLD_WIDTH - PLAYER_WIDTH / 2
        } else if (x + PLAYER_WIDTH / 2 > GameScreen.WORLD_WIDTH) {
            x = 0f
        }

    }
        fun hitStep() {
            velocity.y = PLAYER_JUMP_VELOCITY
            mState = PLAYER_STATE_JUMP
        }

    }

