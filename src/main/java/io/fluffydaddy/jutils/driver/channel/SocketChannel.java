/*
 * Copyright © 2024 fluffydaddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fluffydaddy.jutils.driver.channel;

import io.fluffydaddy.jutils.driver.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.Charset;

public class SocketChannel implements Channel {
    private final Socket socket;
    
    public SocketChannel(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public InputStream openInput() throws IOException {
        return socket.getInputStream();
    }
    
    @Override
    public OutputStream openOutput() throws IOException {
        return socket.getOutputStream();
    }
    
    @Override
    public Reader openReader(Charset charset) throws IOException {
        return new InputStreamReader(openInput(), charset);
    }
    
    @Override
    public Writer openWriter(Charset charset) throws IOException {
        return new OutputStreamWriter(openOutput(), charset);
    }
}
