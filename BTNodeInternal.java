import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BTNodeInternal extends BTNode
{
   public ArrayList<BTNode> children;
   
   public BTNodeInternal()
   {
      this.children = new ArrayList<>();
      this.keys = new ArrayList<>();
   }
   
   public void insert(String key, BPlusTree tree)
   {
      /*int index = this.findInsertIndex(key);
      BTNodeLeaf child = (BTNodeLeaf) this.children.get(index);
      child.insert(key, tree);*/
      if (wordExists(key, this)) {
         BTNodeLeaf leafToInc = this.getLeaf(key, this);
         leafToInc.keyCounts.set(leafToInc.keys.indexOf(key), leafToInc.keyCounts.get(leafToInc.keys.indexOf(key)) + 1);
      } else {
         this.insertLeaf(key, tree.root, tree);
      }



      //next we have to fix the tree
      //this.fixBPlusTree(tree);

   }

   private Boolean insertLeaf(String key, BTNode root, BPlusTree tree) {
      if (root instanceof  BTNodeInternal) {
         int index = findInsertIndex(key, root);
         return this.insertLeaf(key, ((BTNodeInternal) root).children.get(index), tree);
      } else{
         root.insert(key, tree);
         //fix parent, recursive return to parent to check if parent needs to be fixed
         this.fixBPlusTree(root.parent, tree);
         return true;
      }

   }

   //fix internal node
   private Boolean fixBPlusTree(BTNode root, BPlusTree tree) {
      //if root, do fixing here
      if (root.keys.size() > BTNode.SIZE) {
         if (root.equals(tree.root)) {
            //create a new root node
            this.fixRoot(tree);
            return true;

         } else {
            //if current root is not the root of the tree, do an internal node fix
            BTNodeInternal internalNode = (BTNodeInternal) root;
            internalNode.fixInternal(tree);
            return fixBPlusTree(root.parent, tree);
         }
      }
      return true;
   }

   public void fixInternal(BPlusTree tree) {
      int splitKey = this.keys.size()/2;
      //add split key to new root and remove it from the current root
      this.parent.keys.add(this.keys.get(splitKey));
      Collections.sort(this.parent.keys);
      String keyToTrack = this.keys.remove(splitKey);
      splitKey = this.keys.size()/2; //get updated split key for leafs
      //create 2 new internal nodes and split the remaining keys into them
      BTNodeInternal newNode1 = new BTNodeInternal();
      BTNodeInternal newNode2 = new BTNodeInternal();
      this.addKeys(0, splitKey, newNode1);
      this.addKeys(splitKey+1, this.keys.size()-1, newNode2);

      //need to add root's leaf nodes
      BTNodeInternal root = (BTNodeInternal) tree.root;
      if (this.children.get(0) instanceof  BTNodeLeaf) {
         this.addLeafs(newNode1);
         this.addLeafs(newNode2);
      } else {
         this.addInternalNodes(newNode1);
         this.addInternalNodes(newNode2);
      }


      newNode1.parent = this.parent;
      newNode2.parent = this.parent;

      int index = this.parent.keys.indexOf(keyToTrack);
      this.parent.children.add(index, newNode1);
      this.parent.children.add(index+1, newNode2);
      this.parent.children.remove(this);

   }

   public void fixRoot(BPlusTree tree) {
      BTNodeInternal newRoot = new BTNodeInternal();
      //find the key to split on
      int splitKey = this.keys.size()/2;
      //add split key to new root and remove it from the current root
      newRoot.keys.add(this.keys.remove(splitKey));
      splitKey = this.keys.size()/2; //get updated split key for leafs
      //create 2 new internal nodes and split the remaining keys into them
      BTNodeInternal leftNode = new BTNodeInternal();
      BTNodeInternal rightNode = new BTNodeInternal();
      this.addKeys(0, splitKey, leftNode);
      this.addKeys(splitKey+1, this.keys.size()-1, rightNode);
      //need to add root's leaf nodes
      BTNodeInternal root = (BTNodeInternal) tree.root;
      if (root.children.get(0) instanceof  BTNodeLeaf) {
         this.addLeafs(leftNode);
         this.addLeafs(rightNode);
      } else {
         this.addInternalNodes(leftNode);
         this.addInternalNodes(rightNode);
      }


      leftNode.parent = newRoot;
      rightNode.parent = newRoot;

      newRoot.children.add(leftNode);
      newRoot.children.add(rightNode);

      this.parent = newRoot;
   }

   public int findInsertIndex(String word) {
      for (int i = 0; i <= this.keys.size(); i++) {

         if (i == this.keys.size()) {
            return i;
         }

         if (word.compareTo(this.keys.get(i)) <= 0) {
            return i;
         }

      }

      return -1;
   }

   private void addKeys(int start, int end, BTNodeInternal node) {
      for (int i = start; i <= end ; i++) {
         node.keys.add(this.keys.get(i));
      }
   }

   private void addInternalNodes(BTNodeInternal node) {
      for (int i = 0; i <= node.keys.size(); i++) {
         BTNodeInternal child = (BTNodeInternal) this.children.removeFirst();
         child.parent = node;
         node.children.add(child);
      }
   }

   private void addLeafs(BTNodeInternal node) {
      for (int i = 0; i <= node.keys.size(); i++) {
         BTNodeLeaf child = (BTNodeLeaf) this.children.removeFirst();
         child.parent = node;
         node.children.add(child);
      }
   }

   public int findInsertIndex(String word, BTNode root) {
      for (int i = 0; i <= root.keys.size(); i++) {

         if (i == root.keys.size()) {
            return i;
         }

         if (word.compareTo(root.keys.get(i)) <= 0) {
            return i;
         }
      }
      return -1;
   }

   private BTNode traverseToFirstLeaf(BTNode root) {
      if (root instanceof BTNodeInternal) {
         return this.traverseToFirstLeaf(((BTNodeInternal) root).children.get(0));
      } else{
         return root;
      }

   }

   public void printLeavesInSequence()
   {
      BTNodeLeaf firstLeaf = (BTNodeLeaf) this.traverseToFirstLeaf(this);
      firstLeaf.printLeavesInSequence();
   }
   
   public void printStructureWKeys()
   {

         printNode(this, 0);

   }

   private BTNodeLeaf getLeaf (String word, BTNode root) {
      BTNodeLeaf firstLeaf = (BTNodeLeaf) this.traverseToFirstLeaf(root);
      BTNodeLeaf cur = firstLeaf;
      while (cur != null) {
         for (int i = 0; i < cur.keys.size(); i++) {
            if (cur.keys.get(i).equals(word)) {
               return cur;
            }
         }
         cur = cur.nextLeaf;
      }
      return null;
   }

   public Boolean rangeSearch(String startWord, String endWord)
   {
      this.traverseToWord(this,startWord,endWord);

      return true;
   }

   private boolean traverseToWord(BTNode root, String startWord, String endWord) {
      if (root instanceof  BTNodeInternal) {
         int index = findInsertIndex(startWord, root);
         return this.traverseToWord(((BTNodeInternal) root).children.get(index), startWord, endWord);
      } else{
         root.rangeSearch(startWord, endWord);
         return true;
      }
   }


   private void printNode(BTNode node, int level) {
      if (node instanceof BTNodeInternal) {
         BTNodeInternal internalNode = (BTNodeInternal) node;

         for (int i = 0; i < internalNode.keys.size(); i++) {
            printNode(internalNode.children.get(i), level + 2);
            printSpaces(level);
            System.out.println("Key: " + internalNode.keys.get(i));
         }
         printNode(internalNode.children.get(internalNode.children.size() - 1), level + 2);

      } else if (node instanceof BTNodeLeaf) {

         BTNodeLeaf leafNode = (BTNodeLeaf) node;
         printSpaces(level);
         System.out.println("Leaf Node: ");
         for (int i = 0; i < leafNode.keys.size(); i++) {
            printSpaces(level + 2);
            System.out.println("Key: " + leafNode.keys.get(i) + ", Count: " + leafNode.keyCounts.get(i));
         }
      }
   }

   private void printSpaces(int count) {
      for (int i = 0; i < count; i++) {
         System.out.print("  "); // Adjust spacing as needed
      }
   }

   public Boolean wordExists(String word, BTNode root) {
      BTNodeLeaf firstLeaf = (BTNodeLeaf) this.traverseToFirstLeaf(this);
      BTNodeLeaf cur = firstLeaf;
      while (cur != null) {
         for (int i = 0; i < cur.keys.size(); i++) {
            if (cur.keys.get(i).equals(word)){
               return true;
            }
         }
         cur = cur.nextLeaf;
      }
      return false;
   }

   public Boolean searchWord(String word)
   {
      BTNodeLeaf firstLeaf = (BTNodeLeaf) this.traverseToFirstLeaf(this);
      if (firstLeaf.searchWord(word)) {
         return true;
      }

      return false;
   }
}