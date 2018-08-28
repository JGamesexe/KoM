package nativelevel.skills;

import nativelevel.Jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Skill {

    private List<String> lore = new ArrayList<String>();
    private String nome;
    private int nivel;
    private Jobs.Classe classe;
    private boolean precisaPrimaria = false;
    private boolean skillDeCraft = false;

    public Skill(String nome, int nivel, boolean precisaPrimaria) {
        this.nome = nome;
        this.nivel = nivel;
        this.precisaPrimaria = precisaPrimaria;
    }

    public Skill(Jobs.Classe classe, String nome, int nivel, boolean precisaPrimaria, String[] lore) {
        this.nome = nome;
        this.nivel = nivel;
        this.classe = classe;
        this.precisaPrimaria = precisaPrimaria;
        this.lore = Arrays.asList(lore);
    }

    public Skill(Jobs.Classe classe, String nome, int nivel, boolean precisaPrimaria, String[] lore, boolean skillDeCraft) {
        this(classe, nome, nivel, precisaPrimaria, lore);
        this.skillDeCraft = skillDeCraft;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(String[] lore) {
        this.lore = Arrays.asList(lore);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Jobs.Classe getClasse() {
        return classe;
    }

    public void setClasse(Jobs.Classe classe) {
        this.classe = classe;
    }

    public boolean isPrecisaPrimaria() {
        return precisaPrimaria;
    }

    public void setPrecisaPrimaria(boolean precisaPrimaria) {
        this.precisaPrimaria = precisaPrimaria;
    }

    public boolean isSkillDeCraft() {
        return skillDeCraft;
    }

    public void setSkillDeCraft(boolean skillDeCraft) {
        this.skillDeCraft = skillDeCraft;
    }

}
