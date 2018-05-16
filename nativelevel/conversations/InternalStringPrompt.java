package nativelevel.conversations;

import nativelevel.guis.guilda.GuildaMembrosGUI;
import nativelevel.guis.terreno.TerrenoPrivadoGUI;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class InternalStringPrompt extends StringPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        switch ((String) conversationContext.getSessionData("id")) {
            case "addTerrenoAmigo":
                return "§e§l- §eDigite o nick do jogador";
            case "setTerrenoOwner":
                return "§e§l- §eDigite o nick do jogador";
            case "terrenoDesconquistar":
                return "§c§l- §cPara confirmar que deseja desconquistar digite '§nD3SC0NQU1ST4R§c' caso queria cancelar isso digite qualquer §noutra §ccoisa";
            case "addMembroGuilda":
                return "§e§l- §eDigite o nick do jogador";
        }

        return "§5§lNão achei ID de conv";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        switch ((String) conversationContext.getSessionData("id")) {
            case "addTerrenoAmigo":
                Player p = (Player) conversationContext.getForWhom();
                if (s.length() > 3) {
                    Player palvo = Bukkit.getPlayer(s);
                    if (palvo != null) {
                        if (!p.getName().equalsIgnoreCase(palvo.getName())) {
                            Location loc = (Location) conversationContext.getSessionData("location");
                            if (ClanLand.isMemberAt(loc, palvo.getUniqueId())) {
                                ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
                                ClanPlayer cpalvo = ClanLand.manager.getClanPlayer(palvo);
                                if (cp != null && cpalvo != null && cp.getTag().equalsIgnoreCase(cpalvo.getTag())) {
                                    ClanLand.addMemberAt(loc, palvo);
                                    p.sendRawMessage("§a" + palvo.getName() + " foi adicionado como amigo.");
                                } else p.sendRawMessage("§cO jogador " + palvo.getName() + " não é da sua guilda.");
                            } else p.sendRawMessage("§cO jogador " + palvo.getName() + " já é um amigo deste terreno");
                        } else p.sendRawMessage("§cVocê já é dono do terreno né não?");
                    } else p.sendRawMessage("§cNick (" + s + ") inválido ou jogador offline");
                } else p.sendRawMessage("§cNick(" + s + ") inválido.");


                GUI gui = new TerrenoPrivadoGUI(p);

                GUI.open(p, gui);

                return null;
            case "setTerrenoOwner":
                p = (Player) conversationContext.getForWhom();
                if (s.length() > 3) {
                    Player palvo = Bukkit.getPlayer(s);
                    if (palvo != null) {
                        Location loc = (Location) conversationContext.getSessionData("location");
                        if (!ClanLand.getOwnerAt(loc)[0].equalsIgnoreCase(palvo.getUniqueId().toString())) {
                            ClanPlayer cpalvo = ClanLand.manager.getClanPlayer(palvo);
                            if (cpalvo != null) {
                                ClanLand.setOwnerAt(loc, palvo);
                                if (loc.getChunk().equals(p.getLocation().getChunk()))
                                    ClanLand.update(p, p.getLocation());
                                else p.sendRawMessage("§a" + palvo.getName() + " se tornou dono daquele terreno.");
                            } else p.sendRawMessage("§cO jogador " + palvo.getName() + " não é da sua guilda.");
                        } else p.sendRawMessage("§c" + palvo.getName() + " já é dono do terreno né não?");
                    } else p.sendRawMessage("§cNick (" + s + ") inválido ou jogador offline");
                } else p.sendRawMessage("§cNick(" + s + ") inválido.");

                return null;
            case "terrenoDesconquistar":
                p = (Player) conversationContext.getForWhom();
                if (s.equalsIgnoreCase("D3SC0NQU1ST4R")) {
                    Location loc = (Location) conversationContext.getSessionData("location");
                    ClanLand.removeClanAt(loc);
                    if (loc.getChunk().equals(p.getLocation().getChunk())) ClanLand.update(p, p.getLocation());
                    else p.sendRawMessage("§eVocê desconquistou o terreno com sucesso");
                }

                return null;
            case "addMembroGuilda":
                p = (Player) conversationContext.getForWhom();
                Clan clan = (Clan) conversationContext.getSessionData("clan");
                ClanPlayer cps = ClanLand.manager.getClanPlayer(p);
                if (cps != null && cps.getTag().equalsIgnoreCase(clan.getTag())) {
                    if (s.length() > 3) {
                        Player palvo = Bukkit.getPlayer(s);
                        if (palvo != null) {
                            if (!p.getName().equalsIgnoreCase(palvo.getName())) {
                                ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
                                if (cp == null) {
                                    ClanLand.manager.invitePlayerToClan(cps, palvo, clan);
                                    p.sendRawMessage("§a" + palvo.getName() + " foi convidado para guilda.");
                                } else {
                                    if (cp.getTag().equalsIgnoreCase(cps.getTag()))
                                        p.sendRawMessage("§cO jogador " + palvo.getName() + " já é membro desta guilda.");
                                    else
                                        p.sendRawMessage("§cO jogador " + palvo.getName() + "já é membro de outra guilda...");
                                }
                            } else p.sendRawMessage("§cVocê já é membro desta guilda né não?");
                        } else p.sendRawMessage("§cNick (" + s + ") inválido ou jogador offline");
                    } else p.sendRawMessage("§cNick(" + s + ") inválido.");
                } else {
                    p.sendMessage("§cVish man acho que você foi tirado da guilda o,O");
                    return null;
                }
                gui = new GuildaMembrosGUI(p, clan);

                GUI.open(p, gui);

                return null;

        }

        conversationContext.getForWhom().sendRawMessage("§5§lNão achei ID de conv");
        return null;
    }
}
