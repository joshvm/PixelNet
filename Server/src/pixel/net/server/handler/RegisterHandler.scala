package pixel.net.server.handler

import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.ChannelHandlerContext
import pixel.net.server.utils.Utils
import pixel.net.server.Opcodes
import pixel.net.server.player.{Players, Player}

object RegisterHandler extends EventHandler{

  def handle(ctx: ChannelHandlerContext, player: Player, buf: ByteBuf){
    val name: String = Utils.readString(buf)
    val pass: String = Utils.readString(buf)
    if(!validateName(ctx, name) || !validatePass(ctx, pass))
      return
    Players += new Player(System.nanoTime, name, pass)
    Utils.sendPopupMessage(ctx, s"Successfully registered $name")
  }

  private def validateName(ctx: ChannelHandlerContext, name: String): Boolean = {
    if(!checkRange(ctx, "name", name, 1, 12))
      return false
    if(!checkChars(ctx, "name", name))
      return false
    if(Players.get(name).isDefined){
      Utils.sendPopupMessage(ctx, s"$name is already registered")
      return false
    }
    true
  }

  private def validatePass(ctx: ChannelHandlerContext, pass: String): Boolean = {
    checkRange(ctx, "password", pass, 5, 15) && checkChars(ctx, "password", pass)
  }

  private def checkRange(ctx: ChannelHandlerContext, name: String, value: String, min: Int, max: Int): Boolean = {
    if(!isBetween(value.length, min, max)){
      Utils.sendPopupMessage(ctx, s"$name must be inbetween [$min, $max] in length")
      return false
    }
    true
  }

  private def checkChars(ctx: ChannelHandlerContext, name: String, value: String): Boolean = {
    for(c <- value.toCharArray if !isBetween(c, 65, 90) && !isBetween(c, 97, 122) && !isBetween(c, 48, 57)){
      Utils.sendPopupMessage(ctx, s"$name can only contain letters and numbers")
      return false
    }
    true
  }

  private def isBetween(c: Int, min: Int, max: Int): Boolean = c >= min && c <= max

}
