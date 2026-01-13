package com.dofun.safe.deviceinfo.utils

import java.math.BigDecimal
import java.math.RoundingMode

object DecimalUtils {
    private const val DEF_DIV_SCALE: Int = 2

    fun add(value1: Double, value2: Double): Double {
        val b1 = BigDecimal(value1.toString())
        val b2 = BigDecimal(value2.toString())
        return b1.add(b2).toDouble()
    }

    fun sub(value1: Double, value2: Double): Double {
        val b1 = BigDecimal(value1.toString())
        val b2 = BigDecimal(value2.toString())
        return b1.subtract(b2).toDouble()
    }

    fun mul(value1: Double, value2: Double): Double {
        val b1 = BigDecimal(value1.toString())
        val b2 = BigDecimal(value2.toString())
        return b1.multiply(b2).toDouble()
    }

    fun divide(dividend: Double, divisor: Double): Double {
        return divide(dividend, divisor, DEF_DIV_SCALE)
    }

    fun divide(dividend: Double, divisor: Double, scale: Int): Double {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b1 = BigDecimal(dividend.toString())
        val b2 = BigDecimal(divisor.toString())
        return b1.divide(b2, scale, RoundingMode.HALF_UP).toDouble()
    }

    fun round(value: Double, scale: Int): Double {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b = BigDecimal(value.toString())
        val one = BigDecimal("1")
        return b.divide(one, scale, RoundingMode.HALF_UP).toDouble()
    }
}
