package com.example.minesweeper.model

import android.util.Log
import java.util.LinkedList
import java.util.Queue

class MineSweeperModel(wwidth: Int, hheight: Int) {

    val width = wwidth
    val height = hheight
    val board = ArrayList<ArrayList<GameSquare>>()//(wwidth)
    init {
        for (i in 0..<width) {
            board.add(0, ArrayList<GameSquare>())
            for (j in 0..<height) {
                board[0].add(j, GameSquare())
            }
        }

    }

    fun setBomb(x: Int, y: Int) {
        board[x][y].setBomb()
    }
    fun isBomb(x: Int, y: Int): Boolean {
        return board[x][y].isBomb()
    }
    fun flagFlip(x: Int, y: Int) {
        board[x][y].flipFlag()
    }
    fun revealSquare(x: Int, y: Int) {
        board[x][y].makeRevealed()
    }
    fun getNumber(x: Int, y: Int): Int {
        return board[x][y].getCount()
    }

    fun calculateNumerics() {
        for (i in 0..<width) {
            for (j in 0..<height) {
                if (board[i][j].isBomb()) {
                    continue
                }
                var count = 0
                for (w in i - 1..i + 1) {
                    for (h in j - 1..j + 1) {
                        if ((w < 0) or (w == width) or (h < 0) or (h == height)) {
                            continue
                        } else {
                            if (board[w][h].isBomb()) {
                                count+=1
                            }
                        }
                    }
                }
                board[i][j].assignCount(count)
            }
        }
    }

    fun revealingBFS(tX: Int, tY: Int) {
        val queue: Queue<Pair<Int, Int>> = LinkedList()

        queue.add(Pair(tX, tY))
        while (queue.size>0) {
            val (i, j) = queue.remove()
            board[i][j].makeRevealed()
            for (w in i - 1..i + 1) {
                for (h in j - 1..j + 1) {
                    if ((w < 0) or (w == width) or (h < 0) or (h == height)) {
                        continue
                    } else {
                        if (0 == board[w][h].getCount() && !board[w][h].isRevealed()) {
                            queue.add(Pair(w, h))
                        }
                        board[w][h].makeRevealed()
                    }
                }
            }
        }
    }

}