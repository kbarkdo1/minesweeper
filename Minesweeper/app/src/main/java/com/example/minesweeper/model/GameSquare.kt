package com.example.minesweeper.model

class GameSquare() {
    private var bomb = false
    private var flag = false
    private var count = 0
    private var revealed = false

    fun isBomb(): Boolean { return bomb}
    fun isFlagged(): Boolean { return flag}
    fun number(): Int { return count}
    fun isRevealed(): Boolean { return revealed}

    fun getCount(): Int {return count}
    fun setBomb() {
        bomb = true
    }
    fun flipFlag() {
        flag = !flag
    }
    fun assignCount(c: Int) {
        count = c
    }
    fun makeRevealed() {
        revealed = true
    }

    fun reset() {
        revealed = false
        flag = false
        bomb = false
        count = 0
    }

}