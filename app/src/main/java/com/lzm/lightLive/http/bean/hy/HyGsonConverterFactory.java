package com.lzm.lightLive.http.bean.hy;


import static java.nio.charset.StandardCharsets.UTF_8;


import android.util.Log;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.util.CommonUtil;

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

public class HyGsonConverterFactory extends Converter.Factory {

    private static final String TAG = "Gson";

    private final Gson gson;

    private HyGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    public static HyGsonConverterFactory create() {
        return create(new Gson());
    }

    public static HyGsonConverterFactory create(Gson gson) {
        return new HyGsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations,
                                                            @NonNull Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonResponseBodyConverter<>(gson, adapter, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, @NonNull Annotation[] parameterAnnotations,
                                                          @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonRequestBodyConverter<>(gson, adapter, type);
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

    static final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;
        private final Type type;

        CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String response = value.string();
            JsonObject obj = new Gson().fromJson(response, JsonObject.class);
            Log.e(TAG, "convert: " + obj );
            if(obj.has("data") && obj.get("data") != null) {
                HyRoom roomInfo = new Gson().fromJson(obj.get("data"), HyRoom.class);
                Log.e(TAG, "convert: " + roomInfo.toString() );
                String roomName = roomInfo.hyLiveData.getRoomName();
                long userCount = roomInfo.hyLiveData.getUserCount();
                long profileRoom = roomInfo.hyLiveData.getProfileRoom();
                String screenshot = roomInfo.hyLiveData.getScreenshot();
                String fullName = roomInfo.hyLiveData.getGameFullName();

                if(roomInfo.hyStream != null
                        && roomInfo.hyStream.getFlv() != null
                        && roomInfo.hyStream.getFlv().getMultiLine().get(0) != null) {
                    String url = roomInfo.hyStream.getFlv().getMultiLine().get(0).url;
                    roomInfo.setLiveStreamUri(url);
                }

                String nick = roomInfo.hyProfileInfo.nick;
                String avatar = roomInfo.hyProfileInfo.avatar;
                int activityCount = roomInfo.hyProfileInfo.activityCount;

                roomInfo.setHostName(nick);
                roomInfo.setRoomName(roomName);
                roomInfo.setCateName(fullName);
                roomInfo.setHostAvatar(avatar);
                roomInfo.setHeatNum(userCount);
                roomInfo.setThumbUrl(screenshot);
                roomInfo.setFansNum(activityCount);
                roomInfo.setStreamStatus(CommonUtil.generateLiveStatus(roomInfo.hyLiveStatus));
                roomInfo.setRoomId(String.valueOf(profileRoom));
                obj.remove("data");
                obj.add("data", new Gson().toJsonTree(roomInfo, new TypeToken<HyRoom>() {}.getType() ));
                response = obj.toString();
            }
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
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
