package pixel.net.server

import io.netty.channel.{ChannelPipeline, ChannelInitializer}
import io.netty.channel.socket.SocketChannel

object ServerInitializer extends ChannelInitializer[SocketChannel]{

  def initChannel(channel: SocketChannel){
    val pipeline: ChannelPipeline = channel.pipeline()
    pipeline.addLast("handler", new ServerHandler())
  }

}
