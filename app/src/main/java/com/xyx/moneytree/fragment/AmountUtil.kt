package com.xyx.moneytree.fragment

import java.text.DecimalFormat

object AmountUtil {

    private val formatter = DecimalFormat("#,###")

    @JvmStatic
    fun formatAmount(amount: Double): String = formatter.format(amount)
}