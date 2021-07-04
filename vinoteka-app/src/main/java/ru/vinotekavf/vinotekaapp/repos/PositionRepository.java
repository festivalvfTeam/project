package ru.vinotekavf.vinotekaapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vinotekavf.vinotekaapp.dto.PositionProviderDto;
import ru.vinotekavf.vinotekaapp.entities.Position;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT new ru.vinotekavf.vinotekaapp.dto.PositionProviderDto(" +
            "pr.name, pr.phone, po.maker, po.vendorCode, po.productName, " +
            "po.volume, po.releaseYear, po.price, po.remainder, po.fvVendorCode, " +
            "po.fvProductName) FROM Position po JOIN po.provider pr")
    public List<PositionProviderDto> getJoinInformation();

}
