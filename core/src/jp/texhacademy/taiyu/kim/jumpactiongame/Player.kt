package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.graphics.Texture//テクスチャクラスを設定する

class Player (texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {

    //プレイヤークラス引数には、テクスチャークラス型の、textureという名前ですよ。画像にはx、y座標があって、縦と横がありますよ。
    //さらにGameObjectクラスを継承しているよ。

    //コンパニオンオブジェクトで、横幅や高さ、状態や速度を設定する
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
        setSize(PLAYER_WIDTH, PLAYER_HEIGHT)//setsizeメソッド
        mState = PLAYER_STATE_FALL//初期の状態を、落ちている状態にする
    }

    //スクリーンのrenderメソッドから呼ばれることを想定しているアップデートメソッド
    //ここでは表示する位置の決定と、状態が変わるかどうかの確認を行う
    fun update(delta: Float, accelX: Float) {

        velocity.add(0f, GameScreen.GRAVITY * delta)
        //速度を追加している。ゲームスクリーンに重力がつく？　＊デルタってなんだ？

        velocity.x = -accelX / 10 * PLAYER_MOVE_VELOCITY
        //x軸（横）を移動する速度は、ーアクセルXの１０分の１　＊　プレイヤームーブ速度（ここでは２０.０f）


        setPosition(x + velocity.x * delta, y + velocity.y * delta)
        //セットポジション

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

