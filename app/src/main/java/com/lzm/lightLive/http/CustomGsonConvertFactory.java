package com.lzm.lightLive.http;

import static java.nio.charset.StandardCharsets.UTF_8;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomGsonConvertFactory extends Converter.Factory{

    private static final String TAG = "CustomGsonConvertFactory";

    private final Gson gson;

    private final ResponseConverter converter;

    public abstract static class ResponseConverter {
        protected abstract String convertResponse(ResponseBody value) throws IOException;
    }

    private CustomGsonConvertFactory(Gson gson, ResponseConverter converter) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.converter = converter;
    }

    public static CustomGsonConvertFactory create(ResponseConverter converter) {
        return create(new Gson(), converter);
    }

    public static CustomGsonConvertFactory create(Gson gson, ResponseConverter converter) {
        return new CustomGsonConvertFactory(gson, converter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type,
                                                            @NonNull Annotation[] annotations,
                                                            @NonNull Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonConvertFactory.CustomGsonResponseBodyConverter<>(gson, adapter, type, converter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, @NonNull Annotation[] parameterAnnotations,
                                                          @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonConvertFactory.CustomGsonRequestBodyConverter<>(gson, adapter, type);
    }

    static final class CustomGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = StandardCharsets.UTF_8;

        private final Gson gson;
        private final TypeAdapter<T> adapter;
        private final Type type;

        CustomGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    public static class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        private final Gson gson;
        private final TypeAdapter<T> adapter;
        private final Type type;
        private final ResponseConverter responseConverter;

        public CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter,
                                               Type type, ResponseConverter responseConverter) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;
            this.responseConverter = responseConverter;
        }

        @Override
        public T convert(@NonNull ResponseBody value) throws IOException {
            String response = responseConverter.convertResponse(value);
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
            if(null != response) {
                value.close();
            }
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            Reader reader = new InputStreamReader(inputStream, charset);
            JsonReader jsonReader = gson.newJsonReader(reader);
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        }
    }
}
