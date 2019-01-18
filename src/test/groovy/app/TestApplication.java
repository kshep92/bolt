package app;

import app.controllers.HttpContextController;
import com.boltframework.Bolt;
import com.boltframework.utils.Middleware;
import com.boltframework.web.WebService;
import com.boltframework.web.routing.ControllerCollection;
import com.boltframework.web.routing.InterceptorCollection;
import com.boltframework.web.routing.ResourceHandlerCollection;
import io.vertx.ext.web.handler.FaviconHandler;

public class TestApplication extends WebService {

  @Override
  public void addInterceptors(InterceptorCollection collection) {
    collection.addInterceptor(Middleware.convertVertxHandler(FaviconHandler.create("web/public/favicon.a91f16cf.ico")));
  }

  @Override
  public void addResourceHandlers(ResourceHandlerCollection resources) {
    resources.addHandler("/public/*", "web/public").cache(false);
  }

  @Override
  public void addControllers(ControllerCollection controllers) {
    controllers.register(HttpContextController.class);
  }

  public static void main(String[] args) {
    Bolt.createService(TestApplication.class).start();
  }

}
