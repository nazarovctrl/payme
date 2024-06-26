package io.github.nazarovctrl.paymemerchantapi.controller;

import io.github.nazarovctrl.paymemerchantapi.dto.reqeust.RequestForm;
import io.github.nazarovctrl.paymemerchantapi.dto.result.Result;
import io.github.nazarovctrl.paymemerchantapi.exp.UnauthorizedRequest;
import io.github.nazarovctrl.paymemerchantapi.util.AuthUtil;
import io.github.nazarovctrl.paymemerchantapi.service.MerchantService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * The class for handling Payme Merchant API requests
 *
 * @author Azimjon Nazarov
 */
@RestController
@RequestMapping("${payme.merchant.api.path}")
public class MerchantController {
    /***
     * {@link MerchantService} for processing requests
     */
    private final MerchantService merchantService;
    /***
     * {@link AuthUtil} for checking authorization of requests
     */
    private final AuthUtil authUtil;

    /***
     * @param merchantService bean
     * @param authUtil bean
     */
    public MerchantController(MerchantService merchantService, AuthUtil authUtil) {
        this.merchantService = merchantService;
        this.authUtil = authUtil;
    }

    /***
     * @param request RPC from Payme
     * @param requestForm request body
     * @return if request authorized returns result of RPC method, otherwise returns Unauthorized request (code = -32504)
     */
    @PostMapping
    public ResponseEntity<Result> handle(HttpServletRequest request, @RequestBody(required = false) RequestForm requestForm) {
        if (authUtil.isUnauthorized(request.getHeader("Authorization"))) {
            throw new UnauthorizedRequest();
        }
        return ResponseEntity.ok(merchantService.handle(requestForm));
    }
}
