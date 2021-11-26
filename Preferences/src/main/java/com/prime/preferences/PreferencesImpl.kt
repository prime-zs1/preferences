package com.prime.preferences

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.Preferences as Preferences0

private const val TAG = "Preferences"

private const val PREFERENCE_NAME = "Shared_Preferences"


internal class PreferencesImpl(context: Context) : Preferences {

    private val scope = CoroutineScope(
        Dispatchers.Main + SupervisorJob()
    )

    private val Context.store: DataStore<Preferences0> by preferencesDataStore(
        name = PREFERENCE_NAME
    )

    private val store = context.store

    private val flow: Flow<Preferences0>
        get() = store.data.catch { exception ->
            when (exception) {
                is IOException -> {
                    Log.e(TAG, "getString: $exception")
                    emit(emptyPreferences())
                }
                else -> throw exception
            }
        }


    override fun <T> get(key: Key<T>): Flow<T?> = flow.map { it[key.original] }

    override fun <T> get(key: Key1<T>): Flow<T> = flow.map { it[key.original] ?: key.defaultValue }

    override fun <T, O> get(key: Key2<T, O>): Flow<O?> =
        flow.map { it[key.original]?.let { key.converter.to(it) } }

    override fun <T, O> get(key: Key3<T, O>): Flow<O> =
        flow.map { it[key.original]?.let { key.converter.to(it) } ?: key.defaultValue }


    private fun <T> set(key: DataStoreKey<T>, value: T) {
        scope.launch {
            store.edit {
                it[key] = value
            }
        }
    }


    override fun <T> set(key: Key<T>, value: T) = set(key.original, value)

    override fun <T> set(key: Key1<T>, value: T) {
        set(key.original, value)
    }

    override fun <T, O> set(key: Key2<T, O>, value: O) {
        set(key.original, key.converter.from(value))
    }

    override fun <T, O> set(key: Key3<T, O>, value: O) {
        set(key.original, key.converter.from(value))
    }

    override fun <T> minusAssign(key: Key<T>) {
        scope.launch {
            store.edit {
                it -= key.original
            }
        }
    }

    override fun <T> contains(key: Key<T>): Boolean {
        return flow.map { preference -> key.original in preference }.collect()
    }

    override fun <T> Flow<T>.toStateFlow(started: SharingStarted): StateFlow<T> {
        return stateIn(scope, started, initialValue = collect())
    }
    
    @Composable
    override fun <T> Flow<T>.observeAsState() = collectAsState(collect())
}
