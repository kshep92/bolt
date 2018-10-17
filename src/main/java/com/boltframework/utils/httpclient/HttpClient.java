package com.boltframework.utils.httpclient;

import com.boltframework.utils.Strings;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
public class HttpClient {

  private String url;
  private HttpRequest request;
  private ReadyState readyState;

  public HttpClient() {
    readyState = ReadyState.Idle;
  }

  public Boolean isReady() {
    return readyState == ReadyState.Ready;
  }

  public ReadyState getReadyState() {
    return readyState;
  }

  public void setReadyState(ReadyState readyState) {
    this.readyState = readyState;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public HttpClient createRequest(HttpRequest request) {
    this.request = request;
    return this;
  }
  public void then(Consumer<HttpResponse> callback) {
     callback.accept(sendRequest());
  }

  private HttpResponse sendRequest() {
    HttpResponse response = new HttpResponse();
    HttpURLConnection connection;
    try {
      URL url = new URL(this.url + request.getPath());
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(request.getMethod());
      connection.setInstanceFollowRedirects(request.getFollowRedirects());
      if(!request.getBody().isEmpty()) {
        connection.setDoOutput(true);
        writeRequestBody(request, connection);
      }
      if(!request.getCookies().isEmpty())
        addCookies(request, connection);
      int responseCode = connection.getResponseCode();
      InputStream inputStream;
      StringBuilder body = new StringBuilder();
      if(responseCode >= 200 && responseCode < 400) inputStream = connection.getInputStream();
      else inputStream = connection.getErrorStream();
      if(inputStream != null) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.lines().forEach(body::append);
      }

      // Build the response
      response.setPath(request.getPath());
      response.setStatus(responseCode);
      response.setBody(body.toString());
      response.setUrl(connection.getURL().toString());
      response.setHeaders(getHeaders(connection));
      response.setCookies(getCookies(connection));
    } catch (Exception e) {
      System.err.println(getClass().getName() + ": " + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  private void addCookies(HttpRequest pendingRequest, HttpURLConnection connection) {
    StringBuilder sb = new StringBuilder();
    pendingRequest.getCookies().forEach((key, val) -> {
      sb.append(String.format("%s=%s; path=%s", key, val.value(), val.path())).append("; ");
      String cookie = sb.toString();
      connection.addRequestProperty("Cookie", Strings.trimEnd(cookie));
    });
  }

  private HashMap<String,HttpEntity.Cookie> getCookies(HttpURLConnection connection) {
    HashMap<String, HttpEntity.Cookie> cookies = new HashMap<>();
    connection.getHeaderFields().forEach((key, value) -> {
      if(key != null && key.toLowerCase().contains("cookie")) {
        value.forEach(val -> {
          HttpEntity.Cookie cookie = HttpEntity.Cookie.create(val);
          cookies.put(cookie.name(), cookie);
        });
      }
    });
    return cookies;
  }

  private HashMap<String, List<String>> getHeaders(HttpURLConnection connection) {
    HashMap<String, List<String>> headers = new HashMap<>();
    connection.getHeaderFields().forEach((key, value) -> {
      if (key != null && !key.toLowerCase().contains("cookie")) {
        headers.put(key, value);
      }
    });
    return headers;
  }

  private void writeRequestBody(HttpRequest request, HttpURLConnection conn) {
    DataOutputStream stream;
    conn.setRequestProperty("Content-Type", request.getContentType());
    try {
      stream = new DataOutputStream(conn.getOutputStream());
      stream.writeBytes(request.getBody());
      stream.flush();
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public enum ReadyState {
    Idle, Ready, Error
  }
}
