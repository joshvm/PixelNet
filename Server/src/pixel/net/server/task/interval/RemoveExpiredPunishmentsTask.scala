package pixel.net.server.task.interval

import pixel.net.server.task.Task
import java.util.concurrent.TimeUnit
import pixel.net.server.punishment.Punishments

object RemoveExpiredPunishmentsTask extends Task(TimeUnit.MINUTES.toMillis(5)){

  def run() = Punishments.removeExpired()

}
