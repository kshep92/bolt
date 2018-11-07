package com.boltframework.test.mocks;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;

public class MockHttpServerRequest implements HttpServerRequest {
  @Override
  public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public HttpServerRequest handler(Handler<Buffer> handler) {
    return null;
  }

  @Override
  public HttpServerRequest pause() {
    return null;
  }

  @Override
  public HttpServerRequest resume() {
    return null;
  }

  @Override
  public HttpServerRequest endHandler(Handler<Void> endHandler) {
    return null;
  }

  @Override
  public HttpVersion version() {
    return null;
  }

  @Override
  public HttpMethod method() {
    return null;
  }

  @Override
  public String rawMethod() {
    return null;
  }

  @Override
  public boolean isSSL() {
    return false;
  }

  @Override
  public String scheme() {
    return null;
  }

  @Override
  public String uri() {
    return null;
  }

  @Override
  public String path() {
    return null;
  }

  @Override
  public String query() {
    return null;
  }

  @Override
  public String host() {
    return null;
  }

  @Override
  public HttpServerResponse response() {
    return null;
  }

  @Override
  public MultiMap headers() {
    return null;
  }

  @Override
  public String getHeader(String headerName) {
    return null;
  }

  @Override
  public String getHeader(CharSequence headerName) {
    return null;
  }

  @Override
  public MultiMap params() {
    return null;
  }

  @Override
  public String getParam(String paramName) {
    return null;
  }

  @Override
  public SocketAddress remoteAddress() {
    return null;
  }

  @Override
  public SocketAddress localAddress() {
    return null;
  }

  @Override
  public SSLSession sslSession() {
    return null;
  }

  @Override
  public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
    return new X509Certificate[0];
  }

  @Override
  public String absoluteURI() {
    return null;
  }

  @Override
  public HttpServerRequest bodyHandler(Handler<Buffer> bodyHandler) {
    return null;
  }

  @Override
  public NetSocket netSocket() {
    return null;
  }

  @Override
  public HttpServerRequest setExpectMultipart(boolean expect) {
    return null;
  }

  @Override
  public boolean isExpectMultipart() {
    return false;
  }

  @Override
  public HttpServerRequest uploadHandler(Handler<HttpServerFileUpload> uploadHandler) {
    return null;
  }

  @Override
  public MultiMap formAttributes() {
    return null;
  }

  @Override
  public String getFormAttribute(String attributeName) {
    return null;
  }

  @Override
  public ServerWebSocket upgrade() {
    return null;
  }

  @Override
  public boolean isEnded() {
    return false;
  }

  @Override
  public HttpServerRequest customFrameHandler(Handler<HttpFrame> handler) {
    return null;
  }

  @Override
  public HttpConnection connection() {
    return null;
  }
}
