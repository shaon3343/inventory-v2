import akka.http.scaladsl.model.StatusCodes.ClientError
import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent._
import javax.inject.Singleton

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      //Status(statusCode)("A client error occurred: " + message)
      NotFound(views.html.errors.page_404.render())
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      //InternalServerError("A server error occurred: " + exception.getMessage)
      InternalServerError(
        views.html.errors.page_404.render()
      )

    )
  }
}
