package com.softcare.raphnote.model

interface ChangeObserver {
    fun chang(_long: Long)
    fun change(_string: String?)
}