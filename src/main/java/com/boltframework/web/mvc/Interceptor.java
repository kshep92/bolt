package com.boltframework.web.mvc;

import com.boltframework.web.HttpContext;

import java.util.function.Consumer;

public interface Interceptor extends Consumer<HttpContext> {
}
