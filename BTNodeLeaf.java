import org.hamcrest.core.IsInstanceOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class BTNodeLeaf extends BTNode
{
   public ArrayList<Integer> keyCounts;
   public BTNodeLeaf nextLeaf;
   public BTNodeLeaf prevLeaf;
   
   public BTNodeLeaf()
   {
      this.keyCounts = new ArrayList<>();
      this.keys = new ArrayList<>();
      this.nextLeaf = null;
   }
   
   public void insert(String word, BPlusTree tree)
   {
       //increment key count if root is in keys
       if (this.keys.contains(word)) {
           int newValue = this.keyCounts.get(this.keys.indexOf(word)) + 1;
           this.keyCounts.set(this.keys.indexOf(word), newValue);

       } else {
           //insert key into node
           this.keys.add(word);
           this.sortKeys(this.keys);
           this.keyCounts.add(this.keys.indexOf(word), 1);

           //check if the node has reached threshold size
           if (this.keys.size() > BTNode.SIZE) {
               //we want to execute this code when the root is a leaf node (aka this node)
               if (this.parent == null) {
                    //create a new Root that is an internal node and add the split node to the root
                    this.newRootSetup(tree);
                } else {
                   //if it's not a root node then we want to copy the split node up to the parent (if it's not full)
                   this.setupLeafsWithExistingParent(tree);
               }
           }
       }
   }

   private void newRootSetup(BPlusTree tree) {
       BTNodeInternal newRoot = new BTNodeInternal();

       int keyToMove = (this.keys.size()/2);
       newRoot.keys.add(this.keys.get(keyToMove));
       //create left leaf node of new root
       BTNodeLeaf leftNode = new BTNodeLeaf();
       addKeys(0,keyToMove,leftNode);
       leftNode.parent = newRoot;

       //create right leaf node of new root
       BTNodeLeaf rightNode = new BTNodeLeaf();
       addKeys(keyToMove, this.keys.size(), rightNode);
       rightNode.parent = newRoot;

       leftNode.nextLeaf = rightNode;
       rightNode.prevLeaf = leftNode;

      if (this.prevLeaf != null) {
          this.prevLeaf.nextLeaf = leftNode;
          leftNode.prevLeaf = this.prevLeaf;
       }

       if (this.nextLeaf != null) {
           rightNode.nextLeaf = this.nextLeaf;
           this.nextLeaf.prevLeaf = rightNode;
       }

       newRoot.children.add(leftNode);
       newRoot.children.add(rightNode);

       tree.root = newRoot;
   }

   private void setupLeafsWithExistingParent(BPlusTree tree) {
       int keyToMove = (this.keys.size()/2);
       this.parent.keys.add(this.keys.get(keyToMove));
       this.sortKeys(this.parent.keys);

       BTNodeLeaf newNode1 = new BTNodeLeaf();
       addKeys(0,keyToMove,newNode1);
       //create right leaf node of new root
       BTNodeLeaf newNode2 = new BTNodeLeaf();
       addKeys(keyToMove, this.keys.size(), newNode2);

       newNode1.parent = this.parent;
       newNode2.parent = this.parent;

       newNode1.nextLeaf = newNode2;
       newNode2.prevLeaf = newNode1;

      if (this.prevLeaf != null) {
           this.prevLeaf.nextLeaf = newNode1;
           newNode1.prevLeaf = this.prevLeaf;
       }

       if (this.nextLeaf != null) {
           newNode2.nextLeaf = this.nextLeaf;
           this.nextLeaf.prevLeaf = newNode2;
       }

       int index = this.parent.keys.indexOf(this.keys.get(keyToMove));
       this.parent.children.add(index, newNode1);
       this.parent.children.add(index+1, newNode2);
       this.parent.children.remove(this);

       //this.rebuildLeafPointers(this.parent.children);

   }

   public void rebuildLeafPointers(ArrayList<BTNode> leafList) {
       for (int i = 0; i  < leafList.size(); i++) {
           if (i != leafList.size()-1) {
               BTNodeLeaf cur = (BTNodeLeaf) leafList.get(i);
               BTNodeLeaf next = (BTNodeLeaf) leafList.get(i+1);
               cur.nextLeaf = next;
               next.prevLeaf = cur;
           }
       }
   }

   private void addKeys(int start, int end, BTNodeLeaf leaf) {
       for (int i = start; i < end ; i++) {
           leaf.keys.add(this.keys.get(i));
           leaf.keyCounts.add(this.keyCounts.get(i));
       }
   }

   public void printLeavesInSequence()
   {
      StringBuilder keys = new StringBuilder();
      BTNodeLeaf cur = this;
      while (cur != null) {
          for (int i = 0; i < cur.keys.size(); i++) {
              keys.append(cur.keys.get(i) + "\n");
          }
          cur = cur.nextLeaf;
      }
      System.out.println(keys);
   }

   public void printStructureWKeys()
   {
       int level = 0;
       BTNodeLeaf leafNode = this;
       printSpaces(level);
       System.out.println("Leaf Node: ");
       for (int i = 0; i < leafNode.keys.size(); i++) {
           printSpaces(level + 2);
           System.out.println("Key: " + leafNode.keys.get(i) + ", Count: " + leafNode.keyCounts.get(i));
       }
   }

    private void printSpaces(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print("  "); // Adjust spacing as needed
        }
    }
   public void sortKeys(ArrayList<String> keys) {
      Collections.sort(keys);
   }
   public int findInsertIndex(String word) {
      return 0;
   }

   public Boolean rangeSearch(String startWord, String endWord)
   {
      StringBuilder keys = new StringBuilder();
      BTNodeLeaf cur = this;
      while (cur != null) {
          for (int i = 0; i < cur.keys.size(); i++) {
              if (cur.keys.get(i).equals(endWord) || cur.keys.get(i).compareTo(endWord) > 0) {
                  keys.append(cur.keys.get(i) + "\n");
                  System.out.println(keys );
                  return true;
              }
              keys.append(cur.keys.get(i) + "\n");
          }
          cur = cur.nextLeaf;
      }
      return false;
   }
   
   public Boolean searchWord(String word){

       BTNodeLeaf cur = this;
       while (cur != null) {
           for (int i = 0; i < cur.keys.size(); i++) {
               if (cur.keys.get(i).equals(word)) {
                   System.out.println(cur.keys.get(i)+ ", " + cur.keyCounts.get(i));
                   return true;
               }
           }
           cur = cur.nextLeaf;
       }
       System.out.println("Could not find the desired word!");
       return false;
   }


}