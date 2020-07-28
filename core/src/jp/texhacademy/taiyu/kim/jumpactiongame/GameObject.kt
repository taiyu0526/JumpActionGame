//ゲームオブジェクトクラスは、スプライトクラスを継承して、「ベクター２」クラスをプロパティに持つクラスとする

package jp.texhacademy.taiyu.kim.jumpactiongame


import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2 //プロパティにX,Yを持つクラス。座標などのX,Yを持つ場合に利用する今回は速度を保持するために使う（他にどんな利用方法が？）



open class GameObject(texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int):Sprite(texture, srcX, srcY, srcWidth, srcHeight){

    val velocity: Vector2

    //初期化する（初期化ってどういう意味があるんだっけ？）
    init{
        velocity = Vector2()
    }
}
