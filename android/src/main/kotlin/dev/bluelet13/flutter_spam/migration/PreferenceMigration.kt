package dev.bluelet13.flutter_spam.migration

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import io.flutter.Log

class PreferenceMigration {

    companion object {
        const val TAG = "flutter_spam"
        const val KEY_MIGRATE = "flutter_spam.migrate"
    }

    private val migrationPreferenceNameKey = "dev.bluelet13.flutter_spam.PREFERENCE_NAME"
    private val spamPreferencesName = "FlutterSPAM"
    private val sharedPreferencesName = "FlutterSharedPreferences"

    fun migrate(context: Context) {
        val packageName = context.packageName

        val spam = context.getSharedPreferences(spamPreferencesName, Context.MODE_PRIVATE)
        if (!spam.getBoolean(KEY_MIGRATE, false)) {

            val info = context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val src = info.metaData.get(migrationPreferenceNameKey)?.let { context.getSharedPreferences(it as String, Context.MODE_PRIVATE) }
                    ?: PreferenceManager.getDefaultSharedPreferences(context)
            val dist = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

            val entries = src.all

            val editor = dist.edit()
            for (entry in entries) {
                edit(editor, entry.key, entry.value)
                Log.i(TAG, "migrate ${entry.key} => ${entry.value.toString()}")
            }
            editor.apply()

            spam.edit().putBoolean(KEY_MIGRATE, true).apply()
        } else {
            Log.i(TAG, "This app($packageName) has already been migrated.")
        }
    }

    private fun edit(editor: SharedPreferences.Editor, key: String, value: Any?) {
        val migrationKey = "flutter.$key"
        when (valueType(value)) {
            ValueType.Boolean -> editor.putBoolean(migrationKey, value as Boolean)
            ValueType.Float -> editor.putFloat(migrationKey, value as Float)
            ValueType.Int -> editor.putInt(migrationKey, value as Int)
            ValueType.Long -> editor.putLong(migrationKey, value as Long)
            ValueType.String -> editor.putString(migrationKey, value as String)
            ValueType.StringSet -> editor.putStringSet(migrationKey, value as Set<String>)
            else -> print("This value is no supported : $value")
        }
    }

    private fun valueType(value: Any?): ValueType {
        if (value is Boolean) return ValueType.Boolean
        if (value is Float) return ValueType.Float
        if (value is Int) return ValueType.Int
        if (value is Long) return ValueType.Long
        if (value is String) return ValueType.String
        if (value is Set<*>) return ValueType.StringSet
        return ValueType.Unknown
    }

}