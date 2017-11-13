package com.example.abalik.start;

import java.util.Vector;


public class huffman {

    private static final int R = 256;
    private static String text;

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public static String compress(String s) {

        char[] input = s.toCharArray();

        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        Node root = buildTree(freq);

        String[] st = new String[R];
        buildCode(st, root, "");

        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    text = text + '0';
                } else if (code.charAt(j) == '1') {
                    text = text + '1';
                }
            }
        }
        return text;
    }

    private static Node buildTree(int[] freq) {
        Vector<Node> v = new Vector<Node>(1);

        for (char i = 0; i < R; i++) {
            if (freq[i] > 0)
                v.add(new Node(i, freq[i], null, null));
        }

        while (v.size() > 1) {
            int i_min1 = 0, i_min2 = 0, min1;

            min1 = v.get(0).freq;
            for (int i = 0; i < v.size(); i++) {

                if (min1 > v.get(i).freq) {

                    i_min2 = i_min1;
                    i_min1 = i;
                    min1 = v.get(i).freq;
                }
            }
            v.add(new Node('0', v.get(i_min1).freq + v.get(i_min2).freq, null, null));
            v.remove(i_min1);
            v.remove(i_min2);
        }
        return v.get(0);
    }

    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }
}

