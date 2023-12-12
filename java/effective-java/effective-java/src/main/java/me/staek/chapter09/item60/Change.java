package me.staek.chapter09.item60;

/**
 * 1달라로 10센트, 20센트, 30센트 ..  물건을 구매하는 로직이다
 * double형으로 연산하면, 정확한 값이 나오지 않음을 알 수 있다.
 */
public class Change {
    public static void main(String[] args) {
        double funds = 1.00;
        int itemsBought = 0;
        for (double price = 0.10; funds >= price; price += 0.10) {
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + " items bought."); // 3
        System.out.println("Change: $" + funds); // 0.3999999999999999
    }
}
