package com.make.my.day.hm2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.Test;

public class Homework02 {

  @Test
  public void concatenateChars() {
    //TODO: create your realization with lambda
    Function<Character[], String> charConcatenator = chars -> {
      StringBuilder sb = new StringBuilder();
      for (char e : chars) {
        sb.append(e);
      }
      return sb.toString();
    };

    String result_1 = charConcatenator.apply(new Character[]{'a', 'b', 'c'});
    String result_2 = charConcatenator.apply(new Character[]{'H', 'e', 'l', 'l', 'o'});
    String result_3 = charConcatenator.apply(new Character[]{'T', 'u', 'r', 't', 'l', 'e'});

    assertEquals("abc", result_1);
    assertEquals("Hello", result_2);
    assertEquals("Turtle", result_3);
  }

  @Test
  public void reversedWord() {
    //TODO: create your realization with lambda
    Predicate<String> isReversedStringTheSame = word ->  new StringBuilder(word).reverse().toString().equals(word);

    boolean result_1 = isReversedStringTheSame.test("abccba");
    boolean result_2 = isReversedStringTheSame.test("level");
    boolean result_3 = isReversedStringTheSame.test("cow");
    boolean result_4 = isReversedStringTheSame.test("radar");
    boolean result_5 = isReversedStringTheSame.test("mellow");
    boolean result_6 = isReversedStringTheSame.test("madam");

    assertTrue(result_1);
    assertTrue(result_2);
    assertFalse(result_3);
    assertTrue(result_4);
    assertFalse(result_5);
    assertTrue(result_6);
  }

  private class Counter {

    private Function<String, Integer> transform;
    private BinaryOperator<Integer> summarizer;

    public Counter(Function<String, Integer> transform,
        BinaryOperator<Integer> summarizer) {
      this.transform = transform;
      this.summarizer = summarizer;
    }

    public int getSum(String firstInput, String secondInput) {
      int firstTransformedNumber = transform.apply(firstInput);
      int secondTransformedNumber = transform.apply(secondInput);
      return summarizer.apply(firstTransformedNumber, secondTransformedNumber);
    }
  }

  @Test
  public void transformAndProvideSumWithCounter() {
    //TODO: create your realization with lambda
    Function<String, Integer> transform = str -> Integer.parseInt(str);

    //TODO: create your realization with lambda
    BinaryOperator<Integer> increment = (int1, int2) -> int1 + int2;

    Counter sut_1 = new Counter(transform, increment);
    Counter sut_2 = new Counter(transform, increment);
    Counter sut_3 = new Counter(transform, increment);

    assertEquals(5, sut_1.getSum("3", "2"));
    assertEquals(10, sut_2.getSum("5", "5"));
    assertEquals(57, sut_3.getSum("24", "33"));
  }

  @Test
  public void consumerPrintInPrintStream() {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final PrintStream original = System.out;
    System.setOut(new PrintStream(outContent));

    //TODO: method must print parameters in System.out
    Consumer<Object> printHelloInSystemOut = input -> System.out.print(input);

    printHelloInSystemOut.accept("hello");
    assertEquals("hello", outContent.toString());

    outContent.reset();

    printHelloInSystemOut.accept("epam");
    assertEquals("epam", outContent.toString());

    outContent.reset();

    printHelloInSystemOut.accept(12345);
    assertEquals("12345", outContent.toString());

    System.setOut(original);
  }

  @Test
  public void testPredicateScenario() {

    //TODO: create your realization with lambda
    Predicate<String> isStartsWithXX = str -> str.startsWith("XX");

    //TODO: create your realization with lambda
    Predicate<String> isEndWithZZZ = isStartsWithXX.and(str -> str.endsWith("ZZZ"));

    //TODO: create your realization with lambda
    Predicate<String> haveFiveStarsInRow = isEndWithZZZ.or(str -> str.contains("*****"));

    assertFalse(haveFiveStarsInRow.test("Xnot_rightZZZ"));
    assertTrue(haveFiveStarsInRow.test("XXsuperduperZZZ"));
    assertFalse(haveFiveStarsInRow.test("XXnotrighttooZZ"));
    assertTrue(haveFiveStarsInRow.test("one*two**three***four****five*****"));
    assertFalse(haveFiveStarsInRow.test("this**will**fail*"));
  }

  @Test
  public void chainOfFunctionInvocation() {
    List<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));

    //TODO: create realization with lambda
    Predicate<Integer> isEven = int1 -> int1 % 2 == 0;

    List<Integer> evenNumbersList = new ArrayList<>();

    for (Integer num : numbers) {
      if (isEven.test(num)){
        evenNumbersList.add(num);
      }
    }

    assertTrue(evenNumbersList.containsAll(Arrays.asList(2,4,6,8,10)));

    //TODO: create realization with lambda
    Function<List<Integer>, List<Integer>> sumWithNextElement = list -> {
      List processedList = new ArrayList();
      for (int i = 0; i < list.size(); i++) {
        if (i == list.size()-1) {
          processedList.add(list.get(i) + list.get(0));
        } else {
          processedList.add(list.get(i) + list.get(i + 1));
        }
      }
      return processedList;
    };

    List<Integer> newNumbers = sumWithNextElement.apply(evenNumbersList);

    assertTrue(newNumbers.containsAll(Arrays.asList(6,10,14,18,12)));

    //TODO: create realization with lambda
    Predicate<Integer> moreThanThirteen = num -> num > 13;

    List<Integer> numbersMoreThanFifteen = new ArrayList<>();

    for (Integer num : newNumbers) {
      if (moreThanThirteen.test(num)){
        numbersMoreThanFifteen.add(num);
      }
    }

    assertTrue(numbersMoreThanFifteen.containsAll(Arrays.asList(14,18)));
  }

  //TODO: implement predicate with lambda
  private static Predicate<String> lengthMoreThanSeven() {
    return str -> str.length() > 7;
  }

  //TODO: implement, if predicate correct "str" => "strstr" if not "str..."
  private static Function<Predicate<String>, Function<String, String>> doubleStringOrAddThreeDots() {
    return stringPredicate -> str -> {
      if (stringPredicate.test(str)) return str.concat(str);
      else return str.concat("...");
    };
  }

  //TODO: implement, return word length from function
  private static Function<String, Integer> lengthOfWord(
      Function<Predicate<String>, Function<String, String>> doubledOrWithThreeDots) {
    return str -> doubledOrWithThreeDots.apply(lengthMoreThanSeven()).apply(str).length();
  }

  @Test
  public void checkDeepCallBack() {
    assertFalse(lengthMoreThanSeven().test("no"));
    assertTrue(lengthMoreThanSeven().test("wonderful"));
    assertFalse(lengthMoreThanSeven().test("welcome"));


    assertEquals("no...", doubleStringOrAddThreeDots()
        .apply(lengthMoreThanSeven()).apply("no"));

    assertEquals("wonderfulwonderful", doubleStringOrAddThreeDots()
        .apply(lengthMoreThanSeven()).apply("wonderful"));

    assertEquals("welcome...", doubleStringOrAddThreeDots()
        .apply(lengthMoreThanSeven()).apply("welcome"));


    assertEquals(5, lengthOfWord(doubleStringOrAddThreeDots()).apply("no").intValue());
    assertEquals(18, lengthOfWord(doubleStringOrAddThreeDots()).apply("wonderful").intValue());
    assertEquals(10, lengthOfWord(doubleStringOrAddThreeDots()).apply("welcome").intValue());
  }

  private class LazyProperty {
    private Supplier<Integer> supplier;
    private Integer lazy;

    public LazyProperty(Supplier<Integer> supplier) {
      this.supplier = supplier;
    }

    //TODO: initialize "lazy" using "supplier" only one time
    public int getLazy() {
      if (this.lazy == null) { this.lazy = supplier.get(); }
      return this.lazy;
    }
  }

  @Test
  public void lazyLoading() {

    //TODO: provide supplier in constructor with lambda
    LazyProperty sut = new LazyProperty(() -> 1);
    LazyProperty sut_2 = new LazyProperty(() -> 2);
    LazyProperty sut_3 = new LazyProperty(() -> 3);

    assertNull(sut.lazy);
    assertNull(sut_2.lazy);
    assertNull(sut_3.lazy);

    assertEquals(1,sut.getLazy());
    assertEquals(2,sut_2.getLazy());
    assertEquals(3,sut_3.getLazy());

    assertEquals(1,sut.lazy.intValue());
    assertEquals(2,sut_2.lazy.intValue());
    assertEquals(3,sut_3.lazy.intValue());
  }

  @Test
  public void andThenTest() {
    //TODO: realize with lambda
    Function<Integer, Integer> sumIntegerOnSix = num -> num + 6;
    Function<Integer, Integer> thenMinusThree = num -> num - 3;
    Function<Integer, Integer> afterMultipleOnFive = num -> num * 5;

    Integer result = sumIntegerOnSix
        .andThen(thenMinusThree)
        .andThen(afterMultipleOnFive)
        .apply(10);

    assertEquals(65, result.intValue());
  }
}

