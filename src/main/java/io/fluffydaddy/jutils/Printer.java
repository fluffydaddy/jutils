package io.fluffydaddy.jutils;

import java.io.PrintStream;
import java.io.Writer;

public class Printer extends PrintStream {
    public Writer writer;

    public Printer(Writer writer) {
        super(new OutputStreamWriter(writer));
        this.writer = writer;
    }

    @Override
    public String toString() {
        return writer.toString();
    }
}
