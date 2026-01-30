package org.devsu.com.cuentamovimientos.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.devsu.com.cuentamovimientos.application.usecase.MovimientoService;
import org.devsu.com.cuentamovimientos.domain.entity.Movimiento;
import org.devsu.com.cuentamovimientos.infrastructure.dto.MovimientoRequest;
import org.devsu.com.cuentamovimientos.infrastructure.dto.MovimientoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {
        List<MovimientoResponse> movimientos = movimientoService.obtenerTodosLosMovimientos()
                .stream()
                .map(this::movimientoToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable Long id) {
        return movimientoService.obtenerMovimientoPorId(id)
                .map(movimiento -> ResponseEntity.ok(movimientoToResponse(movimiento)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<MovimientoResponse>> obtenerPorCuenta(@PathVariable Long cuentaId) {
        List<MovimientoResponse> movimientos = movimientoService.obtenerMovimientosPorCuenta(cuentaId)
                .stream()
                .map(this::movimientoToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("/cuenta/{cuentaId}")
    public ResponseEntity<?> registrarMovimiento(
            @PathVariable Long cuentaId,
            @Valid @RequestBody MovimientoRequest request) {
        try {
            Movimiento movimiento;

            if ("Debito".equalsIgnoreCase(request.getTipoMovimiento())) {
                movimiento = movimientoService.registrarDebito(cuentaId, request.getValor());
            } else if ("Credito".equalsIgnoreCase(request.getTipoMovimiento())) {
                movimiento = movimientoService.registrarCredito(cuentaId, request.getValor());
            } else {
                return ResponseEntity.badRequest().body("Tipo de movimiento inválido. Use 'Debito' o 'Credito'");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(movimientoToResponse(movimiento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar movimiento");
        }
    }


    private MovimientoResponse movimientoToResponse(Movimiento movimiento) {
        return MovimientoResponse.builder()
                .movimientoId(movimiento.getMovimientoId())
                .fecha(movimiento.getFecha())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .valor(movimiento.getValor())
                .saldoDisponible(movimiento.getSaldoDisponible())
                .cuentaId(movimiento.getCuenta().getCuentaId())
                .build();
    }

}
