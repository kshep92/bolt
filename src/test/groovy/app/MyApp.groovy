package app

import com.boltframework.Bolt
import com.boltframework.BoltApplication
import com.boltframework.config.ContextConfiguration
import com.boltframework.config.RouteRegistry
import com.boltframework.config.ServerConfiguration
import com.google.inject.Inject
import io.vertx.ext.web.Cookie

@ContextConfiguration(Configuration)
class MyApp extends BoltApplication {

  UserController userController

  @Inject
  MyApp(UserController userController) {
    this.userController = userController
  }

  @Override
  void configure(ServerConfiguration config) {
    config.httpConfiguration.port = 9000
    config.resources().url('/*')
  }

  @Override
  void doRoutes(RouteRegistry routes) {
    routes.staticFiles('/public/*', 'web/public')
    routes.get({ it.response.ok('get') })
    routes.get('/header', { it.response.addHeader('foo', 'bar').ok('header') })
    routes.get('/cookie', {
      def fooCookie = Cookie.cookie('foo', 'bar')
      it.addCookie(fooCookie).addCookie(Cookie.cookie('extra', 'cookie')).response.ok('cookie')
    })
    routes.post('/cookie', {
      String value = it.getCookie('foo').value
      it.response.send(value)
    })
    routes.post('/post', { it.response.send(it.bodyAsString) })
    routes.put('/put', { it.response.send('put') })
    routes.delete('/delete', { it.response.send('delete') })
    routes.mount('/users', userController)
    routes.get('/shutdown', { it.response.ok("Goodbye"); stop() })
    routes.get('/redirect', { it.response.redirect('http://www.google.com') } )
  }

  public static void main(String[] args) {
    // Manual start
    /*def app = Guice.createInjector(new Configuration()).getInstance(MyApp)
    app.start()*/

    // Manual start using Bolt
    /*
    * def app = Bolt.getInstance(MyApp)
    * app.start(started -> ... )
    * */

    Bolt.run(MyApp)
  }
}
