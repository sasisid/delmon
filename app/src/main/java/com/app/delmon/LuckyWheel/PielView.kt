package com.app.delmon.LuckyWheel

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.res.ResourcesCompat

import com.app.delmon.R
import com.app.delmon.LuckyWheel.LuckyWheelUtils.drawableToBitmap
import com.app.delmon.Model.SpinnerListData
import kotlin.math.min


class PielView : View {
    private var mRange: RectF = RectF()
    private val mRange1: RectF = RectF()
    private var mRadius = 0
    private var mArcPaint: Paint? = null
    private var mBackgroundPaint: Paint? = null
    private var mTextPaint: Paint? = null
    private val mStartAngle = 0f
    private var mCenter = 0
    private var mPadding = 0
    private var mTargetIndex = 0
    private var mRoundOfNumber = 4
    private var isRunning = false
    private var defaultBackgroundColor = -1
    private var drawableCenterImage: Drawable? = null
    private var textColor: Int = Color.WHITE
    private var mLuckyItemList: List<SpinnerListData.Data>? = null
    private var mPieRotateListener: PieRotateListener? = null

    interface PieRotateListener {
        fun rotateDone(index: Int)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setPieRotateListener(listener: PieRotateListener?) {
        mPieRotateListener = listener
    }

    private fun init() {
        mArcPaint = Paint()
        mArcPaint!!.isAntiAlias = true
        mArcPaint!!.isDither = true
        mTextPaint = Paint()
        mTextPaint!!.color = textColor
        mTextPaint!!.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10F,
            resources.displayMetrics
        )
        val font: Typeface = ResourcesCompat.getFont(context, R.font.poppins_semibold)!!
        mTextPaint!!.typeface = font
        mTextPaint!!.letterSpacing = 0.3.toFloat()
        mRange = RectF(mPadding.toFloat(), mPadding.toFloat(),
            (mPadding + mRadius).toFloat(), (mPadding + mRadius).toFloat()
        )
    }

    fun setData(luckyItemList: List<SpinnerListData.Data>?) {
        mLuckyItemList = luckyItemList
        invalidate()
    }

    fun setPieBackgroundColor(color: Int) {
        defaultBackgroundColor = color
        invalidate()
    }

    fun setPieCenterImage(drawable: Drawable?) {
        drawableCenterImage = drawable
        invalidate()
    }

    fun setPieTextColor(color: Int) {
        textColor = color
        invalidate()
    }

    private fun drawPieBackgroundWithBitmap(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(
            bitmap, null, Rect(
                mPadding / 2, mPadding / 2,
                measuredWidth - mPadding / 2, measuredHeight - mPadding / 2
            ), null
        )
    }

    /**
     *
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mLuckyItemList == null) {
            return
        }
        drawBackgroundColor(canvas, defaultBackgroundColor)
        init()
        var tmpAngle = mStartAngle
        val sweepAngle: Float = (360 / mLuckyItemList!!.size).toFloat()
        for (i in 0 until mLuckyItemList!!.size) {
            if (i%2==0){
                mArcPaint!!.color = Color.parseColor("#752BEF")
            }else{
                mArcPaint!!.color = Color.parseColor("#8E84FF")
            }
//            mArcPaint!!.color = mLuckyItemList!![i].color
            canvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint!!)
//            drawText(canvas, tmpAngle, sweepAngle, mLuckyItemList!![i].title!!)
            if (mLuckyItemList!![i].type=="GIFT"){
               drawImage(canvas, tmpAngle, BitmapFactory.decodeResource(resources, R.drawable.spinner_gift))
            }else{
                drawImage(canvas, tmpAngle, BitmapFactory.decodeResource(resources, R.drawable.spin_cash))
            }
    //            if (mLuckyItemList!![i].icon!=null && mLuckyItemList!![i].icon!=0){
    //                Log.d("TAG", "onDraw:$canvas == $tmpAngle == ${ mLuckyItemList!![i].icon}")
    //                drawImage(canvas, tmpAngle, BitmapFactory.decodeResource(resources, mLuckyItemList!![i].icon))
    //            }
            tmpAngle += sweepAngle
        }
        drawCenterImage(canvas, drawableCenterImage)
    }

    private fun drawBackgroundColor(canvas: Canvas, color: Int) {
        if (color == -1) return
        mBackgroundPaint = Paint()
        mBackgroundPaint!!.color = color
        canvas.drawCircle(mCenter.toFloat(), mCenter.toFloat(), mCenter.toFloat(),
            mBackgroundPaint!!
        )
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width: Int = min(measuredWidth, measuredHeight)
        mPadding = if (paddingLeft === 0) 10 else paddingLeft
        mRadius = width - mPadding * 2
        mCenter = width / 2
        setMeasuredDimension(width, width)
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param bitmap
     */
    private fun drawImage(canvas: Canvas, tmpAngle: Float, bitmap: Bitmap) {
        Log.d("TAG", "drawImage: ")

        val imgWidth: Int = mRadius / mLuckyItemList!!.size
        val angle = ((tmpAngle + 360 / mLuckyItemList!!.size / 2) * Math.PI / 180) as Double
        val x = (mCenter + mRadius / 2 / 2 * Math.cos(angle.toDouble())).toInt()
        val y = (mCenter + mRadius / 2 / 2 * Math.sin(angle.toDouble())).toInt()
        val rect = Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2)
        canvas.drawBitmap(bitmap, null, rect, null)
    }

    private fun drawCenterImage(canvas: Canvas, drawable: Drawable?) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        var bitmap = drawableToBitmap(drawable!!)
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 90, 90, false)
        canvas.drawBitmap(bitmap,
            (measuredWidth / 2 - bitmap.width / 2).toFloat(),
            (measuredHeight / 2 - bitmap.height / 2).toFloat(),
            null)
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    private fun drawText(canvas: Canvas, tmpAngle: Float, sweepAngle: Float, mStr: String) {
        val path = Path()
        path.addArc(mRange, tmpAngle, sweepAngle)
        val textWidth: Float = mTextPaint!!.measureText(mStr)
        val hOffset = (mRadius * Math.PI / mLuckyItemList!!.size / 2 - textWidth / 2)
        val vOffset = mRadius / 2 / 3
        canvas.drawTextOnPath(mStr, path, hOffset.toFloat(), (vOffset - 35).toFloat(), mTextPaint!!)
    }

    /**
     * @return
     */
    private val angleOfIndexTarget: Float
        private get() {
            val tempIndex = if (mTargetIndex == 0) 1 else mTargetIndex
            return (360 / mLuckyItemList!!.size * tempIndex).toFloat()
        }

    /**
     * @param numberOfRound
     */
    fun setRound(numberOfRound: Int) {
        mRoundOfNumber = numberOfRound
    }

    /**
     * @param index
     */
    fun rotateTo(index: Int) {
        if (isRunning) {
            return
        }
        mTargetIndex = index
        rotation = 0F
        val targetAngle: Float =
            360 * mRoundOfNumber + 270 - angleOfIndexTarget + 360 / mLuckyItemList!!.size / 2
        animate()
            .setInterpolator(DecelerateInterpolator())
            .setDuration(mRoundOfNumber * 250 + 1500L)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(p0: Animator) {
                    isRunning = true
                }

                override fun onAnimationEnd(p0: Animator) {
                    isRunning = false
                    if (mPieRotateListener != null) {
                        mPieRotateListener!!.rotateDone(mTargetIndex)
                    }
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
            .rotation(targetAngle)
            .start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}