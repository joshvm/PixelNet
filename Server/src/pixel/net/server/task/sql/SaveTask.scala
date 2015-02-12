package pixel.net.server.task.sql

import pixel.net.server.task.OneTimeTask
import pixel.net.server.db.Saveable

class SaveTask(s: Saveable) extends OneTimeTask{
  
  def run() = s.save()

}
