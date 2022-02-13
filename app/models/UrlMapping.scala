package models

import java.sql.Timestamp

case class UrlMapping(id: Long, longUrl: String, created_at: Timestamp)
