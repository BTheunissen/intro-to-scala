package fundamentals.level04

/**
  * The exercises here are adapted from: http://www.cis.upenn.edu/~cis194/spring13/hw/02-ADTs.pdf
  *
  * Let's finish off this course by building a CLI program!
  * We will build a program to parse a log file (containing Info, Warn and Error messages) and print out errors over a severity level.
  *
  * Finish each exercise and we will head over to `Main.scala` to hook it all up with the CLI.
  */
object LogParser {

  /**
    * Here is how a log file may look.
    *
    * The `|` at the start of each line can be ignored. This is how Scala represents a multi-line String.
    */
  val logFile: String =
    """|I,147,mice in the air
       |W,149,could've been bad
       |E,5,158,some strange error
       |E,2,148,istereadea""".stripMargin

  /**
    * Let's define an ADT to represent all possible log messages.
    *
    * Hint: Add `sealed` in front of the `trait`s below to make sure you can only extend them from within this file.
    */

  /**
    * Start with all the possible log levels
    * - Info
    * - Warning
    * - Error with (severity: Int)
    */
  sealed trait LogLevel

  case object Info extends LogLevel
  case object Warning extends LogLevel
  case class Error(severity: Int) extends LogLevel

  /**
    * Now create an ADT for `LogMessage`, where `LogMessage` can be one of two possibilities:
    * - KnownLog with (logLevel, timestamp, message)
    * - UnknownLog with (message)
    */
  type Timestamp = Int

  sealed trait LogMessage

  case class KnownLog(logLevel: LogLevel, timestamp: Timestamp, message: String) extends LogMessage
  case class UnknownLog(message: String) extends LogMessage

 /**
   * - Once you have defined your data types:
   * 1. Remove `import Types._` from [LogParserTest.scala](src/test/scala/fundamentals/level04/LogParserTest.scala)
   * 2. Comment out the contents of src/test/scala/fundamentals/level04/Types.scala
   */

  /**
    * Define a function to parse an individual log message.
    *
    * scala> parseLog("I,147,mice in the air")
    * = KnownLog(Info, 147, "mice in the air")
    *
    * scala> parseLog("E,2,148,weird")
    * = KnownLog(Error(2), 148, "weird")
    *
    * scala> parseLog("X blblbaaaaa")
    * = UnknownLog("X blblbaaaaa")
    *
    * Hint: use the `split` method on `String` and pattern match on `Array`
    **/
  def parseLog(rawLog: String): LogMessage = rawLog.split(',') match {
    case Array("E", severity, timestamp, message) => KnownLog(Error(severity.toInt), timestamp.toInt, message)
    case Array("W", timestamp, message) => KnownLog(Warning, timestamp.toInt, message)
    case Array("I", timestamp, message) => KnownLog(Info, timestamp.toInt, message)
    case Array(message) => UnknownLog(message)
  }

  /**
    * scala> parseLogFile("I,147,mice in the air\nX blblbaaaaa")
    * = List(KnownLog(Info, 147, "mice in the air"), UnknownLog("X blblbaaaaa"))
    *
    * Hint: Use `parseLog`
    * Hint: Convert an Array to a List with .toList
    * What if we get an empty line from the fileContent?
    */
  def parseLogFile(rawContents: String): List[LogMessage] = rawContents match {
    case "" => List()
    case contents => contents.split('\n').toList.map(parseLog)
  }

  /**
    * Define a function that returns only logs that are errors over the given severity level.
    * It should not return any log that is not an error.
    *
    * scala> getErrorsOverSeverity(List(KnownLog(Error(2), 123, some error msg"), UnknownLog("blblbaaaaa")), 1)
    * = List(KnownLog(Error(2), 123, some error msg"))
    *
    * scala> getErrorsOverSeverity(List(KnownLog(Error(2), 123, some error msg")), 2)
    * = List()
    **/
  def getErrorsOverSeverity(logs: List[LogMessage], severity: Int): List[LogMessage] = logs.filter {
    case KnownLog(Error(logSeverity), _, _) if logSeverity > severity => true
    case _ => false
  }

  /**
    * Write a function to convert a `LogMessage` to a readable `String`.
    *
    * scala> showLogMessage(KnownLog(Info, 147, "mice in the air"))
    * = "Info (147) mice in the air"
    *
    * scala> showLogMessage(KnownLog(Error(2), 147, "weird"))
    * = "Error 2 (147) weird"
    *
    * scala> showLogMessage(UnknownLog("message"))
    * = "Unknown log: message"
    *
    * Hint: Pattern match and use string interpolation
    **/
  def showLogMessage(log: LogMessage): String = log match {
    case KnownLog(Error(severity), timestamp, message) => s"""Error $severity ($timestamp) $message"""
    case KnownLog(Warning, timestamp, message) => s"""Warning ($timestamp) $message"""
    case KnownLog(Info, timestamp, message) => s"""Info ($timestamp) $message"""
    case UnknownLog(message) => s"Unknown log: $message"
  }

  /**
    * Use `showLogMessage` on error logs with severity greater than the given `severity`.
    *
    * scala> showErrorsOverSeverity(logFile, 2)
    * = List("Error 5 (158) some strange error")
    *
    * Hint: Use `parseLogFile`, `getErrorsOverSeverity` and `showLogMessage`
    **/
  def showErrorsOverSeverity(fileContent: String, severity: Int): List[String] = {
    val parsedLogFile = parseLogFile(fileContent)
    getErrorsOverSeverity(parsedLogFile, severity).map(showLogMessage)
  }

  /**
    * Now head over to `Main.scala` in the same package to complete the rest of the program.
    */

}
