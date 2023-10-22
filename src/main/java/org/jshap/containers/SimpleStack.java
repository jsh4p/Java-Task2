package org.jshap.containers;

/**
 * Класс SimpleStack представляет собой реализацию стека, понадобится для работы с постфиксной формой
 * @param <T> тип данных элементов стека
 * @author jshap
 */
public class SimpleStack <T> {
    /**
     * Node - класс узлов, из которых состоит список
     */
    final private static class Node <T> {
        final private T data;
        private Node<T> next;

        /**
         * Конструктор с параметром
         * @param data данные, которые будут храниться в узле
         */
        private Node(T data) {
            this.data = data;
        }

        /**
         * Переопределенный метод toString класса Object
         * @return возвращает строку с информацией об элементе узла
         */
        @Override
        public String toString() {
            return data.toString();
        }
    }

    private Node<T> head;
    private int size;

    /**
     * Конструктор по умолчанию
     */
    public SimpleStack() {
        head = null;
        size = 0;
    }

    /**
     * Метод добавления элемента в стек
     * @param data данные, которые будут храниться в узле
     */
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

    /**
     * Метод получения значения, хранящегося на вершине стека
     * @return значение на вершине
     * @throws NullPointerException стек пустой
     */
    public T top() {
        if (head == null) {
            throw new NullPointerException();
        }

        return head.data;
    }

    /**
     * Метод удаления хранящегося на вершине стека значения
     * @return значение, которое мы удалили
     * @throws NullPointerException стек пустой
     */
    public T pop() {
      if (head == null) {
          throw new NullPointerException();
      }

      T returnVal = head.data;
      head = head.next;

      --size;

      return returnVal;
    }

    /**
     * Метод очистки стека
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Метод проверки стека на пустоту
     * @return булевое значение
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Метод получения размерности стека
     * @return размер стека
     */
    public int size() {
        return size;
    }

    /**
     * Переопределенный метод toString класса Object
     * @return возвращает строку с информацией об элементах стека
     */
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
