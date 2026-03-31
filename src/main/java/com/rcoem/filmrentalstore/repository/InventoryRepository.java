package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.FilmCountDTO;
import com.rcoem.filmrentalstore.entities.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
   // Page<Inventory> findByStore_StoreId(Byte id, Pageable pageable);

    List<Inventory> findByStore_StoreId(Byte storeId);

    default List<FilmCountDTO> getFilmCountByStore(Byte storeId) {
        List<Inventory> inventories = findByStore_StoreId(storeId);

        Map<String, Long> map = inventories.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getFilm().getTitle(),
                        Collectors.counting()
                ));

        return map.entrySet()
                .stream()
                .map(e -> new FilmCountDTO(e.getKey(), e.getValue()))
                .toList();
    }
}
