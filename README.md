# html-exception-formatter
a Java library to format exceptions to pretty html


## Usage

**Very basic usage**

```java
  String html = new HtmlExceptionsFormatter().toString(exception);
```

**Setting Options**

```java
  HtmlExceptionFormatOptions options = new HtmlExceptionFormatOptions();
  options.setPrintDetails(true);
  String html = new HtmlExceptionsFormatter().toString(exception);
```

**Non Exception messages**


```java
  HtmlExceptionFormatOptions options = new HtmlExceptionFormatOptions();
  options.setPrintDetails(true);
  String html = new HtmlExceptionsFormatter().toString("404 Route not found", "The selected \"/noPath\" route wasn't found.");
```


**Writing into `Appendable`**


```java
  HtmlExceptionFormatOptions options = new HtmlExceptionFormatOptions();
  options.setPrintDetails(true);
  new HtmlExceptionsFormatter().formatMessage(System.err, exception);
```
