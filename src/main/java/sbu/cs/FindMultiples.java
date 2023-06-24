package sbu.cs;

/*
    In this exercise, you must write a multithreaded program that finds all
    integers in the range [1, n] that are divisible by 3, 5, or 7. Return the
    sum of all unique integers as your answer.
    Note that an integer such as 15 (which is a multiple of 3 and 5) is only
    counted once.

    The Positive integer n > 0 is given to you as input. Create as many threads as
    you need to solve the problem. You can use a Thread Pool for bonus points.

    Example:
    Input: n = 10
    Output: sum = 40
    Explanation: Numbers in the range [1, 10] that are divisible by 3, 5, or 7 are:
    3, 5, 6, 7, 9, 10. The sum of these numbers is 40.

    Use the tests provided in the test folder to ensure your code works correctly.
 */

import java.util.Scanner;

public class FindMultiples
{

    public static class multipleFinder implements Runnable {

        int index;//threads index
        int[] number;//integers that are  divisible by 3, 5, or 7 in this thread
        int first;//first element of the thread's range
        int last;//last element of the thread's range

        public multipleFinder(int index, int[] number, int first, int last) {
            this.index = index;
            this.number = number;
            this.first = first;
            this.last = last;
        }
        @Override
        public void run() {

            for (int i = first; i <= last; i++) {
                if (i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                    number[index]+=i;
                }
            }
        }
    }

    /*
    The getSum function should be called at the start of your program.
    New Threads and tasks should be created here.
    */
    public static int getSum(int n) {

        int sum = 0;
        int threadsNum = 3;
        int range = (n / threadsNum) + 1;

        int[][] threadsRanges = new int[threadsNum][2];
        for (int i = 0; i < threadsNum; i++){
            threadsRanges[i][0] = i * range + 1;//first element of range
            if ((i+1)*range < n) {//last element of range
                threadsRanges[i][1] = (i+1)*range;
            } else {
                threadsRanges[i][1] = n;
            }
        }

        Thread[] threads = new Thread[threadsNum];
        int[] number = new int[threadsNum];

        for (int i = 0; i < threadsNum; i++) {
            number[i] = 0;
            threads[i] = new Thread(new multipleFinder(i, number, threadsRanges[i][0], threadsRanges[i][1]));
            threads[i].start();
        }

        for (int i = 0; i < threadsNum; i++) {
            try {//******************
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Something went wrong");
            }
            sum += number[i];
        }

        return sum;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int n = input.nextInt();
        System.out.println(getSum(n));
    }
}
