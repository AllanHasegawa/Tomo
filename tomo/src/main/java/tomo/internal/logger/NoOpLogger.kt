package tomo.internal.logger

internal object NoOpLogger : Logger {
    override fun d(throwable: Throwable?, msg: () -> String) = Unit
    override fun i(throwable: Throwable?, msg: () -> String) = Unit
    override fun e(throwable: Throwable?, msg: () -> String) = Unit
}