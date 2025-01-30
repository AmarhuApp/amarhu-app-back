package com.amarhu.production.service;

import com.amarhu.production.entity.Production;
import com.amarhu.production.repository.ProductionRepository;
import com.amarhu.user.entity.User;
import com.amarhu.video.entity.Video;
import com.amarhu.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductionLastMonthService {

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ProductionService productionService;

    private static final BigDecimal REVENUE_THRESHOLD = BigDecimal.valueOf(10); // Umbral para videos caídos
    private static final BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(0.78); // Porcentaje de impuestos
    private static final BigDecimal PRODUCTION_COST = BigDecimal.valueOf(5.4); // Coste por video
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formato de fecha estándar

    public List<Production> getProductionForLastMonth() {
        // Obtener la fecha actual
        LocalDate today = LocalDate.now();

        // Obtener el día actual del mes
        int currentDay = today.getDayOfMonth();

        // Validación para evitar fechas fuera de rango
        if (currentDay == 1) {
            throw new IllegalArgumentException("No se puede comparar el primer día del mes actual con el mes pasado");
        }

        // Obtener el primer día del mes pasado
        LocalDate startDate = today.minusMonths(1).withDayOfMonth(1);

        // Obtener el rango de días correspondiente al mes pasado
        LocalDate endDate = startDate.plusDays(currentDay - 2);

        // Consultar la base de datos directamente con LocalDate
        List<Video> videos = videoRepository.findAll().stream()
                .filter(video -> productionService.isWithinDateRange(video.getDate(), startDate.toString(), endDate.toString()))
                .collect(Collectors.toList());

        User user = null; // Para escenarios con usuarios específicos, deberías asignar aquí

        return List.of(productionService.calculateProduction(user, videos, startDate));
    }

    public Production calculateLastMonthProduction(User user) {
        LocalDate now = LocalDate.now();
        YearMonth lastMonth = YearMonth.from(now.minusMonths(1));

        // Convertir rango de fechas a cadenas en el formato "yyyy-MM-dd"
        String startDate = lastMonth.atDay(1).format(DATE_FORMATTER);
        String endDate = lastMonth.atEndOfMonth().format(DATE_FORMATTER);

        // Filtrar videos del mes pasado
        List<Video> videos = videoRepository.findAll().stream()
                .filter(video -> isWithinDateRange(video.getDate(), startDate, endDate))
                .collect(Collectors.toList());

        // Calcular la producción mensual basada en los videos
        return calculateProduction(user, videos, lastMonth.atEndOfMonth());
    }

    private boolean isWithinDateRange(String rawDate, String startDate, String endDate) {
        try {
            // Convertir la fecha del video al formato LocalDate
            String formattedDate = rawDate.substring(0, 10); // Extraer "yyyy-MM-dd" de la cadena
            LocalDate videoDate = LocalDate.parse(formattedDate, DATE_FORMATTER);
            LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);

            // Validar si la fecha está dentro del rango
            return (videoDate.isEqual(start) || videoDate.isAfter(start)) &&
                    (videoDate.isEqual(end) || videoDate.isBefore(end));
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la fecha: " + rawDate, e);
        }
    }

    private Production calculateProduction(User user, List<Video> videos, LocalDate productionDate) {
        int totalVideos = videos.size();
        int videosCaidos = (int) videos.stream()
                .filter(video -> BigDecimal.valueOf(video.getEstimatedRevenue()).compareTo(REVENUE_THRESHOLD) < 0)
                .count();

        BigDecimal gananciaTotal = videos.stream()
                .map(video -> BigDecimal.valueOf(video.getEstimatedRevenue())) // Convertir Double a BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gananciaMenosImpuestos = gananciaTotal.multiply(TAX_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalGeneradoPorCaidos = videos.stream()
                .filter(video -> BigDecimal.valueOf(video.getEstimatedRevenue()).compareTo(REVENUE_THRESHOLD) < 0)
                .map(video -> BigDecimal.valueOf(video.getEstimatedRevenue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal costeTotalProduccion = BigDecimal.valueOf(totalVideos).multiply(PRODUCTION_COST);

        BigDecimal gananciaNeta = gananciaMenosImpuestos.subtract(totalGeneradoPorCaidos);

        // Crear entidad Production
        Production production = new Production();
        production.setUser(user);
        production.setTotalVideos(totalVideos);
        production.setVideosCaidos(videosCaidos);
        production.setGananciaTotal(gananciaTotal);
        production.setGananciaMenosImpuestos(gananciaMenosImpuestos);
        production.setGananciaNeta(gananciaNeta);
        production.setCosteProduccion(PRODUCTION_COST.intValue());
        production.setCosteTotalProduccion(costeTotalProduccion);
        production.setTotalGeneradoPorCaidos(totalGeneradoPorCaidos);
        production.setDate(productionDate); // Fecha del mes pasado

        return productionRepository.save(production);
    }
}
