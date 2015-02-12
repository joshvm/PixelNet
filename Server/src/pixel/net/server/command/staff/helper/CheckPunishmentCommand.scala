package pixel.net.server.command.staff.helper

import pixel.net.server.command.Command
import pixel.net.server.punishment.Punishment
import pixel.net.server.player.{Players, Player, Rank}

object CheckPunishmentCommand extends Command("checkpunishment", "[target]", Rank.Helper){

  def execute(player: Player, args: Array[String]){
    val targetOpt: Option[Player] = Players.get(args(0))
    if(targetOpt.isEmpty){
      player.sendMessage("Unable to find player: " + args(0))
      return
    }
    val target: Player = targetOpt.get
    val punishmentOpt: Option[Punishment] = target.punishment
    if(punishmentOpt.isEmpty){
      player.sendMessage(s"$target is not punished")
      return
    }
    val punishment: Punishment = punishmentOpt.get
    player.sendMessage(s"[Punished] $target | ${punishment.objFlags} | ${punishment.hours} Hours")
    player.sendMessage(s"[Punished] For: ${punishment.reason}")
    player.sendMessage(s"[Punished] Issuer: ${punishment.issuer}")
  }

}
