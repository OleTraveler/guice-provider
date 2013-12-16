# Guice Provider

Helper functions to convert Guice injector into scalaz Reader providers.

See examples in GuiceProviderSpec: https://github.com/OleTraveler/guice-provider/blob/master/src/test/scala/GuiceProviderSpec.scala.

This project exists because the application I work on uses Guice for dependency injection, but I wanted to start implementing some smaller modules to  use scalaz Reader after this interesting talk: http://www.youtube.com/watch?v=ZasXwtTRkio
