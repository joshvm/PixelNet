package pixel.net.server.task.sql

import pixel.net.server.task.OneTimeTask
import pixel.net.server.punishment.Punishment
import java.sql.PreparedStatement
import pixel.net.server.db.Database

class RemovePunishmentTask(p: Punishment) extends OneTimeTask{

  def run(){
    val stmt: PreparedStatement = Database.connection.prepareStatement(
      "DELETE FROM punishments WHERE victimId=?"
    )
    stmt.setLong(1, p.victimId)
    stmt.executeUpdate()
    stmt.close()
  }

}
