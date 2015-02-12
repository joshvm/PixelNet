package pixel.net.server.db

import java.sql.{DriverManager, Connection}

object Database {

  Class.forName("com.mysql.jdbc.Driver")

  val connection: Connection = DriverManager.getConnection("jdbc:mysql://localhost/pixelnet", "root", "admin")

}
