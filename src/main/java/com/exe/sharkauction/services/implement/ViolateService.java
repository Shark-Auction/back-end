package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.ViolateEntity;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.repositories.IViolateRepository;
import com.exe.sharkauction.services.IViolateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolateService implements IViolateService {
    private IViolateRepository violateRepository;
    private IUserRepository userRepository;

    @Override
    public ViolateEntity createViolate(ViolateEntity violate){
        UserEntity existingUser = userRepository
                .findById(violate.getUser().getId())
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", violate.getUser().getId()));
        violate.setUser(existingUser);
        return violateRepository.save(violate);

    }

    @Override
    public ViolateEntity getViolateById(long id ){
        return violateRepository.findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("Violate", "id", id));
    }
    @Override
    public List<ViolateEntity> getAllViolate(){
        return violateRepository.findAll();
    }
    @Override
    public List<ViolateEntity> getViolateByUserId(long userId){
        return violateRepository.findAllByUserId(userId);
    }
    @Override
    public void deleteViolate(long id){
        ViolateEntity violate = violateRepository.findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("Violate", "id", id));

        violateRepository.delete(violate);
    }


}