package org.jshap.containers;

//по-хорошему, вынести Node, либо наследовать список от стека

/**
 * Класс LinkedList представляет собой реализацию односвязного списка
 * @param <T> тип данных элементов списка
 * @author jshap
 */
public class LinkedList <T> {
    /**
     * Node - класс узлов, из которых состоит список
     */
    private static class Node <T> {
        private final T data;
        private Node<T> next;

        /**
         * Конструктор с параметром
         * @param data данные, которые будут храниться в узле
         */
        private Node(T data) {
            this.data = data;
            next = null;
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
    public LinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Метод добавления элемента в конец списка
     * @param data данные, которые будут храниться в узле
     */
    public void pushBack(T data) {
        if (head == null) {
            head = new Node<>(data);
        } else {
            Node<T> curNode = head;

            while (curNode.next != null) {
                curNode = curNode.next;
            }
            curNode.next = new Node<>(data);
        }

        ++size;
    }

    /**
     * Метод получения хранящегося в списке значения по индексу
     * @param ind индекс, по которому находится значение
     * @return значение найденного элемента
     * @throws IndexOutOfBoundsException при выходе за границы списка
     */
    public T at(int ind) {
        if (ind < 0 || ind > size - 1) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> curNode = head;
        int i = 0;

        while (i != ind) {
            curNode = curNode.next;
            ++i;
        }

        return curNode.data;
    }

    /**
     * Метод удаления хранящегося в списке значения по индексу
     * @param ind индекс, по которому находится значение
     * @throws IndexOutOfBoundsException при выходе за границы списка
     */
    public void remove(int ind) {
        if (ind < 0 || ind > size - 1) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> curNode = head;
        Node<T> prevNode = null;
        int i = 0;

        while (i != ind) {
            prevNode = curNode;
            curNode = curNode.next;
            ++i;
        }

        if (prevNode == null) {
            head = curNode.next;
        } else {
            prevNode.next = curNode.next;
            curNode.next = null;
        }

        --size;
    }

    /**
     * Метод очистки списка
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Метод проверки списка на пустоту
     * @return булевое значение
     */
    public boolean isEmpty() {
        return (head == null);
    }

    /**
     * Метод получения размерности списка
     * @return размер списка
     */
    public int size() {
        return size;
    }

    /**
     * Переопределенный метод toString класса Object
     * @return возвращает строку с информацией об элементах списка
     */
    @Override
    public String toString() {
        String str = "[ ";
        Node<T> curNode = head;

        while (curNode != null) {
            str += curNode.toString();
            if (curNode.next != null) {
                str += " -> ";
            }

            curNode = curNode.next;
        }

        str += " ]";

        return str;
    }
}
