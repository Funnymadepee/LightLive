package com.lzm.lightLive.http

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class CustomGsonConvertFactory private constructor(gson: Gson?, converter: ResponseConverter) :
    Converter.Factory() {
    private val gson: Gson
    private val converter: ResponseConverter

    abstract class ResponseConverter {
        @Throws(IOException::class)
        abstract fun convertResponse(value: ResponseBody): String
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonResponseBodyConverter<Any>(gson, adapter, type, converter)
    }

    override fun requestBodyConverter(
        type: Type, parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonRequestBodyConverter<Any>(gson, adapter, type)
    }

    internal class CustomGsonRequestBodyConverter<T>(
        private val gson: Gson,
        private val adapter: TypeAdapter<out T>,
        private val type: Type
    ) : Converter<T, RequestBody> {
        private val mediaType = "application/json; charset=UTF-8".toMediaTypeOrNull()
        private val charset = StandardCharsets.UTF_8
        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer: Writer = OutputStreamWriter(buffer.outputStream(), charset)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value as Nothing?)
            jsonWriter.close()
            return buffer.readByteString().toRequestBody(mediaType)
        }
    }

    class CustomGsonResponseBodyConverter<T>(
        private val gson: Gson, private val adapter: TypeAdapter<out T>,
        private val type: Type, private val responseConverter: ResponseConverter
    ) : Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            val response = responseConverter.convertResponse(value)
            val contentType = value.contentType()
            val charset =
                if (contentType != null) contentType.charset(StandardCharsets.UTF_8) else StandardCharsets.UTF_8
            value.close()
            val inputStream: InputStream = ByteArrayInputStream(response.toByteArray())
            val reader: Reader = InputStreamReader(inputStream, charset)
            val jsonReader = gson.newJsonReader(reader)
            return value.use {
                adapter.read(jsonReader)
            }
        }
    }

    companion object {
        fun create(converter: ResponseConverter): CustomGsonConvertFactory {
            return create(Gson(), converter)
        }

        fun create(gson: Gson?, converter: ResponseConverter): CustomGsonConvertFactory {
            return CustomGsonConvertFactory(gson, converter)
        }
    }

    init {
        if (gson == null) throw NullPointerException("gson == null")
        this.gson = gson
        this.converter = converter
    }
}