package dev.bluelet13.flutter_spam

import androidx.annotation.NonNull
import dev.bluelet13.flutter_spam.migration.PreferenceMigration
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterSpamPlugin : FlutterPlugin {

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val preference = PreferenceMigration()
        preference.migrate(flutterPluginBinding.applicationContext)
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val preference = PreferenceMigration()
            preference.migrate(registrar.context())
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

}
