package com.example.minesweeper.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.minesweeper.MainActivity
import com.example.minesweeper.R
import com.example.minesweeper.model.MineSweeperModel

class MineSweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    private var flagCount: Int = 9
    private val gridWidth = 10
    private val gridHeight = 10
    private val model = MineSweeperModel(gridWidth, gridHeight)
    private var mode: Boolean = false
    private var over: Boolean = false
    private val revealedBM = BitmapFactory.decodeResource(resources, R.drawable.revealed)
    private val flagBM = BitmapFactory.decodeResource(resources, R.drawable.flagged)
    private val bombBM = BitmapFactory.decodeResource(resources, R.drawable.bomb)
    private val coveredBM = BitmapFactory.decodeResource(resources, R.drawable.covered)
    private val textPaints = arrayOf(Paint(),Paint(),Paint(),Paint(),Paint(),Paint(),Paint(),Paint())

    init {

        textPaints[0].color = Color.BLACK
        textPaints[1].color = Color.GREEN
        textPaints[2].color = Color.RED
        textPaints[3].color = Color.MAGENTA
        textPaints[4].color = Color.YELLOW
        textPaints[5].color = Color.LTGRAY
        textPaints[6].color = Color.CYAN
        textPaints[7].color = Color.DKGRAY

        textPaints.forEach {
            it.textSize = 5f
        }
    }
    init {
        // setting bombs
        initialize()
    }

    private fun initialize() {
        flagCount = 9
        mode = false
        over = false;

        val bombsX = arrayOf(1, 5, 3, 8, 9, 7, 7, 3, 6)
        val bombsY = arrayOf(4, 6, 2, 9, 5, 7, 3, 5, 2)
        for (i in 0..8) {
            model.setBomb(bombsX[i], bombsY[i])
        }
        // setting numbers
        model.calculateNumerics()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        textPaints.forEach {
            it.textSize = (height/gridHeight).toFloat()
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!over) {
            for (i in 0..<gridWidth) {
                for (j in 0..<gridHeight) {
                    if (model.board[i][j].isRevealed()) {
                        if (model.board[i][j].isBomb()) {
                            canvas?.drawBitmap(bombBM, null, RectF((width / gridWidth.toFloat() * i), (height / gridHeight.toFloat() * j), (width / gridWidth.toFloat() * (i+1)), (height / gridHeight.toFloat() * (j+1))), null)
                        } else {
                            canvas?.drawBitmap(revealedBM, null, RectF((width / gridWidth.toFloat() * i), (height / gridHeight.toFloat() * j), (width / gridWidth.toFloat() * (i+1)), (height / gridHeight.toFloat() * (j+1))), null)
                            var num = model.getNumber(i, j)
                            if (!(num == 0)) {
                                canvas?.drawText(model.getNumber(i, j).toString(), (width / gridWidth.toFloat() * (i.toFloat()+0.25)).toFloat(), height / gridHeight.toFloat() * (j+1), textPaints[model.getNumber(i, j)-1])
                            }
                        }
                    } else if (model.board[i][j].isFlagged()) {
                        canvas?.drawBitmap(flagBM, null, RectF((width / gridWidth.toFloat() * i), (height / gridHeight.toFloat() * j), (width / gridWidth.toFloat() * (i+1)), (height / gridHeight.toFloat() * (j+1))), null)
                    } else { // if (!model.board[i][j].isRevealed())
                        canvas?.drawBitmap(coveredBM, null, RectF((width / gridWidth.toFloat() * i), (height / gridHeight.toFloat() * j), (width / gridWidth.toFloat() * (i+1)), (height / gridHeight.toFloat() * (j+1))), null)
                    }
                }
            }
        } else {
            for (i in 0..<gridWidth) {
                for (j in 0..<gridHeight) {
                    if (model.board[i][j].isBomb()) {
                        canvas?.drawBitmap(bombBM, null, RectF((width / gridWidth.toFloat() * i), (height / gridHeight.toFloat() * j), (width / gridWidth.toFloat() * (i+1)), (height / gridHeight.toFloat() * (j+1))), null)
                    } else {
                        canvas?.drawBitmap(revealedBM, null, RectF((width / gridWidth.toFloat() * i), (height / gridHeight.toFloat() * j), (width / gridWidth.toFloat() * (i+1)), (height / gridHeight.toFloat() * (j+1))), null)
                        var num = model.getNumber(i, j)
                        if (!(num == 0)) {
                            canvas?.drawText(model.getNumber(i, j).toString(),
                                (width / gridWidth.toFloat() * (i.toFloat()+0.25)).toFloat(), height / gridHeight.toFloat() * (j+1), textPaints[model.getNumber(i, j)-1])
                        }
                    }
                }
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!over) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val tX = event.x.toInt() / (width / gridWidth)
                val tY = event.y.toInt() / (height / gridHeight)

                if (mode) {
                    if (model.isBomb(tX, tY)) {
                        model.flagFlip(tX, tY)
                    } else {
                        endGame("flagged non-bomb")
                    }

                    flagCount -= 1
                    if (flagCount == 0) {
                        (context as MainActivity).win()
                        over = true
                    }
                } else {
                    if (model.isBomb(tX, tY)) {
                        endGame("revealed bomb")
                    } else {
                        reveal(tX,tY)
                    }
                }
                invalidate()
            }
        }
        return true
    }

    private fun reveal(tX: Int, tY: Int) {
        if (model.isBomb(tX, tY)) {
            over = true
            invalidate()
            return
        } else if (model.getNumber(tX,tY) == 0) {
            model.revealingBFS(tX, tY)
        }
        model.revealSquare(tX, tY)
    }

    private fun endGame(s: String) {
        over = true
        (context as MainActivity).lose()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    fun resetGame() {
        over = false
        flagCount = 9
        if (mode == true) {
            changeMode()
        }
        for (i in 0..<gridWidth) {
            for (j in 0..<gridHeight) {
                model.board[i][j].reset()
            }
        }
        initialize()

        invalidate()
    }

    fun changeMode() {
        mode = !mode
        if (mode == true) {
            (context as MainActivity).changeButtonText(context.getString(R.string.reveal))
        } else {
            (context as MainActivity).changeButtonText(context.getString(R.string.flag))
        }
    }
}