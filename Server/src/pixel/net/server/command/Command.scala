package pixel.net.server.command

import pixel.net.server.player.{Rank, Player}

abstract class Command(val prefix: String, val argSytnax: String = "", val rank: Rank) {
  
  def execute(player: Player, args: Array[String])
  
  protected def join(array: Array[String], start: Int): String = {
    val bldr: StringBuilder = new StringBuilder
    for(i <- start until array.length)
      bldr.append(array(i)).append(" ")
    bldr.toString().trim()
  }

}

object Command{
  val Prefix: String = "::"
}
