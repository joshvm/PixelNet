package pixel.net.server.punishment

import scala.collection.mutable
import java.sql.{ResultSet, Statement}
import pixel.net.server.db.Database
import pixel.net.server.task.TaskSystem
import pixel.net.server.task.sql.{RemovePunishmentTask, AddPunishmentTask}

object Punishments {
  
  private lazy val Map: mutable.Map[Long, Punishment] = mutable.Map()
  
  load()
  
  def +=(p: Punishment){
    Map += p.victimId -> p
    TaskSystem.submit(new AddPunishmentTask(p))
  }

  def -=(p: Punishment){
    Map -= p.victimId
    TaskSystem.submit(new RemovePunishmentTask(p))
  }

  def get(id: Long): Option[Punishment] = Map.get(id)

  def removeExpired(){
    for(p <- Map.values if p.isExpired)
      this -= p
  }
  
  private def load(){
    val stmt: Statement = Database.connection.createStatement()
    val rs: ResultSet = stmt.executeQuery("SELECT * FROM punishments")
    while(rs.next){
      val flags: Int = rs.getInt(1)
      val issuerId: Long = rs.getLong(2)
      val victimId: Long = rs.getLong(3)
      val reason: String = rs.getString(4)
      val hours: Int = rs.getInt(5)
      val time: Long = rs.getLong(6)
      val p: Punishment = new Punishment(issuerId, victimId, reason, hours, time)
      p.flags = flags
      Map += p.victimId -> p
    }
    rs.close()
    stmt.close()
  }

}
