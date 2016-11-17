package org.slieb.formatter;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FormaterUsage {

    public static void main(String[] args) throws IOException {
        HtmlExceptionFormatOptions options = new HtmlExceptionFormatOptions();
        options.setTheme(Theme.GRAY);
        HtmlExceptionFormatter htmlExceptionFormatter = new HtmlExceptionFormatter(options);

        File errorFile = new File("error.html");
        try (OutputStream istream = new FileOutputStream(errorFile)) {
            Throwable throwable = new Throwable("There is a very long error that nobody knows and I hate it.");
            IOUtils.write(htmlExceptionFormatter.toString(throwable), istream);
        }

        File messageFile = new File("message.html");
        try (OutputStream istream = new FileOutputStream(messageFile)) {
            IOUtils.write(htmlExceptionFormatter.toString("There is some error content",
                                                          "There is a very long error that nobody knows and I hate it."), istream);
        }
    }
}
