package com.stewsters.forkknife

import java.io.File

private val maleNames = File("data/maleNames.txt").readText()
    .split("\n")
    .map { it.trim() }

private val femaleNames = File("data/femaleNames.txt").readText()
    .split("\n")
    .map { it.trim() }

//private val surnames = File("/data/surnames.txt").readText()
//    .split("\n")
//    .map { it.trim() }


fun getMaleName() = maleNames.random() //+ " " + surnames.random()

fun getFemaleName() = femaleNames.random() //+ " " + surnames.random()
