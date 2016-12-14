package com.chiva;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.LongStream;

/**
 * @author 赵振华
 * @Date 2016年12月14日 下午5:45:42
 */
public class LambdaExample {
    static List<Long> numbers = createNumbers();

    public static void main(String[] args) throws Exception {
        compParalle();
        threadSum1();
        threadSum2();
        threadSum3();
    }

    private static void compParalle() {
        long[] result = new long[1];
        result[0] = 0;
        LongStream.range(1, 1000).forEach(n -> result[0] = (result[0] + n) * n);
        System.out.println("serial:" + result[0]);
        LongStream.range(1, 1000).parallel().forEach(n -> result[0] = (result[0] + n) * n);
        System.out.println("parallel:" + result[0]);
        LongStream.range(1, 1000).parallel().forEachOrdered(n -> result[0] = (result[0] + n) * n);
        System.out.println("para-order:" + result[0]);

        long reduce = LongStream.range(0, 1000).reduce(0, (a, c) -> (a + c) * c);
        System.out.println(reduce);
    }

    public static List<Long> createNumbers() {
        List<Long> numbers = new ArrayList<>();
        Random random = new Random();
        int max = 1000000 + random.nextInt(1000000);
        for (int i = 0; i < max; i++) {
            numbers.add((long) random.nextInt(100));
        }
        return numbers;
    }

    private static void threadSum1() throws Exception {
        SummingUnit summingUnit = new SummingUnit();
        int size = numbers.size();
        Thread task1 = new Thread(new SumTask(numbers, 0, size / 2, summingUnit));
        task1.start();

        Thread task2 = new Thread(new SumTask(numbers, size / 2, size, summingUnit));
        task2.start();

        task1.join();
        System.out.println("sum:" + summingUnit.getSum());
    }

    private static void threadSum2() throws Exception {
        SummingUnit summingUnit1 = new SummingUnit();
        SummingUnit summingUnit2 = new SummingUnit();
        int size = numbers.size();
        Thread task1 = new Thread(new SumTask(numbers, 0, size / 2, summingUnit1));
        task1.start();

        Thread task2 = new Thread(new SumTask(numbers, size / 2, size, summingUnit2));
        task2.start();

        task1.join();
        task2.join();
        summingUnit1.combine(summingUnit2);
        System.out.println("sum:" + summingUnit1.getSum());
    }

    private static void threadSum3() throws Exception {
        System.out.println("sum:" + numbers.parallelStream().collect(() -> new SummingUnit(),
                (summUnit, value) -> summUnit.sum(value), (summUnit, other) -> summUnit.combine(other)).getSum());
        System.out.println("sum:"
                + numbers.parallelStream().collect(SummingUnit::new, SummingUnit::sum, SummingUnit::combine).getSum());
        System.out.println("sum:" + numbers.parallelStream().collect(new SummingCollector()));
        System.out.println(numbers.parallelStream().mapToLong(i -> i).sum());
        System.out.println(numbers.parallelStream().reduce(0L, (a, c) -> a + c));
    }

    static class SummingUnit {
        private long _sum;

        public SummingUnit() {
            System.out.println("summing unit");
        }

        public long getSum() {
            return _sum;
        }

        public synchronized void sum(long other) {
            _sum += other;
        }

        public void combine(SummingUnit other) {
            _sum += other._sum;
        }
    }

    static class SumTask implements Runnable {
        private final List<Long> _numbers;
        private final int _start;
        private final int _end;
        private final SummingUnit _summingUnit;

        public SumTask(List<Long> numbers, int start, int end, SummingUnit summingUnit) {
            this._numbers = numbers;
            this._start = start;
            this._end = end;
            this._summingUnit = summingUnit;
        }

        @Override
        public void run() {
            for (int i = _start; i < _end; i++) {
                _summingUnit.sum(_numbers.get(i));
            }
        }
    }

    static class SummingCollector implements Collector<Long, SummingUnit, Long> {

        @Override
        public Supplier<SummingUnit> supplier() {
            return SummingUnit::new;
        }

        @Override
        public BiConsumer<SummingUnit, Long> accumulator() {
            return SummingUnit::sum;
        }

        @Override
        public BinaryOperator<SummingUnit> combiner() {
            return (l, r) -> {
                l.combine(r);
                return l;
            };
        }

        @Override
        public Function<SummingUnit, Long> finisher() {
            return SummingUnit::getSum;
        }

        @Override
        public Set<Characteristics> characteristics() {
            //			return EnumSet.of(Characteristics.UNORDERED);
            return EnumSet.of(Characteristics.CONCURRENT);
        }

    }

    public class Person {
        private String _givenName;
        private String _surname;
        private Gender _gender;
        private int _age;
        private Map<Integer, ArticleInfo> _selling = new ConcurrentHashMap<>();
        private Map<Integer, ArticleInfo> _buying = new ConcurrentHashMap<>();
        private int _discount;

        public String getGivenName() {
            return _givenName;
        }

        public void setGivenName(String givenName) {
            _givenName = givenName;
        }

        public String getSurname() {
            return _surname;
        }

        public void setSurname(String surname) {
            _surname = surname;
        }

        public Gender getGender() {
            return _gender;
        }

        public void setGender(Gender gender) {
            _gender = gender;
        }

        public boolean isFemale() {
            return _gender == Gender.Female;
        }

        public int getAge() {
            return _age;
        }

        public void setAge(int age) {
            _age = age;
        }

        public boolean isVendor() {
            return _selling.size() > 0;
        }

        public int getDiscount() {
            return _discount;
        }

        public void setDiscount(int discount) {
            _discount = discount;
        }

        public Map<Integer, ArticleInfo> getSelling() {
            return _selling;
        }

        public void setSelling(Map<Integer, ArticleInfo> selling) {
            _selling = selling;
        }

        public Map<Integer, ArticleInfo> getBuying() {
            return _buying;
        }

        public void setBuying(Map<Integer, ArticleInfo> buying) {
            _buying = buying;
        }

    }

    public class ArticleInfo {
        private final int _articleNo;
        private long _quantity;
        private Money _amount;

        public ArticleInfo(int articleNo) {
            _articleNo = articleNo;
            _amount = new Money();
        }

        public int getArticleNo() {
            return _articleNo;
        }

        public long getQuantity() {
            return _quantity;
        }

        public void setQuantity(long quantity) {
            _quantity = quantity;
        }

        public Money getAmount() {
            return _amount;
        }

        public void setAmount(Money amount) {
            _amount = amount;
        }

        public void addQuantity(long quantity) {
            _quantity += quantity;
        }

        public void addPrice(long cents) {
            _amount.add(cents);
        }
    }

    public enum Gender {
        Male, Female;
    }

    public class Money {

        private long _cents;

        Money() {
            _cents = 0;
        }

        Money(String value) {
            setValue(value);
        }

        public long getCents() {
            return _cents;
        }

        public void setCents(long cents) {
            _cents = cents;
        }

        public String getValue() {
            return _cents / 100 + "." + _cents % 100;
        }

        public void setValue(String value) {
            int pos = value.indexOf(".");
            if (pos == -1) {
                _cents = 100 * Long.parseLong(value);
            } else {
                _cents = 100 * Long.parseLong(value.substring(0, pos));
                String decimals = value.substring(pos + 1) + "00";
                _cents += Long.parseLong(decimals.substring(0, 2));
            }
        }

        public void add(long cents) {
            _cents += cents;
        }
    }

    public class AverageBuilder {
        private int _count;
        private long _cents;

        public int getCount() {
            return _count;
        }

        public long getCents() {
            return _cents;
        }

        public void add(long cents) {
            _count++;
            _cents += cents;
        }

        public void add(AverageBuilder other) {
            _count += other.getCount();
            _cents += other.getCents();
        }

        public double getAverage() {
            return _cents / 100D / _count;
        }
    }

    public class GroupAverageCollector
            implements Collector<Person, Map<Integer, AverageBuilder>, Map<Integer, Double>> {

        @Override
        public Supplier<Map<Integer, AverageBuilder>> supplier() {
            return () -> new HashMap<>();
        }

        @Override
        public BiConsumer<Map<Integer, AverageBuilder>, Person> accumulator() {
            return (m, p) -> add(m, p);
        }

        @Override
        public BinaryOperator<Map<Integer, AverageBuilder>> combiner() {
            return (left, right) -> {
                combine(left, right);
                return left;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.UNORDERED);
            // no Characteristics.CONCURRENT!
        }

        private void add(Map<Integer, AverageBuilder> map, Person person) {
            int group = person.getAge() / 10;
            if (!map.containsKey(group)) {
                map.put(group, new AverageBuilder());
            }
            long cents = person.getBuying().values().stream().mapToLong(a -> a.getAmount().getCents()).sum();
            map.get(group).add(cents);
        }

        private void combine(Map<Integer, AverageBuilder> left, Map<Integer, AverageBuilder> right) {
            for (int group : right.keySet()) {
                if (!left.containsKey(group)) {
                    left.put(group, right.get(group));
                } else {
                    left.get(group).add(right.get(group));
                }
            }
        }

        private Map<Integer, Double> finish(Map<Integer, AverageBuilder> map) {
            Map<Integer, Double> result = new HashMap<>();
            for (int group : map.keySet()) {
                result.put(group, map.get(group).getAverage());
            }
            return result;
        }

        @Override
        public Function<Map<Integer, AverageBuilder>, Map<Integer, Double>> finisher() {
            return m -> finish(m);
        }

    }
}
