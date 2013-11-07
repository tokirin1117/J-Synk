package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.{HttpServer, ServerWebSocket}
import org.jokbal.pusher.net.{WebSocketConnection, SockJsSocketConnection, ConnectionManager}
import org.vertx.scala.core.sockjs.{SockJSSocket, SockJSServer}
import org.vertx.scala.core.json.JsonObject

class SocketServer extends Verticle {

  override def start() {
    val port = 8080
    val httpServer: HttpServer = vertx.createHttpServer()
    val sockJsServer: SockJSServer = vertx.createSockJSServer(httpServer)
    httpServer.websocketHandler(webSocketOpenHandler _)
    sockJsServer.installApp(new JsonObject().putString("prefix", "/"), sockJsSocketOpenHandler _)
    httpServer listen 8080
    println("WebSocket Server Listening on " + port)
    println("SockJs Server Listening on " + port)
  }

  def webSocketOpenHandler(socket: ServerWebSocket):Unit = {
    ConnectionManager.connect(new WebSocketConnection(socket))
  }

  def sockJsSocketOpenHandler(socket: SockJSSocket) {
    ConnectionManager.connect(new SockJsSocketConnection(socket))
  }
}