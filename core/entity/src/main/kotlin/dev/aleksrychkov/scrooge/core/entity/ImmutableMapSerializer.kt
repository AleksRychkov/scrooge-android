package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SealedSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias SerializableImmutableMap<K, V> =
    @Serializable(ImmutableMapSerializer::class)
    ImmutableMap<K, V>

class ImmutableMapSerializer<K, V>(
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>
) : KSerializer<ImmutableMap<K, V>> {

    @OptIn(SealedSerializationApi::class)
    private class PersistentMapDescriptor : SerialDescriptor by serialDescriptor<Map<String, String>>() {
        override val serialName: String = "kotlinx.serialization.immutable.ImmutableMap"
    }

    override val descriptor: SerialDescriptor = PersistentMapDescriptor()

    override fun serialize(encoder: Encoder, value: ImmutableMap<K, V>) {
        MapSerializer(keySerializer, valueSerializer).serialize(encoder, value.toMap())
    }

    override fun deserialize(decoder: Decoder): ImmutableMap<K, V> {
        return MapSerializer(keySerializer, valueSerializer).deserialize(decoder).toPersistentMap()
    }
}
