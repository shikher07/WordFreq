

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignmentGMO {

    TrieNode root; // global variable defining the root of the trie structure

    class MaxHeap{
        int size;    // capacity of the heap(int this case it's 20)
        int currIndex; // denoting the current available index in heap to insert next Node
        Node[] heap;   // implementing heap of nodes through array(all the  process will take place in this)
        MaxHeap(int size){    // contructor
            this.size=size;
            currIndex=0;      // initial available index will always be 0(empty heap)
            heap=new Node[size];
        }

        public void insert(TrieNode temp, int index, String word) {  // inserting node in a heap
            if (index != -1) {     // checking if node already exists
                heap[index].val = temp.freq;
                heap[index].pointer = temp;
                siftUp(index);
            } else {
                if(currIndex<size){  //  checking if space is availble in current heap
                    heap[currIndex]=new Node(temp.freq, temp,word ); // directly inserting in free space
                    heap[currIndex].pointer.index = currIndex;
                    siftUp(currIndex); // to get to its right priority place in heap
                    currIndex++; // incrementing index for next element
                }
                else {
                    int min=temp.freq;
                    int minIndex=-1;
                    for (int k = (size / 2); k < size; k++) {  // to calculate min value node(less than temp.freq)
                        if(heap[k].val<min){
                            min=heap[k].val;
                            minIndex=k;
                        }
                    }
                    if (minIndex!=-1) {  // if min value exists than we need to replace it with current node
                        heap[minIndex].pointer.index=-1;
                        heap[minIndex] = new Node(temp.freq, temp, word);
                        siftUp(minIndex);
                    }
                }

            }
        }

        private void siftUp(int i) {                       // shifting the node up in the heap according to node.val
            int parent = i%2==0? (i/2)-1 : i/2 ;   // calculating parent node
            if(parent>=0 && heap[parent].val<heap[i].val){   // checking if swap is needed
                heap[parent].pointer.index=i;
                heap[i].pointer.index=parent;
                Node temp = heap[parent];       // swaping the nodes(shifting up)
                heap[parent]=heap[i];
                heap[i]=temp;

                siftUp(parent);     // recursively repeating the function till the right pos is reached
                return;
            }
            heap[i].pointer.index=i;   // updating index in the trie of this particular node
        }

        class Node implements Comparable<Node> {   // individual Nodes of the heap
            int val;    // frequency of the word in this node(comparing factor)
            TrieNode pointer;  // pointing the trieNode of the word's pos in the trie
            String word;    // current word in this node
            Node(int freq, TrieNode temp, String word){
                this.val=freq;
                this.pointer=temp;
                this.word=word;
            }

            @Override
            public int compareTo(Node o) {
                return o.val - this.val;
            }
        }
    } // MaxHeap class end

    class TrieNode{   // constituting the nodes of the trie structure
        int index;    // pointing to its index in the heap
        TrieNode[] map; // mapping to other TrieNodes linked to it
        int freq;   // storing the count of occurrences
        boolean isEnd;  // marking the end of word
        TrieNode(){
            index=-1;   // initial index before inserting in the heap
            map=new TrieNode[26];
            isEnd=false;
            freq=0;  // intial count is 0
        }
    }   // end of TrieNode class

    void printS(String[] doc, int k){  // for printing the words

        if(doc.length == 0){
            System.out.println("File is Empty !!");
            return;
        }

        MaxHeap mHeap = new MaxHeap(k);  // creating MaxHeap class with size K
        buildTrieHeap(doc, mHeap);  // building and executing trie and MaxHeap together
        Arrays.sort(mHeap.heap);    // sorting heap according to its node.val using comparator
        for(int i=0;i<k;i++){
            if(mHeap.heap[i]==null)
                break;
            System.out.println(mHeap.heap[i].word + " "+ mHeap.heap[i].val);
        }
    }
    void buildTrieHeap(String[] doc, MaxHeap mHeap){
        int n=doc.length;
        root=new TrieNode();   // defining the root of trie
        for(int i=0;i<n;i++){   // inserting each word in trie
            String a = doc[i];
            insert(a,mHeap);
        }
    }
    void insert(String a, MaxHeap mHeap){
        TrieNode temp=root;  // initially always start with the root
        int len  = a.length();
        for(int i=0;i<len;i++){
            char c = a.charAt(i);
            //System.out.println(c);
            if(temp.map[c-'a']==null){   // checking if the particular node already exist
                temp.map[c-'a']=new TrieNode();
            }
            temp=temp.map[c-'a'];   // going to that particular node
        }
        temp.isEnd=true;    // marking the end of word
        temp.freq++;       // incrementing its frequency
        mHeap.insert(temp, temp.index,a);   // inserting in heap
    }

    public static void main(String args[]) throws IOException{
        long time1 = System.currentTimeMillis();
        int k = 20;
        FileInputStream fis = null;
        List<String> ls = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        AssignmentGMO ob = new AssignmentGMO();

        try{
            fis = new FileInputStream(args[0]);
            int c;
            while((c=fis.read()) != -1){
                if((c>=65 && c<=90) || (c>=97 && c<=122)){
                    sb.append((char)c);
                }
                else {
                    if(sb.length()>0){
                        ls.add(sb.toString().toLowerCase());
                        sb.delete(0,sb.length());
                    }
                }
            }
            //System.out.print(ls);
        }catch(Exception e){
            System.out.println("Error while reading: "+ e.getLocalizedMessage());
        }

        int listSize=ls.size();
        String[] doc= new String[listSize];
        for(int i=0;i<listSize;i++){
            doc[i]=ls.get(i);
        }
        ob.printS(doc,k);
        long time2 = System.currentTimeMillis();
        System.out.println("Total Time taken in seconds: " + (double)(time2-time1)/100);
    }


}
