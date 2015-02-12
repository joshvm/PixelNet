package pixel.net.server.task

abstract class OneTimeTask extends Task(1){

  override def execute(): Unit = {
    run()
    stop()
  }

}
