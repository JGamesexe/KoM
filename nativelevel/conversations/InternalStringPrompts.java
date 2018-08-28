package nativelevel.conversations;

import nativelevel.KoM;
import nativelevel.guis.guilda.GuildaAllysGUI;
import nativelevel.guis.guilda.GuildaCreateGUI;
import nativelevel.guis.guilda.GuildaMembrosGUI;
import nativelevel.guis.guilda.GuildaRivalsGUI;
import nativelevel.guis.terreno.TerrenoPrivadoGUI;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.md_5.bungee.api.ChatColor;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class InternalStringPrompts {

    public static Conversation createConversation(Player player, String id) {
        return KoM.conversationFactory.
                withFirstPrompt(InternalStringPrompts.getStringPrompt(id)).
                withLocalEcho(true).
                withModality(false).
                withConversationCanceller(new InactivityConversationCanceller(KoM._instance, 25)).
                buildConversation(player);
    }

    private static StringPrompt getStringPrompt(String id) {
        switch (id) {
            case "addTerrenoAmigo":
                return new addTerrenoAmigo();
            case "setTerrenoOwner":
                return new setTerrenoOwner();
            case "terrenoDesconquistar":
                return new terrenoDesconquistar();
            case "addMembroGuilda":
                return new addMembroGuilda();
            case "createGuilda":
                return new createGuilda();
            case "sairDaGuilda":
                return new sairDaGuilda();
            case "desbandarGuilda":
                return new desbandarGuilda();
            case "addAlly":
                return new addAlly();
            case "addRival":
                return new addRival();
        }
        return new invalidID();
    }

    private static class addTerrenoAmigo extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§e§l- §eDigite o nick do jogador";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player p = (Player) conversationContext.getForWhom();
            if (s.length() > 3) {
                Player palvo = Bukkit.getPlayer(s);
                if (palvo != null) {
                    if (!p.getName().equalsIgnoreCase(palvo.getName())) {
                        Location loc = (Location) conversationContext.getSessionData("location");
                        if (!ClanLand.isMemberAt(loc, palvo.getUniqueId())) {
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
        }

    }

    private static class setTerrenoOwner extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§e§l- §eDigite o nick do jogador";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player p = (Player) conversationContext.getForWhom();
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
        }

    }

    private static class terrenoDesconquistar extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§4§l- §cPara confirmar que deseja desconquistar digite '§nD3SC0NQU1ST4R§c' caso queria cancelar isso digite qualquer §noutra§c coisa";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player p = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("D3SC0NQU1ST4R")) {
                Location loc = (Location) conversationContext.getSessionData("location");
                ClanLand.removeClanAt(loc);
                if (loc.getChunk().equals(p.getLocation().getChunk())) ClanLand.update(p, p.getLocation());
                else p.sendRawMessage("§eVocê desconquistou o terreno com sucesso");
            }

            return null;
        }
    }

    private static class addMembroGuilda extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§e§l- §eDigite o nick do jogador";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player p = (Player) conversationContext.getForWhom();
            Clan clan = (Clan) conversationContext.getSessionData("clan");
            ClanPlayer cps = ClanLand.manager.getClanPlayer(p);
            if (cps != null && cps.getTag().equalsIgnoreCase(clan.getTag())) {
                if (s.length() > 3) {
                    Player palvo = Bukkit.getPlayer(s);
                    if (palvo != null) {
                        if (!p.hasMetadata("tutorial")) {
                            if (!p.getName().equalsIgnoreCase(palvo.getName())) {
                                ClanPlayer cp = ClanLand.manager.getClanPlayer(palvo);
                                if (cp == null) {
                                    ClanLand.manager.invitePlayerToClan(cps, palvo, clan);
                                    p.sendMessage("§a" + palvo.getName() + " foi convidado para guilda.");
                                } else {
                                    if (cp.getTag().equalsIgnoreCase(cps.getTag())) p.sendMessage("§cO jogador " + palvo.getName() + " já é membro desta guilda.");
                                    else p.sendMessage("§cO jogador " + palvo.getName() + "já é membro de outra guilda...");
                                }
                            } else p.sendMessage("§cVocê já é membro desta guilda né não?");
                        } else p.sendMessage("§cEsse jogador ainda está no Tutorial...");
                    } else p.sendMessage("§cNick (" + s + ") inválido ou jogador offline");
                } else p.sendMessage("§cNick(" + s + ") inválido.");
            } else {
                p.sendMessage("§cVish man acho que você foi tirado da guilda o,O");
                return null;
            }
            GUI gui = new GuildaMembrosGUI(p, clan);

            GUI.open(p, gui);

            return null;
        }

    }

    private static class createGuilda extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            String tipo = (String) conversationContext.getSessionData("tipo");

            if (tipo.equalsIgnoreCase("tag")) return "§e§l- §eInsira a §9T§cA§aG§e da sua guilda!";
            else if (tipo.equalsIgnoreCase("name")) return "§e§l- §eInsira o nome da sua guilda!";

            return null;
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player player = (Player) conversationContext.getForWhom();

            String tipo = (String) conversationContext.getSessionData("tipo");

            String tag = (String) conversationContext.getSessionData("tag");
            String name = (String) conversationContext.getSessionData("name");

            String regex = "^[&0-9a-zA-Zà-úÀ-Ú\\s]*$";
            if (s.matches(regex)) {
                if (tipo.equalsIgnoreCase("tag")) {
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("&[4k-or]", "");
                    String clearTag = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', s)).replaceAll("[&§][0-9a-fk-or]", "");
                    if (!clearTag.equalsIgnoreCase("KoM") &&
                            !clearTag.equalsIgnoreCase("SAFE") &&
                            !clearTag.equalsIgnoreCase("WARZ") &&
                            !clearTag.equalsIgnoreCase("RUIN") &&
                            !clearTag.equalsIgnoreCase("CLAN") &&
                            !clearTag.equalsIgnoreCase("STAFF")) {
                        if (clearTag.length() <= 5) {
                            Clan clan = ClanLand.getClan(clearTag);
                            if (clan != null) player.sendMessage("§cJá existe uma Guilda com está TAG.");
                            else tag = s;
                        } else {
                            player.sendMessage("§cTAGs podem ter no máximo 5 caracteres.");
                        }
                    } else {
                        player.sendMessage("§cEssa TAG não pode ser utilizada.");
                    }
                } else if (tipo.equalsIgnoreCase("name")) {
                    if (s.length() > 25) player.sendMessage("§cUé sua guilda não precisa de um nome tão grande né...");
                    else name = s;
                }
            } else {
                player.sendMessage("§cNão inventa moda, usa só numeros e letras =D");
            }

            GUI.open(player, new GuildaCreateGUI(name, tag));

            return null;
        }

    }

    private static class sairDaGuilda extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§4§l- §cAo deixar uma guilda você não pode voltar para ela depois de 2 horas \n" +
                    "§4§l- §cPara confirmar que deseja sair digite '§nS41R§c' caso queria cancelar isso digite qualquer §noutra§c coisa";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player p = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("S41R")) {
                ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
                Clan clan = cp.getClan();
                clan.addBb(cp.getName(), cp.getName() + " deixou a guilda...");
                clan.removePlayerFromClan(cp.getUniqueId());
            } else {
                p.sendMessage("§aVocê cancelou o processo de deixar a guilda!");
            }

            return null;
        }

    }

    private static class desbandarGuilda extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§4§l- §cAo desmanchar sua guilda, todos terrenos já domnimados serão desprotegidos, e qualquer recurso envolvendo diretamente está guilda serão perdidos, todos os membros atuais também ficaram sem guilda. \n" +
                    "§4§l- §cSe deseja continuar com está ação digite '§nD3SM4NT3L4R§c' caso queria cancelar isso digite qualquer §noutra§c coisa";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player p = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("D3SM4NT3L4R")) {
                ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
                Clan clan = cp.getClan();
                KoM.announce("§bA Guilda " + clan.getName() + " caiu em ruínas...");
                clan.disband();
            } else {
                p.sendMessage("§aVocê cancelou o processo de desmanchar a guilda!");
            }

            return null;
        }

    }

    private static class addAlly extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§6§l- §eDigite a TAG da guilda que você deseja solicitar um pacto de aliado";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player player = (Player) conversationContext.getForWhom();
            ClanPlayer clanPlayer = ClanLand.manager.getClanPlayer(player);
            if (clanPlayer != null) {
                if (clanPlayer.isLeader()) {
                    Clan clanTarget = ClanLand.getClan(s);
                    if (clanTarget != null) {
                        if (!clanTarget.isRival(clanPlayer.getTag())) {
                            if (!clanTarget.isAlly(clanPlayer.getTag())) {
                                if (!GuildaAllysGUI.hasSend(clanPlayer.getTag(), clanTarget.getTag())) {
                                    GuildaAllysGUI.addSolicitacao(clanTarget.getTag(), clanPlayer.getTag());
                                    player.sendMessage("§aPedido de pacto enviado para " + s.toUpperCase());
                                } else {
                                    player.sendMessage("§cJá foi solicitiado um pacto com está guilda");
                                }
                            } else {
                                player.sendMessage("§eVocê já possui um pacto com está guilda");
                            }
                        } else {
                            player.sendMessage("§cEstá guilda possui uma rivalidade com a sua, impossibilitando estabelecer um pacto");
                        }
                    } else {
                        player.sendMessage("§cNenhuma guilda com a tag " + s.toUpperCase());
                    }
                } else {
                    player.sendMessage("§cVocê não é lider para executar esse pedido");
                }
                GUI.open(player, new GuildaAllysGUI(clanPlayer.getClan(), clanPlayer));
            } else {
                player.sendMessage("§cVocê não tem guilda para fazer isso");
            }
            return null;
        }

    }

    private static class addRival extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§6§l- §eDigite a TAG da guilda que você iniciará uma guerra";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            Player player = (Player) conversationContext.getForWhom();
            ClanPlayer clanPlayer = ClanLand.manager.getClanPlayer(player);
            if (clanPlayer != null) {
                if (clanPlayer.isLeader()) {
                    Clan clanTarget = ClanLand.getClan(s);
                    if (clanTarget != null) {
                        if (!clanTarget.isAlly(clanPlayer.getTag())) {
                            if (!clanTarget.isRival(clanPlayer.getTag())) {
                                player.sendMessage("§eFoi iniciada uma rivalidade com a guilda " + s.toUpperCase());
                                clanPlayer.getClan().addRival(clanTarget);
                            } else {
                                player.sendMessage("§cJá existe uma rivalidade com está guilda");
                            }
                        } else {
                            player.sendMessage("§cVocê possui um pacto com está guilda, quebre-o para iniciar uma rivalidade");
                        }
                    } else {
                        player.sendMessage("§cNenhuma guilda com a tag " + s.toUpperCase());
                    }
                } else {
                    player.sendMessage("§cVocê não é lider para executar esse pedido");
                }
                GUI.open(player, new GuildaRivalsGUI(clanPlayer.getClan(), clanPlayer));
            } else {
                player.sendMessage("§cVocê não tem guilda para fazer isso");
            }
            return null;
        }

    }

    private static class invalidID extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "§5§lNão achei ID de conv (Chama Staff...)";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            conversationContext.getForWhom().sendRawMessage("§5§lNão achei ID de conv (Chama Staff...)");
            return null;
        }
    }

}
