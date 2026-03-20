public class Main {
    public static void main(String[] args) {
        DbComponent<PostgresAdapter> db = new DbComponent<>(
            new PostgresAdapter(),
            "localhost", 
            5432, 
            "visual",
            "postgres",
            "1509",
            50,
            "queries.properties"
        );

        System.out.println("1. Ejecutando transacción para insertar estudiante...");
        db.transaction(ctx -> {
            try {
                ctx.query("insertEstudiante", "Samer", "Ingeniería");
                System.out.println("Estudiante insertado correctamente.");
            } catch (Exception e) {
                throw new RuntimeException("Error en la transacción", e);
            }
        });

        System.out.println("\n2. Ejecutando query normal para consultar estudiantes...");
        var listaEstudiantes = db.query("getEstudiantes");
        System.out.println("Resultado en la base de datos: " + listaEstudiantes);
    }
}