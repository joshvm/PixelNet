package pixel.net.server.command.staff.helper

import pixel.net.server.command.staff.punish.RevokePunishmentCommand
import pixel.net.server.punishment.PunishmentFlag
import pixel.net.server.player.Rank

object UnMuteCommand extends RevokePunishmentCommand("unmute", Rank.Helper, PunishmentFlag.Mute)