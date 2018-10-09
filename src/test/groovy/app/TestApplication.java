package app;

import app.controllers.Accounts;
import app.controllers.Application;
import app.controllers.Users;
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
    collection.addInterceptor(ctx -> ctx.put("permissions", new String[] {"read", "delete"})).addPathRegex("/users/(create|update)");
    collection.addInterceptor(ctx -> {
      ctx.addCookie("Name", "Jimmy Trice");
      ctx.next();
    }).addPath("/users/*");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerCollection resources) {
    resources.addHandler("/public/*", "web/public").cache(false);
  }

  @Override
  public void addControllers(ControllerCollection controllers) {
    controllers.register(Application.class)
        .register(Accounts.class)
        .register(Users.class);
  }

  public static void main(String[] args) {
    Bolt.createService(TestApplication.class).start();
  }

}
