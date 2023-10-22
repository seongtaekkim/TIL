package me.staek.chapter06.item37;

/**
 * 배열을 이용한 이중 인덱싱
 */
public enum _03_Phase {
    SOLID, LIQUID, GAS;

    public enum Transition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;
        private static final Transition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };
        public static Transition from(_03_Phase from, _03_Phase to) {
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }
    }
    public static void main(String[] args) {
        for (_03_Phase src : _03_Phase.values()) {
            for (_03_Phase dst : _03_Phase.values()) {
                Transition transition = Transition.from(src, dst);
                if (transition != null)
                    System.out.printf("%s to %s : %s %n", src, dst, transition);
            }
        }
    }
}
