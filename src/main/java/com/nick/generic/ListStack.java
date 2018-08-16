package com.nick.generic;

public class ListStack<AnyType> implements StackInterface<AnyType>
{
    private Node<AnyType> top;

 /**
   * Creates a Stack
   */
    public ListStack( )
    {
        top = null;
    }

    /**
     * Tests if the stack is empty.
     */
    public boolean isEmpty( )
    {
        return top == null;
    }

    /**
     * Make the stack logically empty.
     */
    public void clear( )
    {
        top = null;
    }

    /**
     * Inserts a new item into the stack.
     */
    public void push(AnyType data)
    {
        top = new Node<AnyType>(data, top);
    }

    /**
     *  Removes and returns the item at the top of this stack.
     */
    public AnyType pop( )
    {
        if(isEmpty( )) throw new StackException("Stack is empty");
        AnyType data = top.data;
        top = top.next;
        return data;
    }

    /**
     *  Returns the top item without its removal
     */
    public AnyType peek( )
    {
        if(isEmpty( ))  throw new StackException("Stack is empty");
        return top.data;
    }

    /**
     * Returns a string representation of the Stack.
     */
   public String toString()
   {
      if(isEmpty()) return "[ ]";

      StringBuffer out = new StringBuffer("[");
      Node<AnyType> tmp = top;
      while(tmp != null)
      {
         out.append(tmp.data + "  ");
         tmp = tmp.next;
      }

      out.append("]");
      return out.toString();
   }

   private static class Node<AnyType>
   {
      public AnyType data;
      public Node<AnyType> next;

      public Node(AnyType data)
      {
         this(data, null );
      }

      public Node(AnyType data, Node<AnyType> n)
      {
         this.data = data; next = n;
      }
   }

   public static void main(String[] args)
   {
      ListStack<Integer> s = new ListStack<Integer>();

      try
      {

         for(int i = 0; i < 6; i++) s.push(i);

         //s.clear();
         System.out.println(s);

         for(int i = 0; i < 5; i++) s.pop();

         System.out.println(s);

      }
      catch (StackException e)
      {
         System.err.println(e);
      }
   }
}


/**              StackInterface           **/

interface StackInterface<AnyType>
{
/**
* Tests if the stack is empty.
*/
public boolean isEmpty();

/**
*  Removes and returns the item at the top of this stack.
*/
public AnyType pop() throws StackException;

/**
*  Returns the top item without its removal
*/
public AnyType peek() throws StackException;

/**
* Inserts an item onto the top of the stack.
*/
public void push(AnyType e) throws StackException;

/**
* Removes all items from the stack.
*/
public void clear();
}


/**              StackException           **/


class StackException extends RuntimeException
{
public StackException(String name)
{
super(name);
}

public StackException()
{
super();
}
}