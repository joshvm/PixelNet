package pixel.net.server.player

import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.{Unpooled, ByteBuf}
import pixel.net.server.Opcodes
import pixel.net.server.utils.Utils
import pixel.net.server.db.{Saveable, Database}
import java.sql.PreparedStatement
import io.netty.util.AttributeKey
import pixel.net.server.punishment.{Punishments, Punishment}

class Player(val id: Long, val name: String, var pass: String) extends Saveable{
  
  var ctx: ChannelHandlerContext = null
  var rank: Rank = Rank.Player
  var x: Short = 0
  var y: Short = 0
  var width: Byte = 25
  var height: Byte = 25
  var speed: Byte = 2
  var color: Int = rank.color

  var loginTime: Long = -1
  
  def isConnected: Boolean = ctx != null && (ctx.channel.isActive || ctx.channel.isOpen)

  def punishment: Option[Punishment] = Punishments.get(id)

  def sendLogin(){
    val buf: ByteBuf = Unpooled.buffer(22 + name.length)
    buf.writeByte(Opcodes.Login) //1
    buf.writeLong(id) //8
    Utils.writeString(buf, name) //1 + name.length
    buf.writeByte(rank.id) //1
    buf.writeShort(x) //2
    buf.writeShort(y) //2
    buf.writeByte(width) //1
    buf.writeByte(height) //1
    buf.writeByte(speed) //1
    buf.writeInt(color) //4
    send(buf)
  }
  
  def setLocation(x: Short, y: Short, force: Boolean = false){
    if(this.x == x && this.y == y)
      return
    val newX: Short = if(x < 0 || x > Utils.GameWidth + width) this.x else x
    val newY: Short = if(y < 0 || y > Utils.GameHeight + height) this.y else y
    if(this.x == newX && this.y == newY)
      return
    val dx: Short = Math.abs(this.x - newX).toShort
    val dy: Short = Math.abs(this.y - newY).toShort
    if(!force && (dx > speed || dy > speed))
      Players.sendStaffMessage(s"$name is trying to tele: speed=$speed, dx=$dx, dy=$dy")
    this.x = newX
    this.y = newY
    val buf: ByteBuf = Unpooled.buffer(14)
    buf.writeByte(Opcodes.PlayerUpdate) //1
    buf.writeLong(id) //8
    buf.writeByte(UpdateFlags.Location) //1
    buf.writeShort(newX) //2
    buf.writeShort(newY) //2
    Players.send(buf)
  }

  def setSize(width: Byte, height: Byte){
    if(this.width == width && this.height == height)
      return
    val newWidth: Byte = if(width < 0) this.width else width
    val newHeight: Byte = if(height < 0) this.height else height
    if(this.width == newWidth && this.height == newHeight)
      return
    this.width = newWidth
    this.height = newHeight
    val buf: ByteBuf = Unpooled.buffer(12)
    buf.writeByte(Opcodes.PlayerUpdate) //1
    buf.writeLong(id) //8
    buf.writeByte(UpdateFlags.Size) //1
    buf.writeByte(width) //1
    buf.writeByte(height) //1
    Players.send(buf)
  }
  
  def setRank(rank: Rank){
    if(this.rank == rank)
      return
    val oldRank: Rank = this.rank
    this.rank = rank
    val buf: ByteBuf = Unpooled.buffer(11)
    buf.writeByte(Opcodes.PlayerUpdate) //1
    buf.writeLong(id) //8
    buf.writeByte(UpdateFlags.Rank) //1
    buf.writeByte(rank.id) //1
    Players.send(buf)
    val promotion: Boolean = rank.id > oldRank.id
    Players.sendMessage(s"[Update] $name has been ${if(promotion) "demoted" else "promoted"} from $oldRank to $rank")
    setColor(rank.color)
  }

  def hasRank(rank: Rank): Boolean = this.rank.id > rank.id

  def setSpeed(speed: Byte){
    if(this.speed == speed)
      return
    val newSpeed: Byte = if(speed < 0) this.speed else speed
    if(this.speed == newSpeed)
      return
    this.speed = newSpeed
    val buf: ByteBuf = Unpooled.buffer(11)
    buf.writeByte(Opcodes.PlayerUpdate) //1
    buf.writeLong(id) //8
    buf.writeByte(UpdateFlags.Speed) //1
    buf.writeByte(speed) //1
    Players.send(buf)
  }

  def setColor(color: Int){
    if(color < 0 || color > 0xFFFFFF)
      return
    this.color = color
    val buf: ByteBuf = Unpooled.buffer(14)
    buf.writeByte(Opcodes.PlayerUpdate) //1
    buf.writeLong(id) //8
    buf.writeByte(UpdateFlags.Color) //1
    buf.writeInt(color) //4
    Players.send(buf)
  }

  def sendPopup(msg: String){
    val buf: ByteBuf = Unpooled.buffer(2 + msg.length)
    buf.writeByte(Opcodes.PopupMessage)
    Utils.writeString(buf, msg)
    send(buf)
  }
  
  def sendMessage(msg: String){
    val buf: ByteBuf = Unpooled.buffer(2 + msg.length)
    buf.writeByte(Opcodes.Message)
    Utils.writeString(buf, msg)
    send(buf)
  }

  def send(buf: ByteBuf){
    if(!isConnected){
      println(s"*** TRYING TO SEND DATA WHEN NOT CONNECTED: $this***")
      return
    }
    ctx.writeAndFlush(buf)
  }

  def save(){
    val stmt: PreparedStatement = Database.connection.prepareStatement(
      "UPDATE players SET " +
        "pass=?, " +
        "rank=?, " +
        "x=?, " +
        "y=?, " +
        "width=?, " +
        "height=?, " +
        "speed=?, " +
        "color=? " +
        "WHERE id=?"
    )
    stmt.setString(1, pass)
    stmt.setByte(2, rank.id)
    stmt.setShort(3, x)
    stmt.setShort(4, y)
    stmt.setByte(5, width)
    stmt.setByte(6, height)
    stmt.setByte(7, speed)
    stmt.setInt(8, color)
    stmt.setLong(9, id)
    stmt.executeUpdate()
    stmt.close()
  }
  
  override def equals(o: Any): Boolean = {
    if(o == null)
      return false
    val p: Player = o.asInstanceOf[Player]
    p.id == id
  }

  override def toString: String = name
}

object Player{
  val Key: AttributeKey[Player] = AttributeKey.valueOf("player")
}