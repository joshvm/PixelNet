package pixel.net.server.handler

import io.netty.buffer.ByteBuf
import pixel.net.server.player.{Players, Player}
import io.netty.channel.ChannelHandlerContext
import pixel.net.server.utils.Utils
import pixel.net.server.command.Command

object MessageHandler extends EventHandler{
  
  def handle(ctx: ChannelHandlerContext, player: Player, buf: ByteBuf){
    val msg: String = Utils.readString(buf).trim
    if(msg.isEmpty)
      return
    if(msg.startsWith(Command.Prefix)){
      CommandHandler.handle(msg.substring(Command.Prefix.length).toLowerCase, player)
      return
    }
    Players.sendMessage(s"[${player.rank}][${player.name}]: $msg")
  }

}
