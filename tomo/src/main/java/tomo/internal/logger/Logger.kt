package tomo.internal.logger

internal interface Logger {
    fun d(throwable: Throwable? = null, msg: () -> String)
    fun i(throwable: Throwable? = null, msg: () -> String)
    fun e(throwable: Throwable? = null, msg: () -> String)
}
