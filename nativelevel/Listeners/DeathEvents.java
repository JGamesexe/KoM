package nativelevel.Listeners;

import nativelevel.CFG;
import nativelevel.Classes.Mage.spelllist.Paralyze;
import nativelevel.Classes.Paladin;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.ItemRaroAleatorio;
import nativelevel.Custom.Items.RecipeBook;
import nativelevel.Custom.Items.SeguroDeItems;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.sisteminhas.IronGolem;
import nativelevel.sisteminhas.Mobs;
import nativelevel.utils.LocUtils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MonsterEggs;

import java.util.*;

/**
 * @author Ziden
 */
public class DeathEvents implements Listener {

    public static HashMap<UUID, UUID> ultimoDano = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void morreAlguem(final EntityDeathEvent ev) {
        if (ev.getEntity() instanceof Player) {
            ev.setDroppedExp(0);
        } else if (ev.getEntity().getType() == EntityType.CHICKEN || ev.getEntity().getType() == EntityType.PIG || ev.getEntity().getType() == EntityType.COW) {
            ev.setDroppedExp(0);
        }

        if (ev.getEntity().getType().equals(EntityType.IRON_GOLEM)) IronGolem.morre(ev);
        if (!(ev.getEntity() instanceof Player)) Mobs.morreMob(ev);
        if (ev.getEntity() instanceof EnderDragon) {
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.IRON_INGOT, 60));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.DIAMOND, 2));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.DIAMOND, 2));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.DIAMOND, 2));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.EXP_BOTTLE, 10));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.EXP_BOTTLE, 10));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.EXP_BOTTLE, 10));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.GLOWSTONE, 10));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), KoM.geraEs(5));
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), CustomItem.getItem(ItemRaroAleatorio.class).generateItem());
            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), CustomItem.getItem(ItemRaroAleatorio.class).generateItem());
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.DARK_PURPLE + L.m("[EPIC]" + ChatColor.GREEN + "O Dragao do Fim foi morto %", (ev.getEntity().getKiller() == null ? "" : " por " + ev.getEntity().getKiller().getName())));
            }
        }

        EntityDamageEvent.DamageCause causaDaMorte = null;
        if (ev.getEntity().getLastDamageCause() != null) {
            causaDaMorte = ev.getEntity().getLastDamageCause().getCause();
        }

        if (ev instanceof PlayerDeathEvent) playerMorre((PlayerDeathEvent) ev);

//        if (ev instanceof PlayerDeathEvent) {
//            final Player p = (Player) ev.getEntity();
//
//            KoM.debug(p.getName() + " morrendo");
//
//            // arrumando bug de jogar enderpearl pra cima antes de morrer
//            if (GeneralListener.pearls.containsKey(p.getUniqueId())) {
//                GeneralListener.pearls.get(p.getUniqueId()).remove();
//                GeneralListener.pearls.remove(p.getUniqueId());
//            }
//
//            Player pmorto = p;
//            int fama = KoM.database.getFama(pmorto.getUniqueId());
//            int famaPerdida = fama / 10;
//            if (!pmorto.hasMetadata("msgfama")) {
//                MetaShit.setMetaString("msgfama", pmorto, "");
//                pmorto.sendMessage(ChatColor.GREEN + "Fama: " + fama + " " + ChatColor.RED + "-" + famaPerdida);
//            } else {
//                pmorto.sendMessage(ChatColor.GREEN + "Fama: " + ChatColor.YELLOW + "-" + famaPerdida);
//            }
//            KoM.database.setFama(pmorto.getUniqueId(), fama - famaPerdida);
//
//            // bug de voltar qnd morre no cavalo
//            if (p.getVehicle() != null) {
//                p.getVehicle().eject();
//            }
//
//            boolean naoPerdeItem = false;
//            boolean naoPerdeLevel = false;
//
//            Thief.revela(p);
//
//            if (Deuses.odio) {
//                p.sendMessage(ChatColor.RED + "Voce morreu perante o ódio de Ubaj");
//                Deuses.matou++;
//            }
//
//            String tipo = ClanLand.getTypeAt(ev.getEntity().getLocation());
//            boolean seguro = false;
//
//            if (ClanLand.permission.has(p, "kom.vip")) {
//                if (p.getWorld().getName().equalsIgnoreCase("NewDungeon")) {
//                    naoPerdeItem = true;
//                    KoM.debug("vip em dungeon nao eprde item");
//                }
//                naoPerdeLevel = true;
//            }
//
//
//            if (ev.getEntity().getLastDamageCause() != null && ev.getEntity().getLastDamageCause().getCause() == DamageCause.SUFFOCATION) {
//                naoPerdeLevel = true;
//                naoPerdeItem = true;
//            }
//
//            if (tipo.equalsIgnoreCase("WARZ")) {
//                naoPerdeLevel = true;
//            }
//            if (ev.getEntity().getWorld().getName().equalsIgnoreCase("woe") || ev.getEntity().getWorld().getName().equalsIgnoreCase("arena")) {
//                //naoPerdeItem = true;
//                //   if(ev.getEntity().getWorld().getName().equalsIgnoreCase("woe")) {
//                this.morreJogador((PlayerDeathEvent) ev, 0);
//                return;
//                //   }
//            }
//            List<PlayerSpec> spec = PlayerSpec.getSpecs(p);
//            if (p.getLevel() < 10 && KoM.database.getResets(p) == 0 && (spec == null || spec.size() == 0)) {
//                seguro = true;
//                naoPerdeLevel = true;
//            }
//            if (p.getWorld().getName().equalsIgnoreCase("NewDungeon")) {
//                ApplicableRegionSet set = KoM.worldGuard.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
//                if (set != null && set.size() > 0) {
//                    if (set.getFlag(DefaultFlag.ITEM_DROP) == StateFlag.State.DENY) {
//                        naoPerdeItem = true;
//                    }
//                }
//            }
//            if (SeguroDeItems.segurou(p)) {
//                seguro = true;
//                if (KoM.debugMode) {
//                    KoM.log.info("segurando item");
//                }
//            }
//
//            int perdaDeLvl = 0;
//            try {
//                Object tempoMorte = MetaShit.getMetaObject("tempomorte", p);
//                if (tempoMorte != null) {
//                    long tempo = (long) tempoMorte;
//                    long tempoAgora = System.currentTimeMillis() / 1000;
//                    if (tempo + 2 > tempoAgora) {
//                        p.removeMetadata("tempomorte", KoM._instance);
//                        this.morreJogador((PlayerDeathEvent) ev, 0);
//                        return; // só morre 1x a cada 2 sec pra evitar bug
//
//                    }
//                }
//                if ((ev.getEntity().getWorld().getName().equalsIgnoreCase("NewDungeon") && naoPerdeItem) || seguro || ev.getEntity().getWorld().getName().equalsIgnoreCase("NewDungeon") || ClanLand.isWarZone(ev.getEntity().getLocation())) {
//                    ArrayList<ItemStack> listaItems = new ArrayList<ItemStack>();
//                    for (ItemStack ss : p.getInventory().getContents()) {
//                        if (ss != null && (Jobs.rnd.nextInt(8) != 1
//                                || naoPerdeItem
//                                || seguro
//                                || BookUtil.ehLivroDeQuest(ss)
//                                || SeguroDeItems.isItemSeguro(ss))) {
//                            listaItems.add(ss);
//                        }
//                    }
//                    GeneralListener.loots.put(p.getUniqueId(), listaItems);
//                    ev.getDrops().removeAll(listaItems);
//                    if (ev.getDrops().size() > 0) {
//                        p.sendMessage(ChatColor.RED + L.m("Voce dropou % item(s) !", ev.getDrops().size()));
//                    }
//                    perdaDeLvl = 1;
//                } else {
//                    ArrayList<ItemStack> listaItems = new ArrayList<ItemStack>();
//                    for (ItemStack ss : p.getInventory().getContents()) {
//                        if (ss != null && (BookUtil.ehLivroDeQuest(ss) || SeguroDeItems.isItemSeguro(ss))) {
//                            KoM.debug("Salvando item " + ss.getType().name());
//                            listaItems.add(ss);
//                        }
//                    }
//                    GeneralListener.loots.put(p.getUniqueId(), listaItems);
//                    ev.getDrops().removeAll(listaItems);
//                    perdaDeLvl = 1;
//                    KoM.debug("Ainda ficaram " + ev.getDrops() + " items");
//                }
//                if (ev.getEntity().getLastDamageCause() == null || ev.getEntity().getLastDamageCause().getCause() == null) {
//                    perdaDeLvl = 0;
//                } else if (ev.getEntity().getKiller() != null) {
//                    perdaDeLvl = 0;
//                } else if (causaDaMorte == EntityDamageEvent.DamageCause.DROWNING || causaDaMorte == EntityDamageEvent.DamageCause.LAVA || causaDaMorte == EntityDamageEvent.DamageCause.STARVATION) {
//                    perdaDeLvl = 1;
//                }
//                if (naoPerdeLevel) {
//                    perdaDeLvl = 0;
//                }
//                // se um jogador matou otro jogador
//                if (ev.getEntity() instanceof Player && (ev.getEntity().getKiller() != null || GeneralListener.ultimoDano.containsKey(ev.getEntity().getUniqueId()))) {
//                    String local = ClanLand.getTypeAt(ev.getEntity().getLocation());
//
//                    if (!local.equalsIgnoreCase("WARZ") && !ev.getEntity().getLocation().getWorld().getName().equalsIgnoreCase("WoE")) {
//
//                        Player pMorto = (Player) ev.getEntity();
//                        Player pMatou = ev.getEntity().getKiller();
//                        if (pMatou == null) {
//                            pMatou = Bukkit.getPlayer(GeneralListener.ultimoDano.get(ev.getEntity().getUniqueId()));
//                            GeneralListener.ultimoDano.remove(ev.getEntity().getUniqueId());
//                        }
//                        if (pMorto != null && pMatou != null) {
//                            Karma.manoloMata(pMatou, pMorto);
//                        } else {
//                            //Se não existir nenhum dos dois Bye
//                            return;
//                        }
//
//                        ClanPlayer morto = ClanLand.manager.getClanPlayer((Player) ev.getEntity());
//                        ClanPlayer matou = ClanLand.manager.getClanPlayer(pMatou);
//
//                        // se o kra tinha um clan
//                        if (morto != null && morto.getClan() != null) {
//                            // quem matou tem clan ?
//                            if (matou != null && matou.getClan() != null) {
//                                // se sao clans inimigos
//                                if (matou.isRival((Player) ev.getEntity())) {
//                                    if (matou.isTrusted() && morto.isTrusted()) {
//                                        // o ganhador ganha ponto de pilhagem
//                                        int qtdTerrenos = ClanLand.getQtdTerrenos(morto.getClan().getTag());
//                                        int pontosGanhos = 1;
//                                        if (matou.isLeader()) {
//                                            pontosGanhos += 3;
//                                        }
//                                        if (morto.isLeader()) {
//                                            pontosGanhos += 3;
//                                        }
//
//                                        pontosGanhos += (qtdTerrenos / 3);
//
//                                        int ptosPilhagemGanhador = ClanLand.getPtosPilagem(matou.getTag(), morto.getTag());
//                                        int ptosPilhagemDefensor = ClanLand.getPtosPilagem(morto.getTag(), matou.getTag());
//                                        if (ptosPilhagemDefensor - pontosGanhos < 0) {
//                                            ptosPilhagemDefensor = 0;
//                                        }
//
//                                        ClanLand.setPtosPilhagem(morto.getTag(), matou.getTag(), (ptosPilhagemDefensor - pontosGanhos) < 0 ? 0 : (ptosPilhagemDefensor - pontosGanhos));
//                                        ClanLand.setPtosPilhagem(matou.getTag(), morto.getTag(), (ptosPilhagemGanhador + pontosGanhos < 0 ? 0 : (ptosPilhagemGanhador + pontosGanhos)));
//                                        ((Player) ev.getEntity()).sendMessage(ChatColor.RED + L.m("Sua guilda perdeu % PPs para a guilda ", pontosGanhos) + matou.getName());
//                                        ev.getEntity().getKiller().sendMessage(ChatColor.GREEN + L.m("Sua guilda ganhou % PPs da guilda ", pontosGanhos) + morto.getName());
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            } catch (Exception e) {
//                this.morreJogador((PlayerDeathEvent) ev, 0);
//                KoM.log.info("ERRO NO PLAYERDEATH EVENT " + e.getMessage());
//                e.printStackTrace();
//            }
//            this.morreJogador((PlayerDeathEvent) ev, perdaDeLvl);
//        }

        if (ev.getEntity() instanceof Creature) {
            if (!(ev.getEntity() instanceof Player)) {
                if (ev.getEntity().getKiller() != null && ev.getEntity().getKiller().getType() == EntityType.PLAYER) {
                    Player quemMatou = ev.getEntity().getKiller();
                    Paladin.pegaDropsExtrasDeMobs(ev.getEntity(), quemMatou);
                    if (Jobs.getJobLevel(L.get("Classes.Alchemist"), quemMatou) == 1) {
                        int sorte = Jobs.rnd.nextInt(50);
                        if (sorte >= 5 && sorte < 8) {
                            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.SPIDER_EYE, 1));
                        } else if (sorte == 2 || sorte == 3) {
                            ev.getEntity().getWorld().dropItemNaturally(ev.getEntity().getLocation(), new ItemStack(Material.NETHER_WARTS, 3));
                        }
                    }
                }
            }
        }

    }

    public static void Respawna(final Player pl) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, new Runnable() {

            @Override
            public void run() {
                PacketPlayInClientCommand in = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN); // Gets the packet class
                EntityPlayer cPlayer = ((CraftPlayer) pl).getHandle(); // Gets the EntityPlayer class
                cPlayer.playerConnection.a(in);
            }
        }, 5L);
    }

    private static void playerMorre(PlayerDeathEvent ev) {
        Player player = ev.getEntity();

        KoM.debug("!=- playerMorre (" + player.getName() + ") em " + LocUtils.loc2str(player.getLocation()));

        Paralyze.removeParalize(player);

        ev.setKeepInventory(true);
        ev.setKeepLevel(true);
        ev.setDeathMessage(null);

        if (player.getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda)) {
            String tipoAt = ClanLand.getTypeAt(player.getLocation());

            player.setBedSpawnLocation(pertenceAVila(player));

            if (tipoAt.equalsIgnoreCase("WILD")) dropaItens(player, 0.65);
            else dropaItens(player, 0.40);

            UUID killer = null;

            if (player.getKiller() != null) killer = player.getKiller().getUniqueId();
            if (killer == null && ultimoDano.containsKey(player.getUniqueId())) {
                killer = ultimoDano.get(player.getUniqueId());
                ultimoDano.remove(player.getUniqueId());
            }

            if (killer != null) pontosDePilhagem(killer, player.getUniqueId());

        } else if (player.getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {

            int soulPoints = KoM.database.getAlmas(player.getUniqueId());

            if (soulPoints > 0) {
                soulPoints--;
                KoM.database.setAlmas(player.getUniqueId().toString(), soulPoints);

                player.sendMessage("§aVocê não perdeu nenhum item, mas perdeu 1 ponto de alma!");
                player.sendMessage("§aRecupere almas ficando ONLINE =D");

                if (soulPoints == 0)
                    player.sendMessage("§cVocê não tem mais pontos de almas, agora você pode dropar itens em dungeons");
                else if (soulPoints == 1)
                    player.sendMessage("§cVocê tem " + soulPoints + " ponto de alma.");
                else
                    player.sendMessage("§cVocê tem " + soulPoints + " pontos de almas.");
            } else {
                player.sendMessage("§cVocê dropou 15% de seus itens pois estava sem pontos de alma.");
                dropaItens(player, 0.15);
            }
        }

        Respawna(player);

    }

    private static void dropaItens(Player player, Double porc) {
        if (player.isOp() || player.hasPermission("kom.staff")) {
            player.sendMessage("§6STAFF não dropa item morrendo");
            return;
        }

        if (player.getLevel() <= 5) {
            player.sendMessage("§eVocê ainda está nível muito baixo, Jabu não permite que você drope itens.");
            return;
        }

        KoM.debug("É pra dropar " + (int) (porc * 100) + "%");

        List<ItemOnInventory> itens = getPlayersItens(player.getInventory());

        itens.removeIf(itemOnInventory ->
                itemOnInventory.itemStack.getType().equals(Material.COMPASS));
        itens.removeIf(itemOnInventory ->
                itemOnInventory.itemStack.getType().equals(Material.MONSTER_EGG) &&
                        (((SpawnEggMeta) itemOnInventory.itemStack.getItemMeta()).getSpawnedType().equals(EntityType.HORSE) ||
                                ((SpawnEggMeta) itemOnInventory.itemStack.getItemMeta()).getSpawnedType().equals(EntityType.DONKEY) ||
                                ((SpawnEggMeta) itemOnInventory.itemStack.getItemMeta()).getSpawnedType().equals(EntityType.MULE)));
        itens.removeIf(itemOnInventory ->
                itemOnInventory.itemStack.getItemMeta() != null &&
                        itemOnInventory.itemStack.getItemMeta().getDisplayName() != null &&
                        itemOnInventory.itemStack.getItemMeta().getDisplayName().contains("[Quest]"));
        itens.removeIf(itemOnInventory ->
                SeguroDeItems.isSeguro(itemOnInventory.itemStack));
        itens.removeIf(itemOnInventory ->
                RecipeBook.isRecipeBook(itemOnInventory.itemStack));

        if (player.getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda)) {
            porc -= SeguroDeItems.getPorcent(player);
            KoM.debug("Procurei por seguro e agora tá " + (int) (porc * 100) + "%");
        }

        if (itens.size() == 0) {
            player.sendMessage("§cVocê não tinha nada pra dropar o,O");
            return;
        }

        if (porc >= 1) {
            player.sendMessage("Você não dropou nada =D");
            return;
        }

        if (itens.size() == 1) {
            player.sendMessage("§cVocê dropou seu único item ;-;");
            player.getInventory().setItem(itens.get(0).slot, null);
            player.getWorld().dropItem(player.getLocation(), itens.get(0).itemStack);
            return;
        }


        int howManyDrops = (int) (itens.size() * porc);
        if (howManyDrops == 0) howManyDrops = 1;

        Collections.shuffle(itens);

        Iterator<ItemOnInventory> iterator = itens.iterator();
        ItemOnInventory itemx;

        for (int i = howManyDrops; i > 0; i--) {
            itemx = iterator.next();
            player.getInventory().setItem(itemx.slot, null);
            player.getWorld().dropItem(player.getLocation(), itemx.itemStack).setPickupDelay(50);
            iterator.remove();
            KoM.debug("Dropando item do " + player.getName() + " em " + LocUtils.loc2str(player.getLocation()));
        }

        player.updateInventory();

        if (howManyDrops > 1) player.sendMessage("§cVocê dropou " + howManyDrops + " itens ;-;");
        else player.sendMessage("§cVocê dropou " + howManyDrops + " item ;-;");
    }

    private static void pontosDePilhagem(UUID killer, UUID dead) {

        ClanPlayer cpKiller = ClanLand.manager.getClanPlayer(killer);
        ClanPlayer cpDead = ClanLand.manager.getClanPlayer(dead);

        if (cpKiller == null || cpDead == null) return;
        if (!cpKiller.getClan().isRival(cpDead.getTag())) return;
        if (!cpKiller.isTrusted() || !cpDead.isTrusted()) return;

        int pps = 3;

        pps += (ClanLand.getQtdTerrenos(cpDead.getTag()) / 3);

        if (cpDead.isLeader()) pps += cpKiller.isLeader() ? 4 : 2;

        int ptosPilhagemGanhador = ClanLand.getPtosPilagem(cpKiller.getTag(), cpDead.getTag());
        int ptosPilhagemDefensor = ClanLand.getPtosPilagem(cpDead.getTag(), cpKiller.getTag());

        ClanLand.setPtosPilhagem(cpKiller.getTag(), cpDead.getTag(), (ptosPilhagemGanhador + pps));
        ClanLand.setPtosPilhagem(cpDead.getTag(), cpKiller.getTag(), (ptosPilhagemDefensor - pps) < 0 ? 0 : (ptosPilhagemDefensor - pps));

        cpKiller.getClan().addBb(cpKiller.getName(),cpKiller.getName() + " ganhou " + pps + " PPs sobre a guilda " + cpDead.getTag().toUpperCase());
        cpDead.getClan().addBb(cpDead.getName(),cpDead.getName() + " perdeu " + pps + "PPs para guilda " + cpKiller.getTag().toUpperCase());

    }

    private static List<ItemOnInventory> getPlayersItens(PlayerInventory inv) {
        List<ItemOnInventory> items = new ArrayList<>();
        ItemStack item;

        for (byte i = 0; i < 46; i++) {
            item = inv.getItem(i);
            if (item != null && item.getType().equals(Material.AIR)) item = null;
            items.add(new ItemOnInventory(i, item));
        }

        items.removeIf(itemOnInventory -> itemOnInventory.itemStack == null);

        return items;
    }

    private final static Location[] locaisVilas = new Location[]{
            new Location(Bukkit.getWorld(CFG.mundoGuilda), -29.5, 66, -120.5, -45, 0),
            new Location(Bukkit.getWorld(CFG.mundoGuilda), 951.5, 77, 685.5, 45, 0)};

    public static Location pertenceAVila(Player player) {

//      int level = player.getLevel();

        return locaisVilas[0];

    }

    public static Location nearbyVila(Player player) {
        if (!player.getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda)) return player.getWorld().getSpawnLocation();

        Map<Integer, Location> hvilas = new HashMap<>();

        for (Location loc : locaisVilas)
            hvilas.put((int) player.getLocation().distance(loc), loc);

        Map<Integer, Location> vilas = new TreeMap<>(hvilas);

        return vilas.entrySet().iterator().next().getValue();
    }

    private static class ItemOnInventory {
        int slot;
        ItemStack itemStack;

        public ItemOnInventory(int slot, ItemStack itemStack) {
            this.slot = slot;
            this.itemStack = itemStack;
        }

    }

}
