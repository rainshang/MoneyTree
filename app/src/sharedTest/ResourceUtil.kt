object ResourceUtil {
    private fun getJsonString(fileName: String) =
        javaClass.getResourceAsStream(fileName)?.let {
            val string = String(it.readBytes())
            it.close()
            string
        }

    fun getAccounts() = getJsonString("/accounts.json")

    fun getTransactions(uid: Int) = getJsonString("transactions_$uid.json")
}