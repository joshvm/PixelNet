package pixel.net.server.task

import java.util.{TimerTask, Timer}
import pixel.net.server.task.interval.{RemoveExpiredPunishmentsTask, PlayerSavingTask}

object TaskSystem {

  private lazy val Timer: Timer = new Timer()

  submit(PlayerSavingTask)
  submit(RemoveExpiredPunishmentsTask)

  def submit(t: Task){
    val tt: TimerTask = new TimerTask{
      def run(){
        if(t.isStopped)
          cancel()
        else
          t.execute()
      }
    }
    Timer.schedule(tt, t.initDelay, t.interval)
  }

}
