package pixel.net.server.command.staff.punish

import pixel.net.server.command.Command
import pixel.net.server.punishment.{PunishmentFlag, Punishments, Punishment}
import pixel.net.server.task.sql.SaveTask
import pixel.net.server.task.TaskSystem
import pixel.net.server.player.{Players, Player, Rank}

class PunishmentCommand(prefix: String, rank: Rank, flags: PunishmentFlag*) extends Command(prefix, "[target] [hours] [reason]", rank){

  def execute(player: Player, args: Array[String]){
    val targetOpt: Option[Player] = Players.get(args(0))
    if(targetOpt.isEmpty){
      player.sendMessage("Unable to find player: " + args(0))
      return
    }
    val target: Player = targetOpt.get
    val hours: Int = args(1).toInt
    val reason: String = join(args, 2)
    val oldPunishmentOpt: Option[Punishment] = target.punishment
    val p: Punishment = if(oldPunishmentOpt.isEmpty) new Punishment(player.id, target.id, reason, hours) else oldPunishmentOpt.get
    var needsUpdate: Boolean = false
    for(flag <- flags){
      if(p.has(flag)){
        player.sendMessage(s"$flag already found on $target's account")
      }else{
        p += flag
        player.sendMessage(s"$flag added to $target's account")
        needsUpdate = true
      }
    }
    if(oldPunishmentOpt.isEmpty)
      Punishments += p
    else if(needsUpdate)
      TaskSystem.submit(new SaveTask(p))
  }

}
