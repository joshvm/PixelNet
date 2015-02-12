package pixel.net.server.command.player

import pixel.net.server.command.Command
import pixel.net.server.player.{Players, Player, Rank}

object PlayersCommand extends Command(prefix = "players", rank = Rank.Player){

  def execute(player: Player, args: Array[String]){
    val online: Int = Players.online.size
    player.sendMessage(s"There are currently $online players online")
  }

}
