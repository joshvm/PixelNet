package pixel.net.server.command.staff.mod

import pixel.net.server.command.staff.punish.RevokePunishmentCommand
import pixel.net.server.punishment.PunishmentFlag
import pixel.net.server.player.Rank

object UnBanCommand extends RevokePunishmentCommand("unban", Rank.Moderator, PunishmentFlag.Ban)
