package pixel.net.server.handler

import scala.collection.mutable
import pixel.net.server.command.player.PlayersCommand
import pixel.net.server.command.staff.helper.{UnMuteCommand, MuteCommand}
import pixel.net.server.command.Command
import pixel.net.server.command.staff.mod.{BanCommand, UnBanCommand}
import pixel.net.server.command.staff.admin.{IpMuteCommand, IpBanCommand, UnIpBanCommand, UnIpMuteCommand}
import pixel.net.server.player.Player
import pixel.net.server.command.staff.owner.KickAllCommand

object CommandHandler {

  private val Commands: mutable.Map[String, Command] = mutable.Map()

  this += PlayersCommand

  this += MuteCommand
  this += UnMuteCommand

  this += BanCommand
  this += UnBanCommand

  this += IpMuteCommand
  this += UnIpMuteCommand
  this += IpBanCommand
  this += UnIpBanCommand

  this += KickAllCommand

  def handle(str: String, player: Player){
    if(str.isEmpty)
      return
    val i: Int = str.indexOf(" ")
    val prefix: String = str.substring(0, if(i < 0) str.length else i)
    val opt: Option[Command] = Commands.get(prefix)
    if(opt.isEmpty)
      return
    val cmd: Command = opt.get
    if(!player.hasRank(cmd.rank)){
      player.sendMessage(s"You need to be a ${cmd.rank} to use this command")
      return
    }
    val rest: String = if(i < 0) "" else str.substring(i+1)
    try{
      cmd.execute(player, rest.split(" +"))
    }catch{
      case ex: Exception =>
        player.sendMessage(s"Error processing command. Syntax: ${Command.Prefix}${cmd.prefix} ${cmd.argSytnax}".trim)
    }
  }

  def +=(cmd: Command) = Commands += cmd.prefix -> cmd

}
