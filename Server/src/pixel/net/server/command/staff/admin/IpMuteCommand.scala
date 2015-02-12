package pixel.net.server.command.staff.admin

import pixel.net.server.command.staff.punish.PunishmentCommand
import pixel.net.server.punishment.PunishmentFlag
import pixel.net.server.player.Rank

object IpMuteCommand extends PunishmentCommand("ipmute", Rank.Administrator, PunishmentFlag.IpMute, PunishmentFlag.Mute)
