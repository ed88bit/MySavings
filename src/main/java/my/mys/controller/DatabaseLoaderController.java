package my.mys.controller;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import my.mys.service.DatabaseLoaderService;
import my.mys.service.MovementsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class DatabaseLoaderController {

    private final DatabaseLoaderService databaseLoaderService;

    @PostMapping("/loadDatabaseWithExcel")
    public ResponseEntity<String> getOutflowsAtMonth(@RequestParam("excel") @NotNull MultipartFile file) {
        return ResponseEntity.ok(databaseLoaderService.loadDataFromExcel(file));
    }

}
