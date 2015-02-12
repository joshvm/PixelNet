package pixel.net.server.punishment

import pixel.net.server.db.{Saveable, Database}
import java.sql.PreparedStatement
import java.util.concurrent.TimeUnit
import pixel.net.server.player.{Players, Player}

class Punishment(val issuerId: Long, val victimId: Long, val reason: String, val hours: Int, val time: Long = System.currentTimeMillis) extends Saveable{

  var flags: Int = 0

  lazy val issuer: Player = Players.get(issuerId).get
  lazy val victim: Player = Players.get(victimId).get

  def objFlags: Array[PunishmentFlag] = PunishmentFlag.values.filter(has)

  def +=(pf: PunishmentFlag) = flags |= pf.flag

  def has(pf: PunishmentFlag) = (flags & pf.flag) != 0

  def -=(pf: PunishmentFlag) = flags &= ~pf.flag

  def isExpired: Boolean = flags == 0 || System.currentTimeMillis - time >= TimeUnit.HOURS.toMillis(hours)

  def save(){
    val stmt: PreparedStatement = Database.connection.prepareStatement("UPDATE punishments SET flags=? WHERE victimId=?")
    stmt.setInt(1, flags)
    stmt.setLong(2, victimId)
    stmt.executeUpdate()
    stmt.close()
  }

}
