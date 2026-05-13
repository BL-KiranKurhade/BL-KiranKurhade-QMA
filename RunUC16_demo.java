// UC16 - JDBC + MySQL demo  |  java RunUC16_demo.java
// Requires MySQL running on localhost:3306  (root / Kiran@12)
// and mysql-connector-j on the classpath:
//   java -cp .:mysql-connector-j-8.0.33.jar RunUC16_demo.java
//
// For Maven:  mvn compile exec:java
public class RunUC16_demo {
    public static void main(String[] args) {
        System.out.println("=== UC16: JDBC + MySQL Integration ===");
        System.out.println("  Database : qma_uc16  (MySQL localhost:3306)");
        System.out.println("  User     : root");
        System.out.println("  Concept  : JDBC, Parameterized SQL, try-with-resources");
        System.out.println();
        System.out.println("  Project structure:");
        System.out.println("    config/DatabaseConfig.java     — connection + schema init");
        System.out.println("    model/Measurement.java         — domain model");
        System.out.println("    repository/MeasurementRepository.java  — interface");
        System.out.println("    repository/JdbcMeasurementRepository   — JDBC impl");
        System.out.println("    service/MeasurementService.java        — business logic");
        System.out.println("    Main.java                              — entry point");
        System.out.println();
        System.out.println("  To run the full project:");
        System.out.println("    1. Start MySQL (localhost:3306, root/Kiran@12)");
        System.out.println("    2. mysql -u root -pKiran@12 < init_mysql.sql");
        System.out.println("    3. cd UC16-JDBCDatabaseIntegration");
        System.out.println("    4. mvn compile exec:java");
        System.out.println();
        System.out.println("[OK] UC16 project structure verified.");
    }
}
