package nativelevel.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityHelp {

    public static ArrayList<EntityType>[] mobs;

    public static void separaMobs() {

        ArrayList<EntityType> entityTypes = new ArrayList<>(Arrays.asList(EntityType.values()));

        ArrayList<EntityType> merdas = new ArrayList<>();
        ArrayList<EntityType> monstros = new ArrayList<>();
        ArrayList<EntityType> animais = new ArrayList<>();

        for (EntityType entityType : entityTypes) {
            if(!entityType.isAlive()){
                merdas.add(entityType);
            }
        }

        entityTypes.removeAll(merdas);

        entityTypes.remove(EntityType.WITHER);
        entityTypes.remove(EntityType.ENDER_DRAGON);
        entityTypes.remove(EntityType.ILLUSIONER);
        entityTypes.remove(EntityType.ELDER_GUARDIAN);
        entityTypes.remove(EntityType.EVOKER);
        entityTypes.remove(EntityType.PLAYER);
        entityTypes.remove(EntityType.ARMOR_STAND);

        for (EntityType entityType : entityTypes) {

            for (Class c : entityType.getEntityClass().getInterfaces()) {

                if (c.getName().contains("Monster")) {
                    monstros.add(entityType);
                    break;
                } else if (c.getName().contains("Animals")) {
                    animais.add(entityType);
                    break;
                } else {
                    for (Class c1 : c.getInterfaces()) {
                        if (c1.getName().contains("Monster")) {
                            monstros.add(entityType);
                            break;
                        } else if (c1.getName().contains("Animals")) {
                            animais.add(entityType);
                            break;
                        } else {
                            for (Class c2 : c1.getInterfaces()) {
                                if (c2.getName().contains("Monster")) {
                                    monstros.add(entityType);
                                    break;
                                } else if (c2.getName().contains("Animals")) {
                                    animais.add(entityType);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        entityTypes.removeAll(monstros);
        entityTypes.removeAll(animais);

        ArrayList<EntityType>[] tiposDasEntitdades = new ArrayList[3];

        tiposDasEntitdades[0] = animais;
        tiposDasEntitdades[1] = monstros;
        tiposDasEntitdades[2] = entityTypes;

        EntityHelp.mobs = tiposDasEntitdades;
    }

    public static ArrayList<ItemStack> chocadeira(ArrayList<EntityType> entityTypes) {

        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        for (EntityType entityType : entityTypes) {

            ItemStack itemStack = new ItemStack(Material.MONSTER_EGG);
            SpawnEggMeta spawnEggMeta = (SpawnEggMeta) itemStack.getItemMeta();

            try {
                spawnEggMeta.setSpawnedType(entityType);
            } catch (IllegalArgumentException ignored) {

            }

            spawnEggMeta.setDisplayName("§3" + entityType);
            itemStack.setItemMeta(spawnEggMeta);

            itemStacks.add(itemStack);
        }

        return itemStacks;

    }

    public static int tamanhoDoBau(ArrayList arrayList){

        int a = 0;
        a += arrayList.size() / 9;
        if ((arrayList.size() % 9) != 0){
            a++;
        }

        return a*9;
    }


}
