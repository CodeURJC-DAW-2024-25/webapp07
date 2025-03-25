package es.codeurjc.backend.service;

import es.codeurjc.backend.dto.DishDTO;
import es.codeurjc.backend.mapper.DishMapper;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishMapper dishMapper;

    public List<DishDTO> getTop5DishesByRatingPriceRatio() {
        return dishRepository.findAll().stream()
                .map(dish -> {
                    List<Integer> rates = dish.getRates();
                    int avgRate = (int) Math.round(rates.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0));

                    return new DishWithComputedRate(dish, avgRate);
                })
                .sorted((d1, d2) -> Double.compare(
                        ((double) d2.avgRate / d2.dish.getPrice()),
                        ((double) d1.avgRate / d1.dish.getPrice())
                ))
                .limit(5)
                .map(dishWithRate -> dishMapper.toDto(dishWithRate.dish))
                .collect(Collectors.toList());
    }

    private static class DishWithComputedRate {
        private final Dish dish;
        private final int avgRate;

        public DishWithComputedRate(Dish dish, int avgRate) {
            this.dish = dish;
            this.avgRate = avgRate;
        }
    }
}

