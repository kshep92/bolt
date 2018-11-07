package com.boltframework.test.mocks;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpFrame;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;

public class MockHttpServerResponse implements HttpServerResponse {
  @Override
  public HttpServerResponse exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public HttpServerResponse write(Buffer data) {
    return null;
  }

  @Override
  public HttpServerResponse setWriteQueueMaxSize(int maxSize) {
    return null;
  }

  @Override
  public boolean writeQueueFull() {
    return false;
  }

  @Override
  public HttpServerResponse drainHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public int getStatusCode() {
    return 0;
  }

  @Override
  public HttpServerResponse setStatusCode(int statusCode) {
    return null;
  }

  @Override
  public String getStatusMessage() {
    return null;
  }

  @Override
  public HttpServerResponse setStatusMessage(String statusMessage) {
    return null;
  }

  @Override
  public HttpServerResponse setChunked(boolean chunked) {
    return null;
  }

  @Override
  public boolean isChunked() {
    return false;
  }

  @Override
  public MultiMap headers() {
    return null;
  }

  @Override
  public HttpServerResponse putHeader(String name, String value) {
    return null;
  }

  @Override
  public HttpServerResponse putHeader(CharSequence name, CharSequence value) {
    return null;
  }

  @Override
  public HttpServerResponse putHeader(String name, Iterable<String> values) {
    return null;
  }

  @Override
  public HttpServerResponse putHeader(CharSequence name, Iterable<CharSequence> values) {
    return null;
  }

  @Override
  public MultiMap trailers() {
    return null;
  }

  @Override
  public HttpServerResponse putTrailer(String name, String value) {
    return null;
  }

  @Override
  public HttpServerResponse putTrailer(CharSequence name, CharSequence value) {
    return null;
  }

  @Override
  public HttpServerResponse putTrailer(String name, Iterable<String> values) {
    return null;
  }

  @Override
  public HttpServerResponse putTrailer(CharSequence name, Iterable<CharSequence> value) {
    return null;
  }

  @Override
  public HttpServerResponse closeHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public HttpServerResponse endHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public HttpServerResponse write(String chunk, String enc) {
    return null;
  }

  @Override
  public HttpServerResponse write(String chunk) {
    return null;
  }

  @Override
  public HttpServerResponse writeContinue() {
    return null;
  }

  @Override
  public void end(String chunk) {

  }

  @Override
  public void end(String chunk, String enc) {

  }

  @Override
  public void end(Buffer chunk) {

  }

  @Override
  public void end() {

  }

  @Override
  public HttpServerResponse sendFile(String filename) {
    return null;
  }

  @Override
  public HttpServerResponse sendFile(String filename, long offset) {
    return null;
  }

  @Override
  public HttpServerResponse sendFile(String filename, long offset, long length) {
    return null;
  }

  @Override
  public HttpServerResponse sendFile(String filename, Handler<AsyncResult<Void>> resultHandler) {
    return null;
  }

  @Override
  public HttpServerResponse sendFile(String filename, long offset, Handler<AsyncResult<Void>> resultHandler) {
    return null;
  }

  @Override
  public HttpServerResponse sendFile(String filename, long offset, long length, Handler<AsyncResult<Void>> resultHandler) {
    return null;
  }

  @Override
  public void close() {

  }

  @Override
  public boolean ended() {
    return false;
  }

  @Override
  public boolean closed() {
    return false;
  }

  @Override
  public boolean headWritten() {
    return false;
  }

  @Override
  public HttpServerResponse headersEndHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public HttpServerResponse bodyEndHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public long bytesWritten() {
    return 0;
  }

  @Override
  public int streamId() {
    return 0;
  }

  @Override
  public HttpServerResponse push(HttpMethod method, String host, String path, Handler<AsyncResult<HttpServerResponse>> handler) {
    return null;
  }

  @Override
  public HttpServerResponse push(HttpMethod method, String path, MultiMap headers, Handler<AsyncResult<HttpServerResponse>> handler) {
    return null;
  }

  @Override
  public HttpServerResponse push(HttpMethod method, String path, Handler<AsyncResult<HttpServerResponse>> handler) {
    return null;
  }

  @Override
  public HttpServerResponse push(HttpMethod method, String host, String path, MultiMap headers, Handler<AsyncResult<HttpServerResponse>> handler) {
    return null;
  }

  @Override
  public void reset() {

  }

  @Override
  public void reset(long code) {

  }

  @Override
  public HttpServerResponse writeCustomFrame(int type, int flags, Buffer payload) {
    return null;
  }

  @Override
  public HttpServerResponse writeCustomFrame(HttpFrame frame) {
    return null;
  }
}
