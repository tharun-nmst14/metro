package com.metro.ufm.models

import java.time.LocalDate

/** Maps the proposed STEERING_PARAMETER table; it is not used by the current UI page. */
final case class SteeringParameter(
  id: Long,
  code: String,
  value: Option[String],
  status: Option[String],
  validFrom: Option[LocalDate],
  validTo: Option[LocalDate]
) extends Serializable
