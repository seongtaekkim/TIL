package me.staek.chapter11.item79;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Set에 대한 Forwarding method 를 구현한 Forwarding class를 컴포지션 방식으로 사용한 클래스 생성
 */
public class ObservableSet<E> extends ForwardingSet<E> {
    public ObservableSet(Set<E> set) { super(set); }

//    private final ArrayList<SetObserver<E>> observers
//            = new ArrayList<>();
//
//    public void addObserver(SetObserver<E> observer) {
//        synchronized(observers) {
//            observers.add(observer);
//        }
//    }
//
//    public boolean removeObserver(SetObserver<E> observer) {
//        synchronized(observers) {
//            return observers.remove(observer);
//        }
//    }
//
////    private void notifyElementAdded(E element) {
////        synchronized(observers) {
////            Iterator<SetObserver<E>> iterator = observers.iterator();
////            while (iterator.hasNext()) {
////                SetObserver<E> observer = iterator.next();
////                observer.added(this, element);
////            }
//////            for (SetObserver<E> observer : observers)
//////            observer.added(this, element);
////        }
////    }
//
////    //  open calls
//    private void notifyElementAdded(E element) {
//        List<SetObserver<E>> snapshot = null;
//        synchronized(observers) {
//            snapshot = new ArrayList<>(observers);
//        }
//        for (SetObserver<E> observer : snapshot)
//            observer.added(this, element);
//    }

    /**
     * Thread-safe : CopyOnWriteArrayList
     */
    private final CopyOnWriteArrayList<SetObserver<E>> observers = new CopyOnWriteArrayList<>();

    public void addObserver(SetObserver<E> observer) {
        observers.add(observer);
    }

    public boolean removeObserver(SetObserver<E> observer) {
        return observers.remove(observer);
    }

    private void notifyElementAdded(E element) {
        for (SetObserver<E> observer : observers)
            observer.added(this, element);
    }

    @Override public boolean add(E element) {
        boolean added = super.add(element);
        if (added)
            notifyElementAdded(element);
        return added;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c)
            result |= add(element);
        return result;
    }
}
