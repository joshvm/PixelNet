package pixel.net.server.task.sql

import pixel.net.server.task.OneTimeTask
import java.sql.PreparedStatement
import pixel.net.server.db.Database
import pixel.net.server.player.Player

class AddPlayerTask(player: Player) extends OneTimeTask{

  def run(){
    val stmt: PreparedStatement = Database.connection.prepareStatement(
      "INSERT INTO players VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    )
    stmt.setString(1, player.name)
    stmt.setString(2, player.pass)
    stmt.setLong(3, player.id)
    stmt.setByte(4, player.rank.id)
    stmt.setShort(5, player.x)
    stmt.setShort(6, player.y)
    stmt.setByte(7, player.width)
    stmt.setByte(8, player.height)
    stmt.setByte(9, player.speed)
    stmt.setInt(10, player.color)
    stmt.executeUpdate()
    stmt.close()
  }

}
