package org.slieb.formatter;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
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

    public void formatMessage(Appendable appendable, String title, String content, Throwable throwable) throws IOException {
        formatMessage(appendable, new Message(title, content, throwable));
    }

    /**
     * @param appendable A appendable interface to append the output html to
     * @param throwable  the throwable instance
     * @throws IOException
     */
    public void formatMessage(Appendable appendable, Throwable throwable) throws IOException {
        formatMessage(appendable, "An Exception Occurred", null, throwable);
    }

    /**
     * @param appendable A appendable interface to append the output html to
     * @param title      The html message title
     * @param content    The html message body
     * @throws IOException
     */
    public void formatMessage(Appendable appendable, String title, String content) throws IOException {
        formatMessage(appendable, title, content, null);
    }

    /**
     * @param title     The html message title
     * @param content   The html message body
     * @param throwable The throwable instance
     * @return A html string
     * @throws IOException
     */
    public String toString(String title, String content, Throwable throwable) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        formatMessage(stringBuilder, title, content, throwable);
        return stringBuilder.toString();
    }

    /**
     * @param title   The title to display on page.
     * @param content The message content to display on the html page.
     * @return A html string
     * @throws IOException
     */
    public String toString(String title, String content) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        formatMessage(stringBuilder, title, content);
        return stringBuilder.toString();
    }

    /**
     * @param throwable The throwable exception to turn into a html string
     * @return A html string
     * @throws IOException
     */
    public String toString(Throwable throwable) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        formatMessage(stringBuilder, throwable);
        return stringBuilder.toString();
    }
}
