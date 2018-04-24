package com.runt9.heroDynasty.desktop

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.runt9.heroDynasty.NewHeroDynastyApplication

/** Launches the desktop (LWJGL) application.  */
object DesktopLauncher {
    private val defaultConfiguration: LwjglApplicationConfiguration
        get() {
            val configuration = LwjglApplicationConfiguration()
            configuration.title = "hero-dynasty"
            configuration.width = 80 * 10
            configuration.height = 31 * 20
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
        return LwjglApplication(NewHeroDynastyApplication(), defaultConfiguration)
    }
}