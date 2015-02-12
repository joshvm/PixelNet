package pixel.net.server.command.staff.admin

import pixel.net.server.command.staff.punish.RevokePunishmentCommand
import pixel.net.server.punishment.PunishmentFlag
import pixel.net.server.player.Rank

object UnIpBanCommand extends RevokePunishmentCommand("unipban", Rank.Administrator, PunishmentFlag.Ban, PunishmentFlag.IpBan)
