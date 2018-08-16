package com.nick.stack;

import java.util.Stack;

public class MyQueue {

  Stack<Integer> s1 = new Stack<>();
  Stack<Integer> s2 = new Stack<>();

  public void add(int i){
    s1.add(i);
  }

  public int pop(){
    while(!s1.empty()){
      s2.push(s1.pop());
    }

    return s2.pop();
  }
}
