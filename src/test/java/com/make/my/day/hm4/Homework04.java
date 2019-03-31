package com.make.my.day.hm4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class Homework04 {

  @Test
  public void collectToList() {
    String[] words = new String[]{"one", "two", "three"};

    List<String> result = Arrays.stream(words)
            .collect(ArrayList::new , ArrayList::add, ArrayList::addAll);

    assertArrayEquals(words, result.toArray());
  }

  @Test
  public void collectToSet() {
    String[] words = new String[]{"one", "one", "two", "two", "three"};

    Set<String> result = Arrays.stream(words)
            .collect(HashSet::new , Set::add, Set::addAll);

    assertArrayEquals(new String[]{"one", "two", "three"}, result.toArray());
  }

  @Test
  public void collectToMap() {
    String[] words = new String[]{"one", "one", "one", "two", "two", "three"};

    Map<String, Integer> result = Arrays.stream(words)
            .collect(Collectors.toMap(str -> str, str -> 1, (int1, int2) -> int1+int2));

    Map<String, Integer> expected = new HashMap<>();
    expected.put("one", 3);
    expected.put("two", 2);
    expected.put("three", 1);

    assertEquals(expected, result);
  }

  @Test
  public void collectingAndThen() {
    String[] words = new String[]{"one", "one", "one", "two", "two", "three"};

    List<String> result = Arrays.stream(words)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                    list -> list.stream().distinct().map(str -> str.concat(str)).collect(Collectors.toList())
            ));

    assertArrayEquals(new String[]{"oneone", "twotwo", "threethree"}, result.toArray());
  }

  @Test
  public void joining() {
    String[] words = new String[]{"Glass", "Steel", "Wood", "Stone"};

    String result = Arrays.stream(words)
            .collect(Collectors.joining(", ", "Materials[ ", " ]"));

    assertEquals("Materials[ Glass, Steel, Wood, Stone ]", result);
  }

  @Test
  public void groupingByWordsLength() {
    String[] words = new String[]{"one", "one", "one", "two", "two", "three"};

    Map<Integer, List<String>> result = Arrays.stream(words)
            .collect(Collectors.groupingBy(String::length));

    Map<Integer, List<String>> expected = new HashMap<>();
    expected.put(3, Arrays.asList("one", "one", "one", "two", "two"));
    expected.put(5, Arrays.asList("three"));

    assertEquals(expected, result);
  }


  private class Dog{
    private String name;
    private int age;

    public Dog(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Dog dog = (Dog) o;
      return age == dog.age &&
              Objects.equals(name, dog.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, age);
    }
  }


  @Test
  public void dogMappingByAge() {
    List<Dog> dogs = Arrays.asList(
            new Dog("Bim", 4), new Dog("Duke", 7), new Dog("Fenrir", 120),
            new Dog("Bim", 8), new Dog("Lucky", 6), new Dog("Duke", 13));

    Map<String, List<Integer>> result = dogs.stream()
            // TODO: Use here `groupingBy` plus `mapping`
            .sorted(Comparator.comparingInt(Dog::getAge))
            .collect(Collectors.groupingBy(Dog::getName, Collectors.mapping(Dog::getAge, Collectors.toList())));

    Map<String, List<Integer>> expected = new HashMap<>();
    expected.put("Bim", Arrays.asList(4, 8));
    expected.put("Duke", Arrays.asList(7, 13));
    expected.put("Fenrir", Arrays.asList(120));
    expected.put("Lucky", Arrays.asList(6));

    assertEquals(expected, result);
  }

  @Test
  public void partitionByEvenOdd() {
    List<Dog> dogs = Arrays.asList(
            new Dog("Bim", 4), new Dog("Duke", 7), new Dog("Fenrir", 120));

    //TODO: make your realization
    Map<Boolean, List<Dog>> result = dogs.stream()
            .collect(Collectors.partitioningBy(dog -> ((dog.getAge()&1) == 0)));

    Map<Boolean, List<Dog>> expected = new HashMap<>();
    expected.put(true, Arrays.asList(new Dog("Bim", 4), new Dog("Fenrir", 120)));
    expected.put(false, Arrays.asList(new Dog("Duke", 7)));

    assertEquals(expected, result);
  }

  private enum Role{

    ADMIN, USER, MANAGER
  }

  private class User{
    private String email;
    private Role role;

    public User(String email, Role role) {
      this.email = email;
      this.role = role;
    }

    public String getEmail() {
      return email;
    }

    public Role getRole() {
      return role;
    }
  }

  private class UserDTO{
    private String email;

    private List<Role> roles;

    public UserDTO(String email, List<Role> roles) {
      this.email = email;
      this.roles = roles;
    }

    public String getEmail() {
      return email;
    }

    public List<Role> getRoles() {
      return roles;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      UserDTO userDTO = (UserDTO) o;
      return Objects.equals(email, userDTO.email) &&
              Objects.equals(roles, userDTO.roles);
    }

    @Override
    public int hashCode() {
      return Objects.hash(email, roles);
    }

    @Override
    public String toString() {
      return "UserDTO{" +
              "email='" + email + '\'' +
              ", roles=" + roles +
              '}';
    }
  }


  @Test
  public void convertFromUserToUserDTOTest() {
    List<User> usersFromDB = Arrays.asList(
            new User("superman@epam.com", Role.ADMIN),
            new User("superman@epam.com", Role.USER),
            new User("superman@epam.com", Role.MANAGER),
            new User("someone@epam.com", Role.USER),
            new User("sonofsun@epam.com", Role.USER),
            new User("sonofsun@epam.com", Role.MANAGER)
    );


    //TODO: Make your realization
    Map<String, List<Role>> map = usersFromDB.stream()
            .collect( Collectors.toMap(User::getEmail, user -> new ArrayList<>(Arrays.asList(user.getRole())),
                    (list1, list2) -> Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList())));
    List<UserDTO> result = map.entrySet().stream()
            .map(str -> new UserDTO(str.getKey(), str.getValue()))
            .collect(Collectors.toList());

    List<UserDTO> expected = Arrays.asList(
            new UserDTO("someone@epam.com", Arrays.asList(Role.USER)),
            new UserDTO("sonofsun@epam.com", Arrays.asList(Role.USER, Role.MANAGER)),
            new UserDTO("superman@epam.com", Arrays.asList(Role.ADMIN, Role.USER, Role.MANAGER))
    );

    assertEquals(expected, result);
  }
}