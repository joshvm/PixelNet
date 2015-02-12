package pixel.net.server.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.ByteBuf
import pixel.net.server.player.Player

trait EventHandler {

  def handle(ctx: ChannelHandlerContext, player: Player, buf: ByteBuf)

}
