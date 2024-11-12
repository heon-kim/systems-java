package bank;

import java.util.Map;
import java.util.TreeMap;

class InterestRateCalculator {
    private final Map<Integer, Double> interestRatesByMonths;

    public InterestRateCalculator() {
        this.interestRatesByMonths = new TreeMap<>();
        interestRatesByMonths.put(1, 3.0);
        interestRatesByMonths.put(3, 3.35);
        interestRatesByMonths.put(6, 3.4);
        interestRatesByMonths.put(9, 3.35);
        interestRatesByMonths.put(12, 3.35);
        interestRatesByMonths.put(24, 2.9);
        interestRatesByMonths.put(36, 2.9);
        interestRatesByMonths.put(48, 2.9);
        interestRatesByMonths.put(60, 2.9);
    }

    public double getInterestRate(int months) {
        if (interestRatesByMonths.isEmpty()) return 0;

        Map.Entry<Integer, Double> entry = ((TreeMap<Integer, Double>) interestRatesByMonths).floorEntry(months);
        return (entry != null) ? entry.getValue() : 0;
    }

    public void printInterestRates() {
        System.out.println("* 예치 개월에 따른 적용 금리");
        for (Map.Entry<Integer, Double> entry : interestRatesByMonths.entrySet()) {
            System.out.println("    " + entry.getKey() + "개월 이상    " + entry.getValue() + "%");
        }
    }
}
