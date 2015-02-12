package pixel.net.server.handler.update

import io.netty.buffer.ByteBuf
import pixel.net.server.player.{UpdateFlags, Player}
import io.netty.channel.ChannelHandlerContext
import pixel.net.server.handler.EventHandler

object UpdateHandler extends EventHandler{

  private val FlagHandlers: Array[EventHandler] = new Array(5)
  FlagHandlers(UpdateFlags.Location) = UpdateLocationHandler

  def handle(ctx: ChannelHandlerContext, player: Player, buf: ByteBuf){
    val flag: Byte = buf.readByte()
    FlagHandlers(flag).handle(ctx, player, buf)
  }

}
