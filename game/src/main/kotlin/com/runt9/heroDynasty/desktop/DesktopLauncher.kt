package com.runt9.heroDynasty.desktop

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.runt9.heroDynasty.HeroDynastyApplication
import com.runt9.heroDynasty.util.AppConst.cellHeight
import com.runt9.heroDynasty.util.AppConst.cellWidth
import com.runt9.heroDynasty.util.AppConst.gridHeight
import com.runt9.heroDynasty.util.AppConst.gridWidth

/** Launches the desktop (LWJGL) application.  */
object DesktopLauncher {
    private val defaultConfiguration: LwjglApplicationConfiguration
        get() {
            val configuration = LwjglApplicationConfiguration()
            configuration.title = "hero-dynasty"
            configuration.width = (gridWidth * cellWidth).toInt()
            configuration.height = (gridHeight * cellHeight).toInt()
            intArrayOf(128, 64, 32, 16).forEach { size ->
                configuration.addIcon("libgdx$size.png", FileType.Internal)
            }
            return configuration
        }

    @JvmStatic
    fun main(args: Array<String>) {
        createApplication()
    }

    private fun createApplication(): LwjglApplication {
        return LwjglApplication(HeroDynastyApplication(), defaultConfiguration)
    }
}