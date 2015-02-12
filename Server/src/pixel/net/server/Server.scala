package pixel.net.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

object Server {

  private val Port: Int = 7495

  private def start(){
    val boss: EventLoopGroup = new NioEventLoopGroup()
    val worker: EventLoopGroup = new NioEventLoopGroup()
    val bs: ServerBootstrap = new ServerBootstrap()
    bs.group(boss, worker)
    bs.channel(classOf[NioServerSocketChannel])
    bs.childHandler(ServerInitializer)
    try{
      bs.bind(Port).sync.channel.closeFuture.sync()
    }finally{
      boss.shutdownGracefully()
      worker.shutdownGracefully()
    }
  }

  def main(args: Array[String]) = start()

}
