import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BTNodeInternalTests {

    private BPlusTree BPlusTree;

    @BeforeEach
    void setup(){
        BPlusTree = new BPlusTree(null);
        BPlusTree.insertWord("A");
        BPlusTree.insertWord("C");
        BPlusTree.insertWord("B");
        BPlusTree.insertWord("B");
        BPlusTree.insertWord("D");
        BPlusTree.insertWord("E");
        BPlusTree.insertWord("E");
        BPlusTree.insertWord("F");
        BPlusTree.insertWord("G");
        BPlusTree.insertWord("H");
        BPlusTree.insertWord("H");
        BPlusTree.insertWord("I");
        BPlusTree.insertWord("Blue");
        BPlusTree.insertWord("Doctor");
        BPlusTree.insertWord("flame");
        BPlusTree.insertWord("flame");
        BPlusTree.insertWord("Fan");

    }

    @Nested
    @DisplayName("First Split of Internal Node")
    class FirstSplitNodeTests {
        @Test
        @DisplayName("Checking that Root has Correct Keys in Root")
        void Insert_13thKey_CorrectRootKeys() {
            assertEquals(List.of("fan"), BPlusTree.root.keys);

        }

        @Test
        @DisplayName("Checking that Root has Two Children")
        void Insert_13thKey_TwoChildrenInRoot() {
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            assertEquals(2, internalRoot.children.size());

        }

        @Test
        @DisplayName("Checking Internal Child Nodes have Root as Parent")
        void Insert_13thKey_InternalNodeRootAsAParent(){
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            BTNodeInternal firstChild = (BTNodeInternal) internalRoot.children.get(0);
            BTNodeInternal secondChild = (BTNodeInternal) internalRoot.children.get(1);

            assertEquals(BPlusTree.root, firstChild.getParent());
            assertEquals(BPlusTree.root, secondChild.getParent());

        }

        @Test
        @DisplayName("Checking Root's First Child Keys for Correctness")
        void Insert_13thKey_CorrectKeysInFirstChild() {
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            assertEquals(List.of("c", "e"), internalRoot.children.get(0).keys);

        }

        @Test
        @DisplayName("Checking Root's Second Child Key for Correctness")
        void Insert_13thKey_CorrectKeysInSecondChild() {
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            assertEquals(List.of("g"), internalRoot.children.get(1).keys);

        }

        @Test
        @DisplayName("Checking Root's First Child's Children")
        void Insert_13thKey_FirstInternalChildCorrectChildrenCount() {
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            BTNodeInternal firstChild = (BTNodeInternal) internalRoot.children.get(0);
            BTNodeLeaf firstLeafChild = (BTNodeLeaf) firstChild.children.get(0);
            BTNodeLeaf secondLeafChild = (BTNodeLeaf) firstChild.children.get(1);
            BTNodeLeaf thirdLeafChild = (BTNodeLeaf) firstChild.children.get(2);

            assertEquals(3, firstChild.children.size());
            assertEquals(List.of("a", "b", "blue"), firstLeafChild.keys);
            assertEquals(List.of("c", "d", "doctor"), secondLeafChild.keys);
            assertEquals(List.of("e", "f"), thirdLeafChild.keys);

        }

        @Test
        @DisplayName("Checking Root's Second Child's Children")
        void Insert_13thKey_SecondInternalChildCorrectChildrenCount() {
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            BTNodeInternal secondChild = (BTNodeInternal) internalRoot.children.get(1);
            BTNodeLeaf firstLeafChild = (BTNodeLeaf) secondChild.children.get(0);
            BTNodeLeaf secondLeafChild = (BTNodeLeaf) secondChild.children.get(1);

            assertEquals(List.of("fan", "flame"), firstLeafChild.keys);
            assertEquals(List.of("g", "h", "i"), secondLeafChild.keys);

        }

        @Test
        @DisplayName("Checking nextLeaf Links have been Preserved")
        void Insert_13thKey_PreservesNextLeafLinks(){
            BTNodeInternal internalRoot = (BTNodeInternal) BPlusTree.root;
            BTNodeInternal firstChild = (BTNodeInternal) internalRoot.children.get(0);
            BTNodeLeaf firstLeafChild = (BTNodeLeaf) firstChild.children.get(0);
            int nextLeafHops = 0;

            while(firstLeafChild.nextLeaf != null){
                nextLeafHops++;
                firstLeafChild = firstLeafChild.nextLeaf;

            }

            assertEquals(4, nextLeafHops);

        }
    }

    @Nested
    @DisplayName("Checking 14th Key Insertion")
    class FourteenthElementInsert{
        @BeforeEach
        void setup(){
            BPlusTree.insertWord("Fan");
            BPlusTree.insertWord("brink");

        }

        @Test
        @DisplayName("Checking First Root's Child's Children for Correctness")
        void Insert_14thKey_CorrectlySplitNodes(){
            BTNodeInternal rootInternal = (BTNodeInternal) BPlusTree.root;
            BTNodeInternal firstRootChild = (BTNodeInternal) rootInternal.children.get(0);
            BTNodeLeaf firstLeafChild = (BTNodeLeaf) firstRootChild.children.get(0);
            BTNodeLeaf secondLeafChild = (BTNodeLeaf) firstRootChild.children.get(1);
            BTNodeLeaf thirdLeafChild = (BTNodeLeaf) firstRootChild.children.get(2);

            assertEquals(List.of("a","b"), firstLeafChild.keys);
            assertEquals(List.of("blue","brink"), secondLeafChild.keys);
            assertEquals(List.of("c","d","doctor"), thirdLeafChild.keys);
            assertEquals(4, firstRootChild.children.size());
            assertEquals(List.of("blue","c","e"), firstRootChild.keys);

        }
    }

    @Nested
    @DisplayName("Checking 15th, 16th and, 18th Key Insertion")
    class InternalSplitExistingRoot{
        @BeforeEach
        void setup(){
            BPlusTree.insertWord("brink");
            BPlusTree.insertWord("album");
            BPlusTree.insertWord("And");

        }

        @Test
        @DisplayName("Checking First Root's Child's Children for Correctness")
        void Insert_18thKey_CorrectlySplitNodes(){
            BTNodeInternal rootInternal = (BTNodeInternal) BPlusTree.root;
            BTNodeInternal firstRootChild = (BTNodeInternal) rootInternal.children.get(0);
            BTNodeLeaf firstLeafChild = (BTNodeLeaf) firstRootChild.children.get(0);
            BTNodeLeaf secondLeafChild = (BTNodeLeaf) firstRootChild.children.get(1);

            assertEquals(List.of("a","album"), firstLeafChild.keys);
            assertEquals(List.of("and","b"), secondLeafChild.keys);

            assertEquals(List.of("and","blue"),firstRootChild.keys);

            assertEquals(List.of("c","fan"),BPlusTree.root.keys);

            assertEquals(3, ((BTNodeInternal) BPlusTree.root).children.size());
        }
    }

    @Nested
    @DisplayName("Create B+ Trees from Set of Keys")
    class BPLusTreeConstruction{

        private BPlusTree constructedTree;

        @BeforeEach
        void setup(){
            constructedTree = new BPlusTree(null);
            constructedTree.insertWord("index");
            constructedTree.insertWord("A");
            constructedTree.insertWord("B");
            constructedTree.insertWord("D");
            constructedTree.insertWord("E");
            constructedTree.insertWord("K");
            constructedTree.insertWord("J");
            constructedTree.insertWord("I");
            constructedTree.insertWord("H");
            constructedTree.insertWord("W");
            constructedTree.insertWord("Z");
            constructedTree.insertWord("P");
            constructedTree.insertWord("R");
            constructedTree.insertWord("V");
            constructedTree.insertWord("C");
            constructedTree.insertWord("O");
            constructedTree.insertWord("G");
            constructedTree.insertWord("N");
            constructedTree.insertWord("F");
        }

        @Test
        @DisplayName("Checking All Leaf Node's Keys for Correctness")
        void LeafNodes_CorrectValues(){
            BTNodeInternal childOne = (BTNodeInternal) ((BTNodeInternal) constructedTree.root).children.get(0);
            BTNodeInternal childTwo = (BTNodeInternal) ((BTNodeInternal) constructedTree.root).children.get(1);
            BTNodeInternal childThree = (BTNodeInternal) ((BTNodeInternal) constructedTree.root).children.get(2);

            BTNodeLeaf leafOne = (BTNodeLeaf) childOne.children.get(0);
            BTNodeLeaf leafTwo = (BTNodeLeaf) childOne.children.get(1);
            BTNodeLeaf leafThree = (BTNodeLeaf) childOne.children.get(2);
            BTNodeLeaf leafFour = (BTNodeLeaf) childOne.children.get(3);
            BTNodeLeaf leafFive = (BTNodeLeaf) childTwo.children.get(0);
            BTNodeLeaf leafSix = (BTNodeLeaf) childTwo.children.get(1);
            BTNodeLeaf leafSeven = (BTNodeLeaf) childTwo.children.get(2);
            BTNodeLeaf leafEight = (BTNodeLeaf) childThree.children.get(0);
            BTNodeLeaf leafNine = (BTNodeLeaf) childThree.children.get(1);

            assertEquals(List.of("a","b","c"), leafOne.keys);
            assertEquals(List.of("d","e"), leafTwo.keys);
            assertEquals(List.of("f","g"), leafThree.keys);
            assertEquals(List.of("h","i"), leafFour.keys);
            assertEquals(List.of("index","j"), leafFive.keys);
            assertEquals(List.of("k","n"), leafSix.keys);
            assertEquals(List.of("o","p"), leafSeven.keys);
            assertEquals(List.of("r","v"), leafEight.keys);
            assertEquals(List.of("w","z"), leafNine.keys);
            
        }

        @Test
        @DisplayName("Checking All Internal Node's Keys for Correctness")
        void InternalNodes_CorrectKeys(){
            BTNodeInternal childOne = (BTNodeInternal) ((BTNodeInternal) constructedTree.root).children.get(0);
            BTNodeInternal childTwo = (BTNodeInternal) ((BTNodeInternal) constructedTree.root).children.get(1);
            BTNodeInternal childThree = (BTNodeInternal) ((BTNodeInternal) constructedTree.root).children.get(2);

            assertEquals(List.of("d","f","h"),childOne.keys);
            assertEquals(List.of("k","o"),childTwo.keys);
            assertEquals(List.of("w"),childThree.keys);

        }

        @Test
        @DisplayName("Checking Root Node's Keys for Correctness")
        void RootNode_CorrectKeys(){
            BTNodeInternal root = (BTNodeInternal) constructedTree.root;
            assertEquals(List.of("index","r"), root.keys);
        }
    }

}
