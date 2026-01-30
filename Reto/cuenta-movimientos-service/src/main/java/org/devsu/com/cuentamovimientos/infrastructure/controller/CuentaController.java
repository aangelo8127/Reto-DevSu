package org.devsu.com.cuentamovimientos.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.devsu.com.cuentamovimientos.application.usecase.CuentaService;
import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.infrastructure.dto.CuentaRequest;
import org.devsu.com.cuentamovimientos.infrastructure.dto.CuentaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> obtenerTodas() {
        List<CuentaResponse> cuentas = cuentaService.obtenerTodasLasCuentas()
                .stream()
                .map(this::cuentaToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(@PathVariable Long id) {
        return cuentaService.obtenerCuentaPorId(id)
                .map(cuenta -> ResponseEntity.ok(cuentaToResponse(cuenta)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaResponse> obtenerPorNumero(@PathVariable String numeroCuenta) {
        return cuentaService.obtenerCuentaPorNumero(numeroCuenta)
                .map(cuenta -> ResponseEntity.ok(cuentaToResponse(cuenta)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaResponse>> obtenerPorCliente(@PathVariable String clienteId) {
        List<CuentaResponse> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId)
                .stream()
                .map(this::cuentaToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CuentaRequest request) {
        Cuenta cuenta = requestToCuenta(request);
        Cuenta cuentaCreada = cuentaService.crearCuenta(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaToResponse(cuentaCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable Long id, @Valid @RequestBody CuentaRequest request) {
        return cuentaService.obtenerCuentaPorId(id)
                .map(cuentaExistente -> {
                    cuentaExistente.setNumeroCuenta(request.getNumeroCuenta());
                    cuentaExistente.setTipoCuenta(request.getTipoCuenta());
                    cuentaExistente.setEstado(request.getEstado());
                    cuentaService.actualizarCuenta(cuentaExistente);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }


    private CuentaResponse cuentaToResponse(Cuenta cuenta) {
        return CuentaResponse.builder()
                .cuentaId(cuenta.getCuentaId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .saldoDisponible(cuenta.getSaldoDisponible())
                .estado(cuenta.getEstado())
                .clienteId(cuenta.getClienteId())
                .build();
    }

    private Cuenta requestToCuenta(CuentaRequest request) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setSaldoDisponible(request.getSaldoInicial());
        cuenta.setEstado(request.getEstado());
        cuenta.setClienteId(request.getClienteId());
        return cuenta;
    }

}
