NettyHTTPServer
===============

My basic and first http server on netty.io

Особенности имплементации:

  1. Класс (СonnectionsInfo.java) отвечающий за статистику оформлен по паттерну Singleton with double checked locking.
  2. ServerResponseHandler.java - возвращает ответ от сервера в зависимости от того, какой URI в него передается.
  3. Connection - объект представляющий из себя одно соединение или один запрос к серверу.
