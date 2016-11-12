package org.slieb.formatter;

import org.apache.commons.io.IOUtils;
import org.slieb.throwables.ConsumerWithThrowable;
import org.slieb.throwables.FunctionWithThrowable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

class HtmlExceptionMessageFactory {

    public static final HtmlAppender ESCAPED_TITLE_APPENDER = createEscapedAppender(Message::getTitle);

    static HtmlAppender createFormatter(final HtmlExceptionFormatOptions options) {
        final HtmlAppender htmlAppender = createTagAppender("html", null, createHeadAppender(options).andThenAppend(createBodyAppender(options)));
        return createStaticAppender("<!DOCTYPE html>").andThenAppend(htmlAppender);
    }

    private static HtmlAppender createHeadAppender(final HtmlExceptionFormatOptions options) {
        boolean printDetails = options.printDetails();
        final HtmlAppender titleAppender = createTagAppender("title", null, createEscapedAppender(Message::getTitle));
        final HtmlAppender themeAppender = createThemeAppender(options);
        return createTagAppender("head", null, printDetails ? titleAppender.andThenAppend(themeAppender) : themeAppender);
    }

    private static HtmlAppender createThemeAppender(HtmlExceptionFormatOptions options) {
        return (appendable, type) -> {
            final String url = "/org/slieb/html/themes/" + options.getTheme().getName() + ".css";
            try (final InputStream inputStream = HtmlExceptionMessageFactory.class.getResourceAsStream(url)) {
                if (inputStream != null) {
                    appendTag(appendable, type, "style", null, (a, t) -> a.append(IOUtils.toString(inputStream)));
                }
            }
        };
    }

    private static HtmlAppender createBodyAppender(final HtmlExceptionFormatOptions options) {
        final HtmlAppender hrAppender = createStaticAppender("<hr />");
        return createTagAppender("body", null, createHeadingAppenderForThrowable(options)
                .andThenAppend(hrAppender)
                .andThenAppend(createEscapedAppender(Message::getContent))
                .andThenAppend(createCausesAppender(options))
                .andThenAppend(createStacktraceAppender(options))
                .andThenAppend(hrAppender)
                .andThenAppend(createFooterAppender(options)));
    }

    private static HtmlAppender createCausesAppender(final HtmlExceptionFormatOptions options) {
        if (options.printDetails()) {
            final boolean printStacktrace = options.printStacktrace();
            final String throwableTagName = printStacktrace ? "details" : "div";
            final String messageTagName = printStacktrace ? "summary" : "span";
            return createTagAppender("div", new String[]{"causes"}, (appendable, message) -> {
                Throwable current = message.getThrowable();
                if (current != null) {
                    appendable.append("<h2>Causes:</h2>");
                    while (current != null) {
                        ConsumerWithThrowable<Appendable, IOException> consumer = throwableAppender(current);
                        ConsumerWithThrowable<Appendable, IOException> consumerStack = throwableStackAppender(current);
                        appendTag(appendable, message, throwableTagName, new String[]{"throwable"}, (a, b) -> {
                            appendTag(a, b, messageTagName, new String[]{"title"}, (a1, b2) -> consumer.acceptWithThrowable(a1));
                            a.append("<br />");
                            if(printStacktrace) {
                                consumerStack.acceptWithThrowable(a);
                            }
                        });
                        appendable.append("<br />");
                        current = current.getCause();
                    }
                }
            });
        } else {
            return nullAppender();
        }
    }

    private static ConsumerWithThrowable<Appendable, IOException> throwableStackAppender(Throwable current) {
        return (appendable) -> {
            for (final StackTraceElement element : current.getStackTrace()) {
                appendable
                        .append("<span class=line>")
                        .append("&nbsp;at ")
                        .append("<span class=classname>")
                        .append(escapeHtml(element.getClassName()))
                        .append("</span>")
                        .append("<span class=filename>(")
                        .append(escapeHtml(element.getFileName()))
                        .append(":")
                        .append(escapeHtml(String.valueOf(element.getLineNumber())))
                        .append(")</span>")
                        .append("</span>")
                        .append("<br/>");
            }
        };
    }

    private static ConsumerWithThrowable<Appendable, IOException> throwableAppender(Throwable current) {
        return (appendable) -> {
            appendable.append(escapeHtml(current.getClass().getName()))
                      .append(": ")
                      .append(escapeHtml(current.getLocalizedMessage()));
        };
    }

    private static HtmlAppender createStacktraceAppender(final HtmlExceptionFormatOptions options) {
        if (options.printDetails() && options.printStacktrace()) {
            return (appendable, message) -> {
                final Throwable throwable = message.getThrowable();
                if (throwable != null) {
                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    final PrintStream printStream = new PrintStream(byteArrayOutputStream);
                    throwable.printStackTrace(printStream);
                    appendable.append("<div class=\"stacktrace\">");
                    appendable.append("<h2>Raw Stacktrace:</h2>");
                    appendable.append("<pre>")
                              .append(escapeHtml(byteArrayOutputStream.toString()))
                              .append("</pre>");
                    appendable.append("</div>");
                }
            };
        } else {
            return nullAppender();
        }
    }

    private static HtmlAppender createFooterAppender(final HtmlExceptionFormatOptions options) {
        if (options.printFooter()) {
            boolean promote = options.promoteLibrary();
            return (v1, v2) -> v1.append("<center><small>")
                                 .append("Message generated at ")
                                 .append(escapeHtml(ZonedDateTime.now().toLocalDateTime().toString()))
                                 .append(" by ")
                                 .append(promote ? "<a href=\"http://github.com/StefanLiebenberg/html-exception-formatter\">" : "<b>")
                                 .append("Html Exception Formatter.")
                                 .append(options.promoteLibrary() ? "</a>" : "</b>")
                                 .append("</small></center>");
        } else {
            return nullAppender();
        }
    }

    private static HtmlAppender createHeadingAppenderForThrowable(HtmlExceptionFormatOptions options) {
        final HtmlAppender withThrowableTitle = ESCAPED_TITLE_APPENDER.andThenAppend((a, m) -> {
            Throwable t = m.getThrowable();
            if (t != null) {
                a.append(": ").append(escapeHtml(t.getLocalizedMessage()));
            }
        });
        return createTagAppender("h1", null, options.printDetails() ? withThrowableTitle : ESCAPED_TITLE_APPENDER);
    }

    private static HtmlAppender createEscapedAppender(FunctionWithThrowable<Message, String, IOException> functionWithThrowable) {
        return (v1, v2) -> {
            final String str = functionWithThrowable.applyWithThrowable(v2);
            if (str != null) {
                v1.append(escapeHtml(str));
            }
        };
    }

    private static HtmlAppender createTagAppender(final String tagName, final String[] classNames, final HtmlAppender contentAppender) {
        return (appendable, type) -> appendTag(appendable, type, tagName, classNames, contentAppender);
    }

    private static HtmlAppender createStaticAppender(final String csq) {return (v1, v2) -> v1.append(csq);}

    private static HtmlAppender nullAppender() {
        return (v1, v2) -> {};
    }

    private static void appendTag(final Appendable appendable, final Message type, final String tagName, final String[] classNames,
                                  final HtmlAppender contentAppender) throws IOException {
        appendable.append("<").append(tagName);

        if (classNames != null && classNames.length > 0) {
            appendable.append(" class=\"")
                      .append(Arrays.stream(classNames).collect(joining(" "))).append("\"");
        }

        appendable.append(">");
        contentAppender.accept(appendable, type);
        appendable.append("</").append(tagName).append(">");
    }
}
