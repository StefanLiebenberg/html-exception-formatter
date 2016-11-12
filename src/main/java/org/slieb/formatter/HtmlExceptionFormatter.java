package org.slieb.formatter;

import java.io.IOException;

public class HtmlExceptionFormatter {

    private final HtmlAppender throwableHtmlAppender;

    public HtmlExceptionFormatter(final HtmlExceptionFormatOptions options) {
        throwableHtmlAppender = HtmlExceptionMessageFactory.createFormatter(options);
    }

    public HtmlExceptionFormatter() {
        this(new HtmlExceptionFormatOptions());
    }

    public void formatMessage(Appendable appendable, Message message) throws IOException {
        throwableHtmlAppender.acceptWithThrowable(appendable, message);
    }
}
