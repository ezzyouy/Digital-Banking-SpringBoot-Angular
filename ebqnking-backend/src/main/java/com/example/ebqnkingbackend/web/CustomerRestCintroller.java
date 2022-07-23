package com.example.ebqnkingbackend.web;

import com.example.ebqnkingbackend.dtos.CustmerDTO;
import com.example.ebqnkingbackend.entities.Custmer;
import com.example.ebqnkingbackend.exceptions.CustomerNotFoundException;
import com.example.ebqnkingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestCintroller {
    private BankAccountService bas;
    @GetMapping("/customers")
    public List<CustmerDTO> customers(){
        return bas.listCustomer();
    }
    @GetMapping("/customers/search")
    public List<CustmerDTO> searchCustomers(@RequestParam(name="keyword", defaultValue = "") String keyword){
        return bas.searchCustomers("%"+keyword+"%");
    }
    @GetMapping("/customer/{id}")
    public CustmerDTO getCustomer(@PathVariable(name="id") Long customerId) throws CustomerNotFoundException {
            return bas.getCustomer(customerId);
    }
    @PostMapping("/customers")
    public CustmerDTO saveCustomer(@RequestBody CustmerDTO request){
        return bas.saveCustomer(request);
    }
    @PutMapping("/customer/{id}")
    public CustmerDTO updateCustomer(@PathVariable Long id, @RequestBody CustmerDTO custmerDTO){
        custmerDTO.setId(id);
        return bas.updateCustomer(custmerDTO);
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bas.deleteCustomer(id);
    }
}
