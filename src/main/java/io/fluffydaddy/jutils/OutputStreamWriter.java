package io.fluffydaddy.jutils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class OutputStreamWriter extends OutputStream {
    public Writer writer;
	
    public OutputStreamWriter(Writer writer) {
        this.writer = writer;
    }
	
    @Override
    public void write(int b) throws IOException {
        writer.write(b);
    }
	
    @Override
    public void flush() throws IOException {
        writer.flush();
    }
	
    @Override
    public void close() throws IOException {
        writer.close();
    }
	
	@Override
	public String toString() {
		return writer.toString();
	}
}
