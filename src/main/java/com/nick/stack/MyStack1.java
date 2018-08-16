package com.nick.stack;

public class MyStack1<E> {
  Node<E> top;

  public void push(E e){
    Node<E> n = new Node<>(e);
    n.next = top;
    top = n;
  }

  public E pop(E e){
    if(top == null){
      return null;
    }
    top = top.next;
    return top.e;
  }
}
