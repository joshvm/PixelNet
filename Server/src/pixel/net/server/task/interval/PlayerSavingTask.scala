package pixel.net.server.task.interval

import pixel.net.server.task.{TaskSystem, Task}
import pixel.net.server.task.sql.SaveTask
import java.util.concurrent.TimeUnit
import pixel.net.server.player.Players

object PlayerSavingTask extends Task(TimeUnit.MINUTES.toMillis(2)){

  def run(){
    for(p <- Players.online if System.currentTimeMillis - p.loginTime >= TimeUnit.MINUTES.toMillis(10)){
      TaskSystem.submit(new SaveTask(p))
      p.sendMessage("Saving your account")
    }
  }

}
