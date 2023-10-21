package org.jshap.containers;

public class SimpleStack <T> {
    final private static class Node <T> {
        final private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private Node<T> head;
    private int size;

    public SimpleStack() {
        head = null;
        size = 0;
    }

    public T top() {
        if (head == null) {
            throw new NullPointerException();
        }

        return head.data;
    }

    public void push(T data) {
        if (head == null) {
            head = new Node<>(data);
        } else {
            Node<T> newHead = new Node<>(data);
            newHead.next = head;
            head = newHead;
        }

        ++size;
    }

    public T pop() {
      if (head == null) {
          throw new NullPointerException();
      }

      T returnVal = head.data;
      head = head.next;

      --size;

      return returnVal;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public String toString() {
        String str = "[ ";

        SimpleStack<T> newStack = new SimpleStack<>();
        while (head != null) {
            str += "\n\t";
            newStack.push(this.pop());
            str += newStack.head.toString();
        }

        while (newStack.head != null) {
            this.push(newStack.pop());
        }

        str += "\n]";

        return str;
    }
}
