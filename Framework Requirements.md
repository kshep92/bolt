# Framework Requirements

## Design

The framework should be friendly to dependency injection and mocking (for testing).

## Configuration

The user should be able to configure the following properties:
- Http host
- Http port

The framework should provide a utility class for easily reading Booleans, Strings and 
Integers from the Environment.

JVM system properties should be read first, then system environment variables.

## Cookies

Cookies are handled by default on all requests.

## Body handling

Request bodies are parsed for all POST, PUT and PATCH requests.

## Form validation

The framework should provide a built-in means of validating forms.

```
SignupForm form = ctx.getBodyAs(SignupForm.class)
if(!form.valid()) { ctx.badRequest(form.getErrorMessage()) }
else {
  dao.save(form.getInstanceOf(Account.class))
  ctx.ok("Account created!")
}

/* Or */

Account account = dao.findById(Long.parseLong(ctx.request().params('id')))
form.copyPropertiesTo(account)
dao.update(account)

```

## Routing

The framework should provide a simple API to receive HTTP requests and return HTTP responses from the client.

```
routes.get(url, handler)
routes.post(url, handler)
routes.put(url, handler)
routes.patch(url, handler)
routes.delete(url, handler)
```

The framework should facilitate the organization of routes by separation.

```
routes.mount(prefix, controller)
```


## Static files

The user should be able to specify which directories static files should be served from. As
part of this specification, the user should be able to turn caching on and off and specify
how long an asset should be cached for.

By default, if the `app.mode` environment property is not set or set to "dev" or "development" then caching is disabled.
If "prod" or "production" then caching is enabled and all assets will be cached for 3600s (1hr).

```
routes.staticFiles("/public/*", "web/public") // Use default configuration

```

The same can be achieved with the following:

```
StaticFilesConfiguration config = new StaticFilesConfiguration();
config.setCacheEnabled(true);
config.setMaxAge(3600);

routes.staticFiles("/static/*", "web/public", config);
```

## Testing

The framework should provide an easy means of performing integration testing. This would include:

- An abstract superclass that wraps common test initialization logic.
- An HTTP client for building and sending requests
- Automatic start and stop of application server