
import java.nio.file._

import akka.actor._
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent._

object Serve extends App with Service {

  override implicit val system = ActorSystem("tutorial-system")
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()
  override val logger = Logging(system, getClass)

  val pathToMbTiles = args(0)
  require(Paths.get(pathToMbTiles).toFile.exists(), s"not found: $pathToMbTiles")
  val mbtiles: MBTiles = new MBTiles(pathToMbTiles)

  Http().bindAndHandle(root, "0.0.0.0", 8090)
}

trait Service {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer
  val logger: LoggingAdapter

  val mbtiles: MBTiles

  def root = cors() {
    pathPrefix(IntNumber / IntNumber / IntNumber) { (zoom, x, y) =>
      complete {
        Future {
          println(s"Tile: $zoom/$x/$y")
          mbtiles.fetch(zoom, x, y).map { bytes =>
            HttpResponse(entity = HttpEntity(bytes))
          }
        }
      }
    }
  }
}
