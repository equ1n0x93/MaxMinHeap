package com.Main.MaxMinHeap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is Implementing the required Data Structure by the OpenUniversity described as MaxMinHeap
 *
 *
 * MaxMinHeap specifications:
 * 1. Each even depth node is greater than / equal to all of his descendants (Even == Max level)
 * 2. Each even depth node is less than / equal to all of his descendants (Odd == Min level)
 * 3. The class implements the next public methods:
 * - heapify (HEAPIFY)
 * - buildHeap (BUILD-HEAP)
 * - heapExtractMax (HEAP-EXTRACT-MAX)
 * - heapExtractMin (HEAP-EXTRACT-MIN)
 * - heapInsert (HEAP-INSERT)
 * - heapDelete (HEAP-DELETE)
 *
 */

public class MaxMinHeap {

    ArrayList<Integer> heapArray;

    public MaxMinHeap() {
        // Constructor to create the object without any input
        this.heapArray = new ArrayList<>();
    }

    public MaxMinHeap(ArrayList<Integer> arr) {
        // Constructor from a given array
        this.heapArray = (ArrayList<Integer>) arr.clone();
    }

    public MaxMinHeap(String inputFilePath) throws IOException {
        // Constructor that reads the input array from a file
        this.heapArray = new ArrayList<>();

        Scanner scanner = new Scanner(new File(inputFilePath));
        while (scanner.hasNextInt()) {
            this.heapArray.add(scanner.nextInt());
        }
    }

    public void buildHeap() {
        /*
        This method uses the heapify method and run it on the heapArray attribute
        That way it fixes it to match the accepted criteria as a MaxMinHeap
         */
        int size = heapArray.size();
        int LastPossibleWithChildrenIndex = (int) (Math.floor((double) size / 2));
        // Heapify all the nodes that can have children
        for (int index = LastPossibleWithChildrenIndex; index >= 0; index--) {
            heapify(index);
        }
        printHeap();
    }

    public void heapify(int indexToHeapify) {
        /*
        This method is the "routine" method of the MaxMinHeap
        It is used to correct errors assuming that all lower levels work correctly
        This method is separated to 2 sub-methods based on the level of the given index (Max/Min level depth)

        @param indexToHeapify - literaly the index to run Heapify on
         */

        // Check if heapify index exist in the array
        if (!indexExistInHeap(indexToHeapify)) {
            System.out.println("Index for Heapify [" + indexToHeapify + "] not in array, no actions were taken.");
            return;
        }

        // Heapify odd index (min value of all descendant)
        if (getHeapDepthOfIndex(indexToHeapify) % 2 == 1) {
            heapifyMinLevel(indexToHeapify);
        }
        // Heapify even index (max value of all descendant)
        else {
            heapifyMaxLevel(indexToHeapify);
        }
    }

    void heapifyMinLevel(int index) {
        // Check that has kids
        if (getLeftChildIndex(index) == -1)
            return;

        // Find index of smallest child / grandchild
        int minChildIndex = getSmallestIndexUpToGrandChild(index, false);

        // min child is a grandchild of i
        if (isGrandChildOf(index, minChildIndex)) {
            if (heapArray.get(index) > heapArray.get(minChildIndex)) {
                switchPlace(index, minChildIndex);
                // Check if after the switch the new child is bigger than parent otherwise switch with it
                if (heapArray.get(minChildIndex) > heapArray.get(getParentIndex(minChildIndex))) {
                    switchPlace(minChildIndex, getParentIndex(minChildIndex));
                }
                heapifyMinLevel(minChildIndex);
            }
        }
        // min child is a child of i
        else {
            if (heapArray.get(minChildIndex) < heapArray.get(index))
                switchPlace(minChildIndex, index);
        }

    }

    void heapifyMaxLevel(int index) {
        // Check that has kids
        if (getLeftChildIndex(index) == -1)
            return;

        // Find index of smallest child / grandchild
        int maxChildIndex = getLargestIndexUpToGrandChild(index, false);

        // max child is a grandchild of i
        if (isGrandChildOf(index, maxChildIndex)) {
            if (heapArray.get(index) < heapArray.get(maxChildIndex)) {
                switchPlace(index, maxChildIndex);
                // Check if after the switch the new child is smaller than parent otherwise switch with it
                if (heapArray.get(maxChildIndex) < heapArray.get(getParentIndex(maxChildIndex))) {
                    switchPlace(maxChildIndex, getParentIndex(maxChildIndex));
                }

                heapifyMaxLevel(maxChildIndex);
            }
        }
        // max child is a child of i
        else {
            if (heapArray.get(maxChildIndex) > heapArray.get(index))
                switchPlace(maxChildIndex, index);
        }
    }

    public int heapExtractMax() {
        /*
        This method extracts and returns the maximal value found in the heap (the root of the heap)
        It is done by exchanging places with the last index of the heap and then running heapify on the heap root
         */
        int maxIndex = getHeapMaxIndex();
        if (maxIndex == -1) {
            System.out.println("Error, heap is empty!");
            return -1;
        }

        int max = heapArray.get(maxIndex);
        switchPlace(maxIndex, getLastHeapIndex());
        heapArray.remove(getLastHeapIndex());
        heapify(maxIndex);

        return max;
    }

    public int heapExtractMin() {
        /*
        This method extracts and returns the minimal value found in the heap
        It is done by exchanging places with the last index of the heap and then running heapify on the original index
         */
        int minIndex = getHeapMinIndex();
        if (minIndex == -1) {
            System.out.println("Error, heap is empty!");
            return -1;
        }

        int min = heapArray.get(minIndex);
        switchPlace(minIndex, getLastHeapIndex());
        heapArray.remove(getLastHeapIndex());
        heapify(minIndex);

        return min;
    }

    public void heapInsert(int newValue) {
        /*
        Inserts a new value to the end of the array representing the heap
        afterwards the method fixes the new value position to match the MaxMinHeap criteria by using bubbleUp method

        @param newValue - New value to be inserted to the heap
         */

        System.out.println("Inserting [" + newValue + "] to heap.");
        heapArray.add(newValue);
        int newValueIndex = getLastHeapIndex();
        bubbleUp(newValueIndex); // Uses bubbleUp method to fix the position of the new inserted value
        System.out.println("\nNew heap -");
        printHeap();
    }

    public void heapDelete(int index) {
        /*
        Deletes a certain value from the heap by exchanging places with the last value of the heap and then it removes that value
        After that it fixes the heap by running heapify on it

        @param index - Index to delete
         */
        if (!indexExistInHeap(index)) {
            System.out.println("ERROR: Requested index value not in heap (notice that indices start from 0)!");
            return;
        }

        System.out.println("Deleting value [" + heapArray.get(index) + "] in index [" + index + "] from the heap.");
        // Move the deleted value to end of heap and then delete it
        switchPlace(index, getLastHeapIndex());
        heapArray.remove(getLastHeapIndex());
        heapify(index);
        System.out.println("\nNew heap -");
        printHeap();
    }

    private static double log2(int x) {
        /*
        This method is used to get the mathematic result of log with base 2 as a double

        @param x - value to evaluate it's log of base 2
         */
        return (Math.log(x) / Math.log(2));
    }

    private int getLastHeapIndex() {
        /* 
        Returns the index of the last position in the array with value.
         */
        return heapArray.size() - 1;
    }

    private static int getHeapDepthOfIndex(int index) {
        /*
        This method returns the depth of a certain index in the heap tree representation
        Notice that indices in Java start from 0 so the given index value is increased by 1

        @param index - Index to check depth of
         */
        return (int) log2(index + 1);
    }

    private void switchPlace(int firstIndex, int secondIndex) {
        /*
        A simple method to switch the location of two values in the array by their indices
         */
        Integer firstValue = heapArray.get(firstIndex);
        Integer secondValue = heapArray.get(secondIndex);
        heapArray.set(firstIndex, secondValue);
        heapArray.set(secondIndex, firstValue);
    }

    private static boolean isGrandChildOf(int parentIndex, int indexToCheck) {
        /*
        Check if an index is a grandChild of a given parent
        returns True if <indexToCheck> is actually a grand child of <parentIndex>
         */
        int leftestGrandChildren = (((parentIndex + 1) * 2 - 1) + 1) * 2 - 1;
        int rightestGrandChild = (((parentIndex + 1) * 2) + 1) * 2;
        return leftestGrandChildren <= indexToCheck & indexToCheck <= rightestGrandChild;
    }

    private int getSmallestIndexUpToGrandChild(int parentIndex, boolean includeParent) {
        /*
        This method returns the index of the descendant with the minimal value up to a grand child for a given parent node (2 depths)
        It does it with simple comparisons of all the existing children / grandchildren of a given node.

        includeParent parameter sets whether or not should the parent index value checked as a minimum as well.
         */

        int leftChildIndex = getLeftChildIndex(parentIndex);
        int leftGrandChildLeftIndex = getLeftChildIndex(leftChildIndex);
        int leftGrandChildRightIndex = getRightChildIndex(leftChildIndex);
        int rightChildIndex = getRightChildIndex(parentIndex);
        int rightGrandChildLeftIndex = getLeftChildIndex(rightChildIndex);
        int rightGrandChildRightIndex = getRightChildIndex(rightChildIndex);
        int smallestIndex = -1;
        int smallestValue = Integer.MAX_VALUE;

        if (includeParent) {
            smallestIndex = parentIndex;
            smallestValue = heapArray.get(parentIndex);
        }


        // Check left child
        if (indexExistInHeap(leftChildIndex)) {
            if (heapArray.get(leftChildIndex) < smallestValue) {
                smallestIndex = leftChildIndex;
                smallestValue = heapArray.get(leftChildIndex);
            }
        }
        // Check left grand-children
        if (indexExistInHeap(leftGrandChildLeftIndex)) {
            if (heapArray.get(leftGrandChildLeftIndex) < smallestValue) {
                smallestIndex = leftGrandChildLeftIndex;
                smallestValue = heapArray.get(leftGrandChildLeftIndex);
            }
        }
        if (indexExistInHeap(leftGrandChildRightIndex)) {
            if (heapArray.get(leftGrandChildRightIndex) < smallestValue) {
                smallestIndex = leftGrandChildRightIndex;
                smallestValue = heapArray.get(leftGrandChildRightIndex);
            }
        }

        // Check right child
        if (indexExistInHeap(rightChildIndex)) {
            if (heapArray.get(rightChildIndex) < smallestValue) {
                smallestIndex = rightChildIndex;
                smallestValue = heapArray.get(rightChildIndex);
            }
        }
        // Check right grand-children
        if (indexExistInHeap(rightGrandChildLeftIndex)) {
            if (heapArray.get(rightGrandChildLeftIndex) < smallestValue) {
                smallestIndex = rightGrandChildLeftIndex;
                smallestValue = heapArray.get(rightGrandChildLeftIndex);
            }
        }
        if (indexExistInHeap(rightGrandChildRightIndex)) {
            if (heapArray.get(rightGrandChildRightIndex) < smallestValue) {
                smallestIndex = rightGrandChildRightIndex;
            }
        }
        return smallestIndex;
    }

    private int getLargestIndexUpToGrandChild(int parentIndex, boolean includeParent) {
        /*
        This method returns the index of the descendant with the maximal value up to a grand child for a given parent node (2 depths)
        It does it with simple comparisons of all the existing children / grandchildren of a given node.

        includeParent parameter sets whether or not should the parent index value checked as a maximum as well.
         */

        int leftChildIndex = getLeftChildIndex(parentIndex);
        int leftGrandChildLeftIndex = getLeftChildIndex(leftChildIndex);
        int leftGrandChildRightIndex = getRightChildIndex(leftChildIndex);
        int rightChildIndex = getRightChildIndex(parentIndex);
        int rightGrandChildLeftIndex = getLeftChildIndex(rightChildIndex);
        int rightGrandChildRightIndex = getRightChildIndex(rightChildIndex);
        int largestIndex = -1;
        int largestValue = Integer.MIN_VALUE;

        if (includeParent) {
            largestIndex = parentIndex;
            largestValue = heapArray.get(parentIndex);
        }

        // Check left child
        if (indexExistInHeap(leftChildIndex)) {
            if (heapArray.get(leftChildIndex) > largestValue) {
                largestIndex = leftChildIndex;
                largestValue = heapArray.get(leftChildIndex);
            }
        }
        // Check left grand-children
        if (indexExistInHeap(leftGrandChildLeftIndex)) {
            if (heapArray.get(leftGrandChildLeftIndex) > largestValue) {
                largestIndex = leftGrandChildLeftIndex;
                largestValue = heapArray.get(leftGrandChildLeftIndex);
            }
        }
        if (indexExistInHeap(leftGrandChildRightIndex)) {
            if (heapArray.get(leftGrandChildRightIndex) > largestValue) {
                largestIndex = leftGrandChildRightIndex;
                largestValue = heapArray.get(leftGrandChildRightIndex);
            }
        }

        // Check right child
        if (indexExistInHeap(rightChildIndex)) {
            if (heapArray.get(rightChildIndex) > largestValue) {
                largestIndex = rightChildIndex;
                largestValue = heapArray.get(rightChildIndex);
            }
        }
        // Check right grand-children
        if (indexExistInHeap(rightGrandChildLeftIndex)) {
            if (heapArray.get(rightGrandChildLeftIndex) > largestValue) {
                largestIndex = rightGrandChildLeftIndex;
                largestValue = heapArray.get(rightGrandChildLeftIndex);
            }
        }
        if (indexExistInHeap(rightGrandChildRightIndex)) {
            if (heapArray.get(rightGrandChildRightIndex) > largestValue) {
                largestIndex = rightGrandChildRightIndex;
            }
        }
        return largestIndex;
    }

    private boolean indexExistInHeap(int index) {
        /*
         returns true if index exists in the array representing the heap (value between 0 and heap array size)
         */
        return index >= 0 & index < heapArray.size();
    }

    private int getParentIndex(int childIndex) {
        /*
        returns the parent index of a node if it exists in the array that represents the heap (returns -1 if not)
         */
        int parentIndex = (int) Math.ceil((double) childIndex / 2) - 1;

        if (indexExistInHeap(parentIndex)) {
            return parentIndex;
        }
        return -1;
    }

    private int getLeftChildIndex(int parentIndex) {
        /*
        returns the left child index of a node if it exists in the array that represents the heap (returns -1 if not)
         */
        if (!indexExistInHeap(parentIndex)) {
            return -1;
        }

        int leftChildIndex = (parentIndex + 1) * 2 - 1;

        if (indexExistInHeap(leftChildIndex)) {
            return leftChildIndex;
        }
        return -1;
    }

    private int getRightChildIndex(int parentIndex) {
        /*
        returns the right child index of a node if it exists in the array that represents the heap (returns -1 if not)
         */

        if (!indexExistInHeap(parentIndex)) {
            return -1;
        }

        int rightChildIndex = (parentIndex + 1) * 2;

        if (indexExistInHeap(rightChildIndex)) {
            return rightChildIndex;
        }
        return -1;
    }

    private int getHeapMaxIndex() {
        /*
        returns the index of the node with Maximum value in the heap (root of course in the MaxMinHeap)
        If the heap is empty -1 is returned
         */
        if (heapArray.size() > 0) {
            return 0;
        }
        return -1;
    }

    private int getHeapMinIndex() {
        /*
        returns the index of the node with Minimum value in the heap (1st / 2nd depth layer only needs to be checked)
        If the heap is empty -1 is returned
         */

        int min = Integer.MIN_VALUE;
        int minIndex = -1;

        // We check the first, second, third indices in case the heap is very small
        if (indexExistInHeap(0)) {
            min = heapArray.get(0);
            minIndex = 0;

            // Check left Child
            if (indexExistInHeap(1)) {
                if (heapArray.get(1) < min) {
                    min = heapArray.get(1);
                    minIndex = 1;
                }
            }

            // Check right Child
            if (indexExistInHeap(2)) {
                if (heapArray.get(2) < min) {
                    minIndex = 2;
                }
            }
        }
        return minIndex;
    }

    private void bubbleUp(int index) {
        /*
        This method is responsible for moving a certain value up if it is not positioned correctly to match the
        MaxMinHeap criteria.
        It uses two sub methods for bubbleUp depending on the input index level (Min / Max level)

        @param index - Index to bubble up
         */
        int parentIndex = getParentIndex(index);

        if (indexExistInHeap(parentIndex)) {
            // Check heap level - if min / max
            if (getHeapDepthOfIndex(index) % 2 == 1) {
                // Here we know we are at min level, need to check if parent in max level is smaller
                if (heapArray.get(index) > heapArray.get(parentIndex)) {
                    switchPlace(index, parentIndex);
                    bubbleUpMax(parentIndex);
                } else {
                    bubbleUpMin(index);
                }
            } else {
                // Here we know we are at max level, need to check if parent in min level is bigger
                if (heapArray.get(index) < heapArray.get(parentIndex)) {
                    switchPlace(index, parentIndex);
                    bubbleUpMin(parentIndex);
                } else {
                    bubbleUpMax(index);
                }
            }
        }
    }

    private void bubbleUpMin(int index) {
        /*
        Sub method of bubbleUp that bubbles up an index of a Minimum depth level

        @param index - index to bubble up
         */
        int grandParentIndex = getParentIndex(getParentIndex(index));

        if (indexExistInHeap(grandParentIndex)) {
            if (heapArray.get(index) < heapArray.get(grandParentIndex)) {
                switchPlace(index, grandParentIndex);
                bubbleUpMin(grandParentIndex);
            }
        }
    }

    private void bubbleUpMax(int index) {
        /*
        Sub method of bubbleUp that bubbles up an index of a Maximum depth level

        @param index - index to bubble up
         */
        int grandParentIndex = getParentIndex(getParentIndex(index));

        if (indexExistInHeap(grandParentIndex)) {
            if (heapArray.get(index) > heapArray.get(grandParentIndex)) {
                switchPlace(index, grandParentIndex);
                bubbleUpMax(grandParentIndex);
            }
        }
    }

    public void printHeap() {
        /*
        Prints the heap to the user, one time as the raw array, second time as a tree showing each layer
         */
        int arraySize = heapArray.size();
        int deepestLevel = getHeapDepthOfIndex(arraySize - 1);
        double layerLog2;
        System.out.println("\nArray representation:");
        System.out.println(heapArray);

        System.out.print("\nTree layers representation:");

        for (int i = 0; i < arraySize; i++) {

            layerLog2 = log2(i + 1);

            if (layerLog2 == (int) layerLog2) {
                if (getHeapDepthOfIndex(i) % 2 == 0) {
                    System.out.print("\nMax: ");
                } else {
                    System.out.print("\nMin: ");
                }

                for (int j = 0; j < (deepestLevel - getHeapDepthOfIndex(i)); j++) {
                    System.out.print("  ");
                }

            }
            System.out.print(heapArray.get(i) + "  ");
        }
        System.out.println();
    }
}
