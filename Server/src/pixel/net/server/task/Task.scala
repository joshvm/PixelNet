package pixel.net.server.task

abstract class Task(val interval: Long, val initDelay: Long = 0) {

  private var _stop: Boolean = false

  def stop() = _stop = true

  def isStopped: Boolean = _stop

  def run(): Unit

  def execute(): Unit = run()

}
