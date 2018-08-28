/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann
 Patrocionio: InstaMC

 */
package nativelevel.Classes;

import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import nativelevel.Attributes.Mana;
import nativelevel.Classes.Blacksmithy.Blacksmith;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.FolhaDeMana;
import nativelevel.Custom.Items.PedraDoPoder;
import nativelevel.CustomEvents.BlockHarvestEvent;
import nativelevel.CustomEvents.FinishCraftEvent;
import nativelevel.Equipment.Generator.EquipGenerator;
import nativelevel.Jobs;
import nativelevel.Jobs.TipoClasse;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.Lang.LangMinecraft;
import nativelevel.Lang.PT;
import nativelevel.Listeners.DamageListener;
import nativelevel.Listeners.GeneralListener;
import nativelevel.Listeners.InteractEvents;
import nativelevel.Menu.Menu;
import nativelevel.MetaShit;
import nativelevel.gemas.Raridade;
import nativelevel.sisteminhas.*;
import nativelevel.skills.Skill;
import nativelevel.skills.SkillMaster;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Farmer extends KomSystem {

    public static final Jobs.Classe classe = Jobs.Classe.Fazendeiro;
    public static final String name = "Fazendeiro";

    public static void onHit(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (weapon.getType().equals(Material.SHEARS) && ev.getEntity() instanceof Animals) {
            hitAnimal(ev, (Animals) ev.getEntity(), TipoClasse.PRIMARIA);
        } else if (weapon.getType().toString().contains("HOE")) {

            if (weapon.getType().name().contains("GOLD")) ev.setDamage(ev.getDamage() + 0.5);

            ev.setDamage(ev.getDamage() * 1.10);

        }

    }

    public static void onDamaged(EntityDamageEvent ev) {

        Player damaged = (Player) ev.getEntity();

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || ev.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            if (!((ev.getDamage() * 1.5) >= damaged.getHealth())) ev.setDamage(ev.getDamage() * 1.5);
        }

    }

    private static void hitAnimal(EntityDamageByEntityEvent ev, Animals animal, TipoClasse tipoClasse) {
        if (!animal.isAdult()) {
            DamageListener.darDano(null, (double) 7, (Player) ev.getDamager(), EntityDamageEvent.DamageCause.CUSTOM);
            ((Player) ev.getDamager()).playSound(ev.getDamager().getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 0.1f, 1.5f);
        } else if (SkillMaster.temSkill((Player) ev.getDamager(), Skills.Recursos_Extras_de_Animais.skill)) {
            ev.setDamage(4);
            recursosExtras(tipoClasse, (Player) ev.getDamager(), animal);
        }

    }

    public static void tameWolf(EntityTameEvent event) {
        int jobLvl = Jobs.getJobLevel("Fazendeiro", ((Player) event.getOwner()));
        if (jobLvl == 0 || (Jobs.rnd.nextInt(5) != 1)) {
            ((Player) event.getOwner()).sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.RED + L.m("Voce falhou em domesticar o animal!"));
            if (event.getEntity() instanceof Wolf) {
                ((Wolf) event.getEntity()).setAngry(true);
                ((Wolf) event.getEntity()).setTamed(false);
            }
            event.setCancelled(true);
        }
    }

    public static void poisonaSlimeball(PlayerPickupItemEvent ev) {

        if (!ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon") && Jobs.getJobLevel("Fazendeiro", ev.getPlayer()) != 1) {
            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 3));
            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 3));
            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
            PlayEffect.play(VisualEffect.SPELL_MOB, ev.getPlayer().getLocation(), "num:2");
            ev.getItem().remove();
            ev.getPlayer().sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.RED + L.m("Voce foi envenenado por uma slimeball envenenada e está decompondo!"));
            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, 10, 0);
            Thief.revela(ev.getPlayer());
            ev.setCancelled(true);
        } else {
            if (ev.getPlayer().getLevel() >= 9) {
                if (!ev.getPlayer().isSneaking()) {
                    double veloCima = 0.3D;
                    double forcaChute = 2D;
                    Mobs.kick(veloCima, forcaChute, ev.getPlayer(), ev.getItem());
                    ev.setCancelled(true);
                }
            }
        }
    }

    public static void cortaFolha(Player p, Block folha) {
        if (folha.hasMetadata("jogadorpois")) {
            return;
        }
        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.SHEARS) {
            return;
        }
        int level = Jobs.getJobLevel("Fazendeiro", p);
        if (level == 1 && Jobs.rnd.nextBoolean()) {
            folha.getWorld().dropItemNaturally(folha.getLocation(), CustomItem.getItem(FolhaDeMana.class).generateItem());
            GeneralListener.givePlayerExperience(2, p);
            if (Jobs.rnd.nextInt(3) == 1) {
                folha.getWorld().dropItemNaturally(folha.getLocation(), new ItemStack(Material.APPLE));
            }
        } else if (level == 2 && Jobs.rnd.nextInt(10) == 1) {
            folha.getWorld().dropItemNaturally(folha.getLocation(), CustomItem.getItem(FolhaDeMana.class).generateItem());
            GeneralListener.givePlayerExperience(2, p);
        }
    }

    public static final Enchantment[] enchantsRandom = {Enchantment.LOOT_BONUS_MOBS, Enchantment.DURABILITY, Enchantment.LOOT_BONUS_MOBS, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.WATER_WORKER, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_FIRE};

    public static void blockHarvest(BlockHarvestEvent ev) {

    }

    @EventHandler
    public static void interageCavalo(PlayerInteractEntityEvent ev) {

        if (ev.getRightClicked() != null && ev.getRightClicked().getType() == EntityType.DONKEY) {
            ev.setCancelled(true);
            return;
        }


        if (ev.getRightClicked() != null && ev.getRightClicked().getType() == EntityType.HORSE) {
            Horse h = (Horse) ev.getRightClicked();
            if (h.getOwner() != null && h.getOwner().getUniqueId() == ev.getPlayer().getUniqueId()) {
                if (h.getPassengers() == null) {
                    ItemStack ovo = Horses.getCavalo(h);
                    ev.setCancelled(true);
                    ev.getRightClicked().remove();
                    ev.getPlayer().getInventory().addItem(ovo);
                }
            }
        }
    }

    //  FAZER COLHEIT
    public static void depoisCraft(FinishCraftEvent ev) {
        Player p = ev.getPlayer();
        Material feitoDe = Blacksmith.getToolLevel(ev.getResult().getType());
        if (feitoDe == Material.LEATHER) {

            int chanceExp = ev.getPlayer().getLevel();

            LeatherArmorMeta lMeta = (LeatherArmorMeta) ev.getResult().getItemMeta();
            KoM.debug("fazendo armor lea");
            if (lMeta.getColor() == Bukkit.getItemFactory().getDefaultLeatherColor()) {

                int chanceExceptional = 5 + (ev.getPlayer().getLevel() / 2);

                if (Jobs.getJobLevel(Jobs.Classe.Fazendeiro, p) == TipoClasse.PRIMARIA) {
                    if (Jobs.rnd.nextInt(100) < chanceExceptional) {
                        Raridade rar = Raridade.Comum;
                        int sorte = Jobs.rnd.nextInt(ev.getPlayer().getLevel());
                        if (sorte == 99) {
                            rar = Raridade.Epico;
                        } else if (sorte > 65) {
                            rar = Raridade.Raro;
                        } else if (sorte > 20) {
                            rar = Raridade.Incomum;
                        }
                        ItemStack item = ev.getResult();
                        EquipGenerator.gera(item, rar);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(rar.getIcone() + " " + LangMinecraft.get().get(item) + " Excepcional");
                        item.setItemMeta(meta);
                        item.addEnchantment(Enchantment.DURABILITY, 3);
                        ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce fez uma armadura Excepcional !"));
                    } else {
                        p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.GOLD + L.m("Voce fez uma armadurade couro !"));
                    }
                } else {
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.GOLD + L.m("Voce fez uma armadurade couro !"));
                }
            } else {
                if (Jobs.rnd.nextInt(3) == 1) {
                    for (int x = 0; x < 2; x++) {
                        try {
                            if (Mana.spendMana(p, 5)) {
                                Enchantment e = enchantsRandom[Jobs.rnd.nextInt(enchantsRandom.length)];
                                if (!ev.getResult().containsEnchantment(e)) {
                                    int srt = Jobs.rnd.nextInt(100);
                                    int level = 1;
                                    if (srt == 1) {
                                        level = 4;
                                    } else if (srt > 1 && srt < 3) {
                                        level = 3;
                                    } else if (srt > 3 && srt < 50) {
                                        level = 2;
                                    }
                                    ev.getResult().addEnchantment(e, level);
                                }
                            }
                        } catch (Exception e) {
                            KoM.log.info("tentei adicionar o encantamento " + e.toString() + " no item " + ev.getResult().getType().name() + " e falhei...");
                        }
                    }
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.GOLD + L.m("Voce pintou a armadura !"));
                } else {
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.GOLD + L.m("Voce falhou em pintar a armadura !"));
                    lMeta.setColor(Bukkit.getItemFactory().getDefaultLeatherColor());
                }

            }
        } else {
            p.sendMessage(Menu.getSimbolo("Fazendeiro") + " " + ChatColor.GOLD + PT.craft_sucess);
        }
        ItemMeta meta = ev.getResult().getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        ev.getResult().setItemMeta(meta);
    }

    public static void handleEgg(final PlayerInteractEvent ev) {

        if (ev.getAction() == Action.PHYSICAL) {
            return;
        }

        if (ev.getHand() == EquipmentSlot.OFF_HAND) {
            ev.setCancelled(true);
            KoM.debug("Cancelado em handleEgg");
            return;
        }

        SpawnEggMeta meta = (SpawnEggMeta) ev.getPlayer().getInventory().getItemInMainHand().getItemMeta();

        KoM.debug("hangleEgg: " + ev.getPlayer().getInventory().getItemInMainHand().getType().name() + " - " + ev.getPlayer().getInventory().getItemInMainHand().getDurability());

        if (ClanLand.getTypeAt(ev.getPlayer().getLocation()).equalsIgnoreCase("SAFE") && meta.getSpawnedType() != EntityType.HORSE) {
            ev.getPlayer().sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + L.m("Voce nao pode fazer isto em uma Cidade"));
            ev.setCancelled(true);
            return;
        }

        if (!ev.getPlayer().isOp() && ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode botar ovos aqui !"));
            ev.setCancelled(true);
            return;
        }

        // villager
        /*
         if (ev.getPlayer().getInventory().getItemInMainHand().getDurability() == 120 && ev.getClickedBlock() != null) {
         String aqui = ClanLand.getTypeAt(ev.getClickedBlock().getLocation());
         if (!aqui.equalsIgnoreCase("Safe")) {
         ev.getPlayer().sendMessage(L.m("Voce nao pode fazer isto aqui !"));
         ev.setCancelled(true);
         return;
         }
         HashSet<Material> m = null;
         if (!SimpleClanKom.canBuild(ev.getPlayer(), ev.getPlayer().getTargetBlock(m, 20).getLocation(), ev.getPlayer().getTargetBlock(m, 20), false)) {
         ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce só pode criar um lojista na sua casa !"));
         ev.setCancelled(true);
         return;
         }
         }
         */
        if (meta == null) {
            return;
        }
        if (meta.getSpawnedType() == EntityType.WOLF) {
            ev.setCancelled(true);
            //cavalo
        } else if (meta.getSpawnedType() == EntityType.HORSE) {
            Horses.takeOutFromEgg(ev);
        }

    }

    private static final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 175, 2);

    @EventHandler
    public static void pesca(PlayerFishEvent ev) {

        if (ev.getState().equals(PlayerFishEvent.State.FISHING) || ev.getState().equals(PlayerFishEvent.State.BITE) || ev.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)) {

            ItemStack hand = ev.getPlayer().getInventory().getItemInMainHand();
            short durability = hand.getDurability();

            if (durability >= hand.getType().getMaxDurability()) ev.getPlayer().getInventory().setItemInMainHand(null);
            else ev.getPlayer().getInventory().getItemInMainHand().setDurability(++durability);

        } else if (ev.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {

            if (ev.getCaught() == null || !(ev.getCaught() instanceof Item)) return;

            XP.changeExp(ev.getPlayer(), XP.getExpPorAcao(ev.getPlayer().getLevel()), 3, true);

            Item item = (Item) ev.getCaught();

            KoM.announce("called");
            if (Jobs.dado(2) == 1) {
                item.setItemStack(Tesouros.geraTesouro());
                return;
            }

            if (Jobs.dado(2) == 1) {
                item.setItemStack(CustomItem.getItem(PedraDoPoder.class).generateItem());
                return;
            }

            ItemStack itemStack = item.getItemStack();

            if (!itemStack.getType().equals(Material.RAW_FISH)) item.setItemStack(new ItemStack(Material.RAW_FISH, 1, (short) (Jobs.dado(4) - 1)));

        } else {

            if (!SkillMaster.temSkill(ev.getPlayer(), Skills.Vara_de_Pescar.skill) || ClanLand.isSafeZone(ev.getHook().getLocation())) {
                ev.getHook().remove();
                ev.setCancelled(true);
                return;
            }

            if (ev.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {

                Entity hooked = ev.getHook();

                if (hooked == null || hooked.isDead()) return;

                if ((hooked instanceof ArmorStand || hooked instanceof ItemFrame || hooked instanceof Painting) && InteractEvents.touchIn(ev.getPlayer(), ev.getHook().getLocation()))
                    if (InteractEvents.touchIn(ev.getPlayer(), ev.getHook().getLocation())) {
                        ev.getHook().remove();
                        ev.setCancelled(true);
                        return;
                    }

                hooked.teleport(ev.getPlayer());

//              Vector toDamager = ev.getPlayer().getLocation().toVector().subtract(hooked.getLocation().toVector());
//              hooked.setVelocity(toDamager.normalize().multiply(1.5));

            } else if (ev.getState().equals(PlayerFishEvent.State.IN_GROUND)) {

                for (Entity nearby : ev.getHook().getNearbyEntities(2, 2, 2)) {
                    if (!(nearby instanceof Player)) continue;
                    if (Jobs.dado(3) != 3) continue;

//                  Vector toDamager = ev.getPlayer().getLocation().toVector().subtract(nearby.getLocation().toVector());
//                  nearby.setVelocity(toDamager.normalize().multiply(0.75));
                    ((Player) nearby).addPotionEffect(slow, true);

                    nearby.sendMessage("§cUm ganho puxa seu pé!");
                    ev.getPlayer().sendMessage("§aVocê puxa pelo o alvo pelo pé!");
                    break;
                }

            }

        }

    }

    @EventHandler
    public static void nasceFilhote(EntityBreedEvent ev) {
        ev.setCancelled(true);
    }

    public static Horse.Color pegaCor(String s) {
        s = s.trim();
        if (KoM.debugMode) {
            KoM.log.info("pegando tipo " + s);
        }
        for (Horse.Color cor : Horse.Color.values()) {
            if (KoM.debugMode) {
                KoM.log.info("verificando " + cor.name());
            }
            if (cor.name().equalsIgnoreCase(s)) {
                if (KoM.debugMode) {
                    KoM.log.info("retornei " + cor.name());
                }
                return cor;
            }
        }
        return Horse.Color.CREAMY;
    }

    public static Horse.Style pegaTipo(String s) {
        s = s.trim();
        if (KoM.debugMode) {
            KoM.log.info("pegando tipo " + s);
        }
        for (Horse.Style cor : Horse.Style.values()) {
            if (KoM.debugMode) {
                KoM.log.info("verificando " + cor.name());
            }
            if (cor.name().equalsIgnoreCase(s)) {
                if (KoM.debugMode) {
                    KoM.log.info("retornei " + cor.name());
                }
                return cor;
            }
        }
        return Horse.Style.NONE;
    }

    public static Horse.Variant pegaVariant(String s) {
        s = s.trim();
        return Horse.Variant.HORSE;
    }

    public static void fixLeatherArmor(PlayerInteractEvent ev) {
        if (Jobs.getJobLevel("Fazendeiro", ev.getPlayer()) == 1) {
            if (ev.getPlayer().getInventory().contains(Material.LEATHER)) {
                if (ev.getPlayer().getInventory().getItemInMainHand().getDurability() == 0) {
                    ev.getPlayer().sendMessage(ChatColor.RED + "Este item esta como novo");
                } else {
                    if (Mana.spendMana(ev.getPlayer(), 50)) {
                        PlayEffect.play(VisualEffect.SPELL_INSTANT, ev.getPlayer().getLocation(), "num:40");
                        ev.getPlayer().sendMessage(ChatColor.GREEN + "Voce arrumou a armadura");
                        ev.getPlayer().getInventory().getItemInMainHand().setDurability((short) 0);
                        KoM.removeInventoryItems(ev.getPlayer().getInventory(), Material.LEATHER, 1);
                    }
                }
            } else {
                ev.getPlayer().sendMessage(ChatColor.RED + "Voce precisa de couro");
            }
        }

    }

    private static void recursosExtras(TipoClasse classe, Player player, Animals animal) {
        int manyDrop;
        if (classe.equals(TipoClasse.PRIMARIA)) manyDrop = 3;
        else if (classe.equals(TipoClasse.SECUNDARIA)) manyDrop = 1;
        else return;

        int diff = 20;
        if (animal.hasMetadata("recursosExtras")) diff = 70;
        if (!Jobs.hasSuccess(diff, name, player)) return;

        MetaShit.setMetaObject("recursosExtras", animal, true);

        ItemStack[] dropsExtras;
        if (animal instanceof Chicken) dropsExtras = new ItemStack[]{new ItemStack(Material.FEATHER, Jobs.dado(manyDrop + 2)), new ItemStack(Material.RAW_CHICKEN, Jobs.dado(manyDrop))};
        else if (animal instanceof Cow) dropsExtras = new ItemStack[]{new ItemStack(Material.LEATHER, Jobs.dado(manyDrop + 2)), new ItemStack(Material.RAW_BEEF, Jobs.dado(manyDrop))};
        else if (animal instanceof Pig) dropsExtras = new ItemStack[]{new ItemStack(Material.PORK, Jobs.dado(manyDrop + 1))};
        else if (animal instanceof Sheep) dropsExtras = new ItemStack[]{new ItemStack(Material.MUTTON, Jobs.dado(manyDrop))};
        else if (animal instanceof Rabbit) dropsExtras = new ItemStack[]{new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.RABBIT_HIDE, Jobs.dado(manyDrop)), new ItemStack(Material.RABBIT)};
        else if (animal instanceof Parrot) dropsExtras = new ItemStack[]{new ItemStack(Material.FEATHER, Jobs.dado(manyDrop + 4))};
        else if (animal instanceof PolarBear) dropsExtras = new ItemStack[]{new ItemStack(Material.RAW_FISH, Jobs.dado(manyDrop + 2)), new ItemStack(Material.SNOW_BALL, Jobs.dado(manyDrop + 5))};
        else return;

        animal.getWorld().playSound(animal.getLocation(), Sound.BLOCK_NOTE_BELL, 0.4f, 1f);

        for (ItemStack drop : dropsExtras) {
            animal.getWorld().dropItemNaturally(animal.getLocation(), drop);
        }

        player.sendMessage("§aVocê coletou recursos extras do Animal");

    }

    public static boolean transformaEmOvO(Player p, Entity animal) {
        // soh vale pra fazendeiro primário
        if (p.getInventory().getItemInMainHand().getType() != Material.EGG) {
            return false;
        }

        if (p.getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            return false;
        }

        KoM.debug("transforma em ovo");

        if (animal.hasMetadata("recurso")) {
            long recurso = Long.valueOf(MetaShit.getMetaString("recurso", animal));
            long agora = System.currentTimeMillis() / 1000;
            if (recurso + 60 * 20 > agora) {
                p.sendMessage(ChatColor.RED + L.m("Este animal foi coletado recurso a muito pouco tempo, aguarde..."));
                return false;
            }
        }

        int dificuldade = 60;
        short idMonstro = 0;
        if (animal instanceof Pig) {
            idMonstro = 90;
        } else if (animal instanceof Sheep) {
            idMonstro = 91;
        } else if (animal instanceof Cow) {
            idMonstro = 92;
        } else if (animal instanceof Horse) {
            idMonstro = 100;
            dificuldade = 70;
        } else if (animal instanceof Chicken) {
            idMonstro = 93;
        } else if (animal instanceof Wolf) {
            // idMonstro = 95;
        } else if (animal instanceof Ocelot) {
            idMonstro = 98;
        } else if (animal instanceof Spider) {
            dificuldade = 80;
            idMonstro = 52;
        } else if (animal instanceof Zombie) {
            idMonstro = 54;
            dificuldade = 80;
        } else if (animal instanceof Skeleton) {
            idMonstro = 51;
            dificuldade = 80;
        } else if (animal instanceof MushroomCow) {
            idMonstro = 96;
        } else if (animal instanceof Rabbit) {
            idMonstro = 101;
        }
        if (idMonstro == 0) {
            idMonstro = animal.getType().getTypeId();
        }

        KoM.debug("id monstro = " + idMonstro);

        if (animal instanceof Tameable) {
            AnimalTamer dono = ((Tameable) animal).getOwner();
            if (dono != null && !dono.getName().equalsIgnoreCase(p.getName())) {
                p.sendMessage(ChatColor.RED + L.m("Este animal ja tem dono !"));
            }
        }
        if (animal.hasMetadata("recurso")) {
            long recurso = Long.valueOf(MetaShit.getMetaString("recurso", animal));
            long agora = System.currentTimeMillis() / 1000;
            if (recurso + 60 * 10 > agora) {
                p.sendMessage(ChatColor.RED + L.m("Aguarde para colocar este animal no ovo !"));
                return false;
            }
        }

        if (idMonstro != 0) {

            if (p.getLevel() < 30) {
                return false; // skill de por em ovo
            }

            if (!Mana.spendMana(p, 40)) {
                return false;
            }

            if (idMonstro == 95) {
                dificuldade = 30;
            }
            ItemStack ovo = new ItemStack(Material.MONSTER_EGG, 1);
            SpawnEggMeta meta = (SpawnEggMeta) ovo.getItemMeta();
            meta.setSpawnedType(EntityType.fromId(idMonstro));
            ovo.setItemMeta(meta);

            if (Jobs.hasSuccess(dificuldade, "Fazendeiro", p)) {
                if (dificuldade < 70) {
                    animal.remove();
                }
                if (p.getInventory().getItemInMainHand().getAmount() > 1) {
                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                } else {
                    p.setItemInHand(null);
                }
                p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.RED + L.m("Voce falhou em colocar o animal no ovo !"));
                return true;
            }
            if (animal instanceof Horse) {

                Horse h = (Horse) animal;
                if (h.getOwner() == null || !h.getOwner().getName().equalsIgnoreCase(p.getName())) {
                    p.sendMessage(ChatColor.RED + L.m("Voce precisa domar o cavalo primeiro !"));
                    return false;
                }
                if (h.getInventory().getSaddle() == null || h.getInventory().getSaddle().getType() != Material.SADDLE) {
                    p.sendMessage(ChatColor.RED + L.m("Voce precisa por uma cela no cavalo para transforma-lo em ovo !"));
                    return false;
                }
                if (h.getLeashHolder() == null || !(h.getLeashHolder() instanceof Player) || !((Player) h.getLeashHolder()).getName().equalsIgnoreCase(p.getName())) {
                    p.sendMessage(ChatColor.RED + L.m("Voce precisa por uma coleira no cavalo para transforma-lo em ovo !"));
                    return false;
                }
                meta.setDisplayName(ChatColor.GREEN + "Montaria (Não Dropa Qnd Morre)");
                meta.setLore(Arrays.asList(new String[]{
                        ChatColor.GREEN + "Nome" + ChatColor.YELLOW + ": " + ((h.getCustomName() == null || h.getCustomName().length() == 0) ? "Pocotó" : h.getCustomName()),
                        ChatColor.GREEN + "Cor" + ChatColor.YELLOW + ": " + h.getColor().name(),
                        ChatColor.GREEN + "Tipo" + ChatColor.YELLOW + ": " + h.getStyle().name(),
                        ChatColor.GREEN + "Raça" + ChatColor.YELLOW + ": " + h.getVariant().name()
                        // ChatColor.YELLOW + "- Não Dropa Quando Morre"
                }));
                ovo.setItemMeta(meta);
            }
            //cavalo
            if (p.getInventory().getItemInMainHand().getAmount() > 1) {
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            } else {
                p.setItemInHand(null);
            }
            animal.getWorld().dropItemNaturally(animal.getLocation(), ovo);
            animal.remove();
            GeneralListener.givePlayerExperience(15, p);
            p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Fazendeiro") + " " + ChatColor.RED + L.m("Voce colocou o animal no ovo !"));
            return true;
        }
        return false;
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

    public static final List<Skill> skillList = Arrays.asList(
            new Skill(classe, "Plantador", 2, false, new String[]{"§9Aumenta chances de sucesso ao plantar e colher.", "§9Quanto maior seu nivel, maior a chance"}),
            new Skill(classe, "Pescador", 5, false, new String[]{"§9Aumenta chances de sucesso ao pescar.", "§9Quanto maior seu nivel, maior a chance e mais raridades"}),
            new Skill(classe, "Slimeball Envenenada", 9, true, new String[]{"§9Permite craftar e usar slimeballs envenenadas"}),
            new Skill(classe, "Vara de Pescar", 10, true, new String[]{"§9Permite puxar inimigos com vara de pescar."}),
            new Skill(classe, "Recursos Extras de Animais", 13, true, new String[]{"§9Permite coletar recursos extras de animais usando uma tesoura"}),
            new Skill(classe, "Mestre das Enxadas", 15, true, new String[]{"§9Bonus ao bater com enxadas"}),
            new Skill(classe, "Folhas de Mana", 16, true, new String[]{"§9Encontra folhas de mana pegando folhas normais"}),
            new Skill(classe, "Domador de Lobos", 17, true, new String[]{"§9Pode ter um lobo usando um osso magico"}),
            new Skill(classe, "Expert em Couro", 22, true, new String[]{"§9Permite criar armaduras de couro muito melhores"}, true),
            new Skill(classe, "Pokeovos", 30, true, new String[]{"§9Permite guardar animais em ovos"})
    );

    public enum Skills {
        Plantador(skillList.get(0)),
        Pescador(skillList.get(1)),
        Slimeball_Envenenada(skillList.get(2)),
        Vara_de_Pescar(skillList.get(3)),
        Recursos_Extras_de_Animais(skillList.get(4)),
        Mestre_das_Enxadas(skillList.get(5)),
        Folhas_de_Mana(skillList.get(6)),
        Domador_de_Lobos(skillList.get(7)),
        Expert_em_Couro(skillList.get(8)),
        Pokeovos(skillList.get(9));

        public Skill skill;

        Skills(Skill skill) {
            this.skill = skill;
        }
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

}
