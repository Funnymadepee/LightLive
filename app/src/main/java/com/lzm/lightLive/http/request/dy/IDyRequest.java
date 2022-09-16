package com.lzm.lightLive.http.request.dy;

import java.io.IOException;

public interface IDyRequest {

    public byte[] login(String roomId) throws IOException;

    public byte[] joinGroup(String roomId) throws IOException;

    public byte[] heartBeat() throws IOException;

}
