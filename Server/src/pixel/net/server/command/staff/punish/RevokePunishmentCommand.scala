package pixel.net.server.command.staff.punish

import pixel.net.server.command.Command
import pixel.net.server.punishment.{PunishmentFlag, Punishment}
import pixel.net.server.task.sql.{RemovePunishmentTask, SaveTask}
import pixel.net.server.task.TaskSystem
import pixel.net.server.player.{Players, Player, Rank}

class RevokePunishmentCommand(prefix: String, rank: Rank, flags: PunishmentFlag*) extends Command(prefix, "[target]", rank){

  def execute(player: Player, args: Array[String]){
    val targetOpt: Option[Player] = Players.get(args(0))
    if(targetOpt.isEmpty){
      player.sendMessage("Unable to find player: " + args(0))
      return
    }
    val target: Player = targetOpt.get
    val punishmentOpt: Option[Punishment] = target.punishment
    if(punishmentOpt.isEmpty){
      player.sendMessage("No punishment found for: " + target.name)
      return
    }
    val punishment: Punishment = punishmentOpt.get
    var needsUpdate: Boolean = false
    for(flag <- flags){
      if(punishment.has(flag)){
        punishment -= flag
        player.sendMessage(s"$flag removed from $target's account")
        needsUpdate = true
      }else{
        player.sendMessage(s"no $flag found for $target")
      }
    }
    if(needsUpdate)
      TaskSystem.submit(if(punishment.isExpired) new RemovePunishmentTask(punishment) else new SaveTask(punishment))
  }

}
