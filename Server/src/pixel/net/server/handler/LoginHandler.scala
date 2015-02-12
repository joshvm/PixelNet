package pixel.net.server.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.ByteBuf
import pixel.net.server.utils.Utils
import pixel.net.server.punishment.{PunishmentFlag, Punishment}
import pixel.net.server.player.{Players, Player}

object LoginHandler extends EventHandler{

  def handle(ctx: ChannelHandlerContext, player: Player, buf: ByteBuf){
    val name: String = Utils.readString(buf)
    val pass: String = Utils.readString(buf)
    val opt: Option[Player] = Players.get(name)
    if(opt.isEmpty){
      Utils.sendPopupMessage(ctx, "Name not registered: " + name)
      return
    }
    val player: Player = opt.get
    if(!player.pass.equals(pass)){
      Utils.sendPopupMessage(ctx, "Password mismatch")
      return
    }
    val punishmentOpt: Option[Punishment] = player.punishment
    if(punishmentOpt.isDefined){
      val punishment: Punishment = punishmentOpt.get
      if(punishment.has(PunishmentFlag.IpBan)){
        Utils.sendPopupMessage(ctx, "You are ip banned. Try again later")
        return
      }
    }
    ctx.attr(Player.Key).set(player)
    player.ctx = ctx
    Players.join(player)
  }

}
