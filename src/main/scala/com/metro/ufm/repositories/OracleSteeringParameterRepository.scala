package com.metro.ufm.repositories

import java.sql.{Date, PreparedStatement, ResultSet, SQLException, Types}
import scala.util.Using

import com.metro.ufm.database.OracleConnectionProvider
import com.metro.ufm.models.SteeringParameter

 
trait SteeringParameterRepository {
  def findAll(): Seq[SteeringParameter]
  def findByCode(code: String): Option[SteeringParameter]
  def update(parameter: SteeringParameter): Int
}

 
final class OracleSteeringParameterRepository(
  connectionProvider: OracleConnectionProvider
) extends SteeringParameterRepository {

  override def findAll(): Seq[SteeringParameter] = withConnection { connection =>
    Using.Manager { use =>
      val statement = use(connection.prepareStatement(OracleSteeringParameterRepository.FindAllSql))
      val resultSet = use(statement.executeQuery())
      readParameters(resultSet)
    }.get
  }

  override def findByCode(code: String): Option[SteeringParameter] = withConnection { connection =>
    Using.Manager { use =>
      val statement = use(connection.prepareStatement(OracleSteeringParameterRepository.FindByCodeSql))
      statement.setString(1, code)
      val resultSet = use(statement.executeQuery())
      if (resultSet.next()) Some(readParameter(resultSet)) else None
    }.get
  }

   override def update(parameter: SteeringParameter): Int = withConnection { connection =>
    Using.Manager { use =>
      val statement = use(connection.prepareStatement(OracleSteeringParameterRepository.UpdateSql))
      statement.setString(1, parameter.code)
      setOptionalString(statement, 2, parameter.value)
      setOptionalString(statement, 3, parameter.status)
      setOptionalDate(statement, 4, parameter.validFrom)
      setOptionalDate(statement, 5, parameter.validTo)
      statement.setLong(6, parameter.id)
      statement.executeUpdate()
    }.get
  }

  private def withConnection[A](operation: java.sql.Connection => A): A = {
    try {
      Using.Manager { use =>
        val connection = use(connectionProvider.openConnection())
        operation(connection)
      }.get
    } catch {
      case exception: SQLException =>
        throw new SteeringParameterRepositoryException(
          "Steering Parameter database operation failed.",
          exception
        )
      case exception: SteeringParameterRepositoryException =>
        throw exception
    }
  }

  private def readParameters(resultSet: ResultSet): Seq[SteeringParameter] = {
    val parameters = Seq.newBuilder[SteeringParameter]
    while (resultSet.next()) parameters += readParameter(resultSet)
    parameters.result()
  }

  private def readParameter(resultSet: ResultSet): SteeringParameter = SteeringParameter(
    id = resultSet.getLong("STEERING_PARAMETER_ID"),
    code = resultSet.getString("PARAMETER_CODE"),
    value = Option(resultSet.getString("PARAMETER_VALUE")),
    status = Option(resultSet.getString("STATUS")),
    validFrom = Option(resultSet.getDate("VALID_FROM")).map(_.toLocalDate),
    validTo = Option(resultSet.getDate("VALID_TO")).map(_.toLocalDate)
  )

  private def setOptionalString(statement: PreparedStatement, index: Int, value: Option[String]): Unit =
    value match {
      case Some(text) => statement.setString(index, text)
      case None => statement.setNull(index, Types.VARCHAR)
    }

  private def setOptionalDate(
    statement: PreparedStatement,
    index: Int,
    value: Option[java.time.LocalDate]
  ): Unit = value match {
    case Some(date) => statement.setDate(index, Date.valueOf(date))
    case None => statement.setNull(index, Types.DATE)
  }
}

object OracleSteeringParameterRepository {
  private[repositories] val FindAllSql =
    """SELECT STEERING_PARAMETER_ID, PARAMETER_CODE, PARAMETER_VALUE, STATUS, VALID_FROM, VALID_TO
      |FROM STEERING_PARAMETER
      |ORDER BY PARAMETER_CODE""".stripMargin

  private[repositories] val FindByCodeSql =
    """SELECT STEERING_PARAMETER_ID, PARAMETER_CODE, PARAMETER_VALUE, STATUS, VALID_FROM, VALID_TO
      |FROM STEERING_PARAMETER
      |WHERE PARAMETER_CODE = ?""".stripMargin

  private[repositories] val UpdateSql =
    """UPDATE STEERING_PARAMETER
      |SET PARAMETER_CODE = ?, PARAMETER_VALUE = ?, STATUS = ?, VALID_FROM = ?, VALID_TO = ?
      |WHERE STEERING_PARAMETER_ID = ?""".stripMargin
}

final class SteeringParameterRepositoryException(message: String, cause: Throwable)
    extends RuntimeException(message, cause)
