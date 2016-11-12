package org.slieb.formatter;

import org.slieb.throwables.BiConsumerWithThrowable;

import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface HtmlAppender extends BiConsumerWithThrowable<Appendable, Message, IOException> {

    /**
     * @param after The second html appender to chain
     * @return A chain of HtmlAppender interfaces
     */
    default HtmlAppender andThenAppend(HtmlAppender after) {
        Objects.requireNonNull(after);
        return (l, r) -> {
            acceptWithThrowable(l, r);
            after.acceptWithThrowable(l, r);
        };
    }
}
