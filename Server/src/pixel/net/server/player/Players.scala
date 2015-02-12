package pixel.net.server.player

import scala.collection.mutable
import pixel.net.server.task.TaskSystem
import pixel.net.server.task.sql.AddPlayerTask
import io.netty.buffer.{Unpooled, ByteBuf}
import pixel.net.server.Opcodes
import pixel.net.server.utils.Utils
import java.sql.{ResultSet, Statement}
import pixel.net.server.db.Database

object Players{

  private val All: mutable.Map[Long, Player] = mutable.Map()

  load()

  def size: Int = All.size

  def get(id: Long): Option[Player] = All.get(id)

  def get(name: String): Option[Player] = {
    for(p <- All.values if p.name.equalsIgnoreCase(name))
      return Some(p)
    None
  }

  def +=(player: Player){
    All += player.id -> player
    TaskSystem.submit(new AddPlayerTask(player))
  }

  def online: Iterable[Player] = {
    for(p <- All.values if p.isConnected)
      yield p
  }

  def send(buf: ByteBuf, exceptions: Player*){
    for(p <- online if !exceptions.contains(p))
      p.send(buf)
  }

  def sendToStaff(buf: ByteBuf){
    for(p <- online if p.hasRank(Rank.Helper))
      p.send(buf)
  }

  def sendMessage(msg: String, exceptions: Player*){
    for(p <- online if !exceptions.contains(p))
      p.sendMessage(msg)
  }

  def sendStaffMessage(msg: String){
    for(p <- online if p.hasRank(Rank.Helper))
      p.sendMessage(msg)
  }

  def leave(player: Player){
    player.loginTime = -1
    player.ctx = null
    player.save()
    val buf: ByteBuf = Unpooled.buffer(9)
    buf.writeByte(Opcodes.PlayerLeave) //1
    buf.writeLong(player.id) //8
    Players.send(buf)
  }

  def join(player: Player){
    player.loginTime = System.currentTimeMillis()
    player.sendLogin()
    val buf: ByteBuf = Unpooled.buffer(player.name.length + 19)
    buf.writeByte(Opcodes.PlayerJoin) //1
    buf.writeLong(player.id) //8
    Utils.writeString(buf, player.name) //name.length + 1
    buf.writeByte(player.rank.id) //1
    buf.writeShort(player.x) //2
    buf.writeShort(player.y) //2
    buf.writeByte(player.width) //1
    buf.writeByte(player.height) //1
    buf.writeByte(player.speed) //1
    buf.writeInt(player.color) //1
    send(buf, player)
  }

  private def load(){
    val stmt: Statement = Database.connection.createStatement()
    val rs: ResultSet = stmt.executeQuery("SELECT * FROM players")
    while(rs.next){
      val name: String = rs.getString(1)
      val pass: String = rs.getString(2)
      val id: Long = rs.getLong(3)
      val rank: Byte = rs.getByte(4)
      val x: Short = rs.getShort(5)
      val y: Short = rs.getShort(6)
      val width: Byte = rs.getByte(7)
      val height: Byte = rs.getByte(8)
      val speed: Byte = rs.getByte(9)
      val color: Int = rs.getInt(10)
      val player: Player = new Player(id, name, pass)
      player.rank = Rank.byId(rank)
      player.x = x
      player.y = y
      player.width = width
      player.height = height
      player.speed = speed
      player.color = color
      All += player.id -> player
    }
    rs.close()
    stmt.close()
  }

}
