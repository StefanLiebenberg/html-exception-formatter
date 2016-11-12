package org.slieb.formatter;

import org.slieb.throwables.BiConsumerWithThrowable;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
public interface HtmlAppender extends BiConsumerWithThrowable<Appendable, Message, IOException> {

    default HtmlAppender andThenAppend(HtmlAppender after) {
        Objects.requireNonNull(after);
        return (l, r) -> {
            acceptWithThrowable(l, r);
            after.acceptWithThrowable(l, r);
        };
    }
}
