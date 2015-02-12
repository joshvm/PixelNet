package pixel.net.server.command.staff.helper

import pixel.net.server.punishment.PunishmentFlag
import pixel.net.server.command.staff.punish.PunishmentCommand
import pixel.net.server.player.Rank

object MuteCommand extends PunishmentCommand("mute", Rank.Helper, PunishmentFlag.Mute)
