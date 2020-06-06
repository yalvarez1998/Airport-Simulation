//Assignment 3

//Yitzhak Alvarez

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class AirportSimulation1 {
   public static boolean isPlaneComingIntoQueue(int avgInterval) {
       if (Math.random() < (1.0 / avgInterval))
           return true;
       else
           return false;
   }

   public static boolean isPlaneCrashed(int in, int out, int interval) {
       if (out - in > interval) {
           return true;
       } else {
           return false;
       }
   }

   public static void main(String[] args) {
       Scanner in = new Scanner(System.in);
       System.out.print("Amount of minutes to land: ");
       int landTime = in.nextInt();
       System.out.print("Amount of minutes to take off: ");
       int takeoffTime = in.nextInt();
       System.out.print("Average amount of time between planes to land: ");
       int avgArrivalInterval = in.nextInt();
       System.out.print("Average amount of time between planes to take off: ");
       int avgDepartureInterval = in.nextInt();
       System.out.print(
               "Maximum amount of time in the air before crashing: ");
       int crashLimit = in.nextInt();
       System.out.print("Total simulation minutes: ");
       int totalTime = in.nextInt();

       int totalTimeSpentInLandingQueue = 0, totalTimeSpentInTakeoffQueue = 0;
       int numPlanesLanded = 0, numPlanesTookoff = 0;
       int numPlanesCrashed = 0;
       Queue<Integer> landingQueue = new LinkedBlockingQueue<Integer>();
       Queue<Integer> takeoffQueue = new LinkedBlockingQueue<Integer>();
       for (int i = 0; i < totalTime; ++i) {
           if (isPlaneComingIntoQueue(avgArrivalInterval)) {
               landingQueue.add(i);
           }
           if (isPlaneComingIntoQueue(avgDepartureInterval)) {
               takeoffQueue.add(i);
           }
           while (true) {
               while (!landingQueue.isEmpty() && isPlaneCrashed(landingQueue.peek(), i, crashLimit)) {
                   landingQueue.remove();
                   numPlanesCrashed++;
               }
               if (!landingQueue.isEmpty()) {
                   int nextPlane = landingQueue.peek();
                   landingQueue.remove();
                   numPlanesLanded++;
                   totalTimeSpentInLandingQueue += (i - nextPlane);
                   int j;
                   for (j = i; j < landTime + i && j < totalTime; ++j) {
                       if (isPlaneComingIntoQueue(avgArrivalInterval)) {
                           landingQueue.add(j);
                       }
                       if (isPlaneComingIntoQueue(avgDepartureInterval)) {
                           takeoffQueue.add(j);
                       }
                   }
                   i = j;
                   if (i >= totalTime) {
                       break;
                   }
               } else {
                   break;
               }
           }
           if (!takeoffQueue.isEmpty()) {
               int nextPlane = takeoffQueue.peek();
               takeoffQueue.remove();
               numPlanesTookoff++;
               totalTimeSpentInTakeoffQueue += (i - nextPlane);
               int j;
               for (j = i; j < takeoffTime + i && j < totalTime; ++j) {
                   if (isPlaneComingIntoQueue(avgArrivalInterval)) {
                       landingQueue.add(j);
                   }
                   if (isPlaneComingIntoQueue(avgDepartureInterval)) {
                       takeoffQueue.add(j);
                   }
               }
               i = j;
           }
       }
       while (!landingQueue.isEmpty() && isPlaneCrashed(landingQueue.peek(), totalTime, crashLimit)) {
           landingQueue.remove();
           numPlanesCrashed++;
       }
       System.out.println("Number of planes taken off: " + numPlanesTookoff);
       System.out.println("Number of planes landed: " + numPlanesLanded);
       System.out.println("Number of planes crashed: " + numPlanesCrashed);
       System.out.println("Average waiting time for taking off: " + totalTimeSpentInTakeoffQueue / (double) numPlanesTookoff);
       System.out.println("Average waiting time for landing: " + totalTimeSpentInLandingQueue / (double) numPlanesLanded);
   }
}