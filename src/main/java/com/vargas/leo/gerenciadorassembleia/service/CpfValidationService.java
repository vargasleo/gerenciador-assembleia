package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.domain.VotingPowerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-info", url = "user-info.herokuapp.com/users")
public interface CpfValidationService {

    @RequestMapping(method = RequestMethod.GET, value = "{cpf}")
    VotingPowerDTO isAllowedToVote(@PathVariable("cpf") String cpf);

}
