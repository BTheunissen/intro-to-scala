package day2.level03

import cats.effect.IO
import day2.level03.models._

/**
  * A DataStore implementation that uses sample data from `models.scala`
  */
object HardcodedDataStore extends DataStore {

  override def getAllMovies(): IO[Either[String, List[models.Movie]]] = ???

  override def getReviews(movieId: MovieId): IO[Either[String, List[models.Review]]] = ???

  override def addMovie(name: String, desc: String): IO[Either[String, Unit]] = ???

  override def addReview(movieId: MovieId, author: String, comment: String): IO[Either[String, Unit]] = ???

}