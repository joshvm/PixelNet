package pixel.net.server.task.sql

import pixel.net.server.task.OneTimeTask
import pixel.net.server.punishment.Punishment
import java.sql.PreparedStatement
import pixel.net.server.db.Database

class AddPunishmentTask(p: Punishment) extends OneTimeTask{

  def run(){
    val stmt: PreparedStatement = Database.connection.prepareStatement(
      "INSERT INTO punishments VALUES(?, ?, ?, ?, ?, ?)"
    )
    stmt.setInt(1, p.flags)
    stmt.setLong(2, p.issuerId)
    stmt.setLong(3, p.victimId)
    stmt.setString(4, p.reason)
    stmt.setInt(5, p.hours)
    stmt.setLong(6, p.time)
    stmt.executeUpdate()
    stmt.close()
  }

}
