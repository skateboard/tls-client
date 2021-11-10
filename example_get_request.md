# Example GET request
You must set the path to the DLL
```java
System.setProperty("tls.client.path", "native_client.dll");
```

Then you have to instate the HttpClient
```java
final HttpClient httpClient = new HttpClient.Builder()  
        .build();
```

You can set things like the cookie jar and proxy like so
```java
final HttpClient httpClient = new HttpClient.Builder()  
        .cookieJar(testCookieJar)  
        .proxy("PROXY_STRING")
        .build();
```

to make a basic request like this
```java
Request request = new Request.Builder()  
        .url("https://httpbin.org/anything")  
        .get()
        .build();
```

Getting the response
```java
final Response response = httpClient.execute(request);  
  
System.out.println(response.getStatusCode());  
System.out.println(response.getData());
```
