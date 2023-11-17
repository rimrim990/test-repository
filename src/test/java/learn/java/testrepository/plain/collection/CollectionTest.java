package learn.java.testrepository.plain.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 자료구조 학습 테스트")
public class CollectionTest {

    @Test
    @DisplayName("2차원 배열 정렬")
    void sort_arr() {
        // given
        int[][] arr = {{2, 5}, {1,2}, {2, 4}};

        // when
        Arrays.sort(arr, (o1, o2) -> Arrays.compare(o1, o2));

        // then
        assertThat(arr).isDeepEqualTo(new int[][] {{1,2}, {2,4}, {2,5}});
    }

    @Test
    @DisplayName("컬렉션 역순 정렬")
    void sort_collection() {
        // given
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);

        // when
        list.sort(Comparator.reverseOrder());

        // then
        assertThat(list).containsExactly(2, 1);
    }


    @Test
    @DisplayName("자바 배열 동등성 비교")
    void arr_compare() {
        // given
        int[] a1 = {1, 2};
        int[] a2 = {1, 2};
        int[] a3 = {2, 3};

        // when & then
        assertThat(Arrays.equals(a1, a2)).isTrue();
        assertThat(Arrays.equals(a2, a3)).isFalse();
    }

    @Test
    @DisplayName("Deque 원소 삽입, 제거")
    void deque() {
        // given
        Deque<Integer> dq = new ArrayDeque<>();

        // when
        dq.push(1);
        dq.push(3);

        // then
        assertThat(dq.pop()).isEqualTo(3);
        assertThat(dq.pop()).isEqualTo(1);
    }

    @Test
    @DisplayName("클래스 정렬")
    void sort_customClass() {
        // given
        Node n1 = new Node(4, 2);
        Node n2 = new Node(2, 3);
        Node n3 = new Node(7, 2);

        List<Node> list = new ArrayList<>();
        list.add(n1);
        list.add(n2);
        list.add(n3);

        // when
        list.sort(Comparator.comparingInt((Node o) -> o.val1).
            thenComparingInt(o -> o.val2));

        // then
        assertThat(list).containsExactly(n2, n1, n3);
    }

    @Test
    @DisplayName("자바 최소 힙")
    void minHeap() {
        // given
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt((Node n) -> n.val1));

        // when
        pq.offer(new Node(3, 2));
        pq.offer(new Node(1, 2));

        // then
        assertThat(pq).containsExactly(new Node(1, 2), new Node(3, 2));
    }

    private static class Node {

        private int val1;
        private int val2;

        public Node(int val1, int val2) {
            this.val1 = val1;
            this.val2 = val2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return val1 == node.val1 && val2 == node.val2;
        }

        @Override
        public String toString() {
            return "Node{" +
                "val1=" + val1 +
                ", val2=" + val2 +
                '}';
        }
    }
}
