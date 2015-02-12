package pixel.net.server.command.staff.owner

import pixel.net.server.command.Command
import pixel.net.server.player.{Players, Player, Rank}

object KickAllCommand extends Command("kickall", rank = Rank.Owner){

  def execute(player: Player, args: Array[String]){
    for(p <- Players.online if p != player)
      p.ctx.close()
  }

}
