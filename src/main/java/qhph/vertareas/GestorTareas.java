package qhph.vertareas;

import java.time.LocalDate;
import java.util.*;

public class GestorTareas {
    private List<Tarea> tareas;

     // AtributosInferidos se maneja como record para no crear un constructor para cada variable.

    public record AtributosInferidos(String prioridad, String metodoEstudio) {}

     // Constructor. Inicializa la lista de tareas.

    public GestorTareas() {
        this.tareas = new ArrayList<>();
    }

     // Agrega una nueva tarea a la lista y le asigna un orden personalizado.

    public void agregarTarea(Tarea tarea) {
        tarea.setOrdenPersonalizado(obtenerSiguienteOrdenPersonalizado());
        this.tareas.add(tarea);
    }

     // Elimina una tarea de la lista, se usa remove gracias al arraylist.

    public void eliminarTarea(Tarea tarea) {
        this.tareas.remove(tarea);
    }

     // Método vacío en este diseño (la tarea se actualiza por referencia en el diálogo).

    public void actualizarTarea(Tarea tarea) {
    }

     // Invierte el estado de completado de una tarea.

    public void alternarEstadoCompletado(Tarea tarea) {
        tarea.setCompletada(!tarea.isCompletada());
    }



     // Devuelve una copia de la lista de todas las tarea, para que no sea modificada.

    public List<Tarea> obtenerTareas() {
        return new ArrayList<>(this.tareas);
    }

     // Infere prioridad y método de estudio basados en la desc y la fecha de vencimiento.

    public AtributosInferidos inferirAtributos(String descripcion, LocalDate fechaVencimiento) {
        return new AtributosInferidos(inferirPrioridad(descripcion, fechaVencimiento), inferirMetodoEstudio(descripcion));
    }

    private String inferirPrioridad(String description, LocalDate dueDate) {
        String descLower = description.toLowerCase();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if (dueDate != null && (dueDate.isEqual(today) || dueDate.isEqual(tomorrow))) { return "Alta"; }

        if (descLower.contains("absoluta") || descLower.contains("ahora") || descLower.contains("ahora mismo") ||
                descLower.contains("ahorita") || descLower.contains("al instante") || descLower.contains("alto impacto") ||
                descLower.contains("antes de fin de dia") || descLower.contains("antes de fin de día") ||
                descLower.contains("apremiante") || descLower.contains("bloqueante") || descLower.contains("central") ||
                descLower.contains("core") || descLower.contains("critica") || descLower.contains("crítica") ||
                descLower.contains("critico") || descLower.contains("crítico") || descLower.contains("deadline inminente") ||
                descLower.contains("definitivo") || descLower.contains("emergencia") || descLower.contains("esencial") ||
                descLower.contains("evaluacion") || descLower.contains("evaluación") || descLower.contains("examen") ||
                descLower.contains("final") || descLower.contains("fundamental") || descLower.contains("imperativo") ||
                descLower.contains("imprescindible") || descLower.contains("inapelable") || descLower.contains("iniciar ahora") ||
                descLower.contains("inmediato") || descLower.contains("last call") || descLower.contains("muy importante") ||
                descLower.contains("no negociable") || descLower.contains("obligatorio") || descLower.contains("parcial") ||
                descLower.contains("para nota") || descLower.contains("primero") || descLower.contains("primordial") ||
                descLower.contains("prioridad maxima") || descLower.contains("prioridad máxima") || descLower.contains("quiz") ||
                descLower.contains("requerido") || descLower.contains("resolucion") || descLower.contains("resolución") ||
                descLower.contains("sin falta") || descLower.contains("super importante") || descLower.contains("taller") ||
                descLower.contains("tarea urgente") || descLower.contains("top priority") || descLower.contains("urgente") ||
                descLower.contains("ultimo plazo") || descLower.contains("último plazo") || descLower.contains("vital") ||
                descLower.contains("ya")) {
            return "Alta"; }

        if (descLower.contains("actividad") || descLower.contains("actualizacion") || descLower.contains("actualización") ||
                descLower.contains("actualizar") || descLower.contains("ajustes") || descLower.contains("analisis") ||
                descLower.contains("análisis") || descLower.contains("análisis detallado") || descLower.contains("antes de") ||
                descLower.contains("breve plazo") || descLower.contains("cargar") || descLower.contains("cierre") ||
                descLower.contains("coordinacion") || descLower.contains("coordinación") || descLower.contains("coordinar") ||
                descLower.contains("confirmacion") || descLower.contains("confirmación") || descLower.contains("consolidacion") ||
                descLower.contains("consolidación") || descLower.contains("consolidar") || descLower.contains("consolidar datos") ||
                descLower.contains("considerable") || descLower.contains("control") || descLower.contains("crear") ||
                descLower.contains("curso") || descLower.contains("desarrollar") || descLower.contains("desarrollo") ||
                descLower.contains("documentar") || descLower.contains("ejecutar") || descLower.contains("en proceso") ||
                descLower.contains("ensayo") || descLower.contains("entrega") || descLower.contains("evaluar") || descLower.contains("fase 1") ||
                descLower.contains("fase beta") || descLower.contains("fase dos") || descLower.contains("fecha limite") ||
                descLower.contains("fecha límite") || descLower.contains("feedback") || descLower.contains("finaliza") ||
                descLower.contains("finalizar") || descLower.contains("gestionar") || descLower.contains("implementacion") ||
                descLower.contains("implementación") || descLower.contains("implementar") || descLower.contains("importante") ||
                descLower.contains("informacion") || descLower.contains("información") || descLower.contains("informe") ||
                descLower.contains("intermedio") || descLower.contains("investigacion") || descLower.contains("investigación") ||
                descLower.contains("medio plazo") || descLower.contains("mitad de plazo") || descLower.contains("moderado") ||
                descLower.contains("organizar") || descLower.contains("pendiente") || descLower.contains("plan") ||
                descLower.contains("planificacion") || descLower.contains("planificación") || descLower.contains("planificar") ||
                descLower.contains("plazo") || descLower.contains("plazo corto") || descLower.contains("preparacion") ||
                descLower.contains("preparación") || descLower.contains("preparar") || descLower.contains("presentacion") ||
                descLower.contains("presentación") || descLower.contains("prioridad") || descLower.contains("prioritario") ||
                descLower.contains("programado") || descLower.contains("programar") || descLower.contains("propuesta") ||
                descLower.contains("proyecto") || descLower.contains("proximo paso") || descLower.contains("próxima etapa") ||
                descLower.contains("publicar") || descLower.contains("recopilar informacion") || descLower.contains("recopilar información") ||
                descLower.contains("refinar") || descLower.contains("reporte") || descLower.contains("resumen ejecutivo") ||
                descLower.contains("revision") || descLower.contains("revisión") || descLower.contains("revisar a fondo") ||
                descLower.contains("revisión interna") || descLower.contains("reunion") || descLower.contains("reunión") ||
                descLower.contains("segunda fase") || descLower.contains("seguimiento") || descLower.contains("semi urgente") ||
                descLower.contains("seminario") || descLower.contains("solicitar") || descLower.contains("subir") ||
                descLower.contains("tarea") || descLower.contains("tarea grupal") || descLower.contains("terminar") ||
                descLower.contains("trabajo") || descLower.contains("tutorial") || descLower.contains("ultimo dia") ||
                descLower.contains("último día") || descLower.contains("validación") || descLower.contains("verificar") ||
                descLower.contains("vence") || descLower.contains("webinar")) {
            return "Media"; }

        if (descLower.contains("a largo plazo") || descLower.contains("acuerdate") || descLower.contains("acuérdate") ||
                descLower.contains("adicional") || descLower.contains("archivar") || descLower.contains("background") ||
                descLower.contains("borrador") || descLower.contains("brainstorming") || descLower.contains("breve") ||
                descLower.contains("casual") || descLower.contains("consultar") || descLower.contains("corregir") ||
                descLower.contains("cuando pueda") || descLower.contains("cuando tenga tiempo") || descLower.contains("delegar") ||
                descLower.contains("demo") || descLower.contains("discrecional") ||
                descLower.contains("ejemplo") || descLower.contains("en cola") || descLower.contains("entretenimiento") ||
                descLower.contains("eventual") || descLower.contains("exploracion") || descLower.contains("exploración") ||
                descLower.contains("explorar") || descLower.contains("explorar ideas") || descLower.contains("facil") ||
                descLower.contains("fácil") || descLower.contains("fondo") || descLower.contains("futuro desarrollo") ||
                descLower.contains("futuro lejano") || descLower.contains("generar") || descLower.contains("hobby") ||
                descLower.contains("idea") || descLower.contains("informal") || descLower.contains("investigar") ||
                descLower.contains("investigación preliminar") || descLower.contains("ir de shopping") || descLower.contains("jugar") ||
                descLower.contains("leer") || descLower.contains("listado") || descLower.contains("luego") ||
                descLower.contains("mantener") || descLower.contains("mas tarde") || descLower.contains("más tarde") ||
                descLower.contains("mejorar") || descLower.contains("no crítico") || descLower.contains("no es prioritario") ||
                descLower.contains("no importante") || descLower.contains("no urgente") || descLower.contains("nota mental") ||
                descLower.contains("observar") || descLower.contains("ocio") || descLower.contains("opcional") ||
                descLower.contains("opcionalmente") || descLower.contains("opcion") || descLower.contains("opción") ||
                descLower.contains("para ocio") || descLower.contains("parking") || descLower.contains("pendiente largo") ||
                descLower.contains("pensar") || descLower.contains("plan general") || descLower.contains("posiblemente") ||
                descLower.contains("posponer") || descLower.contains("probar") || descLower.contains("prueba") ||
                descLower.contains("recordar") || descLower.contains("recordatorio") || descLower.contains("recomendar") ||
                descLower.contains("repasar") || descLower.contains("revisar") || descLower.contains("rutina") ||
                descLower.contains("secundario") || descLower.contains("si hay tiempo") || descLower.contains("simple") ||
                descLower.contains("sin apuro") || descLower.contains("sin impacto") || descLower.contains("sugerencia") ||
                descLower.contains("tal vez") || descLower.contains("un dia de estos") || descLower.contains("ver luego") ||
                descLower.contains("ver mas tarde")) { return "Baja"; }
        return "Baja"; // Por defecto es Baja.
    }



    // Infere si el método de estudio "Pomodoro" es recomendado.
    //uso matches en vez de contains ya que contains es mas eficiente para retornar varias opciones, matches devuelve mejor cuando es una u otra.

    private String inferirMetodoEstudio(String description) {
        String descLower = description.toLowerCase();
        // Busca palabras clave relacionadas con el estudio profundo/concentrado usando regex.
        if (descLower.matches(".*(proyecto de software|adquirir habilidades|analisis|análisis|análisis a fondo|analizar|analizar caso|" +
                "aprender|aprender nuevo|aprendizaje activo|aprendizaje significativo|apuntes|autoevaluacion|autoevaluación|" +
                "bloque de estudio|bloque de tiempo|bloqueo de tiempo|capacitacion|capacitación|capacitarme|" +
                "certificacion|certificación|clase|clase magistral|charla|semestral|comprender|" +
                "concentracion|concentración|concentrarse|concept mapping|concepto abstracto|concepto clave|conceptos clave|" +
                "consolidacion|consolidación|contenido complejo|cronometro|cronómetro|cuestionario|curso|curso online|" +
                "desafio intelectual|desafío intelectual|desarrollo de habilidades|desglozar|deep dive|disciplina|dominio|" +
                "ejercicio practico|ejercicio práctico|ejercicios|ejercicios de aplicación|ejercicios de fijación|" +
                "ejercicios resueltos|enfoque|enfoque profundo|entender|entender a fondo|entender base|entrenamiento|" +
                "esquema|esquematizar|estudiar a fondo|estudiar para|estudio focalizado|estudio profundo|estructurar|" +
                "evaluacion formativa|evaluación formativa|evaluar progreso|examen|examen final|fase de estudio|fichas|" +
                "flashcards|flujo de trabajo|focalizar|foco|formacion|formación|fundamentos|guia|guía|guia de estudio|" +
                "guía de estudio|grupo de estudio|hacer tarea|intensiva|interleaving|intervalo de estudio|investigar tema|" +
                "investigacion profunda|investigación profunda|know how|lectura comprensiva|lectura critica|lectura crítica|" +
                "lectura densa|lectura intensiva|leer|leccion|lección|libro|manejo del tiempo|manual|mapear|mapas mentales|" +
                "material complejo|material didactico|material didáctico|memorizar|memoria activa|mentoria|mentoría|" +
                "metodologia|metodología|metodo de estudio|método de estudio|monografia|monografía|objetivo de estudio|" +
                "parcial|pausas activas|perfeccionar|pensamiento critico|pensamiento crítico|plan de estudio|pomodoro|" +
                "practica|práctica|practicar|práctica constante|pre-requisito|preparar examen|preparar presentación|" +
                "preparacion|preparación|preparatoria|profundizar|profundo|productividad|proyecto academico|" +
                "proyecto académico|proyecto de grado|pruebas rapidas|quiz|rendimiento|reforzar|remontar|repasar|" +
                "repasar conceptos|repaso|repaso activo|repaso estructurado|repaso general|retencion|retención|" +
                "retrieval practice|revision exhaustiva|revisión exhaustiva|revisar|revisar notas|resolver problema|" +
                "resolucion de problemas|resolución de problemas|resumir|resumir capitulo|resumen|sesion de estudio|" +
                "sesión de estudio|sesion intensa|sesión intensa|simulacro|simulacion de examen|simulación de examen|simular|" +
                "sin distracciones|skill building|spaced repetition|sprint de estudio|study session|subrayar|taller|" +
                "tecnica|técnica|tecnica pomodoro|técnica pomodoro|teorema|teoria|teoría|test|tesis|tema nuevo|" +
                "trabajo final|tutoria|tutoría|tutorial).*")) {
            return "Pomodoro"; // Si alguna de las palabras clave coincide, se recomienda Pomodoro.
        }

        return "Ninguno"; // Si no se encuentra ninguna palabra clave de estudio, no se recomienda Pomodoro.
    }



     // Ordena una lista de tareas según un criterio.

    public void ordenarTareas(List<Tarea> tareasAOrdenar, String criterio) {
        Comparator<Tarea> comparator;
        switch (criterio) {
            case "Por fecha de vencimiento": comparator = Comparator.comparing(Tarea::getFechaVencimiento, Comparator.nullsLast(Comparator.naturalOrder())); break;
            case "Por fecha de modificación (más recientes)": comparator = Comparator.comparing(Tarea::getFechaModificacion, Comparator.nullsLast(Comparator.naturalOrder())).reversed(); break;
            case "Por prioridad": comparator = Comparator.comparingInt((Tarea t) -> getValorPrioridad(t.getPrioridad())).reversed(); break;
            case "Personalizado (arrastrar y soltar)": comparator = Comparator.comparingInt(Tarea::getOrdenPersonalizado); break;
            default: comparator = Comparator.comparing(Tarea::getFechaCreacion); break; // Orden por defecto: fecha de creación.
        }
        Collections.sort(tareasAOrdenar, comparator);

        // Si es orden personalizado, re-numera los "ordenPersonalizado" para reflejar la nueva posición visual.
        if ("Personalizado (arrastrar y soltar)".equals(criterio)) {
            for (int i = 0; i < tareasAOrdenar.size(); i++) {
                tareasAOrdenar.get(i).setOrdenPersonalizado(i);
            }
        }
    }


    /**
     * Convierte prioridad String a valor numérico (útil para ordenar).
     * Ya que comparator usa como referencia los números.
     */
    private int getValorPrioridad(String prioridad) {
        return switch (prioridad.toLowerCase()) {
            case "alta" -> 3;
            case "media" -> 2;
            case "baja" -> 1;
            default -> 0; // Para "Inferir (Beta)".
        };
    }


    private int obtenerSiguienteOrdenPersonalizado() {
        return tareas.isEmpty() ? 0 : tareas.stream().mapToInt(Tarea::getOrdenPersonalizado).max().orElse(-1) + 1;
    }
}
