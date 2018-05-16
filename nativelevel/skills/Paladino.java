package nativelevel.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Paladino {

    public static void load() {

        List<Skill> skills = new ArrayList<Skill>();

        Skill sMob = new Skill("Caçador de Mobs", 6, true);
        sMob.setLore(new String[]{"§9Ganha mais XP ao matar Monstros"});
        skills.add(sMob);

        Skill s1 = new Skill("Usar Escudos", 5, false);
        s1.setLore(new String[]{"§9Permite bloquear ataques em frente usando uma porta de ferro ou escudos."});
        skills.add(s1);

        Skill s2 = new Skill("Resistência Divina", 10, true);
        s2.setLore(new String[]{"§9Recebe menos dano de ataques físicos."});
        skills.add(s2);

        Skill s3 = new Skill("Recompensa Divina", 4, false);
        s3.setLore(new String[]{"§9Ganha items extras ao matar mobs."});
        skills.add(s3);

        Skill s4 = new Skill("Usar Espadas", 2, true);
        s4.setLore(new String[]{"§9Aumenta a precisão com espadas.", "§9§oAumentar o nível aumenta as chances."});
        skills.add(s4);

        Skill s5 = new Skill("Luz Divina", 30, true);
        s5.setLore(new String[]{"§9Pode usar Luz Divina para evitar a morte."});
        skills.add(s5);

        Skill s6 = new Skill("Grito de guerra", 7, true);
        s6.setLore(new String[]{"§9Da um grito olhando pra cima com uma espada na mao chamando atenção de mobs."});
        skills.add(s6);

        Skill s7 = new Skill("Jabu está entre nós", 15, false);
        s7.setLore(new String[]{"Ao levar dano há uma chance de Jabu lhe abençoar."});
        skills.add(s7);


        Collections.sort(skills, new Comparator<Skill>() {
            @Override
            public int compare(Skill p1, Skill p2) {
                return p1.getNivel() - p2.getNivel();
            }
        });

        SkillMaster.skills.put("paladino", skills);

    }

}
