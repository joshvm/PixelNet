package pixel.net.server

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.buffer.ByteBuf
import io.netty.util.ReferenceCountUtil
import pixel.net.server.player.{Players, Player}
import pixel.net.server.handler.{MessageHandler, LoginHandler, RegisterHandler, EventHandler}
import pixel.net.server.handler.update.UpdateHandler
import pixel.net.server.utils.Utils

class ServerHandler extends ChannelInboundHandlerAdapter {

  private val Handlers: Array[EventHandler] = new Array(7)
  Handlers(Opcodes.Register) = RegisterHandler
  Handlers(Opcodes.Login) = LoginHandler
  Handlers(Opcodes.Message) = MessageHandler
  Handlers(Opcodes.PlayerUpdate) = UpdateHandler

  override def handlerRemoved(ctx: ChannelHandlerContext){
    val player: Player = ctx.attr(Player.Key).getAndRemove
    if(player == null)
      return
    Players.leave(player)
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef){
    val buffer: ByteBuf = msg.asInstanceOf[ByteBuf]
    try{
      val opcode: Byte = buffer.readByte()
      val player: Player = ctx.attr(Player.Key).get
      Handlers(opcode).handle(ctx, player, buffer)
    }finally{
      ReferenceCountUtil.release(msg)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, ex: Throwable){
    ex.printStackTrace()
    ctx.close()
  }

}
