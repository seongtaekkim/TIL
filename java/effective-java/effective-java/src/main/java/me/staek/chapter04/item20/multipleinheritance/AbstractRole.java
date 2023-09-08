package me.staek.chapter04.item20.multipleinheritance;

public class AbstractRole implements Role{
    @Override
    public void roleName() {
        System.out.println("my rolename is admin");
    }
}
