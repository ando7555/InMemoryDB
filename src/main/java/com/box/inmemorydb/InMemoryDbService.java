package com.box.inmemorydb;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class InMemoryDbService {


  private final Map<String, String> db = new HashMap<String, String>();
  private final TreeMap<String, Integer> valueCounts = new TreeMap<String, Integer>();

  private final Deque<Map<String, String>> transactionStack = new ArrayDeque<>();

  public void set(String name, String value) {
    if (!transactionStack.isEmpty()) {
      Map<String, String> currentTransaction = transactionStack.peek();
      currentTransaction.put(name, value);
    } else {
      String oldValue = db.get(name);
      if (oldValue != null) {
        decrementCount(oldValue);
      }
      db.put(name, value);
      incrementCount(value);
    }
  }

  public String get(String name) {
    for (Map<String, String> transaction : transactionStack) {
      if (transaction.containsKey(name)) {
        return transaction.get(name);
      }
    }
    return db.getOrDefault(name, "NULL");
  }

  public void delete(String name){
    String currentArgument = db.remove(name);
    if(currentArgument != null){
       decrementCount(currentArgument);
    }
  }

  public int count(String value){
    return valueCounts.getOrDefault(value, 0);
  }

  public void begin(){
    transactionStack.push(new HashMap<>());
  }

  public void rollback(){
    if (!transactionStack.isEmpty()) {
      Map<String, String> lastTransaction = transactionStack.pop();
      for (Map.Entry<String, String> entry : lastTransaction.entrySet()) {
        if (entry.getValue() == null) {
          db.remove(entry.getKey());
        } else {
          db.put(entry.getKey(), entry.getValue());
        }
      }
    } else {
      System.out.println("NO TRANSACTION");
    }
  }

  public void commit(){
    if (transactionStack.isEmpty()){
      System.out.println("NO TRANSACTION");

    }else {
      while (!transactionStack.isEmpty()){
        Map<String, String> transactionMap = transactionStack.pop();
        for ( Map.Entry<String, String> entry : transactionMap.entrySet()) {
          set(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  private void incrementCount(String value){
    if (value != null) {
      valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
    }
  }

  private void decrementCount(String value){
    if (value != null) {
      int count = valueCounts.getOrDefault(value, 0);
      if (count == 1) {
        valueCounts.remove(value);
      } else if (count > 1) {
        valueCounts.put(value, count - 1);
      }
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    InMemoryDbService inMemoryDbService = new InMemoryDbService();
    while (scanner.hasNext()){
      String line = scanner.nextLine();
      String [] parts = line.split(" ");

      String command = parts[0].toUpperCase();

      switch (command){
        case "SET":
          inMemoryDbService.set(parts[1], parts[2]);
          break;
        case "GET":
          System.out.println(inMemoryDbService.get(parts[1]));
          break;
        case "DELETE":
          inMemoryDbService.delete(parts[1]);
          break;
        case "COUNT":
          System.out.println(inMemoryDbService.count(parts[1]));
          break;
        case "BEGIN":
          inMemoryDbService.begin();
          break;
        case "ROLLBACK":
          inMemoryDbService.rollback();
          break;
          case "COMMIT":
          inMemoryDbService.commit();
          break;
        case "END":
          return;
        default:
          System.out.println("Unknown command");
          break;
      }
    }
    scanner.close();
  }
}
