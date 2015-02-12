package pixel.net.server.utils

import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.ChannelHandlerContext
import pixel.net.server.Opcodes

object Utils {

  val GameWidth: Short = 700
  val GameHeight: Short = 500

  def writeString(buf: ByteBuf, str: String){
    buf.writeByte(str.length)
    str.toCharArray.foreach(c => buf.writeByte(c.asInstanceOf[Byte]))
  }

  def readString(buf: ByteBuf): String = {
    val length: Byte = buf.readByte()
    val bldr: StringBuilder = new StringBuilder(length)
    for(i <- 1 to length)
      bldr.append(buf.readByte().asInstanceOf[Char])
    bldr.toString()
  }

  def sendPopupMessage(ctx: ChannelHandlerContext, msg: String){
    val buf: ByteBuf = Unpooled.buffer(2 + msg.length)
    buf.writeByte(Opcodes.PopupMessage)
    Utils.writeString(buf, msg)
    ctx.writeAndFlush(buf)
  }

}
