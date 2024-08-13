package com.growme.growme.data

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/* -------------------------------------------------------------------------------------------
    val logger = LoggerUtils().getInstance()

    logBtn.setOnClickListener {
        logger.debug("디버그 로그").info("인포 로그").warning("워닝 로그").error("에러 로그")
    }
------------------------------------------------------------------------------------------- */

object LoggerUtils {

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    private const val TAG = "LOGGER"

    fun d(message: String) {
        Logger.t(TAG).d(message)
    }

    fun i(message: String) {
        Logger.t(TAG).i(message)
    }

    fun w(message: String) {
        Logger.t(TAG).w(message)
    }

    fun e(message: String) {
        Logger.t(TAG).e(message)
    }
}