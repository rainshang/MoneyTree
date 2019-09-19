package com.xyx.moneytree


object ResourceUtil {

    private fun getJsonString(fileName: String) =
        javaClass.getResourceAsStream(fileName)?.let {
            val string = String(it.readBytes())
            it.close()
            string
        }

    fun getAccounts() = getJsonString("/accounts.json")

}
