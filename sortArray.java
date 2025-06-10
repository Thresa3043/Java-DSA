import java.util.Arrays;

class Solution {
    public int[] sortArray(int[] nums) {
        return mergeSort(nums);
    }

    // Recursively divides and sorts the array
    static int[] mergeSort(int[] arr){
        if (arr.length == 1) return arr; // Base case: single element is sorted

        int mid = arr.length / 2;

        // Divide the array into left and right halves
        int[] left = mergeSort(Arrays.copyOfRange(arr, 0, mid));
        int[] right = mergeSort(Arrays.copyOfRange(arr, mid, arr.length));

        // Merge the sorted halves
        return merge(left, right);
    }

    // Merges two sorted arrays into one sorted array
    private static int[] merge(int[] first, int[] second){
        int[] newArr = new int[first.length + second.length];
        int i = 0, j = 0, k = 0;

        // Merge elements from both arrays in sorted order
        while (i < first.length && j < second.length){
            if (first[i] < second[j]){
                newArr[k++] = first[i++];
            } else {
                newArr[k++] = second[j++];
            }
        }

        // Copy remaining elements from the first array
        while (i < first.length){
            newArr[k++] = first[i++];
        }

        // Copy remaining elements from the second array
        while (j < second.length){
            newArr[k++] = second[j++];
        }

        return newArr;
    }
}