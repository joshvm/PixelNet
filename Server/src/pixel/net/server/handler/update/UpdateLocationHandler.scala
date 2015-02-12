package pixel.net.server.handler.update

import io.netty.buffer.ByteBuf
import pixel.net.server.player.Player
import io.netty.channel.ChannelHandlerContext
import pixel.net.server.handler.EventHandler

object UpdateLocationHandler extends EventHandler{

  def handle(ctx: ChannelHandlerContext, player: Player, buf: ByteBuf){
    val x: Short = buf.readShort()
    val y: Short = buf.readShort()
    player.setLocation(x, y)
  }

}
