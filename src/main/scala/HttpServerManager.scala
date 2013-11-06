package org.jokbal.pusher
import org.vertx.scala.core.Vertx
import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core.http.{RouteMatcher, HttpServerRequest, HttpServer}
import org.vertx.scala.core.buffer.Buffer

class HttpServerManager(vertx : Vertx,config : JsonObject){

  var server : HttpServer = null
  val SERVER_ENABLED = config.getString("server_enabled")
  val SERVER_PORT = config.getNumber("server_port").intValue()

  def startServer() {

    if(SERVER_ENABLED.equals("true")){
      System.out.println("start Http Server!")
      server = vertx.createHttpServer()
      server.requestHandler(this.makeRouteMatcher).listen(SERVER_PORT)
    }
    else throw new Exception("Http Server Config is not defined as true")

  }

  def makeRouteMatcher = {
    System.out.println("Make Routing Matcher")
    var routeMatch = new RouteMatcher()

    routeMatch.post("/apps/:appsId/events",{
      req : HttpServerRequest =>
        val appsId = req.params().get("appsId")

        req.dataHandler{
          bf : Buffer =>
            val length = bf.length()
            val body = bf.getString(0,length)
            val json = new JsonObject(body)

            println(body)

            /*
              TODO : send event by using publishEvent of Channel Class
              */
            req.response().setStatusCode(200).end()



        }



    })

    routeMatch.get("/apps/:appsId/channels",{
      req : HttpServerRequest =>
        val appsId = req.params().get("appsId")
        println("GET channels appsId : " + appsId)

        /*
        TODO : send Http response of all of current channels
        params :
          filter_by_prefix : Filter the returned channels by a specific prefix. For example in order to return only presence channels you would set filter_by_prefix=presence-
          info : A comma separated list of attributes which should be returned for each channel. If this parameter is missing, an empty hash of attributes will be returned for each channel

          available info attribute :
            user_count : Number of distinct users currently subscribed to this channel (a single user may be subscribed many times, but will only count as one)

        response :
         {
            "channels": {
              "presence-foobar": {
                user_count: 42
              },
              "presence-another": {
                user_count: 123
              }
            }
          }
         */
        req.response().setStatusCode(200).end()

    })

    routeMatch.get("/apps/:appsId/channels/:channelName",{
      req : HttpServerRequest =>
        val appsId = req.params().get("appsId")
        val chName = req.params().get("channelName")
        println("GET channels channelName appsId : " + appsId)
        println("GET channels channelName chName : " + chName)

        /*
        TODO : send Http response of current channel
         */

        req.response().setStatusCode(200).end()
    })

    routeMatch.get("/apps/:appsId/channels/:channelName/users",{
      req : HttpServerRequest =>

        val appsId = req.params().get("appsId")
        val chName = req.params().get("channelName")
        println("GET channels channelName users appsId : " + appsId)
        println("GET channels channelName users chName : " + chName)

        /*
         TODO : Returns an array of subscribed users ids
         */

        req.response().setStatusCode(200).end()
    })

    routeMatch



  }

}

