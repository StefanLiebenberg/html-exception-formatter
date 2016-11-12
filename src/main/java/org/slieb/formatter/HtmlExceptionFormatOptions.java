package org.slieb.formatter;

@SuppressWarnings("WeakerAccess")
public class HtmlExceptionFormatOptions {

    private Theme theme = Theme.EMPTY;

    private boolean printDetails = true;

    private boolean printStacktrace = true;

    private boolean printCauses = true;

    private boolean printFooter = true;

    private boolean promoteLibrary = true;

    public boolean printFooter() {
        return printFooter;
    }

    public void setPrintFooter(final boolean printFooter) {
        this.printFooter = printFooter;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(final Theme theme) {
        this.theme = theme;
    }

    public boolean printStacktrace() {
        return printStacktrace;
    }

    public void setPrintStacktrace(final boolean printStacktrace) {
        this.printStacktrace = printStacktrace;
    }

    public boolean printDetails() {
        return printDetails;
    }

    public void setPrintDetails(final boolean printDetails) {
        this.printDetails = printDetails;
    }

    public boolean promoteLibrary() {
        return promoteLibrary;
    }

    public void setPromoteLibrary(final boolean promoteLibrary) {
        this.promoteLibrary = promoteLibrary;
    }

    public boolean printCauses() {
        return printCauses;
    }

    public void setPrintCauses(final boolean printCauses) {
        this.printCauses = printCauses;
    }
}

