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

package io.fluffydaddy.jutils.iostream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * A bridge from an OutputStream to a Writer.
 * <p>
 * This class allows an OutputStream to be adapted to a Writer, enabling writing
 * of character data to an OutputStream. </p>
 */
public class OutputStreamWriter extends OutputStream {
    
    /**
     * The underlying Writer.
     */
    public Writer writer;
    
    /**
     * Constructs a new OutputStreamWriter.
     *
     * @param writer The Writer to which data is written.
     */
    public OutputStreamWriter(Writer writer) {
        this.writer = writer;
    }
    
    /**
     * Writes a byte to the output stream.
     *
     * @param b The byte to be written.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(int b) throws IOException {
        writer.write(b);
    }
    
    /**
     * Flushes the output stream.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        writer.flush();
    }
    
    /**
     * Closes the output stream.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        writer.close();
    }
    
    /**
     * Returns a string representation of the OutputStreamWriter.
     *
     * @return A string representation of the OutputStreamWriter.
     */
    @Override
    public String toString() {
        return writer.toString();
    }
}