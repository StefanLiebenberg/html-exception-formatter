# html-exception-formatter
a Java library to format exceptions to pretty html


## Usage

**Very basic usage**

```java
  String html = new HtmlExceptionsFormatter().toString(exception);
```

**Options**

```java
  HtmlExceptionFormatOptions options = new HtmlExceptionFormatOptions();
  options.setPrintDetails(true);
  String html = new HtmlExceptionsFormatter().toString(exception);
```
