package tomo.internal.logger

import android.util.Log

internal class LogCatLogger(private val debug: Boolean) : Logger {
    override fun d(throwable: Throwable?, msg: () -> String) = if (debug) pipeTag(Log::d, msg(), throwable) else Unit
    override fun i(throwable: Throwable?, msg: () -> String) = pipeTag(Log::i, msg(), throwable)
    override fun e(throwable: Throwable?, msg: () -> String) = pipeTag(Log::e, msg(), throwable)
}

private fun pipeTag(
    f: (String, String, Throwable?) -> Int,
    log: String,
    error: Throwable?
) = f(tag(), log, error).run { Unit }

private fun tag(): String = "A|${findClassName(6)}"

private fun findClassName(traceIndex: Int): String {
    val stackTraceElement = Throwable().stackTrace[traceIndex]
    return stackTraceElement.className
        .split(".").last()  // Strip simple name from full.package.name
        .split("$").first() // Strip name when caller$inside$lambdas
}