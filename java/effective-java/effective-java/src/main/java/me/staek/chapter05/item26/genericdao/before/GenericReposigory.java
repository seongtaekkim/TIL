package me.staek.chapter05.item26.genericdao.before;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GenericReposigory<T extends Entity> {
    private Set<T> entites;

    public GenericReposigory() {
        this.entites = new HashSet<>();
    }

    public Optional<T> findById(Long id) {
        return entites.stream().filter(a -> a.getId().equals(id)).findAny();
    }

    public void add(T account) {
        this.entites.add(account);
    }
}
