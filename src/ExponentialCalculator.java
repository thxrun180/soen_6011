import java.util.Scanner;
public class ExponentialCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter a (real number): ");
                double a = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter b (real number > 0): ");
                double b = Double.parseDouble(scanner.nextLine());
                if (b <= 0) throw new IllegalArgumentException("b must be greater than 0.");
                System.out.print("Enter x (real number): ");
                double x = Double.parseDouble(scanner.nextLine());
                double result = a * Math.pow(b, x);
                System.out.printf("Result of ab^x = %.6f%n", result);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter numeric values.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.print("Do you want to compute again? (yes/no): ");
            String again = scanner.nextLine().trim().toLowerCase();
            if (!again.equals("yes")) break;}
        scanner.close();
    }
}
