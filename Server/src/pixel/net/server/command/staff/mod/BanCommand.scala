package pixel.net.server.command.staff.mod

import pixel.net.server.command.staff.punish.PunishmentCommand
import pixel.net.server.punishment.PunishmentFlag
import pixel.net.server.player.Rank

object BanCommand extends PunishmentCommand("ban", Rank.Moderator, PunishmentFlag.Ban)
