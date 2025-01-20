
import java.io.*;
import java.util.*;

public class index {

    public static void main(String[] args) {
        System.out.println("-------- Elections Arrived ---------\n");
        System.out.println("-------------- Register Yourself! ----------------\n");
        Scanner scanner = new Scanner(System.in);
        final int maximumCandidates = 2;
        final int maximumVoters = 5;
        String[] candidates = new String[maximumCandidates];
        int[] votes = new int[maximumCandidates];
        int[] voterAges = new int[maximumVoters];
        String[] voterCNICs = new String[maximumVoters];
        String[] voterNames = new String[maximumVoters];
        System.out.println("\n--- Returning Officer OF Election Commission ---\n");
        System.out.println("Returning Officer: Only " + maximumCandidates + " candidates seats are vacant.");
        try (FileWriter candidateWriter = new FileWriter("candidateInfo.txt")) {
            for (int i = 0; i < maximumCandidates; i++) {
                System.out.print("Enter candidate " + (i + 1) + " name: ");
                candidates[i] = scanner.nextLine();
                candidateWriter.write("Candidate " + (i + 1) + ": " + candidates[i] + "\n");
                System.out.print("Does the candidate hold dual nationality? (yes/no): ");
                String dualNationality = scanner.nextLine();
                if (dualNationality.toLowerCase().equals("yes")) {
                    System.out.println("Candidate " + candidates[i] + " must DROP their foreign nationality to be eligible.");
                    System.out.print("Do they agree to Drop? (yes/no): ");
                    String response = scanner.nextLine().toLowerCase();
                    if (response.equals("no")) {
                        System.out.println("Candidate " + candidates[i] + " is not eligible to participate.");
                        i--;
                        continue;
                    }
                }
                System.out.println("Candidate " + candidates[i] + " is successfully registered.");
                System.out.println("\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing candidate info: " + e.getMessage());
        }

        System.out.println("\nCollecting Voter Data:");
        try (FileWriter voterWriter = new FileWriter("voterInfo.txt")) {
            for (int i = 0; i < maximumVoters; i++) {
                try {
                    System.out.print("Enter voter " + (i + 1) + " name: ");
                    voterNames[i] = scanner.nextLine();
                    System.out.print("Enter voter " + (i + 1) + " age: ");
                    voterAges[i] = scanner.nextInt();
                    scanner.nextLine();
                    if (voterAges[i] < 18) {
                        throw new Exception("Voter is not eligible (age less than 18).");
                    }
                    System.out.print("Enter voter " + (i + 1) + " CNIC: ");
                    voterCNICs[i] = scanner.nextLine();
                    if (voterCNICs[i].length() != 13) {
                        throw new Exception("Invalid CNIC (must be 13 digits).");
                    }
                    voterWriter.write("Voter " + (i + 1) + ": Name " + voterNames[i] + ", Age "
                            + voterAges[i]
                            + ", CNIC " + voterCNICs[i] + "\n");
                    System.out.println("Voter " + (i + 1) + " is successfully registered.");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing voter info: " + e.getMessage());
        }
        System.out.println("\nVoting Begins:");
        for (int i = 0; i < maximumVoters; i++) {
            if (voterAges[i] >= 18) {
                System.out.println("Voter " + voterNames[i] + " (Age: " + voterAges[i] + "):Choose your candidate.");
                for (int j = 0; j < maximumCandidates; j++) {
                    System.out.println((j + 1) + ". " + candidates[j]);
                }
                try {
                    System.out.print("Enter your choice (1-" + maximumCandidates + "): ");
                    int choice = scanner.nextInt();
                    if (choice < 1 || choice > maximumCandidates) {
                        throw new Exception("Invalid candidate number.");
                    }
                    votes[choice - 1]++;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    scanner.nextLine();
                }
            }
        }

        System.out.println("\nElection Results:");
        int totalVotes = 0;
        for (int i = 0; i < maximumCandidates; i++) {
            totalVotes = totalVotes + votes[i];
        }
        int maxVotes = votes[0];
        int winningCandidateIndex = 0;
        boolean drawVoting = false;
        for (int i = 1; i < maximumCandidates; i++) {
            if (votes[i] > maxVotes) {
                maxVotes = votes[i];
                winningCandidateIndex = i;
                drawVoting = false;
            } else if (votes[i] == maxVotes) {
                drawVoting = true;
            }
        }
        for (int i = 0; i < maximumCandidates; i++) {
            double percentage;
            if (totalVotes > 0) {
                percentage = ((double) votes[i] / totalVotes) * 100;
            } else {
                percentage = 0;
            }
            String formattedPercentage = String.format("%.2f", percentage);
// System.out.println("Percentage: " + formattedPercentage); //* Percentage :
// 100%
            System.out.printf("%s: %d vote (%s%%)%n", candidates[i], votes[i],
                    formattedPercentage);
        }
        if (drawVoting) {
            System.out.println("\nResult: It's a draw!");
        } else {
            System.out.println("\nWinner: " + candidates[winningCandidateIndex] + " with "
                    + maxVotes + " votes!");
        }
        try (FileWriter writer = new FileWriter("electionResults.txt")) {
            for (int i = 0; i < maximumCandidates; i++) {
                double percentage;
                if (totalVotes > 0) {
                    percentage = ((double) votes[i] / totalVotes) * 100;
                } else {
                    percentage = 0;
                }
                String formattedPercentage = String.format("%.2f", percentage);
                writer.write(candidates[i] + ": " + votes[i] + " vote (" + formattedPercentage
                        + "%)\n");
            }
            if (drawVoting) {

                writer.write("\nResult: It's a draw!\n");
            } else {
                writer.write("\nWinner: " + candidates[winningCandidateIndex] + " with "
                        + maxVotes + " votes!\n");
            }
            System.out.println("Results saved to 'electionResults.txt'");
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
        }
        scanner.close();
    }
}
