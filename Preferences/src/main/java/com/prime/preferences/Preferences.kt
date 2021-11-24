package com.prime.preferences

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences as Preferences0

typealias Key<T> = Preferences0.Key<T>

/**
 * [Key] with default value
 */
data class Key1<T>(val key: Key<T>, val defaultValue: T)

/**
 * [Key] with [TwoWayConverter]
 */
data class Key2<S, O>(val key: Key<S>, val converter: TwoWayConverter<O, S>)

/**
 * [Key] with default value and [TwoWayConverter]
 */
data class Key3<S, O>(val key: Key<S>, val defaultValue: O, val converter: TwoWayConverter<O, S>)


/**
 * [TwoWayConverter] class contains the definition on how to convert from an arbitrary type [O]
 * to a [S], and convert the [S] back to the type [O]. This allows
 * preferences to run on any type of objects, e.g. [Color], [Enum] etc.
 */
interface TwoWayConverter<O, S> {
    /**
     * Defines how a type [O] original should be converted to [V]
     */
    fun from(value: O): S

    /**
     * Convert the restored value back to the original Class. If null is returned the value will
     * not be restored and would be initialized again instead.
     */
    fun to(value: S): O
}


interface Preferences {

    /**
     * Returns [Flow] of type T ([String], [Float], [Double], [Int], [Long], String [Set]).
     *
     * Note: This can be nullable if [key] doesn't exist.
     */
    operator fun <T> get(key: Key<T>): Flow<T?>

    /**
     * Note: emits [defaultValue] if orginal not exists.
     * @see get
     */
    operator fun <T> get(key: Key1<T>): Flow<T>

    /**
     * [Flow] of type Any Type.
     *
     * The key contains [TwoWayConverter], which helps in conversion from [O] Original to Savable
     * Note: Savable are of Type ([String], [Float], [Double], [Int], [Long], String [Set]).
     * This can be nullable if [key] doesn't exist.
     */
    operator fun <S, O> get(key: Key2<S, O>): Flow<O?>

    /**
     * **Note returns defaultValue if null**
     *
     * @see get
     */
    operator fun <S, O> get(key: Key3<S, O>): Flow<O>

    operator fun <T> set(key: Key<T>, value: T)

    operator fun <T> set(key: Key1<T>, value: T)

    operator fun <S, O> set(key: Key2<S, O>, value: O)

    operator fun <S, O> set(key: Key3<S, O>, value: O)

    operator fun <T> minusAssign(key: Key<T>)

    @WorkerThread
    operator fun <T> contains(key: Key<T>): Boolean

    /**
     * Collects blocking [runBlocking] the [Flow.first] emitted by [Flow]
     */
    fun <T> Flow<T>.collect(): T = runBlocking { first() }

    /**
     * Creates [StateFlow] from [Flow] using [Flow.StateIn] operator and [Preferences] [Coroutine] Scope.
     */
    fun <T> Flow<T>.toStateFlow(started: SharingStarted): StateFlow<T>

    @Composable
    fun <T> Flow<T>.collectAsState() = collectAsState(collect())

    companion object {

        // Singleton prevents multiple instances of repository opening at the
        // same time.
        private const val TAG = "Preferences"

        @Volatile
        private var INSTANCE: Preferences? = null

        fun get(context: Context): Preferences {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the repository
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesImpl(context.applicationContext as Application)
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Transforms original value to type [Int] and vice versa.
 */
typealias IntTransformer<O> = TwoWayConverter<O, Int>

fun intPreferenceKey(name: String): Key<Int> =
    androidx.datastore.preferences.core.intPreferencesKey(name)

fun intPreferenceKey(name: String, defaultValue: Int): Key1<Int> =
    Key1(intPreferenceKey(name), defaultValue)


fun <O> intPreferenceKey(name: String, transformer: IntTransformer<O>) =
    Key2(intPreferenceKey(name), transformer)

fun <O> intPreferenceKey(
    name: String,
    defaultValue: O,
    transformer: IntTransformer<O>
) = Key3(
    intPreferenceKey(name),
    defaultValue,
    transformer
)



/**
 * Transforms original value to type [Double] and vice versa.
 */
typealias DoubleTransformer<O> = TwoWayConverter<O, Double>

fun doublePreferenceKey(name: String): Key<Double> =
    androidx.datastore.preferences.core.doublePreferencesKey(name)

fun doublePreferenceKey(name: String, defaultValue: Double): Key1<Double> =
    Key1(doublePreferenceKey(name), defaultValue)

fun <O> doublePreferenceKey(name: String, transformer: DoubleTransformer<O>) =
    Key2(doublePreferenceKey(name), transformer)

fun <O> doublePreferenceKey(
    name: String,
    defaultValue: O,
    transformer: DoubleTransformer<O>
) = Key3(
    doublePreferenceKey(name),
    defaultValue,
    transformer
)



/**
 * Transforms original value to type [Float] and vice versa.
 */
typealias FloatTransformer<O> = TwoWayConverter<O, Float>

fun floatPreferenceKey(name: String): Key<Float> =
    androidx.datastore.preferences.core.floatPreferencesKey(name)

fun floatPreferenceKey(name: String, defaultValue: Float): Key1<Float> =
    Key1(floatPreferenceKey(name), defaultValue)

fun <O> floatPreferenceKey(name: String, transformer: FloatTransformer<O>) =
    Key2(floatPreferenceKey(name), transformer)

fun <O> floatPreferenceKey(
    name: String,
    defaultValue: O,
    transformer: FloatTransformer<O>
) = Key3(
    floatPreferenceKey(name),
    defaultValue,
    transformer
)


/**
 * Transforms original value to type [Long] and vice versa.
 */
typealias LongTransformer<O> = TwoWayConverter<O, Long>

fun longPreferenceKey(name: String): Key<Long> =
    androidx.datastore.preferences.core.longPreferencesKey(name)

fun longPreferenceKey(name: String, defaultValue: Long): Key1<Long> =
    Key1(longPreferenceKey(name), defaultValue)

fun <O> longPreferenceKey(name: String, transformer: LongTransformer<O>) =
    Key2(longPreferenceKey(name), transformer)

fun <O> longPreferenceKey(
    name: String,
    defaultValue: O,
    transformer: LongTransformer<O>
) = Key3(
    longPreferenceKey(name),
    defaultValue,
    transformer
)



/**
 * Transforms original value to type [String] and vice versa.
 */
typealias StringTransformer<O> = TwoWayConverter<O, String>

fun stringPreferenceKey(name: String): Key<String> =
    androidx.datastore.preferences.core.stringPreferencesKey(name)

fun stringPreferenceKey(name: String, defaultValue: String): Key1<String> =
    Key1(stringPreferenceKey(name), defaultValue)

fun <O> stringPreferenceKey(name: String, transformer: StringTransformer<O>) =
    Key2(stringPreferenceKey(name), transformer)

fun <O> stringPreferenceKey(
    name: String,
    defaultValue: O,
    transformer: StringTransformer<O>
) = Key3(
    stringPreferenceKey(name),
    defaultValue,
    transformer
)


///String set
fun stringSetPreferenceKey(name: String) =
    androidx.datastore.preferences.core.stringSetPreferencesKey(name)

fun stringSetPreferenceKey(name: String, defaultValue: Set<String>): Key1<Set<String>> =
    Key1(stringSetPreferenceKey(name), defaultValue)
